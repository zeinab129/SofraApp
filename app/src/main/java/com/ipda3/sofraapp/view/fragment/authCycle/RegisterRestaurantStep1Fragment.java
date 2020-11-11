package com.ipda3.sofraapp.view.fragment.authCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ipda3.sofraapp.adapter.SpinnerAdapter;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.helper.GeneralRequest.getData;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class RegisterRestaurantStep1Fragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.register_restaurant_step1_fragment_et_rest_name)
    EditText registerRestaurantStep1FragmentEtRestName;
    @BindView(R.id.register_restaurant_step1_fragment_et_email)
    EditText registerRestaurantStep1FragmentEtEmail;
    @BindView(R.id.register_restaurant_step1_fragment_et_delivery_time)
    EditText registerRestaurantStep1FragmentEtDeliveryTime;
    @BindView(R.id.register_restaurant_step1_fragment_sp_city_spinner)
    Spinner registerRestaurantStep1FragmentSpCitySpinner;
    @BindView(R.id.register_restaurant_step1_fragment_sp_region_spinner)
    Spinner registerRestaurantStep1FragmentSpRegionSpinner;
    @BindView(R.id.register_restaurant_step1_fragment_rl_region_spinner)
    RelativeLayout registerRestaurantStep1FragmentRlRegionSpinner;
    @BindView(R.id.register_restaurant_step1_fragment_et_password)
    EditText registerRestaurantStep1FragmentEtPassword;
    @BindView(R.id.register_restaurant_step1_fragment_et_confirm_password)
    EditText registerRestaurantStep1FragmentEtConfirmPassword;
    @BindView(R.id.register_restaurant_step1_fragment_et_min_order_cost)
    EditText registerRestaurantStep1FragmentEtMinOrderCost;
    @BindView(R.id.register_restaurant_step1_fragment_et_delivery_cost)
    EditText registerRestaurantStep1FragmentEtDeliveryCost;


    private SpinnerAdapter cityAdapter, regionAdapter;
    private AdapterView.OnItemSelectedListener listener;
    private RegisterRestaurantStep2Fragment registerRestaurantStep2Fragment = new RegisterRestaurantStep2Fragment();

    public RegisterRestaurantStep1Fragment() {
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
        View view = inflater.inflate(R.layout.fragment_register_restaurant_step1, container, false);
        unbinder = ButterKnife.bind(this, view);

        setSpinners();

        return view;
    }

    public void setSpinners(){
        cityAdapter = new SpinnerAdapter(getActivity());
        regionAdapter = new SpinnerAdapter(getActivity());

        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    registerRestaurantStep1FragmentRlRegionSpinner.setVisibility(View.VISIBLE);
                    getData(getClient().getRegions(cityAdapter.selectedId), regionAdapter, getString(R.string.region), registerRestaurantStep1FragmentSpRegionSpinner);
                } else {
                    registerRestaurantStep1FragmentRlRegionSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        getData(getClient().getCities(), cityAdapter, getString(R.string.city), registerRestaurantStep1FragmentSpCitySpinner, listener);
    }

    @OnClick(R.id.register_restaurant_step1_fragment_btn_continue)
    public void onViewClicked() {
        String restName = registerRestaurantStep1FragmentEtRestName.getText().toString().trim();
        String email = registerRestaurantStep1FragmentEtEmail.getText().toString().trim();
        String deliveryTime = registerRestaurantStep1FragmentEtDeliveryTime.getText().toString().trim();
        int cityId = cityAdapter.selectedId;
        int regionId = regionAdapter.selectedId;
        String pass = registerRestaurantStep1FragmentEtPassword.getText().toString().trim();
        String confPass = registerRestaurantStep1FragmentEtConfirmPassword.getText().toString().trim();
        String minOrderCost = registerRestaurantStep1FragmentEtMinOrderCost.getText().toString().trim();
        String deliveryCost = registerRestaurantStep1FragmentEtDeliveryCost.getText().toString().trim();

        Bundle bundle = new Bundle();
        bundle.putString("name", restName);
        bundle.putString("email", email);
        bundle.putString("password", pass);
        bundle.putString("confirmPassword", confPass);
        bundle.putString("deliveryTime", deliveryTime);
        bundle.putString("minOrderCost", minOrderCost);
        bundle.putString("deliveryCost", deliveryCost);
        bundle.putInt("cityId", cityId);
        bundle.putInt("regionId", regionId);

        registerRestaurantStep2Fragment.setArguments(bundle);

        replaceFragment(getActivity().getSupportFragmentManager(), R.id.auth_cycle_activity, registerRestaurantStep2Fragment);
    }
}
