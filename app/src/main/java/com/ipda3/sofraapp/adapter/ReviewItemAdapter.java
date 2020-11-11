package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.restaurantReviews.RestaurantReviewsData;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewItemAdapter extends RecyclerView.Adapter<ReviewItemAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<RestaurantReviewsData> restaurantReviewsDataList = new ArrayList<>();
    private String lang;

    public ReviewItemAdapter(Activity context, List<RestaurantReviewsData> restaurantReviewsDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.restaurantReviewsDataList = restaurantReviewsDataList;

        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.itemReviewTvName.setText(restaurantReviewsDataList.get(position).getClient().getName());
        holder.itemReviewTvComment.setText(restaurantReviewsDataList.get(position).getComment());

        switch (restaurantReviewsDataList.get(position).getRate()){
            case "1":
                holder.itemReviewIvImotion.setImageResource(R.drawable.ic_imo1);
                break;
            case "2":
                holder.itemReviewIvImotion.setImageResource(R.drawable.ic_imo2);
                break;
            case "3":
                holder.itemReviewIvImotion.setImageResource(R.drawable.ic_imo3);
                break;
            case "4":
                holder.itemReviewIvImotion.setImageResource(R.drawable.ic_imo4);
                break;
            case "5":
                holder.itemReviewIvImotion.setImageResource(R.drawable.ic_imo5);
                break;
        }
    }

    private void setAction(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return restaurantReviewsDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.item_review_iv_imotion)
        ImageView itemReviewIvImotion;
        @BindView(R.id.item_review_tv_name)
        TextView itemReviewTvName;
        @BindView(R.id.item_review_tv_comment)
        TextView itemReviewTvComment;
        @BindView(R.id.item_review)
        ConstraintLayout itemReview;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
