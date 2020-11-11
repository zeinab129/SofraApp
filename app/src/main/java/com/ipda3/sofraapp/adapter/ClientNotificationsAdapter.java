package com.ipda3.sofraapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.data.model.notification.NotificationData;
import com.ipda3.sofraapp.view.activity.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientNotificationsAdapter extends RecyclerView.Adapter<ClientNotificationsAdapter.ViewHolder> {


    private Context context;
    private BaseActivity activity;
    private List<NotificationData> notificationDataList = new ArrayList<>();
    private String lang;


    public ClientNotificationsAdapter(Activity context, List<NotificationData> notificationDataList) {
        this.context = context;
        this.activity = (BaseActivity) context;
        this.notificationDataList = notificationDataList;

        lang = "eg";
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification,
                parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        setData(holder, position);
        setAction(holder, position);
    }

    private void setData(ViewHolder holder, int position) {
        holder.notificationItemTvTitle.setText(notificationDataList.get(position).getTitle());
        holder.notificationItemTvDate.setText(notificationDataList.get(position).getCreatedAt());
    }

    private void setAction(ViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return notificationDataList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private View view;

        @BindView(R.id.notification_item_iv_icon)
        ImageView notificationItemIvIcon;
        @BindView(R.id.notification_item_tv_title)
        TextView notificationItemTvTitle;
        @BindView(R.id.notification_item_tv_date)
        TextView notificationItemTvDate;

        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            ButterKnife.bind(this, view);
        }

    }
}
