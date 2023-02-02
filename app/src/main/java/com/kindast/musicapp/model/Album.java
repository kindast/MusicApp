package com.kindast.musicapp.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public class Album implements Serializable {
    @Expose
    private String name;
    @Expose
    private String artist;
    @Expose
    private String coverUrl;
    @Expose
    private List<Song> songs;

    public Album(String name, String artist, String coverUrl, List<Song> songs){
        this.name = name;
        this.artist = artist;
        this.coverUrl = coverUrl;
        this.songs = songs;
    }

    public String getName(){
        return name;
    }

    public String getArtist(){
        return artist;
    }

    public String getCoverUrl(){
        return coverUrl;
    }

    public List<Song> getSongs(){
        return songs;
    }
}
