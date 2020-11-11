package com.ipda3.sofraapp.helper;

import android.app.Activity;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.SpinnerAdapter;
import com.ipda3.sofraapp.data.model.generalResponse.GeneralResponse;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.view.activity.HomeCycleActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.SaveData;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.USER_PASSWORD;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.saveUserData;
import static com.ipda3.sofraapp.helper.InternetState.isConnected;

public class GeneralRequest {

    public static void getData(Call<GeneralResponse> call, final SpinnerAdapter adapter, final String hint, Spinner spinner){

        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        adapter.setData(response.body().getData(), hint);
                        spinner.setAdapter(adapter);
                    }
                }catch (Exception e){

                }
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public static void getData(Call<GeneralResponse> call, final SpinnerAdapter adapter, final String hint, Spinner spinner, AdapterView.OnItemSelectedListener listener){
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        adapter.setData(response.body().getData(), hint);
                        spinner.setAdapter(adapter);
                        spinner.setOnItemSelectedListener(listener);
                    }
                }catch (Exception e){}
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {

            }
        });
    }

    public static void userData(final Activity activity, Call<User> method, final String password) {


        if (isConnected(activity)) {

            method.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        HelperMethod.dismissProgressDialog();

                        if (response.body().getStatus() == 1) {

                            SaveData(activity, USER_PASSWORD, password);
                            SaveData(activity, API_TOKEN, response.body().getData().getApiToken());
                            saveUserData(activity, response.body().getData());

                            Intent intent = new Intent(activity, HomeCycleActivity.class);
                            activity.startActivity(intent);
                            activity.finish();

                        }
                        Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_LONG).show();

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    HelperMethod.dismissProgressDialog();
                    Toast.makeText(activity, activity.getString(R.string.error), Toast.LENGTH_LONG).show();
                }
            });

        } else {
            try {
                Toast.makeText(activity, activity.getString(R.string.error_internet), Toast.LENGTH_LONG).show();
            } catch (Exception e) {

            }

        }

    }

}
