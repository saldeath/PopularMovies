package com.example.silwan.popularmovies.ui.ui.main;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mMoviesRecyclerView;
    private MovieAdapter mMovieAdapter;
    private MovieService mNetworkService;
    private GridLayoutManager gridLayoutManager;

    private Cursor mCursor;
    private ArrayList<MovieModel> localMovies = new ArrayList<>();

    private static final int ID_MOVIE_LOADER = 33;

    private static String MOVIES = "movies";
    private static String SCROLL_POSITION = "scrollPosition";
    private Parcelable RECYCLER_STATE;
    private static boolean FAVORITE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initScreen();
        checkSavedInstanceState(savedInstanceState);
    }

    private void checkSavedInstanceState(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            localMovies = savedInstanceState.getParcelableArrayList(MOVIES);
            RECYCLER_STATE = savedInstanceState.getParcelable(SCROLL_POSITION);
            mMovieAdapter.setMovies(localMovies);
            mMoviesRecyclerView.getLayoutManager().onRestoreInstanceState(RECYCLER_STATE);
        } else {
            checkInternetConnection();
        }
    }

    private void initScreen() {
        mNetworkService = NetworkService.createService(MovieService.class);

        mMoviesRecyclerView = findViewById(R.id.rv_movie_list);
        gridLayoutManager = new GridLayoutManager(this, 2);
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
                localMovies = response.body().getResults();
                mMovieAdapter.setMovies(localMovies);
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
                FAVORITE = false;
                return true;
            case R.id.action_top_rated:
                getMovies(Constants.SORT_BY_TOP_RATED);
                FAVORITE = false;
                return true;
            case R.id.action_favorite:
                showFavorite();
                FAVORITE = true;
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
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(Constants.MOVIE_KEY, movieModel);
            startActivity(intent);
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
            movieModel.setId(mCursor.getInt(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_ID)));
            movieModel.setOriginalTitle(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_TITLE)));
            movieModel.setReleaseDate(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE)));
            movieModel.setVoteAverage(mCursor.getDouble(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE)));
            movieModel.setBackdropPath(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_BACK_DROP_PATH)));
            movieModel.setOverview(mCursor.getString(mCursor.getColumnIndex(MoviesContract.MoviesEntry.COLUMN_OVERVIEW)));
            localMovies.add(movieModel);
        }
        mMovieAdapter.setMovies(localMovies);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MOVIES, localMovies);
        outState.putParcelable(SCROLL_POSITION, mMoviesRecyclerView.getLayoutManager().onSaveInstanceState());
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
        if(FAVORITE){
            getFavoriteMovies();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
