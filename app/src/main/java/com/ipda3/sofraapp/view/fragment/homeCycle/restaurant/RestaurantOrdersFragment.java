package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.RestaurantCategoriesAdapter;
import com.ipda3.sofraapp.adapter.RestaurantOrdersListAdapter;
import com.ipda3.sofraapp.adapter.RestaurantsListAdapter;
import com.ipda3.sofraapp.data.model.restaurantOrders.RestaurantOrders;
import com.ipda3.sofraapp.data.model.restaurantOrders.RestaurantOrdersData;
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
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;

public class RestaurantOrdersFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_orders_fragment_rv_orders_list)
    RecyclerView restaurantOrdersFragmentRvOrdersList;

    public String state;
    private RestaurantOrdersListAdapter restaurantOrdersListAdapter;
    private List<RestaurantOrdersData> restaurantOrdersDataList = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private OnEndLess onEndLess;
    private int maxPage = 1;

    public RestaurantOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_orders, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        restaurantOrdersFragmentRvOrdersList.setLayoutManager(linearLayoutManager);

        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getRestaurantOrdersList(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        restaurantOrdersFragmentRvOrdersList.addOnScrollListener(onEndLess);
        restaurantOrdersListAdapter = new RestaurantOrdersListAdapter(getActivity(), restaurantOrdersDataList, state);
        restaurantOrdersFragmentRvOrdersList.setAdapter(restaurantOrdersListAdapter);

        getRestaurantOrdersList(1);
    }

    private void getRestaurantOrdersList(int page) {
        Call<RestaurantOrders> restaurantOrdersCall = getClient().getRestaurantOrders(LoadData(getActivity(), API_TOKEN), state, page);
        startCall(restaurantOrdersCall, page);
    }

    private void startCall(Call<RestaurantOrders> restaurantOrdersCall, int page) {

        restaurantOrdersCall.enqueue(new Callback<RestaurantOrders>() {
            @Override
            public void onResponse(Call<RestaurantOrders> call, Response<RestaurantOrders> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        restaurantOrdersDataList.addAll(response.body().getData().getData());
                        restaurantOrdersListAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantOrders> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        restaurantOrdersListAdapter.notifyDataSetChanged();
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