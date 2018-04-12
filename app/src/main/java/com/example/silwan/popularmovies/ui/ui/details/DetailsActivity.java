package com.example.silwan.popularmovies.ui.ui.details;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.databinding.ActivityDetailsBinding;
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

public class DetailsActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler {

    private MovieModel mMovieModel;
    private ActivityDetailsBinding mActivityDetailsBinding;
    private MovieService mMovieService;
    private RecyclerView mRecyclerViewTrailers, mRecyclerViewReviews;
    private TrailerAdapter mTrailerAdapter;
    private ReviewsAdapter mReviewsAdapter;

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
}
