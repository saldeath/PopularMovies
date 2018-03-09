package com.example.silwan.popularmovies.ui.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.interfaces.MovieService;
import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.models.MoviesResult;
import com.example.silwan.popularmovies.ui.network.NetworkService;
import com.example.silwan.popularmovies.ui.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private MovieService mNetworkService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initScreen();
        getMovies(Constants.SORT_BY_POPULAR);
    }

    private void initScreen() {
        mNetworkService = NetworkService.createService(MovieService.class);

        mMoviesRecyclerView = findViewById(R.id.rv_movie_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter();
        mMoviesRecyclerView.setAdapter(mMovieAdapter);
    }

    private void getMovies(String sort) {
        Call<MoviesResult> movieList = mNetworkService.getMovies(sort);

        movieList.enqueue(new Callback<MoviesResult>() {
            @Override
            public void onResponse(Call<MoviesResult> call, Response<MoviesResult> response) {
                List<MovieModel> movies = response.body().getResults();
                mMovieAdapter.setMovies(movies);
            }

            @Override
            public void onFailure(Call<MoviesResult> call, Throwable t) {
                Log.d("test", call + "" + t);
            }
        });
    }
}
