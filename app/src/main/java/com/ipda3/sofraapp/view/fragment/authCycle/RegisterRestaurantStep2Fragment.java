package com.ipda3.sofraapp.view.fragment.authCycle;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.user.User;
import com.ipda3.sofraapp.helper.MediaLoader;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumConfig;
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
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;

public class RegisterRestaurantStep2Fragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.register_restaurant_step2_fragment_et_phone)
    EditText registerRestaurantStep2FragmentEtPhone;
    @BindView(R.id.register_restaurant_step2_fragment_et_whatsapp)
    EditText registerRestaurantStep2FragmentEtWhatsapp;
    @BindView(R.id.register_restaurant_step2_fragment_ib_store_image)
    ImageButton registerRestaurantStep2FragmentIbStoreImage;


    String restName, pass, confPass, email, deliveryTime,  minOrderCost, deliveryCost;
    int cityId, regionId;

    private String path;
    private ArrayList<AlbumFile> imagesList=new ArrayList<>();

    public RegisterRestaurantStep2Fragment() {
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
        View view = inflater.inflate(R.layout.fragment_register_restaurant_step2, container, false);
        unbinder = ButterKnife.bind(this, view);



        return view;
    }

    @OnClick({R.id.register_restaurant_step2_fragment_ib_store_image, R.id.register_restaurant_step2_fragment_btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.register_restaurant_step2_fragment_ib_store_image:
                openGallery(getActivity(), 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagesList.clear();
                        imagesList = result;
                        path = imagesList.get(0).getPath();
                        onLoadImageFromUrl(registerRestaurantStep2FragmentIbStoreImage, path, getActivity());
                    }
                });
                break;
            case R.id.register_restaurant_step2_fragment_btn_register:

                getRegisterationData();

                break;
        }
    }

    private void getRegisterationData() {
        restName = getArguments().getString("name");
        pass = getArguments().getString("password");
        confPass = getArguments().getString("confirmPassword");
        email = getArguments().getString("email");
        deliveryTime = getArguments().getString("deliveryTime");
        minOrderCost = getArguments().getString("minOrderCost");
        deliveryCost = getArguments().getString("deliveryCost");
        cityId = getArguments().getInt("cityId");
        regionId = getArguments().getInt("regionId");

        String phone = registerRestaurantStep2FragmentEtPhone.getText().toString().trim();
        String whatsapp = registerRestaurantStep2FragmentEtWhatsapp.getText().toString().trim();

        Call<User> restaurantRegisterCall = getClient().restaurantRegister(
                convertToRequestBody(restName),
                convertToRequestBody(email),
                convertToRequestBody(pass),
                convertToRequestBody(confPass),
                convertToRequestBody(phone),
                convertToRequestBody(whatsapp),
                convertToRequestBody(Integer.toString(regionId)),
                convertToRequestBody(deliveryCost),
                convertToRequestBody(minOrderCost),
                convertFileToMultipart(path, "photo"),
                convertToRequestBody(deliveryTime));

        restaurantRegister(restaurantRegisterCall);
    }

    private void restaurantRegister(Call<User> restaurantRegisterCall) {
        restaurantRegisterCall.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                try {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
