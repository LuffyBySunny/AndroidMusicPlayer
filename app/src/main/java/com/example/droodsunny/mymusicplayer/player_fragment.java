package com.example.droodsunny.mymusicplayer;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by DroodSunny on 2017/9/5.
 */

public class player_fragment extends android.support.v4.app.Fragment {

    String songPath;
    ImageView songCover;
    MediaMetadataRetriever metaRetriever;

    public player_fragment(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.player_fragment,container,false);
        Bundle bundle=this.getArguments();
        if(bundle!=null){
            songPath=bundle.getString("Path",null);
            Log.i("Player_Fragment","Song :"+songPath);

        }
        metaRetriever = new MediaMetadataRetriever();
        metaRetriever.setDataSource(songPath);
        songCover = (ImageView)view.findViewById(R.id.mainCover);
        Glide.with(getContext()).load(metaRetriever.getEmbeddedPicture()).placeholder(R.drawable.placeholder).into(songCover);
        return view;
    }
}
