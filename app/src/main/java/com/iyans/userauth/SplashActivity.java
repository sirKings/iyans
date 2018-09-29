package com.iyans.userauth;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.iyans.dashboard.MainActivity;
import com.iyans.utility.App;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getInstance().getPrefManager().isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, ChooseLoginOrSignUpActivity.class));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
