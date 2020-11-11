package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.CartListAdapter;
import com.ipda3.sofraapp.data.room.database.CartDatabase;
import com.ipda3.sofraapp.data.room.model.Cart;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class CartItemsListFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.cart_items_list_fragment_rv_list)
    RecyclerView cartItemsListFragmentRvList;
    @BindView(R.id.cart_items_list_fragment_tv_total_cost)
    TextView cartItemsListFragmentTvTotalCost;

    private CartListAdapter cartListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<Cart> cartList;
    public static double total = 0;


    public CartItemsListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart_items_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        linearLayoutManager = new LinearLayoutManager(getContext());
        cartItemsListFragmentRvList.setLayoutManager(linearLayoutManager);

        cartListAdapter = new CartListAdapter(getActivity(), null);
        cartItemsListFragmentRvList.setAdapter(cartListAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        cartList = CartDatabase.getInstance(getActivity()).cartDao().getAllCartItems();
        cartListAdapter.changData(cartList);

        total = 0.0;
        for (int i = 0; i < cartList.size(); i++) {
            total = total + Double.parseDouble(cartList.get(i).getQuantity()) * Double.parseDouble(cartList.get(i).getPrice());
        }
        cartItemsListFragmentTvTotalCost.setText(String.valueOf(total));

    }

    @OnClick({R.id.cart_items_list_fragment_btn_confirm_order, R.id.cart_items_list_fragment_btn_add_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cart_items_list_fragment_btn_confirm_order:
                ConfirmOrderDetailsFragment confirmOrderDetailsFragment = new ConfirmOrderDetailsFragment();
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, confirmOrderDetailsFragment);
                break;
            case R.id.cart_items_list_fragment_btn_add_more:
//                RestaurantsListFragment restaurantsListFragment = new RestaurantsListFragment();
                RestaurantDetailsContainerFragment restaurantDetailsContainerFragment = new RestaurantDetailsContainerFragment();
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantDetailsContainerFragment);
                break;
        }
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