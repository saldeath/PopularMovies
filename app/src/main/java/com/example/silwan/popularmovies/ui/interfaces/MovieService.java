package com.example.silwan.popularmovies.ui.interfaces;

import android.graphics.Movie;

import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.models.MoviesResult;
import com.example.silwan.popularmovies.ui.models.TrailerModel;
import com.example.silwan.popularmovies.ui.models.TrailerResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Silwan on 02/03/2018.
 */

public interface MovieService {
    @GET("movie/{sort_by}")
    Call<MoviesResult> getMovies(@Path("sort_by") String sortBy);

    @GET("movie/{movie_id}/videos")
    Call<TrailerResult> getTrailers(@Path("movie_id") int movieId);
}
