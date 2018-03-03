package com.example.silwan.popularmovies.ui.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.interfaces.MovieService;
import com.example.silwan.popularmovies.ui.models.MoviesResult;
import com.example.silwan.popularmovies.ui.network.NetworkService;
import com.example.silwan.popularmovies.ui.utils.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getMovies(Constants.SORT_BY_POPULAR);
    }

    private void getMovies(String sort) {
        MovieService networkService = NetworkService.createService(MovieService.class);
        Call<MoviesResult> movieList = networkService.getMovies(sort);

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
