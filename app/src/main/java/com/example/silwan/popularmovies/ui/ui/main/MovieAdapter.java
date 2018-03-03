package com.example.silwan.popularmovies.ui.ui.main;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.silwan.popularmovies.R;

/**
 * Created by Silwan on 03/03/2018.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{



    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie, parent, false);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public ImageView mPoster;

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder {
        public MovieAdapterViewHolder(View itemView) {
            super(itemView);
            mPoster = itemView.findViewById(R.id.iv_poster);
        }
    }
}
