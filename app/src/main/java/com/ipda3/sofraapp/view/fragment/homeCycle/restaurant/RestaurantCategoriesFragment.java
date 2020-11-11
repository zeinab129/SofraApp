package com.ipda3.sofraapp.view.fragment.homeCycle.restaurant;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.RestaurantCategoriesAdapter;
import com.ipda3.sofraapp.data.model.myCategories.MyCategories;
import com.ipda3.sofraapp.data.model.myCategories.MyCategoriesData;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.AlbumFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
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

public class RestaurantCategoriesFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.restaurant_categories_fragment_rv_categories_list)
    RecyclerView restaurantCategoriesFragmentRvCategoriesList;

    private RestaurantCategoriesAdapter restaurantCategoriesAdapter;
    private List<MyCategoriesData> myCategoriesDataList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private int maxPage;

    public static Dialog dialog;
    private String path;
    private ArrayList<AlbumFile> imagesList=new ArrayList<>();
    private EditText categoryName;
    private Button btnAddNewRestaurantCategory;

    public RestaurantCategoriesFragment() {
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
        View view = inflater.inflate(R.layout.fragment_restaurant_categories, container, false);
        unbinder = ButterKnife.bind(this, view);

        init();

        return view;
    }

    public void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        restaurantCategoriesFragmentRvCategoriesList.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getRestaurantCategories();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        restaurantCategoriesFragmentRvCategoriesList.addOnScrollListener(onEndLess);
        restaurantCategoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), myCategoriesDataList);
        restaurantCategoriesFragmentRvCategoriesList.setAdapter(restaurantCategoriesAdapter);

        getRestaurantCategories();
    }


    public void getRestaurantCategories() {
        Call<MyCategories> myCategoriesCall = getClient().getMyCategoriesList(LoadData(getActivity(), API_TOKEN));
        //postsListFragmentRlFilter.setVisibility(View.VISIBLE);
        startCall(myCategoriesCall);
    }

    private void startCall(Call<MyCategories> restaurantCategoriesCall) {
        restaurantCategoriesCall.enqueue(new Callback<MyCategories>() {
            @Override
            public void onResponse(Call<MyCategories> call, Response<MyCategories> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        myCategoriesDataList.addAll(response.body().getData().getData());
                        restaurantCategoriesAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MyCategories> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        myCategoriesDataList = new ArrayList<>();
        restaurantCategoriesAdapter = new RestaurantCategoriesAdapter(getActivity(), myCategoriesDataList);
        restaurantCategoriesFragmentRvCategoriesList.setAdapter(restaurantCategoriesAdapter);
    }

    @OnClick(R.id.restaurant_categories_fragment_f_a_btn_add_category)
    public void onViewClicked() {
        dialog = new Dialog(Objects.requireNonNull(getActivity()));
        dialog.setContentView(R.layout.dialod_add_restaurant_category);
        final CircleImageView categoryImage = dialog.findViewById(R.id.dialog_add_restaurant_category_ib_category_image);
        categoryName = dialog.findViewById(R.id.dialog_add_restaurant_category_et_name);
        btnAddNewRestaurantCategory = dialog.findViewById(R.id.dialog_add_restaurant_category_btn_add);

        categoryImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(getActivity(), 1, imagesList, new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        imagesList.clear();
                        imagesList = result;
                        path = imagesList.get(0).getPath();
                        onLoadImageFromUrl(categoryImage, path, getActivity());
                    }
                });
                dialog.show();
            }
        });

        btnAddNewRestaurantCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewRestaurantCategory();
            }
        });

        dialog.show();
    }

    private void addNewRestaurantCategory() {
        getClient().addNewRestaurantCategory(convertToRequestBody(categoryName.getText().toString()),
                convertFileToMultipart(path, "photo"),
                convertToRequestBody(LoadData(getActivity(), API_TOKEN)))
                .enqueue(new Callback<MyCategories>() {
                    @Override
                    public void onResponse(Call<MyCategories> call, Response<MyCategories> response) {

                        try {
                            if (response.body().getStatus()==1) {
                                dialog.dismiss();
                                reInit();
                                getRestaurantCategories();
                            }

                        } catch (Exception e) {

                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<MyCategories> call, Throwable t) {
                        Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_LONG).show();
                    }
                });
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

    @Override
    public void onResume() {
        super.onResume();
        reInit();
    }
}

