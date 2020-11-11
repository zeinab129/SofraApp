package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.offers.OffersData;
import com.ipda3.sofraapp.view.activity.BaseActivity;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ClientOfferDetailsFragment;
import com.ipda3.sofraapp.view.fragment.homeCycle.client.ReviewItemFoodFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.ipda3.sofraapp.helper.HelperMethod.onLoadImageFromUrl;
import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class ClientOffersAdapter extends RecyclerView.Adapter<ClientOffersAdapter.ViewHolder> {

    private Context context;
    private BaseActivity activity;
    private List<OffersData> offersDataList = new ArrayList<>();
    private String lang;
    private ReviewItemFoodFragment reviewItemFoodFragment;


    public ClientOffersAdapter(Activity context, List<OffersData> offersDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.offersDataList = offersDataList;

        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_client_offer,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        onLoadImageFromUrl(holder.clientOfferItemIvRestImage, offersDataList.get(position).getPhotoUrl(), context);
        holder.clientOfferItemTvDesc.setText(offersDataList.get(position).getDescription());
    }

    private void setAction(ViewHolder holder, int position) {
        holder.clientOfferItemBtnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClientOfferDetailsFragment clientOfferDetailsFragment = new ClientOfferDetailsFragment();
                clientOfferDetailsFragment.offersData = offersDataList.get(position);
                replaceFragment(activity.getSupportFragmentManager(), R.id.home_cycle_activity_fl_home_frame, clientOfferDetailsFragment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return offersDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.client_offer_item_tv_desc)
        TextView clientOfferItemTvDesc;
        @BindView(R.id.client_offer_item_btn_details)
        Button clientOfferItemBtnDetails;
        @BindView(R.id.client_offer_item_iv_rest_image)
        CircleImageView clientOfferItemIvRestImage;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
