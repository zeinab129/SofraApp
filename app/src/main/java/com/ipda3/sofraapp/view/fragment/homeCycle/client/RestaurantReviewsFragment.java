package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.ReviewItemAdapter;
import com.ipda3.sofraapp.data.model.clientReview.ClientReview;
import com.ipda3.sofraapp.data.model.restaurantReviews.RestaurantReviews;
import com.ipda3.sofraapp.data.model.restaurantReviews.RestaurantReviewsData;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

public class RestaurantReviewsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_reviews_fragment_rv_reviews_list)
    RecyclerView restaurantReviewsFragmentRvReviewsList;
    @BindView(R.id.restaurant_reviews_fragment_tv_title)
    TextView restaurantReviewsFragmentTvTitle;
    @BindView(R.id.restaurant_reviews_fragment_btn_add_review)
    Button restaurantReviewsFragmentBtnAddReview;
    @BindView(R.id.restaurant_reviews_fragment_view_line)
    View restaurantReviewsFragmentViewLine;


    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private Integer maxPage = 0;
    private ReviewItemAdapter reviewItemAdapter;
    private List<RestaurantReviewsData> restaurantReviewsDataList = new ArrayList<>();
    private int addRate = 3;
    public static Dialog dialog;

    public boolean restaurant = false;

    public RestaurantReviewsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_restaurant_reviews, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();
        if (restaurant) {
            restaurantReviewsFragmentTvTitle.setVisibility(View.GONE);
            restaurantReviewsFragmentBtnAddReview.setVisibility(View.GONE);
            restaurantReviewsFragmentViewLine.setVisibility(View.GONE);
        }

        return view;
    }

    public void init() {

        linearLayout = new LinearLayoutManager(getActivity());
        restaurantReviewsFragmentRvReviewsList.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getReviews(current_page);
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        restaurantReviewsFragmentRvReviewsList.addOnScrollListener(onEndLess);
        reviewItemAdapter = new ReviewItemAdapter(getActivity(), restaurantReviewsDataList);
        restaurantReviewsFragmentRvReviewsList.setAdapter(reviewItemAdapter);

        getReviews(1);
    }

    private void getReviews(int page) {
        Call<RestaurantReviews> restaurantReviewsCall = getClient().restaurantReviews(RestaurantDetailsContainerFragment.restaurantId, page);
        startCall(restaurantReviewsCall, page);
    }

    private void startCall(Call<RestaurantReviews> restaurantReviewsCall, int page) {
        if (page == 1) {
            reInit();
        }
        restaurantReviewsCall.enqueue(new Callback<RestaurantReviews>() {
            @Override
            public void onResponse(Call<RestaurantReviews> call, Response<RestaurantReviews> response) {
                if (response.body().getStatus() == 1) {
                    try {
                        maxPage = response.body().getData().getLastPage();
                        restaurantReviewsDataList.addAll(response.body().getData().getData());
                        reviewItemAdapter.notifyDataSetChanged();

                    } catch (Exception e) {

                    }
                }
            }

            @Override
            public void onFailure(Call<RestaurantReviews> call, Throwable t) {

            }
        });
    }

    private void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        restaurantReviewsDataList = new ArrayList<>();
        reviewItemAdapter = new ReviewItemAdapter(getActivity(), restaurantReviewsDataList);
        restaurantReviewsFragmentRvReviewsList.setAdapter(reviewItemAdapter);
    }

    @OnClick(R.id.restaurant_reviews_fragment_btn_add_review)
    public void onViewClicked(View view) {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.dialog_review);

        ImageButton rate1 = dialog.findViewById(R.id.dialog_review_ib_imo1);
        ImageButton rate2 = dialog.findViewById(R.id.dialog_review_ib_imo2);
        ImageButton rate3 = dialog.findViewById(R.id.dialog_review_ib_imo3);
        ImageButton rate4 = dialog.findViewById(R.id.dialog_review_ib_imo4);
        ImageButton rate5 = dialog.findViewById(R.id.dialog_review_ib_imo5);

        EditText comment = dialog.findViewById(R.id.dialog_review_et_comment);
        Button btnAdd = dialog.findViewById(R.id.dialog_review_btn_add);


        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.dialog_review_ib_imo1:
                        addRate = 1;
                        break;
                    case R.id.dialog_review_ib_imo2:
                        addRate = 2;
                        break;
                    case R.id.dialog_review_ib_imo3:
                        addRate = 3;
                        break;
                    case R.id.dialog_review_ib_imo4:
                        addRate = 4;
                        break;
                    case R.id.dialog_review_ib_imo5:
                        addRate = 5;
                        break;
                }
                Toast.makeText(getActivity(), addRate+"", Toast.LENGTH_SHORT).show();
            }
        };

        rate1.setOnClickListener(listener);
        rate2.setOnClickListener(listener);
        rate3.setOnClickListener(listener);
        rate4.setOnClickListener(listener);
        rate5.setOnClickListener(listener);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().addRestaurantReview(
                        addRate,
                        comment.getText().toString().trim(),
                        RestaurantDetailsContainerFragment.restaurantId,
                        LoadData(getActivity(), API_TOKEN)).enqueue(new Callback<ClientReview>() {
                    @Override
                    public void onResponse(Call<ClientReview> call, Response<ClientReview> response) {
                        try {
                            if(response.body().getStatus() == 1){
                                Toast.makeText(getActivity(), R.string.review_added_successfully, Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (Exception ex) {
                            Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ClientReview> call, Throwable t) {
                        Toast.makeText(getActivity(), R.string.failed, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dialog.show();

    }

    @Override
    public void onResume() {
        super.onResume();
        reInit();
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
