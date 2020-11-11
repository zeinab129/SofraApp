package com.ipda3.sofraapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.room.database.CartDatabase;
import com.ipda3.sofraapp.data.room.model.Cart;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<Cart> cartList = new ArrayList<>();
    private String lang;
    private int quantity;

    public CartListAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.cartList = cartList;
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.itemCartTvItemName.setText(cartList.get(position).getName());
        holder.itemCartTvItePrice.setText(cartList.get(position).getPrice());
        holder.itemCartTvQuantity.setText(cartList.get(position).getQuantity());
        onLoadImageFromUrl(holder.itemCartIvItemImage, cartList.get(position).getPath(), context);
        quantity = Integer.parseInt(cartList.get(position).getQuantity());

    }

    private void setAction(ViewHolder holder, int position) {
        holder.itemCartBtnMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity> 0) {
                    quantity--;
                    updateItemQuantity(holder, position);
                }
            }
        });

        holder.itemCartBtnPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                updateItemQuantity(holder, position);
            }
        });

        holder.itemCartBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                removeItem(position);
            }
        });
    }

    private void updateItemQuantity(ViewHolder holder, int position) {
        holder.itemCartTvQuantity.setText(quantity+"");
        Cart cartItem = cartList.get(position);
        cartItem.setQuantity(String.valueOf(quantity));
        CartDatabase.getInstance(context).cartDao().updateCartItem(cartItem);
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void removeItem(int position) {
        Cart cartItem = cartList.get(position);
        CartDatabase.getInstance(context).cartDao().deleteCartItem(cartItem);
        cartList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void changData(List<Cart> cartList) {
        this.cartList = cartList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.item_cart_iv_item_image)
        ImageView itemCartIvItemImage;
        @BindView(R.id.item_cart_tv_item_name)
        TextView itemCartTvItemName;
        @BindView(R.id.item_cart_tv_ite_price)
        TextView itemCartTvItePrice;
        @BindView(R.id.item_cart_btn_plus)
        Button itemCartBtnPlus;
        @BindView(R.id.item_cart_tv_quantity)
        TextView itemCartTvQuantity;
        @BindView(R.id.item_cart_btn_minus)
        Button itemCartBtnMinus;
        @BindView(R.id.item_cart_btn_cancel)
        Button itemCartBtnCancel;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
