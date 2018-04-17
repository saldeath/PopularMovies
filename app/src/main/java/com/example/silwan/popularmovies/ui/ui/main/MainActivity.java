package com.example.silwan.popularmovies.ui.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.data.MoviesContract;
import com.example.silwan.popularmovies.ui.interfaces.MovieService;
import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.models.MoviesResult;
import com.example.silwan.popularmovies.ui.network.NetworkService;
import com.example.silwan.popularmovies.ui.ui.details.DetailsActivity;
import com.example.silwan.popularmovies.ui.utils.Constants;
import com.example.silwan.popularmovies.ui.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private MovieService mNetworkService;

    private Cursor mCursor;
    private List<MovieModel> localMovies = new ArrayList<>();

    private static final int ID_MOVIE_LOADER = 33;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initScreen();
        checkInternetConnection();
    }

    private void initScreen() {
        mNetworkService = NetworkService.createService(MovieService.class);

        mMoviesRecyclerView = findViewById(R.id.rv_movie_list);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        mMoviesRecyclerView.setLayoutManager(gridLayoutManager);
        mMoviesRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mMoviesRecyclerView.setAdapter(mMovieAdapter);

        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

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
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_most_popular:
                getMovies(Constants.SORT_BY_POPULAR);
                return true;
            case R.id.action_top_rated:
                getMovies(Constants.SORT_BY_TOP_RATED);
                return true;
            case R.id.action_favorite:
                showFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showFavorite() {
        getFavoriteMovies();
        mMovieAdapter.setMovies(localMovies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sort, menu);
        return true;
    }

    @Override
    public void onClick(MovieModel movieModel) {
        if(movieModel.getId() > 0) {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Constants.MOVIE_KEY, movieModel);
            startActivity(intent);
        }
    }

    private void checkInternetConnection(){
        if(NetworkUtils.isOnline(this)){
            getMovies(Constants.SORT_BY_POPULAR);
        } else {
            Toast.makeText(MainActivity.this, "No internet connection available", Toast.LENGTH_SHORT).show();
        }
    }

    private void getFavoriteMovies(){
        localMovies.clear();
        for(mCursor.moveToFirst(); !mCursor.isAfterLast(); mCursor.moveToNext()){
            MovieModel movieModel = new MovieModel();
            movieModel.setPosterPath((mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH))));
            localMovies.add(movieModel);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id){
            case ID_MOVIE_LOADER:
                Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
                return new CursorLoader(this, uri, null,null,null,null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mCursor = data;
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
