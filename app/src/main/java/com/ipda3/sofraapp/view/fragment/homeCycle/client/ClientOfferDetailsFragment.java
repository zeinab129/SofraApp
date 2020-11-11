package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.offers.OffersData;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


public class ClientOfferDetailsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.client_offer_details_fragment_tv_name)
    TextView clientOfferDetailsFragmentTvName;
    @BindView(R.id.client_offer_details_fragment_tv_details)
    TextView clientOfferDetailsFragmentTvDetails;
    @BindView(R.id.client_offer_details_fragment_tv_from_date)
    TextView clientOfferDetailsFragmentTvFromDate;
    @BindView(R.id.client_offer_details_fragment_tv_to_date)
    TextView clientOfferDetailsFragmentTvToDate;

    public OffersData offersData;


    public ClientOfferDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_offer_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        setData();

        return view;
    }

    private void setData() {
        clientOfferDetailsFragmentTvName.setText(offersData.getName());
        clientOfferDetailsFragmentTvDetails.setText(offersData.getDescription());
        clientOfferDetailsFragmentTvFromDate.setText(offersData.getStartingAt());
        clientOfferDetailsFragmentTvToDate.setText(offersData.getEndingAt());
    }


    @OnClick(R.id.client_offer_details_fragment_btn_get_it_now)
    public void onViewClicked() {
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