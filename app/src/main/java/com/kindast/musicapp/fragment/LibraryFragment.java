package com.kindast.musicapp.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.kindast.musicapp.adapter.AlbumAdapter;
import com.kindast.musicapp.R;
import com.kindast.musicapp.api.MusicApi;
import com.kindast.musicapp.mediaplayer.MediaPlayerSingleton;
import com.kindast.musicapp.mediaplayer.MiniPlayerSingleton;
import com.kindast.musicapp.model.Album;
import com.kindast.musicapp.model.Song;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LibraryFragment extends Fragment {
    private Retrofit retrofit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_library, container, false);
        retrofit = new Retrofit.Builder()
                .baseUrl("https://kindast.site/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MusicApi musicApi = retrofit.create(MusicApi.class);
        Call<List<Album>> call = musicApi.getAlbums("wHXLVvOtE89ZHeGFuGXDVMkme1TUxQSG");
        call.enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful()){
                    List<Album> albums = response.body();
                    ListView listView = view.findViewById(R.id.playlistsView);
                    listView.setDivider(null);
                    listView.setDividerHeight(0);
                    AlbumAdapter adapter = new AlbumAdapter(getContext(), albums);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Album album = adapter.getItem(i);
                            MediaPlayerSingleton.setAlbum(album);
                            ConstraintLayout miniplayer = MiniPlayerSingleton.getMiniplayer();
                            miniplayer.setVisibility(View.VISIBLE);
                            MiniPlayerSingleton.setCoverImage(getContext(), album.getCoverUrl());
                            Song song = album.getSongs().get(0);
                            MiniPlayerSingleton.setSongInfo(song.getName(), song.getArtist());
                            MiniPlayerSingleton.setProgress(0);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {}
        });



        return view;
    }
}
