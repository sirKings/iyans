package com.iyans.dashboard.settings;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.google.android.gms.common.util.CrashUtils;
import com.iyans.R;
import com.iyans.service.ApiClient;
import com.iyans.service.ApiInterface;
import com.iyans.userauth.LoginActivity;
import com.iyans.utility.App;
import com.iyans.utility.Utility;

import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingActivity extends AppCompatActivity implements OnClickListener, OnCheckedChangeListener {
    String TAG = getClass().getSimpleName();
    private Button btnInstagram;
    private Button btnTwiter;
    private Button btnWhatsApp;
    private Button btnfacebook;
    private SwitchCompat swAccountPrivacy;
    private SwitchCompat swImagePreview;
    private SwitchCompat swNotification;
    private SwitchCompat swSoundEffect;
    private TextView tvAbout;
    private TextView tvAboutVersion;
    private TextView tvChangePassword;
    private TextView tvEditProfile;
    private TextView tvLogout;
    private TextView tvReport;
    private TextView tvTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_setting);
        this.tvChangePassword = (TextView) findViewById(R.id.tvChangePassword);
        this.btnfacebook = (Button) findViewById(R.id.btnfacebook);
        this.btnTwiter = (Button) findViewById(R.id.btnTwiter);
        this.btnInstagram = (Button) findViewById(R.id.btnInstagram);
        this.tvEditProfile = (TextView) findViewById(R.id.tvEditProfile);
        this.swSoundEffect = (SwitchCompat) findViewById(R.id.swSoundEffect);
        this.swImagePreview = (SwitchCompat) findViewById(R.id.swImagePreview);
        this.swNotification = (SwitchCompat) findViewById(R.id.swNotification);
        this.tvAbout = (TextView) findViewById(R.id.tvAbout);
        this.tvTerms = (TextView) findViewById(R.id.tvTerms);
        this.tvLogout = (TextView) findViewById(R.id.tvLogout);
        this.tvAboutVersion = (TextView) findViewById(R.id.tvAboutVersion);
        this.btnfacebook.setOnClickListener(this);
        this.btnTwiter.setOnClickListener(this);
        this.btnInstagram.setOnClickListener(this);
        this.tvEditProfile.setOnClickListener(this);
        this.tvAbout.setOnClickListener(this);
        this.tvTerms.setOnClickListener(this);
        this.tvLogout.setOnClickListener(this);
        this.tvChangePassword.setOnClickListener(this);
        this.swSoundEffect.setOnCheckedChangeListener(this);
        this.swImagePreview.setOnCheckedChangeListener(this);
        this.swNotification.setOnCheckedChangeListener(this);
        this.tvAboutVersion.setOnClickListener(this);
        if (App.getInstance().getPrefManager().getSoundEffectState()) {
            this.swSoundEffect.setChecked(true);
        } else if (!App.getInstance().getPrefManager().getSoundEffectState()) {
            this.swSoundEffect.setChecked(false);
        }
        if (App.getInstance().getPrefManager().getImgPrevState()) {
            this.swImagePreview.setChecked(true);
        } else if (!App.getInstance().getPrefManager().getImgPrevState()) {
            this.swImagePreview.setChecked(false);
        }
        if (App.getInstance().getPrefManager().getNotificationState()) {
            this.swNotification.setChecked(true);
        } else if (!App.getInstance().getPrefManager().getNotificationState()) {
            this.swNotification.setChecked(false);
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.btnInstagram:
                openInstagram();
                return;
            case R.id.btnTwiter:
                openTwiter();
                return;
            case R.id.btnfacebook:
                openFacebook(this);
                return;
            case R.id.tvAbout:
                intent = new Intent(this, AboutActivity.class);
                intent.putExtra("for_what", "About");
                startActivity(intent);
                return;
            case R.id.tvAboutVersion:
                intent = new Intent(this, AboutActivity.class);
                intent.putExtra("for_what", "About this Version");
                startActivity(intent);
                return;
            case R.id.tvChangePassword:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                return;
            case R.id.tvEditProfile:
                startActivity(new Intent(this, UpdateProfileActivity.class));
                return;
            case R.id.tvLogout:
                alert();
                return;
            case R.id.tvTerms:
                intent = new Intent(this, AboutActivity.class);
                intent.putExtra("for_what", "Terms");
                startActivity(intent);
                return;
            default:
                return;
        }
    }

    public void alert() {
        Builder builder = new Builder(this);
        builder.setTitle((CharSequence) "Confirm");
        builder.setMessage((int) R.string.logout_msj);
        builder.setPositiveButton((CharSequence) "YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getInstance().getPrefManager().setLoginSession(false);
                SettingActivity.this.startActivity(new Intent(SettingActivity.this, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                dialog.dismiss();
            }
        });
        builder.setNegativeButton((CharSequence) "NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.swImagePreview:
                if (isChecked) {
                    saveImgPrevState("1");
                    return;
                } else {
                    saveImgPrevState("0");
                    return;
                }
            case R.id.swNotification:
                if (isChecked) {
                    saveNotificationState("1");
                    return;
                } else {
                    saveNotificationState("0");
                    return;
                }
            case R.id.swSoundEffect:
                if (isChecked) {
                    App.getInstance().getPrefManager().setSoundEffectState(true);
                    return;
                } else {
                    App.getInstance().getPrefManager().setSoundEffectState(false);
                    return;
                }
            default:
                return;
        }
    }

    public static Intent openFacebook(Context context) {
        try {
            context.getPackageManager().getPackageInfo("com.facebook.katana", 0);
            return new Intent("android.intent.action.VIEW", Uri.parse("fb://page/376227335860239"));
        } catch (Exception e) {
            return new Intent("android.intent.action.VIEW", Uri.parse("https://www.facebook.com/mmdnowofficial/"));
        }
    }

    public void openTwiter() {
        Intent intent;
        try {
            getPackageManager().getPackageInfo("com.twitter.android", 0);
            intent = new Intent("android.intent.action.VIEW", Uri.parse("twitter://user?user_id=USERID"));
            intent.addFlags(CrashUtils.ErrorDialogData.BINDER_CRASH);
        } catch (Exception e) {
            intent = new Intent("android.intent.action.VIEW", Uri.parse("https://twitter.com/mmdnowofficial"));
        }
        startActivity(intent);
    }

    public void openInstagram() {
        Intent likeIng = new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/mmdnowit/"));
        likeIng.setPackage("com.instagram.android");
        try {
            startActivity(likeIng);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://www.instagram.com/mmdnowit/")));
        }
    }

    private void saveNotificationState(final String stateValue) {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", "2");
        hm.put("notification_status", stateValue);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Notification Request ");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.saveNotificationState(hm).enqueue(new Callback<ResponseBody>() {
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Notification Response :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    if (object.getBoolean("status")) {
                        String notificationState = object.getJSONObject("data").getString("notification_status");
                        if (stateValue.equalsIgnoreCase("1")) {
                            App.getInstance().getPrefManager().setNotificationState(true);
                        } else {
                            App.getInstance().getPrefManager().setNotificationState(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Utility.hideProgressHud();
            }
        });
    }

    private void saveImgPrevState(final String stateValue) {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", "2");
        hm.put("image_preview_status", stateValue);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Image Preview Request ");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.saveImgPrevState(hm).enqueue(new Callback<ResponseBody>() {
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Image Preview Response :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    if (object.getBoolean("status")) {
                        String notificationState = object.getJSONObject("data").getString("image_preview_status");
                        if (stateValue.equalsIgnoreCase("1")) {
                            App.getInstance().getPrefManager().setImgPrevState(true);
                        } else {
                            App.getInstance().getPrefManager().setImgPrevState(false);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Utility.hideProgressHud();
            }
        });
    }
}
