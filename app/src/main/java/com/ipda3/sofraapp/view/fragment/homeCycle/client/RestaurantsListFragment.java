package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.RestaurantsListAdapter;
import com.ipda3.sofraapp.adapter.SpinnerAdapter;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsList;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsListData;
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
import static com.ipda3.sofraapp.helper.GeneralRequest.getData;

public class RestaurantsListFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_list_fragment_city_sp_spinner)
    Spinner restaurantListFragmentCitySpSpinner;
    @BindView(R.id.restaurant_list_fragment_rv_restaurants)
    RecyclerView restaurantListFragmentRvRestaurants;
    @BindView(R.id.restaurant_list_fragment_et_key_word)
    EditText restaurantListFragmentEtKeyWord;

    private RestaurantsListAdapter restaurantsListAdapter;
    private SpinnerAdapter citySpinnerAdapter;
    private List<RestaurantsListData> restaurantsListDataList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private int maxPage = 1;
    private boolean filter = false;


    public RestaurantsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurants_list, container, false);
        unbinder = ButterKnife.bind(this, view);

        setSpinner();
        init();

        return view;
    }

    private void setSpinner() {
        citySpinnerAdapter = new SpinnerAdapter(getActivity());
        getData(getClient().getCities(), citySpinnerAdapter, getString(R.string.city), restaurantListFragmentCitySpSpinner);
    }

    public void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        restaurantListFragmentRvRestaurants.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        if (filter) {
                            onFilter(current_page);
                        } else {
                            getRestaurantsList(current_page);
                        }
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        restaurantListFragmentRvRestaurants.addOnScrollListener(onEndLess);
        restaurantsListAdapter = new RestaurantsListAdapter(getActivity(), restaurantsListDataList);
        restaurantListFragmentRvRestaurants.setAdapter(restaurantsListAdapter);

        getRestaurantsList(1);
    }

    private void getRestaurantsList(int page) {
        Call<RestaurantsList> restaurantsListCall = getClient().restaurantsList(page);
        //postsListFragmentRlFilter.setVisibility(View.VISIBLE);
        startCall(restaurantsListCall, page);
    }

    private void onFilter(int current_page) {
        filter = true;
        Call<RestaurantsList> filteredRestaurantsListCall = getClient().restaurantsList(
                restaurantListFragmentEtKeyWord.getText().toString().trim(),
                citySpinnerAdapter.selectedId);
        startCall(filteredRestaurantsListCall, current_page);
    }

    private void startCall(Call<RestaurantsList> restaurantsListCall, int page) {

        restaurantsListCall.enqueue(new Callback<RestaurantsList>() {
            @Override
            public void onResponse(Call<RestaurantsList> call, Response<RestaurantsList> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        restaurantsListDataList.addAll(response.body().getData().getData());
                        restaurantsListAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantsList> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        restaurantsListDataList = new ArrayList<>();
        restaurantsListAdapter = new RestaurantsListAdapter(getActivity(), restaurantsListDataList);
        restaurantListFragmentRvRestaurants.setAdapter(restaurantsListAdapter);
    }

    @OnClick(R.id.restaurant_list_fragment_ib_filter)
    public void onViewClicked() {

        filter = true;
        reInit();
        onFilter(1);

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
