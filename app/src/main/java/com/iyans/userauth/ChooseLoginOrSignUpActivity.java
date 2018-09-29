package com.iyans.userauth;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.iyans.R;

public class ChooseLoginOrSignUpActivity extends AppCompatActivity {
    Button btn_login;
    Button btn_signup;
    TextView tv_copyright;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_choose_login_or_signup);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        btn_login = (Button) findViewById(R.id.btn_login);
        tv_copyright = (TextView) findViewById(R.id.tv_copyright);
        btn_login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseLoginOrSignUpActivity.this, LoginActivity.class));
            }
        });
        btn_signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChooseLoginOrSignUpActivity.this, SignupActivity.class));
            }
        });
    }
}
