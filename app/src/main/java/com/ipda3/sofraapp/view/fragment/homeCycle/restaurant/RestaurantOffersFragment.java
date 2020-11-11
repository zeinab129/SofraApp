package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.RestaurantOffersAdapter;
import com.ipda3.sofraapp.data.model.offers.Offers;
import com.ipda3.sofraapp.data.model.offers.OffersData;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

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

public class RestaurantOffersFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_offers_fragment_rv_offers)
    RecyclerView restaurantOffersFragmentRvOffers;

    private RestaurantAddOfferFragment restaurantAddOfferFragment;
    private RestaurantOffersAdapter restaurantOffersAdapter;
    private List<OffersData> restaurantOffersDataList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private int maxPage;


    public RestaurantOffersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_offers, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        restaurantOffersFragmentRvOffers.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getRestaurantOffers(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        restaurantOffersFragmentRvOffers.addOnScrollListener(onEndLess);
        restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), restaurantOffersDataList);
        restaurantOffersFragmentRvOffers.setAdapter(restaurantOffersAdapter);

        getRestaurantOffers(1);
    }

    private void getRestaurantOffers(int page) {
        Call<Offers> restaurantOffersCall = getClient().getRestaurantOffers(LoadData(getActivity(), API_TOKEN), page);
        startCall(restaurantOffersCall, page);
    }

    private void startCall(Call<Offers> restaurantOffersCall, int page) {

        if(page == 1){
            reInit();
        }

        restaurantOffersCall.enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        restaurantOffersDataList.addAll(response.body().getData().getData());
                        restaurantOffersAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        restaurantOffersDataList = new ArrayList<>();
        restaurantOffersAdapter = new RestaurantOffersAdapter(getActivity(), restaurantOffersDataList);
        restaurantOffersFragmentRvOffers.setAdapter(restaurantOffersAdapter);
    }


    @OnClick(R.id.restaurant_offers_fragment_btn_add)
    public void onViewClicked() {
        restaurantAddOfferFragment =  new RestaurantAddOfferFragment();
        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantAddOfferFragment);
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