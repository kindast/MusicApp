package com.kindast.musicapp.api;

import com.kindast.musicapp.model.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface MusicApi {
    @GET("music/api/v1/liked-albums")
    Call<List<Album>> getAlbums(@Header("token") String token);
}
