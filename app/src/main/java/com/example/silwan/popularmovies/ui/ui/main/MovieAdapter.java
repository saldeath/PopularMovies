package com.example.silwan.popularmovies.ui.ui.main;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.data.MoviesContract;
import com.example.silwan.popularmovies.ui.models.MovieModel;
import com.example.silwan.popularmovies.ui.utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Silwan on 03/03/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{

    private Context mContext;
    private ArrayList<MovieModel> mMovies = new ArrayList<>();
    private MovieAdapterOnClickHandler mMovieAdapterOnClickHandler;
    private Cursor mCursor;

    public MovieAdapter(MovieAdapterOnClickHandler movieAdapterOnClickHandler) {
        this.mMovieAdapterOnClickHandler = movieAdapterOnClickHandler;
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {
        Glide.with(mContext).load(Constants.IMAGE_PATH + mMovies.get(position).getPosterPath()).into(holder.mPoster);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void setMovies(ArrayList<MovieModel> movies){
        mMovies = movies;
        notifyDataSetChanged();
    }


    public interface MovieAdapterOnClickHandler {
        void onClick(MovieModel movieModel);
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPoster;
        private MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPoster = itemView.findViewById(R.id.iv_poster);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mMovieAdapterOnClickHandler.onClick(mMovies.get(getAdapterPosition()));
        }
    }
}
