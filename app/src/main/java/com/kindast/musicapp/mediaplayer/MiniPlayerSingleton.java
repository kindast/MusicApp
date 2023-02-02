package com.kindast.musicapp.mediaplayer;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.kindast.musicapp.MainActivity;
import com.kindast.musicapp.R;

public class MiniPlayerSingleton {
    private static ConstraintLayout miniplayer;
    private static TextView songName, artistName;
    private static ImageView cover;
    private static ProgressBar progressBar;

    public static void setMiniplayer(MainActivity activity){
        miniplayer = activity.findViewById(R.id.mini_layout);
        songName = activity.findViewById(R.id.mini_song_name);
        artistName = activity.findViewById(R.id.mini_artist_name);
        cover = activity.findViewById(R.id.mini_cover);
        progressBar = activity.findViewById(R.id.mini_progressBar);
    }

    public static ConstraintLayout getMiniplayer(){
        return miniplayer;
    }

    public static void setCoverImage(Context context, String url){
        Glide.with(context).load(url).into(cover);
    }

    public static void setSongInfo(String name, String artist){
        songName.setText(name);
        artistName.setText(artist);
    }

    public static void setProgress(int progress){
        progressBar.setMax(MediaPlayerSingleton.getMediaPlayer().getDuration());
        progressBar.setProgress(progress);
    }
}
