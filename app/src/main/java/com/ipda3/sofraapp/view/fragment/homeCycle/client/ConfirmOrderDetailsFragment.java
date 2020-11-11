package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.local.SharedPreferencesManger;
import com.ipda3.sofraapp.data.model.clientNewOrder.ClientNewOrder;
import com.ipda3.sofraapp.data.room.database.CartDatabase;
import com.ipda3.sofraapp.data.room.model.Cart;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;


public class ConfirmOrderDetailsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.confirm_order_details_fragment_et_notes)
    EditText confirmOrderDetailsFragmentEtNotes;
    @BindView(R.id.confirm_order_details_fragment_rb_cash)
    RadioButton confirmOrderDetailsFragmentRbCash;
    @BindView(R.id.confirm_order_details_fragment_rb_online)
    RadioButton confirmOrderDetailsFragmentRbOnline;
    @BindView(R.id.confirm_order_details_fragment_tv_total)
    TextView confirmOrderDetailsFragmentTvTotal;
    @BindView(R.id.confirm_order_details_fragment_tv_delivery_cost)
    TextView confirmOrderDetailsFragmentTvDeliveryCost;
    @BindView(R.id.confirm_order_details_fragment_tv_total_payment)
    TextView confirmOrderDetailsFragmentTvTotalPayment;
    @BindView(R.id.confirm_order_details_fragment_et_address)
    EditText confirmOrderDetailsFragmentEtAddress;
    public static String address;

    public static String payment = "1";
    List<Cart> cartList = new ArrayList<>();
    private List<String> items, quantities, notes;
    public static double total, deliveryCost, totalCost;


    public ConfirmOrderDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_confirm_order_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        total = CartItemsListFragment.total;
        deliveryCost = Double.parseDouble(RestaurantDetailsContainerFragment.restaurant.getDeliveryCost());
        totalCost = total + deliveryCost;
        confirmOrderDetailsFragmentTvTotal.setText(String.valueOf(total));
        confirmOrderDetailsFragmentTvDeliveryCost.setText(String.valueOf(deliveryCost));
        confirmOrderDetailsFragmentTvTotalPayment.setText(String.valueOf(totalCost));

        init();

        return view;
    }

    private void init() {

        items = new ArrayList<>();
        quantities = new ArrayList<>();
        notes = new ArrayList<>();

        cartList = CartDatabase.getInstance(getActivity()).cartDao().getAllCartItems();
        for (int i = 0; i < cartList.size(); i++){
            items.add(cartList.get(i).getId()+"");
            quantities.add(cartList.get(i).getQuantity());
            notes.add(cartList.get(i).getDesc());
        }

    }

    @OnClick({R.id.confirm_order_details_fragment_rb_cash, R.id.confirm_order_details_fragment_rb_online, R.id.confirm_order_details_fragment_btn_confirm_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_order_details_fragment_rb_cash:
                confirmOrderDetailsFragmentRbCash.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_circle));
                confirmOrderDetailsFragmentRbOnline.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_circle));
                payment = "1";
                break;
            case R.id.confirm_order_details_fragment_rb_online:
                confirmOrderDetailsFragmentRbCash.setBackground(getActivity().getResources().getDrawable(R.drawable.ic_circle));
                confirmOrderDetailsFragmentRbOnline.setBackground(getActivity().getResources().getDrawable(R.drawable.shape_circle));
                payment = "2";
                break;
            case R.id.confirm_order_details_fragment_btn_confirm_order:
                confirmOrder();
                break;
        }
    }

    private void confirmOrder() {
        address = confirmOrderDetailsFragmentEtAddress.getText().toString();
        Call<ClientNewOrder> clientNewOrderCall = getClient().clientNewOrderRequest(
                RestaurantDetailsContainerFragment.restaurant.getId().toString(),
                confirmOrderDetailsFragmentEtNotes.getText().toString(),
                confirmOrderDetailsFragmentEtAddress.getText().toString(),
                payment,
                RestaurantDetailsContainerFragment.restaurant.getPhone(),
                RestaurantDetailsContainerFragment.restaurant.getName(),
                SharedPreferencesManger.LoadData(getActivity(), SharedPreferencesManger.API_TOKEN),
                items,
                quantities,
                notes);

        clientNewOrderCall.enqueue(new Callback<ClientNewOrder>() {
            @Override
            public void onResponse(Call<ClientNewOrder> call, Response<ClientNewOrder> response) {
                try {
                    Toast.makeText(getActivity(), response.body().getMsg(), Toast.LENGTH_SHORT).show();
                    if(response.body().getStatus() == 1){
                        CompletOrderFragment completOrderFragment = new CompletOrderFragment();
                        replaceFragment(getActivity().getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, completOrderFragment);
                    }
                }catch (Exception ex){
                    Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ClientNewOrder> call, Throwable t) {
                try {
                    Toast.makeText(baseActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(baseActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
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