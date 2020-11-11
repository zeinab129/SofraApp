package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.foodItem.FoodItem;
import com.ipda3.sofraapp.data.model.foodItem.FoodItemData;
import com.ipda3.sofraapp.data.room.database.CartDatabase;
import com.ipda3.sofraapp.data.room.model.Cart;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class ReviewItemFoodFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.review_item_food_fragment_iv_image)
    ImageView reviewItemFoodFragmentIvImage;
    @BindView(R.id.review_item_food_fragment_tv_name)
    TextView reviewItemFoodFragmentTvName;
    @BindView(R.id.review_item_food_fragment_tv_desc)
    TextView reviewItemFoodFragmentTvDesc;
    @BindView(R.id.review_item_food_fragment_tv_price)
    TextView reviewItemFoodFragmentTvPrice;
    @BindView(R.id.food_item_tv_special_offer)
    TextView foodItemTvSpecialOffer;
    @BindView(R.id.review_item_food_fragment_et_special_order)
    EditText reviewItemFoodFragmentEtSpecialOrder;
    @BindView(R.id.review_item_food_fragment_tv_quantity)
    TextView reviewItemFoodFragmentTvQuantity;

    public FoodItemData foodItemData;

    private int quantity = 0;

    public ReviewItemFoodFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_review_item_food, container, false);
        unbinder = ButterKnife.bind(this, view);

        onLoadImageFromUrl(reviewItemFoodFragmentIvImage, foodItemData.getPhotoUrl(), getActivity());
        reviewItemFoodFragmentTvName.setText(foodItemData.getName());
        reviewItemFoodFragmentTvDesc.setText(foodItemData.getDescription());

        if(foodItemData.getHasOffer()){
            foodItemTvSpecialOffer.setVisibility(View.VISIBLE);
            reviewItemFoodFragmentTvPrice.setText(foodItemData.getOfferPrice());
        }else{
            reviewItemFoodFragmentTvPrice.setText(foodItemData.getPrice());
        }

        return view;
    }

    @OnClick({R.id.review_item_food_fragment_btn_plus, R.id.review_item_food_fragment_btn_minus, R.id.review_item_food_fragment_ib_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.review_item_food_fragment_btn_plus:
                quantity++;
                reviewItemFoodFragmentTvQuantity.setText(quantity + "");
                break;
            case R.id.review_item_food_fragment_btn_minus:
                if(quantity > 0) {
                    quantity--;
                    reviewItemFoodFragmentTvQuantity.setText(quantity + "");
                }
                break;
            case R.id.review_item_food_fragment_ib_cart:

                String itemImagePath = foodItemData.getPhotoUrl();
                String itemName = reviewItemFoodFragmentTvName.getText().toString();
                String itemDescription = reviewItemFoodFragmentTvDesc.getText().toString();
                String itemPrice = reviewItemFoodFragmentTvPrice.getText().toString();
                String itemSpecialOrder = reviewItemFoodFragmentEtSpecialOrder.getText().toString();
                String itemQuantity = reviewItemFoodFragmentTvQuantity.getText().toString();

                if(itemQuantity.equals("0")){
                    Toast.makeText(getContext(), getString(R.string.please_enter_quantity), Toast.LENGTH_SHORT).show();
                }else{
                    Cart cart = new Cart(foodItemData.getId(),
                            itemImagePath,
                            itemName,
                            itemDescription,
                            itemPrice,
                            itemSpecialOrder,
                            itemQuantity);

                    //adding the new cart item to the room database
                    CartDatabase.getInstance(getContext()).cartDao().addCartItem(cart);

                    Toast.makeText(getContext(), "Item added successfully!", Toast.LENGTH_SHORT).show();
                    CartItemsListFragment cartItemsListFragment = new CartItemsListFragment();
                    replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, cartItemsListFragment);

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