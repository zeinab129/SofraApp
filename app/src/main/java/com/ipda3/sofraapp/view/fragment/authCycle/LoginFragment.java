package com.ipda3.sofraapp.view.fragment.authCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.sofraapp.helper.GeneralRequest.userData;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class LoginFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.login_fragment_et_email)
    EditText loginFragmentEtEmail;
    @BindView(R.id.login_fragment_et_password)
    EditText loginFragmentEtPassword;

    public boolean isClient;

    public LoginFragment() {
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
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick({R.id.login_fragment_tv_forget_password, R.id.login_fragment_btn_login, R.id.login_fragment_tv_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_fragment_tv_forget_password:
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.auth_cycle_activity, new ForgetPasswordFragment());
                break;
            case R.id.login_fragment_btn_login:
                onLogin();
                break;
            case R.id.login_fragment_tv_register:
                if (LoadBoolean(getActivity(), CLIENT)) {
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.auth_cycle_activity, new ClientRegisterFragment());
                }
                else {
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.auth_cycle_activity, new RegisterRestaurantStep1Fragment());
                }
                break;
        }
    }

    private void onLogin() {
        String email = loginFragmentEtEmail.getText().toString().trim();
        String password = loginFragmentEtPassword.getText().toString().trim();

        if(LoadBoolean(getActivity(), CLIENT)){
            Call<User> clientLoginCall = getClient().clientLogin(email, password);
            userData(getActivity(), clientLoginCall, password);
        }else {
            Call<User> restaurantLoginCall = getClient().restaurantLogin(email, password);
            userData(getActivity(), restaurantLoginCall, password);
        }



    }

    @Override
    public void onBack() {
        baseActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
