package com.ipda3.sofraapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.clientOrders.ClientOrders;
import com.ipda3.sofraapp.data.model.clientOrders.ClientOrdersData;
import com.ipda3.sofraapp.data.model.generalResponse.GeneralResponse;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;
import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;

public class ClientOrdersListAdapter extends RecyclerView.Adapter<ClientOrdersListAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<ClientOrdersData> clientOrdersDataList = new ArrayList<>();
    private String lang;

    public  String state;

    public ClientOrdersListAdapter(Context context, List<ClientOrdersData> clientOrdersDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.clientOrdersDataList = clientOrdersDataList;
        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client_order,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.itemClientOrderIvImage, clientOrdersDataList.get(position).getRestaurant().getPhotoUrl(), context);
        holder.itemClientOrderTvRestaurantName.setText(clientOrdersDataList.get(position).getRestaurant().getName());
        holder.itemClientOrderTvTotal.setText(clientOrdersDataList.get(position).getTotal());
        holder.itemClientOrderTvAddress.setText(clientOrdersDataList.get(position).getAddress());

        if(state.equals("pending")){
            holder.itemClientOrderBtn.setBackgroundColor(activity.getResources().getColor(R.color.blue));
            holder.itemClientOrderBtn.setText(R.string.cancel);
        }else if(state.equals("current")){
            holder.itemClientOrderBtn.setBackgroundColor(activity.getResources().getColor(R.color.green));
            holder.itemClientOrderBtn.setText(R.string.delivery_confirmation);
        }else{
            holder.itemClientOrderBtn.setClickable(false);
            if (clientOrdersDataList.get(position).getState().equals("delivered")) {
                holder.itemClientOrderBtn.setBackgroundColor(activity.getResources().getColor(R.color.green));
                holder.itemClientOrderBtn.setText(R.string.order_completed);
            }else{
                holder.itemClientOrderBtn.setBackgroundColor(activity.getResources().getColor(R.color.pink_light));
                holder.itemClientOrderBtn.setText(R.string.order_declined);
            }
        }
    }

    private void setAction(ViewHolder holder, int position) {
        holder.itemClientOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state.equals("pending")){
                    getClient().clientDeclineOrder(clientOrdersDataList.get(position).getId(), LoadData(activity, API_TOKEN)).enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            try {
                                Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                if(response.body().getStatus() == 1){
                                    clientOrdersDataList.remove(position);
                                    notifyItemRemoved(position);
                                }
                            }catch (Exception ex){
                                Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();
                        }
                    });

                }else if(state.equals("current")){
                    getClient().clientConfirmOrderDelivery(clientOrdersDataList.get(position).getId(), LoadData(activity, API_TOKEN)).enqueue(new Callback<GeneralResponse>() {
                        @Override
                        public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                            try {
                                Toast.makeText(activity, response.body().getMsg(), Toast.LENGTH_SHORT).show();
                                if(response.body().getStatus() == 1){
                                    clientOrdersDataList.remove(position);
                                    notifyItemRemoved(position);
                                }
                            }catch (Exception ex){
                                Toast.makeText(activity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<GeneralResponse> call, Throwable t) {
                            Toast.makeText(activity, activity.getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return clientOrdersDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.item_client_order_iv_image)
        CircleImageView itemClientOrderIvImage;
        @BindView(R.id.item_client_order_tv_restaurant_name)
        TextView itemClientOrderTvRestaurantName;
        @BindView(R.id.item_client_order_tv_total)
        TextView itemClientOrderTvTotal;
        @BindView(R.id.item_client_order_tv_address)
        TextView itemClientOrderTvAddress;
        @BindView(R.id.item_client_order_btn)
        Button itemClientOrderBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
