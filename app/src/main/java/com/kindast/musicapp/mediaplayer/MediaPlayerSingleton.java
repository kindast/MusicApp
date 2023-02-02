package com.kindast.musicapp.mediaplayer;

import android.media.MediaPlayer;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.kindast.musicapp.model.Album;
import com.kindast.musicapp.model.Song;

import java.io.IOException;

public class MediaPlayerSingleton {
    private static MediaPlayer mediaPlayer;
    private static Album album;

    private static int index;
    private MediaPlayerSingleton() {}
    public static MediaPlayer getMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            index = 0;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    if (album == null){
                        return;
                    }
                    if (index == album.getSongs().size() - 1){
                        mediaPlayer.stop();
                    }
                    else{
                        index++;
                        prepareCurrentSong();
                        mediaPlayer.start();
                    }
                }
            });
            if (album != null){
                prepareCurrentSong();
            }
        }
        return mediaPlayer;
    }

    public static void setAlbum(Album a) {
        boolean isPlaying = mediaPlayer.isPlaying();
        album = a;
        index = 0;
        prepareCurrentSong();
        if (isPlaying){
            mediaPlayer.start();
        }
    }

    public static Album getAlbum() {
        return album;
    }

    public static void nextSong() {
        if (index == album.getSongs().size() - 1){
            return;
        }
        boolean isPlaying = mediaPlayer.isPlaying();
        index++;
        prepareCurrentSong();
        if (isPlaying){
            mediaPlayer.start();
        }
    }

    public static void previousSong() {
        if (index == 0){
            return;
        }
        boolean isPlaying = mediaPlayer.isPlaying();
        index--;
        prepareCurrentSong();
        if (isPlaying){
            mediaPlayer.start();
        }
    }

    public static Song getCurrentSong(){
        return album.getSongs().get(index);
    }

    private static void prepareCurrentSong(){
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(getCurrentSong().getSongUrl());
            mediaPlayer.prepare();
        } catch (IOException e) {}
    }
}
