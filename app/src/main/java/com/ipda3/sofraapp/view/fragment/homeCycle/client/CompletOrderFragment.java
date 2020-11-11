package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.CompleteOrderDetailsAdapter;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsListData;
import com.ipda3.sofraapp.data.room.database.CartDatabase;
import com.ipda3.sofraapp.data.room.model.Cart;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.onPermission;


public class CompletOrderFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.complete_order_fragment_iv_restaurant_image)
    CircleImageView completeOrderFragmentIvRestaurantImage;
    @BindView(R.id.complete_order_fragment_tv_restaurant_name)
    TextView completeOrderFragmentTvRestaurantName;
    @BindView(R.id.complete_order_fragment_tv_date)
    TextView completeOrderFragmentTvDate;
    @BindView(R.id.complete_order_fragment_tv_address)
    TextView completeOrderFragmentTvAddress;
    @BindView(R.id.complete_order_fragment_rv_food_list)
    RecyclerView completeOrderFragmentRvFoodList;
    @BindView(R.id.complete_order_fragment_tv_order_cost)
    TextView completeOrderFragmentTvOrderCost;
    @BindView(R.id.complete_order_fragment_tv_delivery_cost)
    TextView completeOrderFragmentTvDeliveryCost;
    @BindView(R.id.complete_order_fragment_tv_total_cost)
    TextView completeOrderFragmentTvTotalCost;
    @BindView(R.id.complete_order_fragment_tv_payment_method)
    TextView completeOrderFragmentTvPaymentMethod;

    RestaurantsListData restaurant;
    List<Cart> cartList = new ArrayList<>();
    CompleteOrderDetailsAdapter completeOrderDetailsAdapter;
    LinearLayoutManager linearLayoutManager;

    public CompletOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_complet_order, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {

        cartList = CartDatabase.getInstance(getActivity()).cartDao().getAllCartItems();
        completeOrderDetailsAdapter = new CompleteOrderDetailsAdapter(getContext(), cartList);
        CartDatabase.getInstance(getActivity()).cartDao().deleteAllCartItems();
        restaurant = RestaurantDetailsContainerFragment.restaurant;

        linearLayoutManager = new LinearLayoutManager(getActivity());
        completeOrderFragmentRvFoodList.setLayoutManager(linearLayoutManager);
        completeOrderFragmentRvFoodList.setAdapter(completeOrderDetailsAdapter);

        onLoadImageFromUrl(completeOrderFragmentIvRestaurantImage, restaurant.getPhotoUrl(), getActivity());
        completeOrderFragmentTvRestaurantName.setText(restaurant.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM ',' HH:mm");
        String currentDateAndTime = sdf.format(new Date());

        completeOrderFragmentTvDate.setText(currentDateAndTime);

        completeOrderFragmentTvAddress.setText(ConfirmOrderDetailsFragment.address);
        completeOrderFragmentTvOrderCost.setText(String.valueOf(ConfirmOrderDetailsFragment.total));
        completeOrderFragmentTvDeliveryCost.setText(String.valueOf(ConfirmOrderDetailsFragment.deliveryCost));
        completeOrderFragmentTvTotalCost.setText(String.valueOf(ConfirmOrderDetailsFragment.totalCost));
        if (ConfirmOrderDetailsFragment.payment == "1") {
            completeOrderFragmentTvPaymentMethod.setText(getString(R.string.cash));
        } else {
            completeOrderFragmentTvPaymentMethod.setText(getString(R.string.online));
        }


    }

    @OnClick({R.id.complete_order_fragment_btn_refuse, R.id.complete_order_fragment_btn_accept, R.id.complete_order_fragment_btn_call})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.complete_order_fragment_btn_refuse:
                refuseOrderDelivery();
                break;
            case R.id.complete_order_fragment_btn_accept:
                acceptOrderDelivery();
                break;
            case R.id.complete_order_fragment_btn_call:
                onPermission(getActivity());
                getActivity().startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurant.getPhone(), null)));
                break;
        }
    }

    private void refuseOrderDelivery() {
    }

    private void acceptOrderDelivery() {

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