package com.iyans.dashboard.settings;

import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.Gson;
import com.iyans.R;
import com.iyans.model.ProfileModel;
import com.iyans.service.ApiClient;
import com.iyans.service.ApiInterface;
import com.iyans.utility.App;
import com.iyans.utility.GlideApp;
import com.iyans.utility.GlideRequests;
import com.iyans.utility.SessionManager;
import com.iyans.utility.Utility;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfileActivity extends AppCompatActivity implements OnClickListener {
    private static final int CAMERA = 2;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int GALLERY = 1;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private String TAG = getClass().getSimpleName();
    private Button btnSubmit;
    OnDateSetListener date;
    private EditText etDescription;
    private EditText etFirstName;
    private EditText etLastName;
    private ImageView ivProfileImage;
    private Calendar myCalendar;
    private String path;
    private SharedPreferences permissionStatus;
    private RadioButton radioButton;
    private RadioGroup rgGender;
    private boolean sentToSettings = false;
    private TextView tvDob;
    private TextView tvEmail;
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_update_profile);
        permissionStatus = getSharedPreferences("permissionStatus", 0);
        //getUserProfile();
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etDescription = (EditText) findViewById(R.id.etDescription);
        tvUserName = (TextView) findViewById(R.id.tvUserName);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvDob = (TextView) findViewById(R.id.tvDob);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        btnSubmit.setOnClickListener(this);
        tvDob.setOnClickListener(this);
        ivProfileImage.setOnClickListener(this);
    }

    private void showPictureDialog() {
        Builder pictureDialog = new Builder(this);
        pictureDialog.setTitle("Choose from");
        pictureDialog.setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        choosePhotoFromGallary();
                        return;
                    case 1:
                        openCamera();
                        return;
                    default:
                        return;
                }
            }
        });
        pictureDialog.show();
    }

    private void openCamera() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 2);
    }

    public void choosePhotoFromGallary() {
        startActivityForResult(new Intent("android.intent.action.PICK", Media.EXTERNAL_CONTENT_URI), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != 0) {
            if (requestCode == 1) {
                if (data != null) {
                    try {
                        Bitmap bitmap = Media.getBitmap(getContentResolver(), data.getData());
                        ivProfileImage.setImageBitmap(bitmap);
                        path = convertToBase64(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (requestCode == 2) {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                ivProfileImage.setImageBitmap(thumbnail);
                path = convertToBase64(thumbnail);
            }
        }
    }

    private String convertToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, byteArrayOutputStream);
        return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
    }

    private String getValueOfRadioButton() {
        radioButton = (RadioButton) findViewById(rgGender.getCheckedRadioButtonId());
        return radioButton.getText().toString();
    }

    private void showDatePicker() {
        myCalendar = Calendar.getInstance();
        date = new OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };
    }

    private void updateLabel() {
        tvDob.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(myCalendar.getTime()));
    }

    private void getUserProfile() {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Profile Request ");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.getUserProfile(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Profile Response :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    if (object.getBoolean("status")) {
                        ProfileModel mProfileModel = (ProfileModel) new Gson().fromJson(String.valueOf(object.getJSONObject("data")), ProfileModel.class);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("getCountFeeds");
                        stringBuilder2.append(mProfileModel.getCountFeeds());
                        Log.e(TAG, stringBuilder2.toString());
                        etFirstName.setText(String.valueOf(mProfileModel.getFirstName()));
                        etLastName.setText(String.valueOf(mProfileModel.getLastName()));
                        tvUserName.setText(String.valueOf(mProfileModel.getUserName()));
                        tvEmail.setText(String.valueOf(mProfileModel.getEmail()));
                        tvDob.setText(String.valueOf(mProfileModel.getDob()));
                        tvEmail.setText(String.valueOf(mProfileModel.getEmail()));
                        etDescription.setText(String.valueOf(mProfileModel.getDescription()));
                        if (mProfileModel.getGender().equalsIgnoreCase("Female")) {
                            ((RadioButton) rgGender.getChildAt(1)).setChecked(true);
                        } else if (mProfileModel.getGender().equalsIgnoreCase("Male")) {
                            ((RadioButton) rgGender.getChildAt(0)).setChecked(true);
                        }
                        try {
                            GlideRequests with = GlideApp.with(UpdateProfileActivity.this);
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(ApiClient.BASE_URL);
                            stringBuilder3.append(mProfileModel.getImage());
                            with.load(stringBuilder3.toString()).centerCrop().placeholder((int) R.drawable.user_placeholder_white).dontAnimate().into(ivProfileImage);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
                Utility.hideProgressHud();
            }
        });
    }

    private void updateProfile() {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", "2");
        hm.put(SessionManager.KEY_FIRST_NAME, etFirstName.getText().toString().trim());
        hm.put(SessionManager.KEY_LAST_NAME, etLastName.getText().toString().trim());
        hm.put(SessionManager.KEY_GENDER, getValueOfRadioButton());
        hm.put(SessionManager.KEY_DOB, tvDob.getText().toString().trim());
        hm.put("email", tvEmail.getText().toString().trim());
        hm.put(SessionManager.KEY_USERNAME, tvUserName.getText().toString().trim());
        hm.put("description", etDescription.getText().toString().trim());
        if (path != null) {
            hm.put("image", path);
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Update Profile Request ");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.updateProfile(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Update Profile Response :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    if (object.getBoolean("status")) {
                        Toast.makeText(UpdateProfileActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
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

    private void getPermissions() {
        AlertDialog.Builder builder;
        Editor editor;
        if (ContextCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE") == 0) {
            if (ContextCompat.checkSelfPermission(this, "android.permission.CAMERA") == 0) {
                try {
                    showPictureDialog();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, "android.permission.CAMERA")) {
                if (permissionStatus.getBoolean("android.permission.WRITE_EXTERNAL_STORAGE", false)) {
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle((CharSequence) "Need Storage Permission");
                    builder.setMessage((CharSequence) "This app needs storage permission.");
                    builder.setPositiveButton((CharSequence) "Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivityForResult(intent, 101);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
                }
                editor = permissionStatus.edit();
                editor.putBoolean("android.permission.WRITE_EXTERNAL_STORAGE", true);
                editor.commit();
            }
        }
        builder = new AlertDialog.Builder(this);
        builder.setTitle((CharSequence) "Need Storage Permission");
        builder.setMessage((CharSequence) "This app needs storage permission.");
        builder.setPositiveButton((CharSequence) "Grant", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.CAMERA"}, 100);
            }
        });
        builder.setNegativeButton((CharSequence) "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        editor = permissionStatus.edit();
        editor.putBoolean("android.permission.WRITE_EXTERNAL_STORAGE", true);
        editor.commit();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnSubmit) {
            updateProfile();
        } else if (id == R.id.ivProfileImage) {
            getPermissions();
        } else if (id == R.id.tvDob) {
            showDatePicker();
            DatePickerDialog datePickerDialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
            datePickerDialog.show();
        }
    }
}
