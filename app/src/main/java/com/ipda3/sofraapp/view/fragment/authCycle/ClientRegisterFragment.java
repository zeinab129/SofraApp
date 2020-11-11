package com.ipda3.sofraapp.view.fragment.authCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.SpinnerAdapter;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.helper.GeneralRequest.getData;
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;

public class ClientRegisterFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.client_register_fragment_ib_client_image)
    ImageButton clientRegisterFragmentIbClientImage;
    @BindView(R.id.client_register_fragment_et_name)
    EditText clientRegisterFragmentEtName;
    @BindView(R.id.client_register_fragment_et_email)
    EditText clientRegisterFragmentEtEmail;
    @BindView(R.id.client_register_fragment_et_phone)
    EditText clientRegisterFragmentEtPhone;
    @BindView(R.id.client_register_fragment_sp_city_spinner)
    Spinner clientRegisterFragmentSpCitySpinner;
    @BindView(R.id.client_register_fragment_sp_region_spinner)
    Spinner clientRegisterFragmentSpRegionSpinner;
    @BindView(R.id.client_register_fragment_rl_region_spinner)
    RelativeLayout clientRegisterFragmentRlRegionSpinner;
    @BindView(R.id.client_register_fragment_et_password)
    EditText clientRegisterFragmentEtPassword;
    @BindView(R.id.client_register_fragment_et_confirm_password)
    EditText clientRegisterFragmentEtConfirmPassword;

    private SpinnerAdapter cityAdapter, regionAdapter;

    private String path;
    private ArrayList<AlbumFile> imagesList=new ArrayList<>();

    public ClientRegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_register, container, false);
        unbinder = ButterKnife.bind(this, view);

        setSpinners();

        return view;
    }

    private void setSpinners() {
        cityAdapter = new SpinnerAdapter(getActivity());
        regionAdapter = new SpinnerAdapter(getActivity());

        AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {
                    clientRegisterFragmentRlRegionSpinner.setVisibility(View.VISIBLE);
                    getData(getClient().getRegions(cityAdapter.selectedId), regionAdapter, getString(R.string.region), clientRegisterFragmentSpRegionSpinner);
                } else {
                    clientRegisterFragmentRlRegionSpinner.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        getData(getClient().getCities(), cityAdapter, getString(R.string.city), clientRegisterFragmentSpCitySpinner, listener);
    }

    @OnClick({R.id.client_register_fragment_ib_client_image, R.id.client_register_fragment_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.client_register_fragment_ib_client_image:
                openGallery(getActivity(), 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagesList.clear();
                        imagesList = result;
                        path = imagesList.get(0).getPath();
                        onLoadImageFromUrl(clientRegisterFragmentIbClientImage, path, getActivity());
                    }
                });
                break;
            case R.id.client_register_fragment_btn_register:
                register();
                break;
        }
    }

    private void register() {
        getClient().clientRegister(
                convertToRequestBody(clientRegisterFragmentEtName.getText().toString()),
                convertToRequestBody(clientRegisterFragmentEtEmail.getText().toString()),
                convertToRequestBody(clientRegisterFragmentEtPassword.getText().toString()),
                convertToRequestBody(clientRegisterFragmentEtConfirmPassword.getText().toString()),
                convertToRequestBody(clientRegisterFragmentEtPhone.getText().toString()),
                convertToRequestBody(String.valueOf(regionAdapter.selectedId)),
                convertFileToMultipart(path, "profile_image"))
                .enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        try {
                            Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                        }catch (Exception ex){
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}