package com.ipda3.sofraapp.view.fragment.homeCycle.more;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.contactUs.ContactUs;
import com.ipda3.sofraapp.view.activity.AuthCycleActivity;
import com.ipda3.sofraapp.view.activity.SplashCycleActivity;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ClientOffersListFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.RestaurantReviewsFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantOffersFragment;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.clean;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;


public class RestaurantMoreFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_more_fragment_tv_comments_and_reviews)
    TextView restaurantMoreFragmentTvCommentsAndReviews;
    @BindView(R.id.restaurant_more_fragment_tv_my_offers)
    TextView restaurantMoreFragmentTvMyOffers;
    @BindView(R.id.restaurant_more_fragment_view_comments_and_reviews)
    View restaurantMoreFragmentViewCommentsAndReviews;

    public static Dialog dialog;

    RestaurantOffersFragment restaurantOffersFragment;
    ClientOffersListFragment clientOffersListFragment;
    RestaurantChangePasswordFragment restaurantChangePasswordFragment;
    RestaurantReviewsFragment restaurantReviewsFragment;
    ContactUsFragment contactUsFragment;
    AboutAppFragment aboutAppFragment;

    boolean isClient;



    public RestaurantMoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_more, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (LoadBoolean(getActivity(), CLIENT)) {
            isClient = true;
            restaurantMoreFragmentTvCommentsAndReviews.setVisibility(View.GONE);
            restaurantMoreFragmentViewCommentsAndReviews.setVisibility(View.GONE);
            restaurantMoreFragmentTvMyOffers.setText(getString(R.string.offers));

        } else {
            isClient = false;
        }

        return view;
    }


    @OnClick({R.id.restaurant_more_fragment_tv_my_offers, R.id.restaurant_more_fragment_tv_contact_us, R.id.restaurant_more_fragment_tv_about_app, R.id.restaurant_more_fragment_tv_comments_and_reviews, R.id.restaurant_more_fragment_tv_change_pass, R.id.restaurant_more_fragment_tv_sign_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.restaurant_more_fragment_tv_my_offers:
                if(isClient){
                    clientOffersListFragment = new ClientOffersListFragment();
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, clientOffersListFragment);
                }else{
                    restaurantOffersFragment = new RestaurantOffersFragment();
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantOffersFragment);
                }

                break;
            case R.id.restaurant_more_fragment_tv_contact_us:
                contactUsFragment = new ContactUsFragment();
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, contactUsFragment);
                break;
            case R.id.restaurant_more_fragment_tv_about_app:
                aboutAppFragment = new AboutAppFragment();
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, aboutAppFragment);
                break;
            case R.id.restaurant_more_fragment_tv_comments_and_reviews:
                restaurantReviewsFragment = new RestaurantReviewsFragment();
                restaurantReviewsFragment.restaurant = true;
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantReviewsFragment);
                break;
            case R.id.restaurant_more_fragment_tv_change_pass:
                restaurantChangePasswordFragment = new RestaurantChangePasswordFragment();
                replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantChangePasswordFragment);
                break;
            case R.id.restaurant_more_fragment_tv_sign_out:
                dialog = new Dialog(Objects.requireNonNull(getActivity()));
                dialog.setContentView(R.layout.dialod_sign_out);
                ImageButton ibCancel = dialog.findViewById(R.id.dialog_sign_out_ib_cancel);
                ImageButton ibOkay = dialog.findViewById(R.id.dialog_sign_out_ib_okay);

                ibCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                ibOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clean(getActivity());
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), SplashCycleActivity.class);
                        Toast.makeText(getActivity(), R.string.signed_out, Toast.LENGTH_LONG).show();
                        startActivity(intent);
                    }
                });

                dialog.show();

                break;
        }
    }

    @Override
    public void onBack() {
        super.onBack();
        baseActivity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

}
