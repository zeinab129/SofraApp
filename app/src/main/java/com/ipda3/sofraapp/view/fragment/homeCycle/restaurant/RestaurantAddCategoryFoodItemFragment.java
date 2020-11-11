package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.myCategories.MyCategoriesData;
import com.ipda3.sofraapp.data.model.restaurantAddFoodItem.RestaurantAddFoodItem;
import com.ipda3.sofraapp.data.model.restaurantItems.RestaurantItemsData;
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
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;


public class RestaurantAddCategoryFoodItemFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.add_restaurant_category_food_item_fragment_iv_image)
    ImageView addRestaurantCategoryFoodItemFragmentIvImage;
    @BindView(R.id.add_restaurant_category_food_item_fragment_et_name)
    EditText addRestaurantCategoryFoodItemFragmentEtName;
    @BindView(R.id.add_restaurant_category_food_item_fragment_et_desc)
    EditText addRestaurantCategoryFoodItemFragmentEtDesc;
    @BindView(R.id.add_restaurant_category_food_item_fragment_et_price)
    EditText addRestaurantCategoryFoodItemFragmentEtPrice;
    @BindView(R.id.add_restaurant_category_food_item_fragment_et_offer_price)
    EditText addRestaurantCategoryFoodItemFragmentEtOfferPrice;
    @BindView(R.id.add_restaurant_category_food_item_fragment_et_preparing_time)
    EditText addRestaurantCategoryFoodItemFragmentEtPreparingTime;
    @BindView(R.id.add_restaurant_category_food_item_fragment_btn_add)
    Button addRestaurantCategoryFoodItemFragmentBtnAdd;


    public String category;
    public boolean update = false;
    public RestaurantItemsData restaurantItemsData;

    private String path;
    private ArrayList<AlbumFile> imagesList = new ArrayList<>();

    public RestaurantAddCategoryFoodItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_add_category_food_item, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (update) {
            addRestaurantCategoryFoodItemFragmentEtName.setText(restaurantItemsData.getName());
            addRestaurantCategoryFoodItemFragmentEtDesc.setText(restaurantItemsData.getDescription());
            addRestaurantCategoryFoodItemFragmentEtPrice.setText(restaurantItemsData.getPrice());
            addRestaurantCategoryFoodItemFragmentEtOfferPrice.setText(restaurantItemsData.getOfferPrice());
            addRestaurantCategoryFoodItemFragmentEtPreparingTime.setText("20");
            onLoadImageFromUrl(addRestaurantCategoryFoodItemFragmentIvImage, restaurantItemsData.getPhotoUrl(), getContext());

            addRestaurantCategoryFoodItemFragmentBtnAdd.setText(getString(R.string.edit));
        }

        return view;
    }

    @OnClick({R.id.add_restaurant_category_food_item_fragment_iv_image, R.id.add_restaurant_category_food_item_fragment_btn_add})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.add_restaurant_category_food_item_fragment_iv_image:
                openGallery(getActivity(), 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagesList.clear();
                        imagesList = result;
                        path = imagesList.get(0).getPath();
                        onLoadImageFromUrl(addRestaurantCategoryFoodItemFragmentIvImage, path, getActivity());
                    }
                });
                break;
            case R.id.add_restaurant_category_food_item_fragment_btn_add:
                String name, desc, price, offerPrice, preTime;
                if (!update) {

                    name = addRestaurantCategoryFoodItemFragmentEtName.getText().toString().trim();
                    desc = addRestaurantCategoryFoodItemFragmentEtDesc.getText().toString().trim();
                    price = addRestaurantCategoryFoodItemFragmentEtPrice.getText().toString().trim();
                    offerPrice = addRestaurantCategoryFoodItemFragmentEtOfferPrice.getText().toString().trim();
                    preTime = addRestaurantCategoryFoodItemFragmentEtPreparingTime.getText().toString().trim();

                    Call<RestaurantAddFoodItem> restaurantAddFoodItemCall = getClient().restaurantAddFoodItem(
                            convertToRequestBody(desc),
                            convertToRequestBody(price),
                            convertToRequestBody(preTime),
                            convertToRequestBody(name),
                            convertFileToMultipart(path, "photo"),
                            convertToRequestBody(LoadData(getActivity(), API_TOKEN)),
                            convertToRequestBody(offerPrice),
                            convertToRequestBody(category));

                    restaurantAddFoodItemCall.enqueue(new Callback<RestaurantAddFoodItem>() {
                        @Override
                        public void onResponse(Call<RestaurantAddFoodItem> call, Response<RestaurantAddFoodItem> response) {
                            try {
                                Toast.makeText(getContext(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                onBack();
                            } catch (Exception ex) {
                                Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RestaurantAddFoodItem> call, Throwable t) {

                        }
                    });
                } else {

                    Call<RestaurantAddFoodItem> restaurantUpdateFoodItemCall = getClient().restaurantUpdateFoodItem(
                            convertToRequestBody(addRestaurantCategoryFoodItemFragmentEtDesc.getText().toString().trim()),
                            convertToRequestBody(addRestaurantCategoryFoodItemFragmentEtPrice.getText().toString().trim()),
                            convertToRequestBody(addRestaurantCategoryFoodItemFragmentEtPreparingTime.getText().toString().trim()),
                            convertToRequestBody(addRestaurantCategoryFoodItemFragmentEtName.getText().toString().trim()),
                            convertFileToMultipart(path, "photo"),
                            convertToRequestBody(Integer.toString(restaurantItemsData.getId())),
                            convertToRequestBody(LoadData(getActivity(), API_TOKEN)),
                            convertToRequestBody(addRestaurantCategoryFoodItemFragmentEtOfferPrice.getText().toString().trim()),
                            convertToRequestBody(category));

                    restaurantUpdateFoodItemCall.enqueue(new Callback<RestaurantAddFoodItem>() {
                        @Override
                        public void onResponse(Call<RestaurantAddFoodItem> call, Response<RestaurantAddFoodItem> response) {
                            try {
                                Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                            } catch (Exception ex) {
                                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }


                        @Override
                        public void onFailure(Call<RestaurantAddFoodItem> call, Throwable t) {
                            Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


                break;
        }
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
