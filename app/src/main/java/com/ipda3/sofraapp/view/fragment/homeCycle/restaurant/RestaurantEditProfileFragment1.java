package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.SpinnerAdapter;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.sofraapp.helper.GeneralRequest.getData;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class RestaurantEditProfileFragment1 extends BaseFragment {

    Unbinder unbinder;

    @BindView(R.id.restaurant_edit_profile_fragment1_civ_image)
    CircleImageView restaurantEditProfileFragment1CivImage;
    @BindView(R.id.restaurant_edit_profile_fragment1_et_name)
    EditText restaurantEditProfileFragment1EtName;
    @BindView(R.id.restaurant_edit_profile_fragment1_et_email)
    EditText restaurantEditProfileFragment1EtEmail;
    @BindView(R.id.restaurant_edit_profile_fragment1_sp_city_spinner)
    Spinner restaurantEditProfileFragment1SpCitySpinner;
    @BindView(R.id.restaurant_edit_profile_fragment1_rl_city_spinner)
    RelativeLayout restaurantEditProfileFragment1RlCitySpinner;
    @BindView(R.id.restaurant_edit_profile_fragment1_sp_region_spinner)
    Spinner restaurantEditProfileFragment1SpRegionSpinner;
    @BindView(R.id.restaurant_edit_profile_fragment1_rl_region_spinner)
    RelativeLayout restaurantEditProfileFragment1RlRegionSpinner;
    @BindView(R.id.restaurant_edit_profile_fragment1_et_min_order_cost)
    EditText restaurantEditProfileFragment1EtMinOrderCost;


    private UserData userData;
    private AdapterView.OnItemSelectedListener listener;
    private SpinnerAdapter citySpinnerAdapter, regionSpinnerAdapter;
    private String path;
    private ArrayList<AlbumFile> imagesList=new ArrayList<>();
    private RestaurantEditProfileFragment2 restaurantEditProfileFragment2;


    public RestaurantEditProfileFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_edit_profile1, container, false);
        unbinder = ButterKnife.bind(this, view);

        userData = loadUserData(getActivity());
        setSpinners();
        setRestaurantProfileData();

        return view;
    }

    private void setRestaurantProfileData() {

        path = userData.getUser().getPhotoUrl();
        onLoadImageFromUrl(restaurantEditProfileFragment1CivImage, path, getActivity());
        restaurantEditProfileFragment1EtName.setText(userData.getUser().getName());
        restaurantEditProfileFragment1EtEmail.setText(userData.getUser().getEmail());
        restaurantEditProfileFragment1EtMinOrderCost.setText(userData.getUser().getMinimumCharger());
        restaurantEditProfileFragment1SpCitySpinner.setSelection(userData.getUser().getRegion().getCity().getId());
        restaurantEditProfileFragment1SpRegionSpinner.setSelection(userData.getUser().getRegion().getId());
    }

    private void setSpinners() {
        citySpinnerAdapter = new SpinnerAdapter(getActivity());
        regionSpinnerAdapter = new SpinnerAdapter(getActivity());

        listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    restaurantEditProfileFragment1RlRegionSpinner.setVisibility(View.VISIBLE);
                    getData(getClient().getRegions(citySpinnerAdapter.selectedId), regionSpinnerAdapter, getString(R.string.region), restaurantEditProfileFragment1SpRegionSpinner);
                } else {
                    restaurantEditProfileFragment1RlRegionSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        getData(getClient().getCities(), citySpinnerAdapter, getString(R.string.city), restaurantEditProfileFragment1SpCitySpinner, listener);
    }


    @OnClick({R.id.restaurant_edit_profile_fragment1_civ_image, R.id.restaurant_edit_profile_fragment1_btn_continue})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.restaurant_edit_profile_fragment1_civ_image:
                openGallery(getActivity(), 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagesList.clear();
                        imagesList = result;
                        path = imagesList.get(0).getPath();
                        onLoadImageFromUrl(restaurantEditProfileFragment1CivImage, path, getActivity());
                    }
                });
                break;
            case R.id.restaurant_edit_profile_fragment1_btn_continue:
                Bundle bundle = new Bundle();
                bundle.putString("photoUrl", path);
                bundle.putString("restaurantName", restaurantEditProfileFragment1EtName.getText().toString().trim());
                bundle.putString("restaurantEmail", restaurantEditProfileFragment1EtEmail.getText().toString().trim());
                bundle.putInt("cityId", citySpinnerAdapter.selectedId);
                bundle.putInt("regionId", regionSpinnerAdapter.selectedId);
                bundle.putString("restaurantMinOrderCharge", restaurantEditProfileFragment1EtMinOrderCost.getText().toString().trim());

                if(citySpinnerAdapter.selectedId == 0 || regionSpinnerAdapter.selectedId == 0){
                    Toast.makeText(getActivity(), getString(R.string.select_city_and_region_first), Toast.LENGTH_SHORT).show();
                }else{
                    restaurantEditProfileFragment2 = new RestaurantEditProfileFragment2();
                    restaurantEditProfileFragment2.setArguments(bundle);

                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, restaurantEditProfileFragment2);

                }
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
