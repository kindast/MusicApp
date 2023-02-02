package com.kindast.musicapp;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.kindast.musicapp.mediaplayer.MediaPlayerSingleton;
import com.kindast.musicapp.model.Album;
import com.kindast.musicapp.model.Song;

import java.util.Timer;
import java.util.TimerTask;

public class PlayerActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private ImageView playBtn, cover, backBtn, nextBtn, previousBtn;
    private TextView currentTimeTextView, maxTimeTextView, songName, artistName, albumName;
    private Timer timer;
    private TimerTask timerTask;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        FindVariables();
        SetupEvents();
        UpdateSongInfo();
        seekBar.setMax(mediaPlayer.getDuration());
        maxTimeTextView.setText(FormatTime(mediaPlayer.getDuration()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        seekBar.setProgress(mediaPlayer.getCurrentPosition());
        currentTimeTextView.setText(FormatTime(mediaPlayer.getCurrentPosition()));
        if (mediaPlayer.isPlaying()){
            playBtn.setImageResource(R.drawable.pause);
            StartSeekBarUpdate();
        }
        else{
            playBtn.setImageResource(R.drawable.play);
            StopSeekBarUpdate();
        }
    }

    private void FindVariables(){
        songName = findViewById(R.id.player_song_name);
        artistName = findViewById(R.id.player_artist_name);
        albumName = findViewById(R.id.player_album_name);
        mediaPlayer = MediaPlayerSingleton.getMediaPlayer();
        seekBar = findViewById(R.id.player_seekbar);
        currentTimeTextView = findViewById(R.id.player_current_time_tv);
        maxTimeTextView = findViewById(R.id.player_max_time_tv);
        cover = findViewById(R.id.player_cover);
        playBtn = findViewById(R.id.player_play_btn);
        backBtn = findViewById(R.id.player_back_btn);
        nextBtn = findViewById(R.id.player_next_btn);
        previousBtn = findViewById(R.id.player_previous_btn);
    }

    private void SetupEvents(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    currentTimeTextView.setText(FormatTime(mediaPlayer.getCurrentPosition()));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    playBtn.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    StopSeekBarUpdate();
                }
                else{
                    playBtn.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    StartSeekBarUpdate();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerSingleton.nextSong();
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                currentTimeTextView.setText("0:00");
                maxTimeTextView.setText(FormatTime(mediaPlayer.getDuration()));
                songName.setText(MediaPlayerSingleton.getCurrentSong().getName());
                artistName.setText(MediaPlayerSingleton.getCurrentSong().getArtist());
            }
        });

        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaPlayerSingleton.previousSong();
                seekBar.setProgress(0);
                seekBar.setMax(mediaPlayer.getDuration());
                currentTimeTextView.setText("0:00");
                maxTimeTextView.setText(FormatTime(mediaPlayer.getDuration()));
                songName.setText(MediaPlayerSingleton.getCurrentSong().getName());
                artistName.setText(MediaPlayerSingleton.getCurrentSong().getArtist());
            }
        });
    }

    private void UpdateSongInfo(){
        Song song = MediaPlayerSingleton.getCurrentSong();
        Album album = MediaPlayerSingleton.getAlbum();
        if (songName.getText() != song.getName()
                || artistName.getText() != song.getArtist()){
            songName.setText(song.getName());
            artistName.setText(song.getArtist());
            albumName.setText(album.getName());
            Glide.with(getApplicationContext()).load(album.getCoverUrl()).into(cover);
            seekBar.setMax(mediaPlayer.getDuration());
        }
    }

    private void StartSeekBarUpdate(){
        StopSeekBarUpdate();
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mediaPlayer.isPlaying()){
                            int cur = mediaPlayer.getCurrentPosition();
                            int max = mediaPlayer.getDuration();
                            if (cur <= max){
                                seekBar.setProgress(cur);
                                currentTimeTextView.setText(FormatTime(cur));
                                maxTimeTextView.setText(FormatTime(max));
                            }
                        }
                        UpdateSongInfo();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }

    private void StopSeekBarUpdate(){
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        if (timerTask != null) {
            timerTask.cancel();
            timerTask = null;
        }
    }

    private String FormatTime(int millis) {
        int minutes = (millis % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (millis % (1000 * 60)) / 1000;
        return String.format("%d:%02d", minutes, seconds);
    }
}
