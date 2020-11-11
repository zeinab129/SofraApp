package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.ViewPagerWithFragmentAdapter;
import com.ipda3.sofraapp.data.model.restaurantDetails.RestaurantDetails;
import com.ipda3.sofraapp.data.model.restaurantDetails.RestaurantDetailsData;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsList;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsListData;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RestaurantDetailsContainerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_details_container_fragment_tl_tap)
    TabLayout restaurantDetailsContainerFragmentTlTap;
    @BindView(R.id.restaurant_details_container_container_fragment_vp_view_pager)
    ViewPager restaurantDetailsContainerContainerFragmentVpViewPager;
    @BindView(R.id.restaurant_details_container_container_fragment_fl_frame)
    FrameLayout restaurantDetailsContainerContainerFragmentFlFrame;

    FoodListFragment foodListFragment;
    RestaurantReviewsFragment restaurantReviewsFragment;
    RestaurantInformationFragment restaurantInformationFragment;

    public static int restaurantId;
    public static RestaurantsListData restaurant;

    public RestaurantDetailsContainerFragment() {
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
        View view = inflater.inflate(R.layout.fragment_restaurant_details_container, container, false);
        unbinder = ButterKnife.bind(this, view);

        ViewPagerWithFragmentAdapter adapter = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        foodListFragment = new FoodListFragment();
        restaurantReviewsFragment = new RestaurantReviewsFragment();
        restaurantInformationFragment = new RestaurantInformationFragment();

        foodListFragment.restaurantId = restaurantId;

        adapter.addPager(foodListFragment, getString(R.string.food_list));
        adapter.addPager(restaurantReviewsFragment, getString(R.string.comments_and_rate));
        adapter.addPager(restaurantInformationFragment, getString(R.string.store_info));

        restaurantDetailsContainerContainerFragmentVpViewPager.setAdapter(adapter);
        restaurantDetailsContainerFragmentTlTap.setupWithViewPager(restaurantDetailsContainerContainerFragmentVpViewPager);

        return view;
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
