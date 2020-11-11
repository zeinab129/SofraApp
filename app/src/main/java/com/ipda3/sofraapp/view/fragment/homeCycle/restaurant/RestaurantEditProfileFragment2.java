package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.restaurantDetails.RestaurantDetails;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

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
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;

public class RestaurantEditProfileFragment2 extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_edit_profile_fragment2_et_delivery_cost)
    EditText restaurantEditProfileFragment2EtDeliveryCost;
    @BindView(R.id.restaurant_edit_profile_fragment2_et_delivery_time)
    EditText restaurantEditProfileFragment2EtDeliveryTime;
    @BindView(R.id.restaurant_edit_profile_fragment2_sc_status)
    SwitchCompat restaurantEditProfileFragment2ScStatus;
    @BindView(R.id.restaurant_edit_profile_fragment2_et_phone_number)
    EditText restaurantEditProfileFragment2EtPhoneNumber;
    @BindView(R.id.restaurant_edit_profile_fragment2_et_whatsapp)
    EditText restaurantEditProfileFragment2EtWhatsapp;

    private UserData userData;
    private static String status;

    public RestaurantEditProfileFragment2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_edit_profile2, container, false);
        unbinder = ButterKnife.bind(this, view);

        userData = loadUserData(getActivity());
        status = userData.getUser().getAvailability();

        restaurantEditProfileFragment2EtDeliveryCost.setText(userData.getUser().getDeliveryCost());
        restaurantEditProfileFragment2EtDeliveryTime.setText(userData.getUser().getDeliveryTime());
        restaurantEditProfileFragment2EtPhoneNumber.setText(userData.getUser().getPhone());
        restaurantEditProfileFragment2EtWhatsapp.setText(userData.getUser().getWhatsapp());

        if(status.equals("open")){
            restaurantEditProfileFragment2ScStatus.setChecked(true);
        }else {
            restaurantEditProfileFragment2ScStatus.setChecked(false);
        }



        return view;
    }

    @OnClick({R.id.restaurant_edit_profile_fragment2_sc_status, R.id.restaurant_edit_profile_fragment2_btn_edit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.restaurant_edit_profile_fragment2_sc_status:
                if (restaurantEditProfileFragment2ScStatus.isChecked()) {
                    status = "open";
                }else {
                    status = "closed";
                }
                break;
            case R.id.restaurant_edit_profile_fragment2_btn_edit:
                Call<User> editRestaurantProfileCall = getClient().editRestaurantProfile(
                        convertToRequestBody(getArguments().getString("restaurantEmail")),
                        convertToRequestBody(getArguments().getString("restaurantName")),
                        convertToRequestBody(restaurantEditProfileFragment2EtPhoneNumber.getText().toString().trim()),
                        convertToRequestBody(Integer.toString(getArguments().getInt("regionId"))),
                        convertToRequestBody(restaurantEditProfileFragment2EtDeliveryCost.getText().toString().trim()),
                        convertToRequestBody(getArguments().getString("restaurantMinOrderCharge")),
                        convertToRequestBody(status),
                        convertFileToMultipart(getArguments().getString("photoUrl"), "photo"),
                        convertToRequestBody(LoadData(getActivity(), API_TOKEN)),
                        convertToRequestBody(restaurantEditProfileFragment2EtDeliveryTime.getText().toString().trim()));
                editRestaurantProfileCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        try {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                });
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
