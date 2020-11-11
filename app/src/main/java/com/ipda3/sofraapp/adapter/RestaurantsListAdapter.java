package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsListData;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.RestaurantDetailsContainerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class RestaurantsListAdapter extends RecyclerView.Adapter<RestaurantsListAdapter.ViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<RestaurantsListData> restaurantsListDataList = new ArrayList<>();
    private List<ImageButton> rateList = new ArrayList<>();
    private String lang;

    public RestaurantsListAdapter(Activity context, List<RestaurantsListData> restaurantsListDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.restaurantsListDataList = restaurantsListDataList;

        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        rateList.add(holder.restaurantItemIbStar1);
        rateList.add(holder.restaurantItemIbStar2);
        rateList.add(holder.restaurantItemIbStar3);
        rateList.add(holder.restaurantItemIbStar4);

        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.restaurantItemTvRestName.setText(restaurantsListDataList.get(position).getName());
        holder.restaurantItemTvMinDeliveryCost.setText(activity.getString(R.string.delivery_cost) + ": " + restaurantsListDataList.get(position).getDeliveryCost() + " " + activity.getString(R.string.riel));
        holder.restaurantItemTvMinOrderCost.setText(activity.getString(R.string.min_order_cost) + restaurantsListDataList.get(position).getMinimumCharger() + " " + activity.getString(R.string.riel));
        Glide.with(context)
                .load(restaurantsListDataList.get(position).getPhotoUrl())
                .into(holder.restaurantItemIvImage);

        String isActive = restaurantsListDataList.get(position).getAvailability();
        if (isActive.equals("open")) {
            holder.restaurantItemTvRestStatus.setText(activity.getString(R.string.open));
            holder.restaurantItemTvRestStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_dot_green, 0, 0, 0);
        } else {
            holder.restaurantItemTvRestStatus.setText(activity.getString(R.string.close));
            holder.restaurantItemTvRestStatus.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_dot_red, 0, 0, 0);

        }

        int rate = restaurantsListDataList.get(position).getRate();
        setRate(rate);

        holder.restaurantItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestaurantDetailsContainerFragment restaurantDetailsContainerFragment = new RestaurantDetailsContainerFragment();
                restaurantDetailsContainerFragment.restaurantId = restaurantsListDataList.get(position).getId();
                restaurantDetailsContainerFragment.restaurant = restaurantsListDataList.get(position);
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantDetailsContainerFragment);
            }
        });
    }

    public void setRate(int rate) {
        for (int i = 0; i < rateList.size(); i++) {
            if (i <= (rate - 1)) {
                rateList.get(i).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_pink));
            } else {
                rateList.get(i).setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_star_blue));
            }
        }
    }

    private void setAction(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return restaurantsListDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

        @BindView(R.id.restaurant_item)
        RelativeLayout restaurantItem;
        @BindView(R.id.restaurant_item_tv_rest_name)
        TextView restaurantItemTvRestName;
        @BindView(R.id.restaurant_item_tv_rest_status)
        TextView restaurantItemTvRestStatus;
        @BindView(R.id.restaurant_item_ib_star1)
        ImageButton restaurantItemIbStar1;
        @BindView(R.id.restaurant_item_ib_star2)
        ImageButton restaurantItemIbStar2;
        @BindView(R.id.restaurant_item_ib_star3)
        ImageButton restaurantItemIbStar3;
        @BindView(R.id.restaurant_item_ib_star4)
        ImageButton restaurantItemIbStar4;
        @BindView(R.id.restaurant_item_tv_min_order_cost)
        TextView restaurantItemTvMinOrderCost;
        @BindView(R.id.restaurant_item_tv_min_delivery_cost)
        TextView restaurantItemTvMinDeliveryCost;
        @BindView(R.id.restaurant_item_iv_image)
        CircleImageView restaurantItemIvImage;
    }
}
