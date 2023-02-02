package com.kindast.musicapp.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class Song implements Serializable {
    @Expose
    private String name;
    @Expose
    private String artist;
    @Expose
    private String songUrl;

    public Song(String name, String artist, String songUrl){
        this.name = name;
        this.artist = artist;
        this.songUrl = songUrl;
    }

    public String getName(){
        return this.name;
    }

    public String getArtist(){
        return this.artist;
    }

    public String getSongUrl(){
        return this.songUrl;
    }
}
