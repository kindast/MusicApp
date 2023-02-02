package com.kindast.musicapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.kindast.musicapp.MainActivity;
import com.kindast.musicapp.R;
import com.kindast.musicapp.mediaplayer.MediaPlayerSingleton;

public class MediaPlayerService extends Service {
    private NotificationManager notificationManager;
    private static final int NOTIFICATION_ID = 1;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaPlayerSingleton.getMediaPlayer().start();
        showNotification();
        return super.onStartCommand(intent, flags, startId);
    }

    private void showNotification(String title, String artist) {
        Intent activityIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);

        Intent previousIntent = new Intent(this, MediaPlayerService.class)
                .setAction(ACTION_PREVIOUS);
        PendingIntent previousPendingIntent = PendingIntent.getService(this, 0, previousIntent, 0);

        Intent playIntent = new Intent(this, MediaPlayerService.class)
                .setAction(ACTION_PLAY_PAUSE);
        PendingIntent playPendingIntent = PendingIntent.getService(this, 0, playIntent, 0);

        Intent nextIntent = new Intent(this, MediaPlayerService.class)
                .setAction(ACTION_NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getService(this, 0, nextIntent, 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                .setSmallIcon(R.drawable.ic_music_note)
                .setContentTitle(title)
                .setContentText(artist)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setContentIntent(pendingIntent)
                .setAutoCancel(false)
                .addAction(R.drawable.ic_previous, "Previous", previousPendingIntent)
                .addAction(R.drawable.ic_play_pause, "Play", playPendingIntent)
                .addAction(R.drawable.ic_next, "Next", nextPendingIntent);

        startForeground(NOTIFICATION_ID, builder.build());
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("channel_id", "Music player", NotificationManager.IMPORTANCE_LOW);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Override
    public void onDestroy() {
        MediaPlayerSingleton.getMediaPlayer().stop();
        notificationManager.cancel(NOTIFICATION_ID);
        super.onDestroy();
    }
}
