package com.ipda3.sofraapp.view.fragment.homeCycle.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.resetPassword.ResetPassword;
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
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;

public class RestaurantChangePasswordFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_change_password_fragment_et_old_password)
    EditText restaurantChangePasswordFragmentEtOldPassword;
    @BindView(R.id.restaurant_change_password_fragment_et_new_password)
    EditText restaurantChangePasswordFragmentEtNewPassword;
    @BindView(R.id.restaurant_change_password_fragment_et_confirm_new_password)
    EditText restaurantChangePasswordFragmentEtConfirmNewPassword;

    public RestaurantChangePasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_change_password, container, false);
        unbinder = ButterKnife.bind(this, view);


        return view;
    }


    @OnClick(R.id.restaurant_change_password_fragment_btn_change)
    public void onViewClicked() {
        if(LoadBoolean(getActivity(), CLIENT)){

            Call<ResetPassword> clientChangePasswordCall = getClient().clientChangePassword(
                    LoadData(getActivity(), API_TOKEN),
                    restaurantChangePasswordFragmentEtOldPassword.getText().toString().trim(),
                    restaurantChangePasswordFragmentEtNewPassword.getText().toString().trim(),
                    restaurantChangePasswordFragmentEtConfirmNewPassword.getText().toString().trim());

            startCall(clientChangePasswordCall);

        }else{
            Call<ResetPassword> restaurantChangePasswordCall = getClient().restaurantChangePassword(
                    LoadData(getActivity(), API_TOKEN),
                    restaurantChangePasswordFragmentEtOldPassword.getText().toString().trim(),
                    restaurantChangePasswordFragmentEtNewPassword.getText().toString().trim(),
                    restaurantChangePasswordFragmentEtConfirmNewPassword.getText().toString().trim());

            startCall(restaurantChangePasswordCall);
        }
    }

    private void startCall(Call<ResetPassword> restaurantChangePasswordCall) {
        restaurantChangePasswordCall.enqueue(new Callback<ResetPassword>() {
            @Override
            public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                try {
                    Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }catch (Exception ex){
                    Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResetPassword> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
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