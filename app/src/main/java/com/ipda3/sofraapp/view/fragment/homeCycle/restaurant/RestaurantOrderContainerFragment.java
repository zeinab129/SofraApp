package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.ViewPagerWithFragmentAdapter;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ClientMyOrdersFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class RestaurantOrderContainerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_order_container_fragment_tl_tap)
    TabLayout restaurantOrderContainerFragmentTlTap;
    @BindView(R.id.restaurant_order_container_container_fragment_vp_view_pager)
    ViewPager restaurantOrderContainerContainerFragmentVpViewPager;
    @BindView(R.id.restaurant_order_container_fragment_fl_frame)
    FrameLayout restaurantOrderContainerFragmentFlFrame;

    ViewPagerWithFragmentAdapter viewPager;
    private RestaurantOrdersFragment currentOrders, pendingOrders, previousOrders;
    public RestaurantOrderContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_order_container, container, false);
        unbinder = ButterKnife.bind(this, view);

        viewPager = new ViewPagerWithFragmentAdapter(getChildFragmentManager());

        pendingOrders = new RestaurantOrdersFragment();
        currentOrders= new RestaurantOrdersFragment();
        previousOrders = new RestaurantOrdersFragment();

        pendingOrders.state = "pending";
        currentOrders.state = "current";
        previousOrders.state = "completed";

        viewPager.addPager(pendingOrders ,getString(R.string.new_orders));
        viewPager.addPager(currentOrders, getString(R.string.current_Orders));
        viewPager.addPager(previousOrders, getString(R.string.previous_orders));

        restaurantOrderContainerContainerFragmentVpViewPager.setAdapter(viewPager);
        restaurantOrderContainerFragmentTlTap.setupWithViewPager(restaurantOrderContainerContainerFragmentVpViewPager);

        return view;
    }

    @Override
    public void onBack() {
        super.onBack();
        baseActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
