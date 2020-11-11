package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.offers.Offers;
import com.ipda3.sofraapp.data.model.offers.OffersData;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.helper.DateTxt;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantAddOfferFragment;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class RestaurantOffersAdapter extends RecyclerView.Adapter<RestaurantOffersAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<OffersData> restaurantOffersDataList = new ArrayList<>();
    private UserData userData;
    private String lang;

    private RestaurantAddOfferFragment restaurantAddOfferFragment;


    public RestaurantOffersAdapter(Activity context, List<OffersData> restaurantOffersDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.restaurantOffersDataList = restaurantOffersDataList;

        userData = loadUserData(activity);
        lang = Locale.getDefault().getLanguage();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_offer,
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

        holder.restaurantOfferItemTvOffer.setText(restaurantOffersDataList.get(position).getDescription());
        onLoadImageFromUrl(holder.restaurantOfferItemIvImage, restaurantOffersDataList.get(position).getPhotoUrl(), context);

    }

    private void setAction(ViewHolder holder, int position) {

        holder.restaurantOfferItemIbEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantAddOfferFragment = new RestaurantAddOfferFragment();
                restaurantAddOfferFragment.update = true;
                restaurantAddOfferFragment.restaurantOffersData = restaurantOffersDataList.get(position);
                //restaurantAddCategoryFoodItemFragment.category = restaurantItemsDataList.get(position).getCategoryId();
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantAddOfferFragment);
            }
        });


        holder.restaurantOfferItemIbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().restaurantDeleteOffer(restaurantOffersDataList.get(position).getId(), LoadData(activity, API_TOKEN))
                        .enqueue(new Callback<Offers>() {
                            @Override
                            public void onResponse(Call<Offers> call, Response<Offers> response) {
                                try {
                                    restaurantOffersDataList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, restaurantOffersDataList.size());
                                } catch (Exception ex) {
                                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Offers> call, Throwable t) {
                                Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

    }

    private void setSwipe(final ViewHolder holder, final int position) {
        holder.restaurantOfferItemSlrItem.computeScroll();
        if (lang.contentEquals("ar")) {
            holder.restaurantOfferItemSlrItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
        } else {
            holder.restaurantOfferItemSlrItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_RIGHT);
        }

    }


    @Override
    public int getItemCount() {
        return restaurantOffersDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.restaurant_offer_item_ib_edit)
        ImageButton restaurantOfferItemIbEdit;
        @BindView(R.id.restaurant_offer_item_ib_delete)
        ImageButton restaurantOfferItemIbDelete;
        @BindView(R.id.restaurant_offer_item_tv_offer)
        TextView restaurantOfferItemTvOffer;
        @BindView(R.id.restaurant_offer_item_iv_image)
        CircleImageView restaurantOfferItemIvImage;
        @BindView(R.id.restaurant_offer_item_slr_item)
        SwipeRevealLayout restaurantOfferItemSlrItem;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
