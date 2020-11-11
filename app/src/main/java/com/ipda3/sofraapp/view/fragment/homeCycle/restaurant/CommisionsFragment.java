package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.commissions.Commissions;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;

public class CommisionsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.commisions_fragment_tv_details)
    TextView commisionsFragmentTvDetails;
    @BindView(R.id.commisions_fragment_tv_restaurant_sales)
    TextView commisionsFragmentTvRestaurantSales;
    @BindView(R.id.commisions_fragment_tv_app_commissions)
    TextView commisionsFragmentTvAppCommissions;
    @BindView(R.id.commisions_fragment_tv_paid_off)
    TextView commisionsFragmentTvPaidOff;
    @BindView(R.id.commisions_fragment_tv_remaining)
    TextView commisionsFragmentTvRemaining;
    @BindView(R.id.commisions_fragment_tv_raghy_account)
    TextView commisionsFragmentTvRaghyAccount;
    @BindView(R.id.commisions_fragment_tv_ahly_account)
    TextView commisionsFragmentTvAhlyAccount;

    public CommisionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_commisions, container, false);
        unbinder = ButterKnife.bind(this, view);

        getCommissions();

        return view;
    }

    private void getCommissions() {
        Call<Commissions> commissionsCall = getClient().getRestaurantCommissions(LoadData(getActivity(), API_TOKEN));
        startCall(commissionsCall);
    }

    private void startCall(Call<Commissions> commissionsCall) {
        commissionsCall.enqueue(new Callback<Commissions>() {
            @Override
            public void onResponse(Call<Commissions> call, Response<Commissions> response) {
                try {
                    commisionsFragmentTvRestaurantSales.setText(response.body().getData().getTotal());
                    commisionsFragmentTvAppCommissions.setText(response.body().getData().getCommissions());
                    commisionsFragmentTvPaidOff.setText(response.body().getData().getPayments());
                    commisionsFragmentTvRemaining.setText(String.valueOf(response.body().getData().getNetCommissions()));
                }catch (Exception ex){
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Commissions> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }
}