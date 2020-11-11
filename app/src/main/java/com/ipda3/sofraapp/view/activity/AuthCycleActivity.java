package com.ipda3.sofraapp.view.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ipda3.sofraapp.R;
import com.ipda3.sofraapp.view.fragment.authCycle.LoginFragment;

import static com.ipda3.sofraapp.helper.HelperMethod.replaceFragment;

public class AuthCycleActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_cycle);

        replaceFragment(getSupportFragmentManager(), R.id.auth_cycle_activity, new LoginFragment());
    }

}
