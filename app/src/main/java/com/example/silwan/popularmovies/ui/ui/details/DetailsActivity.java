package com.example.silwan.popularmovies.ui.ui.details;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.databinding.ActivityDetailsBinding;
import com.example.silwan.popularmovies.ui.data.MoviesContract;
import com.example.silwan.popularmovies.ui.interfaces.MovieService;
import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.models.ReviewsResult;
import com.example.silwan.popularmovies.ui.models.TrailerModel;
import com.example.silwan.popularmovies.ui.models.TrailerResult;
import com.example.silwan.popularmovies.ui.network.NetworkService;
import com.example.silwan.popularmovies.ui.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailsActivity extends AppCompatActivity implements
        TrailerAdapter.TrailerAdapterOnClickHandler,
        LoaderManager.LoaderCallbacks<Cursor> {

    private MovieModel mMovieModel;
    private ActivityDetailsBinding mActivityDetailsBinding;
    private MovieService mMovieService;
    private RecyclerView mRecyclerViewTrailers, mRecyclerViewReviews;
    private TrailerAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewsAdapter;
    private FloatingActionButton mAdd;
    private boolean mIsFavorite;

    private static final int MOVIE_LOADER_ID = 0;

    public static final String[] MAIN_MOVIES_PROJECTION = {
            MoviesContract.MoviesEntry.COLUMN_ID,
            MoviesContract.MoviesEntry.COLUMN_POSTER_PATH,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieService = NetworkService.createService(MovieService.class);

        mActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        initScreen();

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.MOVIE_KEY)){
            mMovieModel = intent.getExtras().getParcelable(Constants.MOVIE_KEY);
            mActivityDetailsBinding.setMovie(mMovieModel);
            getTrailer(mMovieModel);
            getReviews(mMovieModel);
        }

        initClickListeners();
        getSupportLoaderManager().initLoader(MOVIE_LOADER_ID, null, this);
    }

    private void initClickListeners() {
        mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mIsFavorite) {
                    addToFavorite();
                } else {
                    remoteFromFavorite();
                }

                setFavoriteImage();
            }
        });
    }

    private void addToFavorite() {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_ID, mMovieModel.getId());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_POSTER_PATH, mMovieModel.getPosterPath());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_TITLE, mMovieModel.getTitle());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_BACK_DROP_PATH, mMovieModel.getBackdropPath());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_RELEASE_DATE, mMovieModel.getReleaseDate());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_VOTE_AVERAGE, mMovieModel.getVoteAverage());
            contentValues.put(MoviesContract.MoviesEntry.COLUMN_OVERVIEW, mMovieModel.getOverview());

            Uri uri = getContentResolver().insert(MoviesContract.MoviesEntry.CONTENT_URI, contentValues);

            if (uri != null) {
                mIsFavorite = true;
                Toast.makeText(getBaseContext(), "Movie has been added", Toast.LENGTH_LONG).show();
            }
    }

    private void remoteFromFavorite(){
        String stringId = Integer.toString(mMovieModel.getId());

        Uri uri = MoviesContract.MoviesEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        int delete = getContentResolver().delete(uri,
                null,
                null);

        if(delete > 0){
            mIsFavorite = false;
            Toast.makeText(getBaseContext(), "Movie deleted", Toast.LENGTH_LONG).show();
        }
    }

    private void getReviews(MovieModel movieModel) {
        Call<ReviewsResult> reviews = mMovieService.getReviews(movieModel.getId());
        reviews.enqueue(new Callback<ReviewsResult>() {
            @Override
            public void onResponse(Call<ReviewsResult> call, Response<ReviewsResult> response) {
               mReviewsAdapter.setData(response.body().getResults());
            }

            @Override
            public void onFailure(Call<ReviewsResult> call, Throwable t) {

            }
        });
    }

    private void initScreen() {
        mRecyclerViewTrailers = findViewById(R.id.rv_trailer_list);
        mTrailerAdapter = new TrailerAdapter(this);
        mRecyclerViewTrailers.setAdapter(mTrailerAdapter);
        mRecyclerViewTrailers.setHasFixedSize(true);
        mRecyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        mRecyclerViewReviews = findViewById(R.id.rv_reviews_list);
        mReviewsAdapter = new ReviewsAdapter();
        mRecyclerViewReviews.setAdapter(mReviewsAdapter);
        mRecyclerViewReviews.setHasFixedSize(true);
        mRecyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        mAdd = findViewById(R.id.fab_add);
    }

    private void getTrailer(MovieModel movieModel){
        Call<TrailerResult> trailers = mMovieService.getTrailers(movieModel.getId());
        trailers.enqueue(new Callback<TrailerResult>() {
            @Override
            public void onResponse(Call<TrailerResult> call, Response<TrailerResult> response) {
                List<TrailerModel> models = response.body().getResults();
                mTrailerAdapter.setTrailers(models);
            }

            @Override
            public void onFailure(Call<TrailerResult> call, Throwable t) {

            }
        });
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(Constants.IMAGE_PATH + imageUrl).into(view);
    }

    @Override
    public void onClick(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    private void setFavoriteImage(){
        if(!mIsFavorite){
            mAdd.setImageResource(R.drawable.ic_add_black_24dp);
        } else {
            mAdd.setImageResource(R.drawable.ic_clear_black_24dp);
        }
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(this,
                MoviesContract.MoviesEntry.CONTENT_URI,
                MAIN_MOVIES_PROJECTION,
                MoviesContract.MoviesEntry.COLUMN_ID + " = ?",
                new String[]{ String.valueOf(mMovieModel.getId()) },
                null
        );
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        int cursorCount = data.getCount();
        mIsFavorite = cursorCount > 0;
        setFavoriteImage();
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}
