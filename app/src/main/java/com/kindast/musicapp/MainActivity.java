package com.kindast.musicapp;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.tabs.TabLayout;
import com.kindast.musicapp.adapter.ViewPagerAdapter;
import com.kindast.musicapp.api.MusicApi;
import com.kindast.musicapp.mediaplayer.MediaPlayerSingleton;
import com.kindast.musicapp.mediaplayer.MiniPlayerSingleton;
import com.kindast.musicapp.model.Album;
import com.kindast.musicapp.model.Song;
import com.kindast.musicapp.service.MediaPlayerService;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;
    private ProgressBar progressBar;
    private Timer timer;
    private TimerTask timerTask;
    private Intent playerActivity, mediaService;
    private ImageView cover, playBtn;
    private TextView songName, artistName;
    private ConstraintLayout miniplayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CreateTabs();
        FindVariables();
        SetupEvents();
        MiniPlayerSingleton.setMiniplayer(this);
        mediaService = new Intent(this, MediaPlayerService.class);
        startService(mediaService);
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setMax(mediaPlayer.getDuration());
        progressBar.setProgress(mediaPlayer.getCurrentPosition());
        if (mediaPlayer.isPlaying()){
            playBtn.setImageResource(R.drawable.pause);
            startProgressBarUpdate();
        }
        else{
            playBtn.setImageResource(R.drawable.play);
            stopProgressBarUpdate();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, MediaPlayerService.class));
        super.onDestroy();
    }

    public void CreateTabs(){
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.setTabRippleColor(null);
        ViewPager2 viewPager = findViewById(R.id.viewpager2);

        TabLayout.Tab homeTab = tabLayout.getTabAt(0);
        TabLayout.Tab searchTab = tabLayout.getTabAt(1);
        TabLayout.Tab libraryTab = tabLayout.getTabAt(2);
        ImageView homeIcon = new ImageView(this);
        homeIcon.setImageResource(R.drawable.home_selected);
        homeIcon.setMinimumWidth(45);
        homeIcon.setMinimumHeight(55);
        homeTab.setCustomView(homeIcon);
        ImageView searchIcon = new ImageView(this);
        searchIcon.setImageResource(R.drawable.search_unselected);
        searchIcon.setMinimumWidth(45);
        searchIcon.setMinimumHeight(55);
        searchTab.setCustomView(searchIcon);
        ImageView libraryIcon = new ImageView(this);
        libraryIcon.setImageResource(R.drawable.library_unselected);
        libraryIcon.setMinimumWidth(45);
        libraryIcon.setMinimumHeight(55);
        libraryTab.setCustomView(libraryIcon);

        viewPager.setUserInputEnabled(false);
        viewPager.setPageTransformer(null);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(adapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                setDefaultIcons();
                switch (tab.getPosition()){
                    case 0:
                        homeIcon.setImageResource(R.drawable.home_selected);
                        break;

                    case 1:
                        searchIcon.setImageResource(R.drawable.search_selected);
                        break;

                    case 2:
                        libraryIcon.setImageResource(R.drawable.library_selected);
                        break;
                }
                viewPager.setCurrentItem(tab.getPosition(), false);
            }

            private void setDefaultIcons(){
                homeIcon.setImageResource(R.drawable.home_unselected);
                searchIcon.setImageResource(R.drawable.search_unselected);
                libraryIcon.setImageResource(R.drawable.library_unselected);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void FindVariables(){
        playerActivity = new Intent(this, PlayerActivity.class);
        mediaPlayer = MediaPlayerSingleton.getMediaPlayer();
        progressBar = findViewById(R.id.mini_progressBar);
        cover = findViewById(R.id.mini_cover);
        playBtn = findViewById(R.id.mini_play_btn);
        miniplayer = findViewById(R.id.mini_layout);
        songName = findViewById(R.id.mini_song_name);
        artistName = findViewById(R.id.mini_artist_name);
    }

    private void SetupEvents(){
        miniplayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(playerActivity);
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer.isPlaying()){
                    playBtn.setImageResource(R.drawable.play);
                    mediaPlayer.pause();
                    stopProgressBarUpdate();
                }
                else{
                    playBtn.setImageResource(R.drawable.pause);
                    mediaPlayer.start();
                    startProgressBarUpdate();
                }
            }
        });
    }

    private void UpdateSongInfo(){
        Song song = MediaPlayerSingleton.getCurrentSong();
        if (songName.getText() != song.getName()
                || artistName.getText() != song.getArtist()){
            songName.setText(song.getName());
            artistName.setText(song.getArtist());
            progressBar.setMax(mediaPlayer.getDuration());
        }
    }

    private void startProgressBarUpdate(){
        stopProgressBarUpdate();
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
                                progressBar.setProgress(cur);
                            }
                        }
                        UpdateSongInfo();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 100);
    }

    private void stopProgressBarUpdate(){
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
}