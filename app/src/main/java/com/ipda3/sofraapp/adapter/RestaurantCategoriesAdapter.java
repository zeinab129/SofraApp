package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.app.Dialog;
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
import com.ipda3.sofraapp.data.model.myCategories.MyCategories;
import com.ipda3.sofraapp.data.model.myCategories.MyCategoriesData;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantItemsFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class RestaurantCategoriesAdapter extends RecyclerView.Adapter<RestaurantCategoriesAdapter.ViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<MyCategoriesData> myCategoriesDataList = new ArrayList<>();
    private UserData userData;
    private String lang;
    private int currentPos = 0;

    public static Dialog dialog;
    private String path;
    private ArrayList<AlbumFile> imagesList = new ArrayList<>();
    private EditText categoryName;
    private Button btnAddNewRestaurantCategory;
    private RestaurantItemsFragment restaurantItemsFragment;

    public RestaurantCategoriesAdapter(Activity context, List<MyCategoriesData> myCategoriesDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.myCategoriesDataList = myCategoriesDataList;

        userData = loadUserData(activity);
        lang = Locale.getDefault().getLanguage();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_category,
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
        currentPos = position;
        holder.restaurantCategoryItemTvCategoryName.setText(myCategoriesDataList.get(position).getName());
        Glide.with(context).load(myCategoriesDataList.get(position).getPhotoUrl()).into(holder.restaurantCategoryItemIvCategoryImage);


    }

    private void setAction(ViewHolder holder, int position) {
        holder.restaurantCategoryItemIbDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().deleteRestaurantCategory(LoadData(activity, API_TOKEN), myCategoriesDataList.get(position).getId())
                        .enqueue(new Callback<MyCategories>() {
                            @Override
                            public void onResponse(Call<MyCategories> call, Response<MyCategories> response) {
                                Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                try {
                                    if (response.body().getStatus() == 1) {
                                        myCategoriesDataList.remove(position);
                                        notifyItemRemoved(position);
                                        notifyItemRangeChanged(position, myCategoriesDataList.size());
                                    }
                                } catch (Exception e) {
                                    Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<MyCategories> call, Throwable t) {

                            }
                        });
            }
        });

        holder.restaurantCategoryItemIbEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Objects.requireNonNull(activity));
                dialog.setContentView(R.layout.dialod_add_restaurant_category);
                final CircleImageView categoryImage = dialog.findViewById(R.id.dialog_add_restaurant_category_ib_category_image);
                categoryName = dialog.findViewById(R.id.dialog_add_restaurant_category_et_name);
                btnAddNewRestaurantCategory = dialog.findViewById(R.id.dialog_add_restaurant_category_btn_add);

                btnAddNewRestaurantCategory.setText(activity.getString(R.string.edit));
                categoryName.setText(myCategoriesDataList.get(position).getName());
                Glide.with(context)
                        .load(myCategoriesDataList.get(position).getPhotoUrl())
                        .into(holder.restaurantCategoryItemIvCategoryImage);

                categoryImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGallery(activity, 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                            @Override
                            public void onAction(@NonNull ArrayList<AlbumFile> result) {
                                imagesList.clear();
                                imagesList = result;
                                path = imagesList.get(0).getPath();
                                onLoadImageFromUrl(categoryImage, path, activity);
                            }
                        });
                        dialog.show();
                    }
                });

                btnAddNewRestaurantCategory.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClient().updateRestaurantCategory(
                                convertToRequestBody(categoryName.getText().toString().trim()),
                                convertFileToMultipart(path, "photo"),
                                convertToRequestBody(LoadData(activity, API_TOKEN)),
                                convertToRequestBody(Integer.toString(myCategoriesDataList.get(position).getId())))
                                .enqueue(new Callback<MyCategories>() {
                                    @Override
                                    public void onResponse(Call<MyCategories> call, Response<MyCategories> response) {
                                        try {
                                            Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                        } catch (Exception e) {
                                            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<MyCategories> call, Throwable t) {
                                        Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                dialog.show();
            }
        });

        holder.restaurantCategoryItemIbCatItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restaurantItemsFragment = new RestaurantItemsFragment();
                restaurantItemsFragment.category = myCategoriesDataList.get(position);
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantItemsFragment);
            }
        });
    }

    private void setSwipe(final ViewHolder holder, final int position) {
        holder.restaurantCategoryItemSrlItem.computeScroll();
        if (lang.contentEquals("ar")) {
            holder.restaurantCategoryItemSrlItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_LEFT);
        } else {
            holder.restaurantCategoryItemSrlItem.setDragEdge(SwipeRevealLayout.DRAG_EDGE_RIGHT);
        }

    }


    @Override
    public int getItemCount() {
        return myCategoriesDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.restaurant_category_item_ib_edit)
        ImageButton restaurantCategoryItemIbEdit;
        @BindView(R.id.restaurant_category_item_ib_delete)
        ImageButton restaurantCategoryItemIbDelete;
        @BindView(R.id.restaurant_category_item_iv_category_image)
        ImageView restaurantCategoryItemIvCategoryImage;
        @BindView(R.id.restaurant_category_item_tv_category_name)
        TextView restaurantCategoryItemTvCategoryName;
        @BindView(R.id.restaurant_category_item_srl_item)
        SwipeRevealLayout restaurantCategoryItemSrlItem;
        @BindView(R.id.restaurant_category_item_ib_cat_items)
        ImageButton restaurantCategoryItemIbCatItems;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }
    }
}
