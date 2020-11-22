package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.ClientOrdersListAdapter;
import com.ipda3.sofraapp.adapter.RestaurantCategoriesAdapter;
import com.ipda3.sofraapp.data.model.clientOrders.ClientOrders;
import com.ipda3.sofraapp.data.model.clientOrders.ClientOrdersData;
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

public class ClientMyOrdersFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.client_my_order_fragment_rv_orders)
    RecyclerView clientMyOrderFragmentRvOrders;


    ClientOrdersListAdapter clientOrdersListAdapter;
    List<ClientOrdersData> clientOrdersDataList;
    LinearLayoutManager linearLayoutManager;


    private OnEndLess onEndLess;
    private int maxPage = 1;

    public String state;


    public ClientMyOrdersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_my_orders, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        clientMyOrderFragmentRvOrders.setLayoutManager(linearLayoutManager);

        clientOrdersDataList = new ArrayList<>();
        clientOrdersListAdapter = new ClientOrdersListAdapter(getActivity(), clientOrdersDataList, state);

        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getOrdersList(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        clientMyOrderFragmentRvOrders.addOnScrollListener(onEndLess);
        clientMyOrderFragmentRvOrders.setAdapter(clientOrdersListAdapter);

        getOrdersList(1);
    }

    private void getOrdersList(int page) {
        getClient().getClientOrders(LoadData(getActivity(), API_TOKEN),
                state,
                page)
                .enqueue(new Callback<ClientOrders>() {
                    @Override
                    public void onResponse(Call<ClientOrders> call, Response<ClientOrders> response) {
                        try {
                            if (response.body().getStatus() == 1) {
                                clientOrdersDataList.addAll(response.body().getData().getData());
                                clientOrdersListAdapter.notifyDataSetChanged();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientOrders> call, Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        clientOrdersDataList = new ArrayList<>();
        clientOrdersListAdapter = new ClientOrdersListAdapter(getActivity(), clientOrdersDataList, state);
        clientMyOrderFragmentRvOrders.setAdapter(clientOrdersListAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        reInit();
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