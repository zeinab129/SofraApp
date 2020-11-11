package com.ipda3.sofraapp.view.fragment.homeCycle.more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.settings.Settings;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;

public class AboutAppFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.about_app_fragment_tv_desc)
    TextView aboutAppFragmentTvDesc;

    public AboutAppFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about_app, container, false);
        unbinder = ButterKnife.bind(this, view);

        getAboutApp();
        return view;
    }

    private void getAboutApp() {
        getClient().getSettings().enqueue(new Callback<Settings>() {
            @Override
            public void onResponse(Call<Settings> call, Response<Settings> response) {
                try {
                    if(response.body().getStatus() == 1){
                        aboutAppFragmentTvDesc.setText(response.body().getData().getAboutApp());
                    }
                }catch (Exception ex){
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Settings> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
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