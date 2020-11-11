package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.restaurantDetails.RestaurantDetails;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsList;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;

public class RestaurantInformationFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_information_fragment_tv_status)
    TextView restaurantInformationFragmentTvStatus;
    @BindView(R.id.restaurant_information_fragment_tv_city)
    TextView restaurantInformationFragmentTvCity;
    @BindView(R.id.restaurant_information_fragment_tv_region)
    TextView restaurantInformationFragmentTvRegion;
    @BindView(R.id.restaurant_information_fragment_tv_min_cost)
    TextView restaurantInformationFragmentTvMinCost;
    @BindView(R.id.restaurant_information_fragment_tv_delivery_cost)
    TextView restaurantInformationFragmentTvDeliveryCost;

    public RestaurantInformationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_restaurant_information, container, false);
        unbinder = ButterKnife.bind(this, view);

        getRestaurantInfo();

        return view;
    }

    private void getRestaurantInfo() {
        Call<RestaurantDetails> restaurantInfoCall = getClient().restaurantDetails(RestaurantDetailsContainerFragment.restaurantId);
        restaurantInfoCall.enqueue(new Callback<RestaurantDetails>() {
            @Override
            public void onResponse(Call<RestaurantDetails> call, Response<RestaurantDetails> response) {
                if(response.body().getStatus() == 1) {
                    restaurantInformationFragmentTvStatus.setText(response.body().getData().getAvailability());
                    restaurantInformationFragmentTvCity.setText(response.body().getData().getRegion().getCity().getName());
                    restaurantInformationFragmentTvRegion.setText(response.body().getData().getRegion().getName());
                    restaurantInformationFragmentTvMinCost.setText(response.body().getData().getMinimumCharger() + getString(R.string.riel));
                    restaurantInformationFragmentTvDeliveryCost.setText(response.body().getData().getDeliveryCost() + getString(R.string.riel));
                }else {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RestaurantDetails> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_LONG).show();
            }
        });
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
