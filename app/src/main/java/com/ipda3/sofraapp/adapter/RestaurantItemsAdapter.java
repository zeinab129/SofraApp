package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.foodItem.FoodItem;
import com.ipda3.sofraapp.data.model.restaurantAddFoodItem.RestaurantAddFoodItem;
import com.ipda3.sofraapp.data.model.restaurantItems.RestaurantItemsData;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantAddCategoryFoodItemFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class RestaurantItemsAdapter extends RecyclerView.Adapter<RestaurantItemsAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<RestaurantItemsData> restaurantItemsDataList = new ArrayList<>();
    private UserData userData;
    private String lang;

    private RestaurantAddCategoryFoodItemFragment restaurantAddCategoryFoodItemFragment;


    public RestaurantItemsAdapter(Activity context, List<RestaurantItemsData> restaurantItemsDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.restaurantItemsDataList = restaurantItemsDataList;

        userData = loadUserData(activity);
        lang = Locale.getDefault().getLanguage();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_food_item,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
        setSwipe(holder, position);
    }

    private void setData(ViewHolder holder, int position) {

        holder.restaurantFoodItemTvFoodItemName.setText(restaurantItemsDataList.get(position).getName());
        holder.restaurantFoodItemTvFoodItemDesc.setText(restaurantItemsDataList.get(position).getDescription());
        holder.restaurantFoodItemTvFoodItemPrice.setText(restaurantItemsDataList.get(position).getPrice());
        Glide.with(context).load(restaurantItemsDataList.get(position).getPhotoUrl()).into(holder.restaurantFoodItemIvFoodItemImage);

    }

    private void setAction(ViewHolder holder, int position) {

        holder.restaurantFoodItemIbEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantAddCategoryFoodItemFragment = new RestaurantAddCategoryFoodItemFragment();
                restaurantAddCategoryFoodItemFragment.update = true;
                restaurantAddCategoryFoodItemFragment.restaurantItemsData = restaurantItemsDataList.get(position);
                restaurantAddCategoryFoodItemFragment.category = restaurantItemsDataList.get(position).getCategoryId();
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantAddCategoryFoodItemFragment);
            }
        });


        holder.restaurantFoodItemIbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().restaurantDeleteFoodItem(restaurantItemsDataList.get(position).getId(), LoadData(activity, API_TOKEN))
                        .enqueue(new Callback<FoodItem>() {
                            @Override
                            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                                try{
                                    restaurantItemsDataList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, restaurantItemsDataList.size());
                                }catch (Exception ex){
                                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<FoodItem> call, Throwable t) {
                                Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void setSwipe(final ViewHolder holder, final int position) {
        holder.restaurantFoodItemSrlItem.computeScroll();
        if (lang.contentEquals("ar")) {
            holder.restaurantFoodItemSrlItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
        } else {
            holder.restaurantFoodItemSrlItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_RIGHT);
        }

    }


    @Override
    public int getItemCount() {
        return restaurantItemsDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.restaurant_food_item_ib_edit)
        ImageButton restaurantFoodItemIbEdit;
        @BindView(R.id.restaurant_food_item_ib_delete)
        ImageButton restaurantFoodItemIbDelete;
        @BindView(R.id.restaurant_food_item_iv_food_item_image)
        ImageView restaurantFoodItemIvFoodItemImage;
        @BindView(R.id.restaurant_food_item_tv_food_item_name)
        TextView restaurantFoodItemTvFoodItemName;
        @BindView(R.id.restaurant_food_item_tv_food_item_desc)
        TextView restaurantFoodItemTvFoodItemDesc;
        @BindView(R.id.restaurant_food_item_tv_food_item_price)
        TextView restaurantFoodItemTvFoodItemPrice;
        @BindView(R.id.restaurant_food_item_srl_item)
        SwipeRevealLayout restaurantFoodItemSrlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
