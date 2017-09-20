package com.example.droodsunny.mymusicplayer;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import model.song;

/**
 * Created by DroodSunny on 2017/9/5.
 */

public class MusicPlayer extends FragmentStatePagerAdapter implements MediaPlayer.OnCompletionListener ,ViewPager.OnPageChangeListener {

    public static int i=1;
    private MediaPlayer mediaPlayer;
    private int cursor=0;
    private ArrayList<song> dataset;
    private HashMap<Integer,player_fragment> cachedFragmentHashMap;
    private android.os.Handler loadHandler = new android.os.Handler();
    private Runnable UpdateSong = new Runnable() {
        @Override
        public void run() {
            load();
        }
    };
    //Control Music
     private boolean isPlaying = false;
    private  repeat repeatState=repeat.no;

    public MusicPlayer(ArrayList<song> songCollection,int position,FragmentManager fm) {
        super(fm);
        this.mediaPlayer=new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.dataset = songCollection;
        this.cachedFragmentHashMap=new HashMap<>();
        this.cursor=position;
        this.mediaPlayer.setOnCompletionListener(this);

    }
    public MusicPlayer(int position,FragmentManager fm){
        super(fm);
        this.mediaPlayer=new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.dataset =new ArrayList<>();
        this.cachedFragmentHashMap=new HashMap<>();
        this.cursor=position;
        this.mediaPlayer.setOnCompletionListener(this);

    }
    public MusicPlayer(FragmentManager fm){
        super(fm);
        this.mediaPlayer=new MediaPlayer();
        this.mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        this.dataset =new ArrayList<>();
        this.cachedFragmentHashMap=new HashMap<>();
        this.mediaPlayer.setOnCompletionListener(this);
    }
    public song load(){
        song song=null;
            song = dataset.get(cursor);
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(song.path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(isPlaying){
            play();
        }
        return song;
    }
    public void play(){
        mediaPlayer.start();
        isPlaying=true;
    }
    public void pause(){
        mediaPlayer.pause();
        isPlaying=false;
    }
    public void stop(){
        mediaPlayer.stop();
        isPlaying=false;
    }
   public void reset(){
       mediaPlayer.seekTo(0);
       if(isPlaying()){
           play();

       }else {
           pause();
       }
   }
    public song next(){
        cursor++;
        if(cursor>=dataset.size()){
            if(repeatState==repeat.all){
                cursor=0;
            }else {
                cursor--;
                stop();
                return null;
            }
        }
        return load();
    }

    public song previous(){
        if(getTime() < 1000){
            cursor--;
            if (cursor < 0){
                if (repeatState == repeat.all){
                    cursor = dataset.size();
                    return load();
                }
                else{
                    cursor = 0;
                    reset();
                    return null;
                }
            }else{
                return load();
            }
        }else {
            reset();
            return null;
        }
    }
    public void shuffle(){
        /**
         * In seek of a good implementation
         */
    }
    public void repeat(){
        switch (repeatState){
            case no:
                repeatState=repeat.all;
                break;
            case all:
                repeatState=repeat.single;
                break;
            case single:
                repeatState = repeat.no;
                break;
        }
    }
    public void seekTo(int msec){
        mediaPlayer.seekTo(msec);
    }
    public int getTime(){
        return mediaPlayer.getCurrentPosition();
    }
    public int getDuration(){
        return mediaPlayer.getDuration();
    }
    public song getSong() throws Exception{
        return dataset.get(cursor);
    }
    public song getSong(int index){
        return dataset.get(index);
    }
    public int getCursor(){
        return cursor;
    }
    public int getTrackNumber(){
        return cursor+1;
    }
    public void setCursor(int position){
        cursor = position;
    }
    public int getTotalTracks(){
        return dataset.size();
    }
    public void addSong(song song){
        this.dataset.add(song);
    }
    public enum repeat{
        no,all,single
    }
    public boolean isPlaying(){
        return this.mediaPlayer.isPlaying();
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
         switch (repeatState){
             case all:
             case no:
                 next();
                 break;
             case single:
                 reset();
                 break;
         }

    }

    @Override
    public Fragment getItem(int position) {

        Fragment fragment;
        fragment = new player_fragment();
        Bundle args = new Bundle();
        args.putString("Path",dataset.get(position).path);
        fragment.setArguments(args);
        cachedFragmentHashMap.put(position,(player_fragment) fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return dataset.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
        cachedFragmentHashMap.remove(position);
    }
    public HashMap<Integer, player_fragment> getCachedFragmentHashMap() {
        return cachedFragmentHashMap;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mediaPlayer.stop();
        cursor=position;
        loadHandler.removeCallbacks(UpdateSong);
        loadHandler.postDelayed(UpdateSong,300);

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
