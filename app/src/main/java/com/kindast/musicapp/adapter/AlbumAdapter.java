package com.kindast.musicapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kindast.musicapp.R;
import com.kindast.musicapp.model.Album;

import java.util.List;

public class AlbumAdapter extends BaseAdapter {

    private Context context;
    private List<Album> albums;

    public AlbumAdapter(Context context, List<Album> albums) {
        this.context = context;
        this.albums = albums;
    }

    @Override
    public int getCount() {
        return albums.size();
    }

    @Override
    public Album getItem(int i) {
        return albums.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PlaylistViewHolder holder;
        if (view == null){
            view = LayoutInflater
                    .from(context)
                    .inflate(R.layout.album_item, viewGroup, false);
            holder = new PlaylistViewHolder();
            holder.albumName = (TextView) view.findViewById(R.id.albumName);
            holder.artistName = (TextView) view.findViewById(R.id.artistName);
            holder.cover = (ImageView) view.findViewById(R.id.imageView);
            view.setTag(holder);
        }
        else{
            holder = (PlaylistViewHolder) view.getTag();
        }
        Album album = albums.get(i);
        holder.artistName.setText(album.getArtist());
        holder.albumName.setText((album.getName()));
        Glide.with(context).load(album.getCoverUrl()).into(holder.cover);
        return view;
    }

    public static class PlaylistViewHolder{
        TextView albumName;
        TextView artistName;
        ImageView cover;
    }
}
