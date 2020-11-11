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
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ClientOrderContainerFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.client_order_container_fragment_tl_tap)
    TabLayout clientOrderContainerFragmentTlTap;
    @BindView(R.id.client_order_container_container_fragment_vp_view_pager)
    ViewPager clientOrderContainerContainerFragmentVpViewPager;
    @BindView(R.id.client_order_container_container_fragment_fl_frame)
    FrameLayout clientOrderContainerContainerFragmentFlFrame;


    ViewPagerWithFragmentAdapter viewPager;
    private ClientMyOrdersFragment currentOrders, pendingOrders, previousOrders;

    public ClientOrderContainerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_order_continer, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        viewPager = new ViewPagerWithFragmentAdapter(getChildFragmentManager());
        pendingOrders = new ClientMyOrdersFragment();
        currentOrders = new ClientMyOrdersFragment();
        previousOrders = new ClientMyOrdersFragment();

        pendingOrders.state = "pending";
        currentOrders.state = "current";
        previousOrders.state = "completed";

        viewPager.addPager(pendingOrders, getString(R.string.new_orders));
        viewPager.addPager(currentOrders, getString(R.string.current_Orders));
        viewPager.addPager(previousOrders, getString(R.string.previous_orders));

        clientOrderContainerContainerFragmentVpViewPager.setAdapter(viewPager);
        clientOrderContainerFragmentTlTap.setupWithViewPager(clientOrderContainerContainerFragmentVpViewPager);
    }

    @Override
    public void onBack() {
        super.onBack();
        baseActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}