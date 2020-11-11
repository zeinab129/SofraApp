package com.ipda3.sofraapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import com.ipda3.sofraapp.view.fragment.BaseFragment;

public class BaseActivity extends AppCompatActivity {

    public BaseFragment baseFragment;

    @Override
    public void onBackPressed() {
        baseFragment.onBack();
    }

    public void superBackPressed() {
        super.onBackPressed();
    }
}
