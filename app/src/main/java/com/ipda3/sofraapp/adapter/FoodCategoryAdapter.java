package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.foodCategory.FoodCategoryData;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.FoodListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FoodCategoryAdapter extends RecyclerView.Adapter<FoodCategoryAdapter.ViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<FoodCategoryData> foodCategoryDataList = new ArrayList<>();
    private FoodListFragment foodListFragment;
    private String lang;

    public FoodCategoryAdapter(Activity context, List<FoodCategoryData> foodCategoryDataList, FoodListFragment foodListFragment) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.foodCategoryDataList = foodCategoryDataList;
        this.foodListFragment = foodListFragment;
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_category,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.foodCategoryItemTvName.setText(foodCategoryDataList.get(position).getName());
        Glide.with(context).load(foodCategoryDataList.get(position).getPhotoUrl()).into(holder.foodCategoryItemIvImage);

        holder.foodCategoryItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodListFragment.categoryId = foodCategoryDataList.get(position).getId();
                foodListFragment.getItems(1);
            }
        });
    }

    private void setAction(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return foodCategoryDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.food_category_item_iv_image)
        CircleImageView foodCategoryItemIvImage;
        @BindView(R.id.food_category_item_tv_name)
        TextView foodCategoryItemTvName;
        @BindView(R.id.food_category_item)
        RelativeLayout foodCategoryItem;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
