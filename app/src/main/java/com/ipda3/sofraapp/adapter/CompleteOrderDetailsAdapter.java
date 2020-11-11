package com.ipda3.sofraapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.room.model.Cart;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompleteOrderDetailsAdapter extends RecyclerView.Adapter<CompleteOrderDetailsAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<Cart> cartList = new ArrayList<>();
    private String lang;

    public CompleteOrderDetailsAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.cartList = cartList;
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orders_list_confirmation,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.itemOrdersListConfirmationTvName.setText(cartList.get(position).getQuantity() + " " + cartList.get(position).getName());
        holder.itemOrdersListConfirmationTvPrice.setText(
                String.valueOf(Integer.parseInt(cartList.get(position).getQuantity()) * Double.parseDouble(cartList.get(position).getPrice())));
    }

    private void setAction(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.item_orders_list_confirmation_tv_name)
        TextView itemOrdersListConfirmationTvName;
        @BindView(R.id.item_orders_list_confirmation_tv_price)
        TextView itemOrdersListConfirmationTvPrice;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
