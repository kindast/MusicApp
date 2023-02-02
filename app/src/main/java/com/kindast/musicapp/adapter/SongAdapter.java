package com.kindast.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kindast.musicapp.R;
import com.kindast.musicapp.model.Song;

import java.util.List;

public class SongAdapter extends BaseAdapter {

    private Context context;
    private List<Song> songs;

    public SongAdapter(Context context, List<Song> songs){
        this.context = context;
        this.songs = songs;
    }

    @Override
    public int getCount() {
        return songs.size();
    }

    @Override
    public Object getItem(int i) {
        return songs.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        SongViewHolder holder;
        if (view == null){
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.song_item, viewGroup, false);
            holder = new SongViewHolder();
            holder.songName = (TextView) view.findViewById(R.id.songName);
            holder.artistName = (TextView) view.findViewById(R.id.artistName);
            view.setTag(holder);
        }
        else{
            holder = (SongViewHolder) view.getTag();
        }
        Song song = songs.get(i);
        holder.songName.setText(song.getName());
        holder.artistName.setText(song.getArtist());
        return view;
    }

    public static class SongViewHolder {
        TextView songName;
        TextView artistName;
    }
}


