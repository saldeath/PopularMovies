package com.example.silwan.popularmovies.ui.ui.details;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.models.TrailerModel;
import com.example.silwan.popularmovies.ui.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private Context mContext;
    private List<TrailerModel> mTrailers = new ArrayList<>();
    public final TrailerAdapterOnClickHandler mOnClickHandler;

    public TrailerAdapter(TrailerAdapterOnClickHandler mOnClickHandler) {
        this.mOnClickHandler = mOnClickHandler;
    }

    @NonNull
    @Override
    public TrailerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_trailer, parent, false);
        this.mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerAdapter.ViewHolder holder, int position) {
        TrailerModel trailerModel = mTrailers.get(position);

        Glide.with(mContext)
            .load(NetworkUtils.buildYoutubeThumbnailUrl(trailerModel.getKey()))
            .into(holder.mImageView);
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(Uri Uri);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void setTrailers(List<TrailerModel> trailers){
        this.mTrailers = trailers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.iv_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Uri key = NetworkUtils.buildYoutubeUrl(mTrailers.get(getAdapterPosition()).getKey());

            mOnClickHandler.onClick(key);
        }
    }
}
