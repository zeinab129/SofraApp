package com.ipda3.sofraapp.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.CartItemsListFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ClientEditProfileFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ClientOrderContainerFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.NotificationsFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.RestaurantsListFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.more.RestaurantMoreFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.CommisionsFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantCategoriesFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantEditProfileFragment1;
import com.ipda3.sofraapp.view.fragment.homeCycle.restaurant.RestaurantOrderContainerFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class HomeCycleActivity extends BaseActivity {

    @BindView(R.id.tool_bar_ib_cart)
    ImageButton toolBarIbCart;
    @BindView(R.id.tool_bar_ib_notifications)
    ImageButton toolBarIbNotifications;
    @BindView(R.id.tool_bar_tv_title)
    TextView toolBarTvTitle;
    @BindView(R.id.home_cycle_activity_iv_home)
    ImageView homeCycleActivityIvHome;
    @BindView(R.id.home_cycle_activity_rb_home)
    RadioButton homeCycleActivityRbHome;
    @BindView(R.id.home_cycle_activity_iv_list)
    ImageView homeCycleActivityIvList;
    @BindView(R.id.home_cycle_activity_rb_list)
    RadioButton homeCycleActivityRbList;
    @BindView(R.id.home_cycle_activity_iv_profile)
    ImageView homeCycleActivityIvProfile;
    @BindView(R.id.home_cycle_activity_rb_profile)
    RadioButton homeCycleActivityRbProfile;
    @BindView(R.id.home_cycle_activity_iv_more)
    ImageView homeCycleActivityIvMore;
    @BindView(R.id.home_cycle_activity_rb_more)
    RadioButton homeCycleActivityRbMore;
    @BindView(R.id.home_cycle_activity_rg_navigation)
    RadioGroup homeCycleActivityRgNavigation;
    @BindView(R.id.tool_bar_sub_view)
    RelativeLayout toolBarSubView;

    private List<ImageView> imageViews = new ArrayList<>();


    private RestaurantsListFragment restaurantsListFragment;
    private ClientEditProfileFragment clientEditProfileFragment;
    private ClientOrderContainerFragment clientOrderContainerFragment;


    private RestaurantCategoriesFragment restaurantCategoriesFragment;
    private RestaurantEditProfileFragment1 restaurantEditProfileFragment1;
    private RestaurantOrderContainerFragment restaurantOrderContainerFragment;
    private RestaurantMoreFragment restaurantMoreFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_cycle);
        ButterKnife.bind(this);

        imageViews.add(homeCycleActivityIvHome);
        imageViews.add(homeCycleActivityIvList);
        imageViews.add(homeCycleActivityIvProfile);
        imageViews.add(homeCycleActivityIvMore);

        //client fragments
        restaurantsListFragment = new RestaurantsListFragment();
        clientEditProfileFragment = new ClientEditProfileFragment();
        clientOrderContainerFragment = new ClientOrderContainerFragment();

        //restaurant fragments
        restaurantCategoriesFragment = new RestaurantCategoriesFragment();
        restaurantEditProfileFragment1 = new RestaurantEditProfileFragment1();
        restaurantOrderContainerFragment = new RestaurantOrderContainerFragment();
        restaurantMoreFragment = new RestaurantMoreFragment();

        setNavigation(View.VISIBLE, R.id.home_cycle_activity_rb_home);
        if (LoadBoolean(HomeCycleActivity.this, CLIENT)) {
            toolBarIbCart.setImageDrawable(getResources().getDrawable(R.drawable.ic_cart));
            replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantsListFragment);
            OnCheckedChangeListener(homeCycleActivityRbHome, restaurantsListFragment, 0);
            OnCheckedChangeListener(homeCycleActivityRbList, clientOrderContainerFragment, 1);
            OnCheckedChangeListener(homeCycleActivityRbProfile, clientEditProfileFragment, 2);
            OnCheckedChangeListener(homeCycleActivityRbMore, restaurantMoreFragment, 3);
        } else {
            toolBarIbCart.setImageDrawable(getResources().getDrawable(R.drawable.ic_calculator));
            replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantCategoriesFragment);
            OnCheckedChangeListener(homeCycleActivityRbHome, restaurantCategoriesFragment, 0);
            OnCheckedChangeListener(homeCycleActivityRbList, restaurantOrderContainerFragment, 1);
            OnCheckedChangeListener(homeCycleActivityRbProfile, restaurantEditProfileFragment1, 2);
            OnCheckedChangeListener(homeCycleActivityRbMore, restaurantMoreFragment, 3);
        }

    }

    @OnClick({R.id.tool_bar_ib_cart, R.id.tool_bar_ib_notifications})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tool_bar_ib_cart:
                if (LoadBoolean(HomeCycleActivity.this, CLIENT)) {
                    CartItemsListFragment cartItemsListFragment = new CartItemsListFragment();
                    replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, cartItemsListFragment);
                }else{
                    CommisionsFragment commisionsFragment = new CommisionsFragment();
                    replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, commisionsFragment);
                }
                break;
            case R.id.tool_bar_ib_notifications:
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, notificationsFragment);
                break;
        }
    }


    public void setToolBar(int visibility, String title, View.OnClickListener backActionBtn) {
        toolBarSubView.setVisibility(visibility);

        if (visibility == View.VISIBLE) {
            toolBarTvTitle.setText(title);
        }

    }

    public void setNavigation(int visibility, int id) {

        homeCycleActivityRgNavigation.setVisibility(visibility);

        if (id != 0) {
            homeCycleActivityRgNavigation.check(id);

            switch (id) {
                case R.id.home_cycle_activity_rb_home:
                    setSelection(0);
                    break;
                case R.id.home_cycle_activity_rb_list:
                    setSelection(1);
                    break;
                case R.id.home_cycle_activity_rb_profile:
                    setSelection(2);
                    break;
                case R.id.home_cycle_activity_rb_more:
                    setSelection(3);
                    break;
            }
        }


    }

    private void OnCheckedChangeListener(final RadioButton radioButton, final Fragment fragment, final int i) {
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioButton.isChecked()) {
                    setSelection(i);
                    replaceFragment(getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, fragment);
                }
            }
        });
    }

    private void setSelection(int i) {
        for (int j = 0; j < imageViews.size(); j++) {
            if (i == j) {
                imageViews.get(j).setVisibility(View.VISIBLE);
            } else {
                imageViews.get(j).setVisibility(View.INVISIBLE);
            }
        }

//        if (i == 0 || i == 1) {
//            setToolBar(View.GONE, null, null);
//        } else if (i == 2) {
//            setToolBar(View.VISIBLE, getString(R.string.notifications), null);
//            toolbarBack.setVisibility(View.INVISIBLE);
//        } else if (i == 3) {
//            setToolBar(View.VISIBLE, getString(R.string.more), null);
//            toolbarBack.setVisibility(View.INVISIBLE);
//        }


    }


}
