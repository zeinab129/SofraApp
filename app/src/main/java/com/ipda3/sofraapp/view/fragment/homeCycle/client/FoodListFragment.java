package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.FoodCategoryAdapter;
import com.ipda3.sofraapp.adapter.FoodItemAdapter;
import com.ipda3.sofraapp.data.model.foodCategory.FoodCategory;
import com.ipda3.sofraapp.data.model.foodCategory.FoodCategoryData;
import com.ipda3.sofraapp.data.model.foodItem.FoodItem;
import com.ipda3.sofraapp.data.model.foodItem.FoodItemData;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;

public class FoodListFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.food_list_fragment_rv_categories)
    RecyclerView foodListFragmentRvCategories;
    @BindView(R.id.food_list_fragment_rv_foods)
    RecyclerView foodListFragmentRvFoods;

    public int categoryId = -1;
    public int restaurantId;

    private List<FoodCategoryData> foodCategoryDataList = new ArrayList<>();
    private List<FoodItemData> foodItemDataLis = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private FoodCategoryAdapter foodCategoryAdapter;
    private FoodItemAdapter foodItemAdapter;
    private OnEndLess onEndLess;
    private Integer maxPage;


    public FoodListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_food_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        setCategories();
        setFoodItems();
        return view;
    }

    private void setCategories() {

        linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(RecyclerView.HORIZONTAL);
        foodListFragmentRvCategories.setLayoutManager(linearLayout);


        foodCategoryAdapter = new FoodCategoryAdapter(getActivity(), foodCategoryDataList, this);
        foodListFragmentRvCategories.setAdapter(foodCategoryAdapter);

        Call<FoodCategory> foodCategoryCall = getClient().foodCategoryList(RestaurantDetailsContainerFragment.restaurantId);
        foodCategoryCall.enqueue(new Callback<FoodCategory>() {
            @Override
            public void onResponse(Call<FoodCategory> call, Response<FoodCategory> response) {
                foodCategoryDataList.addAll(response.body().getData());
                foodCategoryAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<FoodCategory> call, Throwable t) {

            }
        });
    }

    public void setFoodItems(){
        linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(RecyclerView.VERTICAL);
        foodListFragmentRvFoods.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getItems(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        foodListFragmentRvFoods.addOnScrollListener(onEndLess);
        foodItemAdapter = new FoodItemAdapter(getActivity(), foodItemDataLis);
        foodListFragmentRvFoods.setAdapter(foodItemAdapter);

        getItems(1);
    }

    public void getItems(int page) {
        if(page == 1){
            reInit();
        }

        Call<FoodItem> foodItemCall = getClient().restaurantFood(restaurantId, categoryId);
        foodItemCall.enqueue(new Callback<FoodItem>() {
            @Override
            public void onResponse(Call<FoodItem> call, Response<FoodItem> response) {
                if (response.body().getStatus() == 1) {
                    try {
                        maxPage = response.body().getData().getLastPage();
                        foodItemDataLis.addAll(response.body().getData().getData());
                        foodItemAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<FoodItem> call, Throwable t) {
                Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        foodItemDataLis = new ArrayList<>();
        foodItemAdapter = new FoodItemAdapter(getActivity(), foodItemDataLis);
        foodListFragmentRvFoods.setAdapter(foodItemAdapter);
    }

    @Override
    public void onBack() {
        super.onBack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
