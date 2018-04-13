package com.example.silwan.popularmovies.ui.ui.details;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.silwan.popularmovies.R;
import com.example.silwan.popularmovies.ui.models.ReviewModel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<ReviewModel> mReviewModel = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewModel reviewModel = mReviewModel.get(position);

        holder.mReview.setText(reviewModel.getContent());
        holder.mAuthor.setText(reviewModel.getAuthor());
    }

    @Override
    public int getItemCount() {
        return mReviewModel.size();
    }

    public void setData(List<ReviewModel> reviewModel){
        this.mReviewModel = reviewModel;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mReview, mAuthor;

        public ViewHolder(View itemView) {
            super(itemView);
            mReview = itemView.findViewById(R.id.tv_review);
            mAuthor = itemView.findViewById(R.id.tv_author);
        }
    }
}
