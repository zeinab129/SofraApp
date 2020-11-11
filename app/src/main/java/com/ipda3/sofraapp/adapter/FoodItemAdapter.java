package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.foodItem.FoodItemData;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ReviewItemFoodFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class FoodItemAdapter extends RecyclerView.Adapter<FoodItemAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<FoodItemData> foodItemDataList = new ArrayList<>();
    private String lang;
    private ReviewItemFoodFragment reviewItemFoodFragment;


    public FoodItemAdapter(Activity context, List<FoodItemData> foodItemDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.foodItemDataList = foodItemDataList;

        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.foodItemIvImage, foodItemDataList.get(position).getPhotoUrl(), context);
        holder.foodItemTvName.setText(foodItemDataList.get(position).getName());
        holder.foodItemTvDesc.setText(foodItemDataList.get(position).getDescription());
        holder.foodItemTvPrice.setText(foodItemDataList.get(position).getPrice());
        if (foodItemDataList.get(position).getHasOffer()) {
            holder.foodItemTvPrice.setPaintFlags(holder.foodItemTvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.foodItemTvOfferPrice.setVisibility(View.VISIBLE);
            holder.foodItemTvSpecialOffer.setVisibility(View.VISIBLE);
            holder.foodItemTvOfferPrice.setText(foodItemDataList.get(position).getOfferPrice());
        }
    }

    private void setAction(ViewHolder holder, int position) {
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewItemFoodFragment = new ReviewItemFoodFragment();
                reviewItemFoodFragment.foodItemData = foodItemDataList.get(position);
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, reviewItemFoodFragment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return foodItemDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.food_item_iv_image)
        ImageView foodItemIvImage;
        @BindView(R.id.food_item_tv_name)
        TextView foodItemTvName;
        @BindView(R.id.food_item_tv_desc)
        TextView foodItemTvDesc;
        @BindView(R.id.food_item_tv_price)
        TextView foodItemTvPrice;
        @BindView(R.id.food_item_tv_offer_price)
        TextView foodItemTvOfferPrice;
        @BindView(R.id.food_item)
        RelativeLayout foodItem;
        @BindView(R.id.food_item_tv_special_offer)
        TextView foodItemTvSpecialOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
