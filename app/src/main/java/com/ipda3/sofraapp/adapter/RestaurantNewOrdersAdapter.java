package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.opengl.EGLImage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.restaurantNewOrders.RestaurantNewOrdersData;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.loadUserData;

public class RestaurantNewOrdersAdapter extends RecyclerView.Adapter<RestaurantNewOrdersAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<RestaurantNewOrdersData> restaurantNewOrdersDataList = new ArrayList<>();
    private UserData userData;
    private String lang;


    public RestaurantNewOrdersAdapter(Activity context, List<RestaurantNewOrdersData> restaurantNewOrdersDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.restaurantNewOrdersDataList = restaurantNewOrdersDataList;

        userData = loadUserData(activity);
        lang = Locale.getDefault().getLanguage();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_new_order,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {

        holder.itemRestaurantNewOrderTvClientName.setText(context.getString(R.string.client) + restaurantNewOrdersDataList.get(position).getClient().getName());
        holder.itemRestaurantNewOrderTvOrderNumber.setText(context.getString(R.string.order_number) + restaurantNewOrdersDataList.get(position).getClientId());
        holder.itemRestaurantNewOrderTvOrderTotalCost.setText(context.getString(R.string.total) + restaurantNewOrdersDataList.get(position).getNet() + context.getString(R.string.riel));
        holder.itemRestaurantNewOrderTvAddress.setText(context.getString(R.string.address) + restaurantNewOrdersDataList.get(position).getAddress());
        Glide.with(context).load(restaurantNewOrdersDataList.get(position).getClient().getPhotoUrl()).into(holder.itemRestaurantNewOrderIvImage);
    }

    private void setAction(ViewHolder holder, int position) {

        holder.itemRestaurantNewOrderBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemRestaurantNewOrderBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        holder.itemRestaurantNewOrderBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }


    @Override
    public int getItemCount() {
        return restaurantNewOrdersDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.item_restaurant_new_order_iv_image)
        ImageView itemRestaurantNewOrderIvImage;
        @BindView(R.id.item_restaurant_new_order_tv_client_name)
        TextView itemRestaurantNewOrderTvClientName;
        @BindView(R.id.item_restaurant_new_order_tv_order_number)
        TextView itemRestaurantNewOrderTvOrderNumber;
        @BindView(R.id.item_restaurant_new_order_tv_order_total_cost)
        TextView itemRestaurantNewOrderTvOrderTotalCost;
        @BindView(R.id.item_restaurant_new_order_tv_address)
        TextView itemRestaurantNewOrderTvAddress;
        @BindView(R.id.item_restaurant_new_order_btn_call)
        Button itemRestaurantNewOrderBtnCall;
        @BindView(R.id.item_restaurant_new_order_btn_accept)
        Button itemRestaurantNewOrderBtnAccept;
        @BindView(R.id.item_restaurant_new_order_btn_cancel)
        Button itemRestaurantNewOrderBtnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
