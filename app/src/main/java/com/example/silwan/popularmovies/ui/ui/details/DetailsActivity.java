package com.example.silwan.popularmovies.ui.ui.details;

import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.databinding.ActivityDetailsBinding;
import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.utils.Constants;

public class DetailsActivity extends AppCompatActivity {

    private MovieModel mMovieModel;
    private ActivityDetailsBinding mActivityDetailsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        mActivityDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_details);

        Intent intent = getIntent();
        if(intent.hasExtra(Constants.MOVIE_KEY)){
            mMovieModel = intent.getExtras().getParcelable(Constants.MOVIE_KEY);
            mActivityDetailsBinding.setMovie(mMovieModel);
        }
    }

    @BindingAdapter({"bind:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext()).load(Constants.IMAGE_PATH + imageUrl).into(view);
    }
}
