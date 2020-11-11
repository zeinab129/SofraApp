package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.RestaurantCategoriesAdapter;
import com.ipda3.sofraapp.adapter.RestaurantItemsAdapter;
import com.ipda3.sofraapp.data.model.myCategories.MyCategories;
import com.ipda3.sofraapp.data.model.myCategories.MyCategoriesData;
import com.ipda3.sofraapp.data.model.restaurantItems.RestaurantItems;
import com.ipda3.sofraapp.data.model.restaurantItems.RestaurantItemsData;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;


public class RestaurantItemsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_items_fragment_tv_category_name)
    TextView restaurantItemsFragmentTvCategoryName;
    @BindView(R.id.restaurant_items_fragment_rv_items_list)
    RecyclerView restaurantItemsFragmentRvItemsList;

    private RestaurantItemsAdapter restaurantItemsAdapter;
    private List<RestaurantItemsData> restaurantItemsDataList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private int maxPage;
    private RestaurantAddCategoryFoodItemFragment restaurantAddCategoryFoodItemFragment;

    public MyCategoriesData category;


    public RestaurantItemsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_items, container, false);
        unbinder = ButterKnife.bind(this, view);

        restaurantItemsFragmentTvCategoryName.setText(category.getName());

        init();

        return view;
    }

    public void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        restaurantItemsFragmentRvItemsList.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getRestaurantItems();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        restaurantItemsFragmentRvItemsList.addOnScrollListener(onEndLess);
        restaurantItemsAdapter = new RestaurantItemsAdapter(getActivity(), restaurantItemsDataList);
        restaurantItemsFragmentRvItemsList.setAdapter(restaurantItemsAdapter);

        getRestaurantItems();
    }


    public void getRestaurantItems() {
        Call<RestaurantItems> restaurantItemsCall = getClient().getRestaurantItemsList(LoadData(getActivity(), API_TOKEN), category.getId());
        startCall(restaurantItemsCall);
    }

    private void startCall(Call<RestaurantItems> restaurantItemsCall) {
        restaurantItemsCall.enqueue(new Callback<RestaurantItems>() {
            @Override
            public void onResponse(Call<RestaurantItems> call, Response<RestaurantItems> response) {
                try {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        restaurantItemsDataList.addAll(response.body().getData().getData());
                        restaurantItemsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<RestaurantItems> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        restaurantItemsDataList = new ArrayList<>();
        restaurantItemsAdapter = new RestaurantItemsAdapter(getActivity(), restaurantItemsDataList);
        restaurantItemsFragmentRvItemsList.setAdapter(restaurantItemsAdapter);

    }

    @OnClick(R.id.restaurant_items_fragment_f_a_btn_add_item)
    public void onViewClicked() {
        restaurantAddCategoryFoodItemFragment = new RestaurantAddCategoryFoodItemFragment();
        restaurantAddCategoryFoodItemFragment.category = Integer.toString(category.getId());
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantAddCategoryFoodItemFragment);
    }

    @Override
    public void onResume() {
        super.onResume();
        reInit();
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
