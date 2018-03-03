package com.example.silwan.popularmovies.ui.ui.main;

import android.graphics.Movie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.interfaces.MovieService;
import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.models.MoviesResult;
import com.example.silwan.popularmovies.ui.network.NetworkService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMovies();
    }

    private void getMovies() {
        MovieService networkService = NetworkService.createService(MovieService.class);
        Call<MoviesResult> movieList = networkService.listRepos();

        movieList.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                MoviesResult movies = response.body();
                Log.d("test", movies + "");
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                Log.d("test", call + "" + t);
            }
        });
    }
}
