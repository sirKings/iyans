package com.iyans.dashboard.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.share.internal.ShareConstants;
import com.iyans.R;
import com.iyans.service.ApiClient;
import com.iyans.service.ApiInterface;
import com.iyans.utility.Utility;
import com.iyans.utility.Validation;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {
    private final String TAG = ChangePasswordActivity.class.getSimpleName();
    private Button btnSubmitNewPass;
    private EditText etOldPassword;
    private EditText etconfPassword;
    private EditText etnewPassword;
    private ImageView iVHidePass;
    private Boolean isClicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_change_password);
        etOldPassword = (EditText) findViewById(R.id.etOldPassword);
        etnewPassword = (EditText) findViewById(R.id.etnewPassword);
        etconfPassword = (EditText) findViewById(R.id.etconfPassword);
        btnSubmitNewPass = (Button) findViewById(R.id.btnSubmitNewPass);
        iVHidePass = (ImageView) findViewById(R.id.iVHidePass);
        btnSubmitNewPass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                renerateNewPassword();
            }
        });
        iVHidePass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                isClicked = !isClicked;
                if (isClicked) {
                    etnewPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    iVHidePass.setImageResource(R.drawable.ic_visibility_black_24dp);
                    return;
                }
                etnewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                iVHidePass.setImageResource(R.drawable.ic_visibility_off_black_24dp);
            }
        });
    }

    public void backPage(View view) {
        finish();
    }

    public void renerateNewPassword() {
        if (!Validation.isValidString(etOldPassword.getText().toString().trim())) {
            etOldPassword.requestFocus();
            Toast.makeText(this, "Please fill the old password ", Toast.LENGTH_SHORT).show();
        } else if (!Validation.isValidPassword(etnewPassword.getText().toString().trim())) {
            etnewPassword.requestFocus();
            Toast.makeText(this, "Please enter valid password", Toast.LENGTH_SHORT).show();
        } else if (etconfPassword.getText().toString().trim().equals(etnewPassword.getText().toString().trim())) {
            changePassword();
        } else {
            etconfPassword.requestFocus();
            Toast.makeText(this, "Password does not match", Toast.LENGTH_SHORT).show();
        }
    }

    private void changePassword() {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", "2");
        hm.put("old_password", etOldPassword.getText().toString().trim());
        hm.put("new_password", etnewPassword.getText().toString().trim());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Change Password Request ");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.changePassword(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Change Password Response :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    if (object.has(ShareConstants.WEB_DIALOG_PARAM_MESSAGE)) {
                        Toast.makeText(ChangePasswordActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                    if (object.getBoolean("status")) {
                        startActivity(new Intent(ChangePasswordActivity.this, SettingActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Utility.hideProgressHud();
            }
        });
    }
}
