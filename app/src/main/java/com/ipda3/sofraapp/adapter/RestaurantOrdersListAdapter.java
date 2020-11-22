package com.ipda3.sofraapp.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.generalResponse.GeneralResponse;
import com.ipda3.sofraapp.data.model.restaurantOrders.RestaurantOrdersData;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.onPermission;

public class RestaurantOrdersListAdapter extends RecyclerView.Adapter<RestaurantOrdersListAdapter.ViewHolder> {



    private Context context;
    private BaseActivity activity;
    private List<RestaurantOrdersData> restaurantOrdersDataList = new ArrayList<>();
    private String lang;
    public String state;
    public static Dialog dialog;

    public RestaurantOrdersListAdapter(Context context, List<RestaurantOrdersData> restaurantOrdersDataList, String state) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.restaurantOrdersDataList = restaurantOrdersDataList;
        this.state = state;
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_restaurant_order,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.restaurantOrderItemIvUserImage, restaurantOrdersDataList.get(position).getClient().getPhotoUrl(), context);
        holder.restaurantOrderItemTvClientName.setText(restaurantOrdersDataList.get(position).getClient().getName());
        holder.restaurantOrderItemTvTotal.setText(restaurantOrdersDataList.get(position).getTotal());
        holder.restaurantOrderItemTvAddress.setText(restaurantOrdersDataList.get(position).getAddress());

        if (state.equals("pending")) {
            holder.restaurantOrderItemLlNewOrders.setVisibility(View.VISIBLE);
            holder.restaurantOrderItemLlCurrentOrders.setVisibility(View.GONE);
            holder.itemRestaurantOrderPreviousOrderTvOrderState.setVisibility(View.GONE);
        } else if (state.equals("current")) {
            holder.restaurantOrderItemLlNewOrders.setVisibility(View.GONE);
            holder.restaurantOrderItemLlCurrentOrders.setVisibility(View.VISIBLE);
            holder.itemRestaurantOrderPreviousOrderTvOrderState.setVisibility(View.GONE);
        } else if (state.equals("complete")) {
            holder.restaurantOrderItemLlNewOrders.setVisibility(View.GONE);
            holder.restaurantOrderItemLlCurrentOrders.setVisibility(View.GONE);
            holder.itemRestaurantOrderPreviousOrderTvOrderState.setVisibility(View.VISIBLE);
            if (restaurantOrdersDataList.get(position).getState().equals("declined") ||
                    restaurantOrdersDataList.get(position).getState().equals("rejected")) {
                holder.itemRestaurantOrderPreviousOrderTvOrderState.setBackgroundColor(activity.getResources().getColor(R.color.pink_light));
                holder.itemRestaurantOrderPreviousOrderTvOrderState.setText(R.string.order_declined);
            } else{
                holder.itemRestaurantOrderPreviousOrderTvOrderState.setBackgroundColor(activity.getResources().getColor(R.color.green));
                holder.itemRestaurantOrderPreviousOrderTvOrderState.setText(R.string.order_completed);
            }
        }

    }

    private void setAction(ViewHolder holder, int position) {
        holder.restaurantOrderItemNewOrderBtnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onPermission(activity);
                activity.startActivity(new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", restaurantOrdersDataList.get(position).getClient().getPhone(), null)));
            }
        });
        holder.restaurantOrderItemNewOrderBtnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().restaurantAcceptOrder(LoadData(activity, API_TOKEN), restaurantOrdersDataList.get(position).getId())
                        .enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                try {
                                    Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    restaurantOrdersDataList.remove(position);
                                    notifyItemRemoved(position);

                                } catch (Exception ex) {
                                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        holder.restaurantOrderItemNewOrderBtnRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Objects.requireNonNull(activity));
                dialog.setContentView(R.layout.dialog_restaurant_order_cancel_reason);
                EditText rejectionReason = dialog.findViewById(R.id.dialog_restaurant_order_cancel_reason_et_reason);
                Button btnCancel = dialog.findViewById(R.id.dialog_restaurant_order_cancel_reason_btn_cancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClient().restaurantRejectOrder(
                                LoadData(activity, API_TOKEN),
                                restaurantOrdersDataList.get(position).getId(),
                                rejectionReason.getText().toString())
                                .enqueue(new Callback<GeneralResponse>() {
                                    @Override
                                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                        try {
                                            Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                            restaurantOrdersDataList.remove(position);
                                            notifyItemRemoved(position);
                                        } catch (Exception ex) {
                                            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                        Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                dialog.show();
            }
        });
        holder.restaurantOrderItemBtnCurrentOrderRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(Objects.requireNonNull(activity));
                dialog.setContentView(R.layout.dialog_restaurant_order_cancel_reason);
                EditText rejectionReason = dialog.findViewById(R.id.dialog_restaurant_order_cancel_reason_et_reason);
                Button btnCancel = dialog.findViewById(R.id.dialog_restaurant_order_cancel_reason_btn_cancel);

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getClient().restaurantRejectOrder(
                                LoadData(activity, API_TOKEN),
                                restaurantOrdersDataList.get(position).getId(),
                                rejectionReason.getText().toString())
                                .enqueue(new Callback<GeneralResponse>() {
                                    @Override
                                    public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                        try {
                                            Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                            dialog.dismiss();
                                            restaurantOrdersDataList.remove(position);
                                            notifyItemRemoved(position);
                                        } catch (Exception ex) {
                                            Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                        Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });

                dialog.show();
            }
        });
        holder.restaurantOrderItemBtnCurrentOrderDeliveryConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().restaurantConfirmOrder(restaurantOrdersDataList.get(position).getId(), LoadData(activity, API_TOKEN))
                        .enqueue(new Callback<GeneralResponse>() {
                            @Override
                            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                                try {
                                    Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                    restaurantOrdersDataList.remove(position);
                                    notifyItemRemoved(position);
                                } catch (Exception ex) {
                                    Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                                Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }


    @Override
    public int getItemCount() {
        return restaurantOrdersDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.restaurant_order_item_iv_user_image)
        ImageView restaurantOrderItemIvUserImage;
        @BindView(R.id.restaurant_order_item_tv_client_name)
        TextView restaurantOrderItemTvClientName;
        @BindView(R.id.restaurant_order_item_tv_total)
        TextView restaurantOrderItemTvTotal;
        @BindView(R.id.restaurant_order_item_tv_address)
        TextView restaurantOrderItemTvAddress;
        @BindView(R.id.restaurant_order_item_new_order_btn_refuse)
        Button restaurantOrderItemNewOrderBtnRefuse;
        @BindView(R.id.restaurant_order_item_new_order_btn_accept)
        Button restaurantOrderItemNewOrderBtnAccept;
        @BindView(R.id.restaurant_order_item_new_order_btn_call)
        Button restaurantOrderItemNewOrderBtnCall;
        @BindView(R.id.restaurant_order_item_btn_current_order_refuse)
        Button restaurantOrderItemBtnCurrentOrderRefuse;
        @BindView(R.id.restaurant_order_item_btn_current_order_delivery_confirm)
        Button restaurantOrderItemBtnCurrentOrderDeliveryConfirm;
        @BindView(R.id.restaurant_order_item_ll_new_orders)
        LinearLayout restaurantOrderItemLlNewOrders;
        @BindView(R.id.restaurant_order_item_ll_current_orders)
        LinearLayout restaurantOrderItemLlCurrentOrders;
        @BindView(R.id.item_restaurant_order_previous_order_tv_order_state)
        TextView itemRestaurantOrderPreviousOrderTvOrderState;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
