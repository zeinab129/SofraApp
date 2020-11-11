package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.offers.Offers;
import com.ipda3.sofraapp.data.model.offers.OffersData;
import com.ipda3.sofraapp.data.model.user.UserData;
import com.ipda3.sofraapp.helper.DateTxt;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.loadUserData;
import static com.ipda3.sofraapp.helper.HelperMethod.convertFileToMultipart;
import static com.ipda3.sofraapp.helper.HelperMethod.convertToRequestBody;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.openGallery;
import static com.ipda3.sofraapp.helper.HelperMethod.showCalender;

public class RestaurantAddOfferFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_add_offer_fragment_tv_title)
    TextView restaurantAddOfferFragmentTvTitle;
    @BindView(R.id.restaurant_add_offer_fragment_et_offer_name)
    EditText restaurantAddOfferFragmentEtOfferName;
    @BindView(R.id.restaurant_add_offer_fragment_et_offer_desc)
    EditText restaurantAddOfferFragmentEtOfferDesc;
    @BindView(R.id.restaurant_add_offer_fragment_et_date_from)
    EditText restaurantAddOfferFragmentEtDateFrom;
    @BindView(R.id.restaurant_add_offer_fragment_et_date_to)
    EditText restaurantAddOfferFragmentEtDateTo;
    @BindView(R.id.restaurant_add_offer_fragment_ll_date)
    LinearLayout restaurantAddOfferFragmentLlDate;
    @BindView(R.id.restaurant_add_offer_fragment_btn_add)
    Button restaurantAddOfferFragmentBtnAdd;
    @BindView(R.id.restaurant_add_offer_fragment_iv_image)
    ImageView restaurantAddOfferFragmentIvImage;
    @BindView(R.id.restaurant_add_offer_fragment_et_price)
    EditText restaurantAddOfferFragmentEtPrice;
    @BindView(R.id.restaurant_add_offer_fragment_et_offer_price)
    EditText restaurantAddOfferFragmentEtOfferPrice;


    private UserData userData;
    private String path;
    private ArrayList<AlbumFile> imagesList = new ArrayList<>();
    private DateTxt dateFrom, dateTo;

    public boolean update = false;
    public OffersData restaurantOffersData;

    public RestaurantAddOfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_restaurant_add_offer, container, false);
        unbinder = ButterKnife.bind(this, view);

        if(update == true){
            restaurantAddOfferFragmentEtOfferName.setText(restaurantOffersData.getName());
            restaurantAddOfferFragmentEtPrice.setText(restaurantOffersData.getPrice() + 10);
            restaurantAddOfferFragmentEtOfferPrice.setText(restaurantOffersData.getPrice());
            restaurantAddOfferFragmentEtOfferDesc.setText(restaurantOffersData.getDescription());
            restaurantAddOfferFragmentEtDateFrom.setText(restaurantOffersData.getStartingAt());
            restaurantAddOfferFragmentEtDateTo.setText(restaurantOffersData.getEndingAt());
            onLoadImageFromUrl(restaurantAddOfferFragmentIvImage, restaurantOffersData.getPhotoUrl(), getActivity());

            restaurantAddOfferFragmentBtnAdd.setText(getString(R.string.edit));
            restaurantAddOfferFragmentTvTitle.setText(R.string.offer_image);
        }

        userData = loadUserData(getActivity());
        setDates();

        return view;
    }

    private void setDates() {

        DecimalFormat mFormat = new DecimalFormat("00");
        Calendar calendar = Calendar.getInstance();
        String cDay = mFormat.format(Double.valueOf(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH))));
        String cMonth = mFormat.format(Double.valueOf(String.valueOf(calendar.get(Calendar.MONTH + 1))));
        String cYear = String.valueOf(calendar.get(Calendar.YEAR));

        dateFrom = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
        dateTo = new DateTxt(cDay, cMonth, cYear, cDay + "-" + cMonth + "-" + cYear);
    }

    @OnClick({R.id.restaurant_add_offer_fragment_iv_image, R.id.restaurant_add_offer_fragment_btn_add, R.id.restaurant_add_offer_fragment_et_date_from, R.id.restaurant_add_offer_fragment_et_date_to})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.restaurant_add_offer_fragment_iv_image:
                openGallery(getActivity(), 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagesList.clear();
                        imagesList = result;
                        path = imagesList.get(0).getPath();
                        onLoadImageFromUrl(restaurantAddOfferFragmentIvImage, path, getActivity());
                    }
                });
                break;
            case R.id.restaurant_add_offer_fragment_btn_add:
                String offerName, briefDesc, offerDateFrom, offerDateTo, price, offerPrice;

                offerName = restaurantAddOfferFragmentEtOfferName.getText().toString().trim();
                briefDesc = restaurantAddOfferFragmentEtOfferDesc.getText().toString().trim();
                offerDateFrom = restaurantAddOfferFragmentEtDateFrom.getText().toString().trim();
                offerDateTo = restaurantAddOfferFragmentEtDateTo.getText().toString().trim();
                price = restaurantAddOfferFragmentEtPrice.getText().toString().trim();
                offerPrice = restaurantAddOfferFragmentEtOfferPrice.getText().toString().trim();

                if(update == false){
                    Call<Offers> restaurantAddOfferCall = getClient().restaurantAddOffer(
                            convertToRequestBody(briefDesc),
                            convertToRequestBody(price),
                            convertToRequestBody(offerDateFrom),
                            convertToRequestBody(offerName),
                            convertFileToMultipart(path, "photo"),
                            convertToRequestBody(offerDateTo),
                            convertToRequestBody(LoadData(getActivity(), API_TOKEN)),
                            convertToRequestBody(offerPrice));

                    startCall(restaurantAddOfferCall);
                }else {
                    Call<Offers> restaurantUpdateOfferCall = getClient().restaurantUpdateOffer(
                            convertToRequestBody(briefDesc),
                            convertToRequestBody(offerPrice),
                            convertToRequestBody(offerDateFrom),
                            convertToRequestBody(offerName),
                            convertFileToMultipart(path, "photo"),
                            convertToRequestBody(offerDateTo),
                            convertToRequestBody(Integer.toString(restaurantOffersData.getId())),
                            convertToRequestBody(LoadData(getActivity(), API_TOKEN)));
                    startCall(restaurantUpdateOfferCall);
                }

                break;

            case R.id.restaurant_add_offer_fragment_et_date_from:
                showCalender(getActivity(), getString(R.string.select_date), restaurantAddOfferFragmentEtDateFrom, dateFrom);
                break;
            case R.id.restaurant_add_offer_fragment_et_date_to:
                showCalender(getActivity(), getString(R.string.select_date), restaurantAddOfferFragmentEtDateTo, dateTo);
                break;
        }
    }

    private void startCall(Call<Offers> restaurantAddOfferCall) {
        restaurantAddOfferCall.enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                try {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if(response.body().getStatus() == 1){
                        onBack();
                    }
                }catch (Exception ex){
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {
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