package com.ipda3.sofraapp.view.fragment.homeCycle.client;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.adapter.ClientNotificationsAdapter;
import com.ipda3.sofraapp.adapter.RestaurantCategoriesAdapter;
import com.ipda3.sofraapp.adapter.RestaurantsListAdapter;
import com.ipda3.sofraapp.data.model.notification.Notification;
import com.ipda3.sofraapp.data.model.notification.NotificationData;
import com.ipda3.sofraapp.data.model.restaurantsList.RestaurantsList;
import com.ipda3.sofraapp.helper.OnEndLess;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.ipda3.sofraapp.data.api.RetrofitClient.getClient;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.API_TOKEN;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadBoolean;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.LoadData;

public class NotificationsFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.notifications_fragment_rv_notifications)
    RecyclerView notificationsFragmentRvNotifications;

    boolean isClient;
    ClientNotificationsAdapter clientNotificationsAdapter;
    List<NotificationData> notificationDataList = new ArrayList<>();
    private LinearLayoutManager linearLayout;
    private OnEndLess onEndLess;
    private int maxPage = 1;

    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (LoadBoolean(getActivity(), CLIENT)) {
            isClient = true;
        } else {
            isClient = false;
        }

        init();

        return view;
    }

    private void init() {
        linearLayout = new LinearLayoutManager(getActivity());
        notificationsFragmentRvNotifications.setLayoutManager(linearLayout);

        onEndLess = new OnEndLess(linearLayout, 1) {
            @Override
            public void onLoadMore(int current_page) {
                if (current_page <= maxPage) {
                    if (maxPage != 0 && current_page != 1) {
                        onEndLess.previous_page = current_page;
                        getNotificationsList();
                    } else {
                        onEndLess.current_page = onEndLess.previous_page;
                    }
                } else {
                    onEndLess.current_page = onEndLess.previous_page;
                }
            }
        };
        notificationsFragmentRvNotifications.addOnScrollListener(onEndLess);
        clientNotificationsAdapter = new ClientNotificationsAdapter(getActivity(), notificationDataList);
        notificationsFragmentRvNotifications.setAdapter(clientNotificationsAdapter);

        getNotificationsList();
    }

    private void getNotificationsList() {
        if(isClient){
            Call<Notification> clientNotificationCall = getClient().getClientNotificationsList(LoadData(getActivity(), API_TOKEN));
            startCall(clientNotificationCall);
        }else{
            Call<Notification> RestaurantNotificationCall = getClient().getRestaurantNotificationsList(LoadData(getActivity(), API_TOKEN));
            startCall(RestaurantNotificationCall);
        }

    }

    private void startCall(Call<Notification> notificationCall) {
        notificationCall.enqueue(new Callback<Notification>() {
            @Override
            public void onResponse(Call<Notification> call, Response<Notification> response) {
                try {
                    if (response.body().getStatus() == 1) {
                        maxPage = response.body().getData().getLastPage();
                        notificationDataList.addAll(response.body().getData().getData());
                        clientNotificationsAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Notification> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.failed), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void reInit() {
        onEndLess.previousTotal = 0;
        onEndLess.current_page = 1;
        onEndLess.previous_page = 1;
        notificationDataList = new ArrayList<>();
        clientNotificationsAdapter = new ClientNotificationsAdapter(getActivity(), notificationDataList);
        notificationsFragmentRvNotifications.setAdapter(clientNotificationsAdapter);
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