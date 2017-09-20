package model;

/**
 * Created by DroodSunny on 2017/9/5.
 */

public class song {
    public String path;
    public String title;
    public String artist;
    public String album;
    public long duration;
    public int track;

    public song(String path, String title, String artist, String album, long duration){
        this.path = path;
        this.title = title;
        this.artist = artist;
        this.album = album;
        this.duration = duration;
    }
}
