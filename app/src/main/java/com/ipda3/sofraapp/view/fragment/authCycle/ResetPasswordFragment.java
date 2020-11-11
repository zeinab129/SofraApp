package com.ipda3.sofraapp.view.fragment.authCycle;

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
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.SaveData;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.USER_PASSWORD;


public class ResetPasswordFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.reset_password_fragment_et_code)
    EditText resetPasswordFragmentEtCode;
    @BindView(R.id.reset_password_fragment_et_password)
    EditText resetPasswordFragmentEtPassword;
    @BindView(R.id.reset_password_fragment_et_confirm_password)
    EditText resetPasswordFragmentEtConfirmPassword;

    public ResetPasswordFragment() {
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
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @OnClick(R.id.forget_password_fragment_btn_send)
    public void onViewClicked() {
        resetPassword();
    }

    private void resetPassword() {
        String code = resetPasswordFragmentEtCode.getText().toString().trim();
        String pass = resetPasswordFragmentEtPassword.getText().toString().trim();
        String conf = resetPasswordFragmentEtConfirmPassword.getText().toString().trim();

        if(LoadBoolean(getActivity(), CLIENT)){
            Call<ResetPassword> newPasswordCall = getClient().clientNewPassword(code, pass, conf);
            newPasswordCall.enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if(response.body().getStatus() == 1){
                        SaveData(getActivity(), USER_PASSWORD, pass);
                    }
                }

                @Override
                public void onFailure(Call<ResetPassword> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Call<ResetPassword> newPasswordCall = getClient().restaurantNewPassword(code, pass, conf);
            newPasswordCall.enqueue(new Callback<ResetPassword>() {
                @Override
                public void onResponse(Call<ResetPassword> call, Response<ResetPassword> response) {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if(response.body().getStatus() == 1){
                        SaveData(getActivity(), USER_PASSWORD, pass);
                    }
                }

                @Override
                public void onFailure(Call<ResetPassword> call, Throwable t) {
                    Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
