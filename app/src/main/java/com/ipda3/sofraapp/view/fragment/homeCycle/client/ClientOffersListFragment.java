package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.ClientOffersAdapter;
import com.ipda3.sofraapp.adapter.RestaurantCategoriesAdapter;
import com.ipda3.sofraapp.adapter.RestaurantsListAdapter;
import com.ipda3.sofraapp.data.model.offers.Offers;
import com.ipda3.sofraapp.data.model.offers.OffersData;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsList;
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

public class ClientOffersListFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.client_offers_list_fragment_rv_offers_list)
    RecyclerView clientOffersListFragmentRvOffersList;

    ClientOffersAdapter clientOffersAdapter;
    LinearLayoutManager linearLayoutManager;
    List<OffersData> offersDataList;
    private OnEndLess onEndLess;
    private int maxPage = 1;



    public ClientOffersListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_offers_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        offersDataList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        clientOffersListFragmentRvOffersList.setLayoutManager(linearLayoutManager);

        onEndLess = new OnEndLess(linearLayoutManager, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getOffersList(current_page);

                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        clientOffersListFragmentRvOffersList.addOnScrollListener(onEndLess);
        clientOffersAdapter = new ClientOffersAdapter(getActivity(), offersDataList);
        clientOffersListFragmentRvOffersList.setAdapter(clientOffersAdapter);

        getOffersList(1);
    }

    private void getOffersList(int page) {
        Call<Offers> offersCall = getClient().getClientOffers(page);
        startCall(offersCall);
    }

    private void startCall(Call<Offers> offersCall) {
        offersCall.enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                try{
                    if (response.body().getStatus() == 1) {

                        maxPage = response.body().getData().getLastPage();
                        offersDataList.addAll(response.body().getData().getData());
                        clientOffersAdapter.notifyDataSetChanged();
                    }
                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        offersDataList = new ArrayList<>();
        clientOffersAdapter = new ClientOffersAdapter(getActivity(), offersDataList);
        clientOffersListFragmentRvOffersList.setAdapter(clientOffersAdapter);
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