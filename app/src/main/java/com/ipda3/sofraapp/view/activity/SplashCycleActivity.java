package com.ipda3.sofraapp.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.view.fragment.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.CLIENT;
import static com.ipda3.sofraapp.data.local.SharedPreferencesManger.SaveData;

public class SplashCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_cycle);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.splash_cycle_activity_btn_order_food, R.id.splash_cycle_activity_btn_sell_food})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.splash_cycle_activity_btn_order_food:
                SaveData(SplashCycleActivity.this, CLIENT, true);
                Intent intent = new Intent(SplashCycleActivity.this, AuthCycleActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.splash_cycle_activity_btn_sell_food:
                SaveData(SplashCycleActivity.this, CLIENT, false);
                Intent intent2 = new Intent(SplashCycleActivity.this, AuthCycleActivity.class);
                startActivity(intent2);
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
