package com.iyans.userauth;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.iyans.R;
import com.iyans.model.AllUsersModel;
import com.iyans.model.ProfileModel;
import com.iyans.utility.App;
import com.iyans.utility.GPSTracker;
import com.iyans.utility.SessionManager;
import com.iyans.utility.Utility;
import com.iyans.utility.Validation;
import com.twitter.sdk.android.core.internal.TwitterApiConstants.Errors;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignupActivity extends AppCompatActivity implements LocationListener {
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1;
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private final String TAG = LoginActivity.class.getSimpleName();
    private ImageView backImageView;
    private Bitmap bitmap;
    private Bitmap bm1;
    private Button btnSignin;
    private Button btnSubmit;
    private OnDateSetListener date;
    private EditText etConfirmPass;
    private EditText etDescription;
    private EditText etEmail;
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etPass;
    private EditText etUserName;
    private String falg_camera_permission = "0";
    private GPSTracker gps;
    Gson gson;
    private String image;
    private String[] imagePermissions = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private CircleImageView ivProfileImage;
    private String[] locationPermissions = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    LocationListener mLocationListener;
    LocationManager mLocationManager;
    private Calendar myCalendar;
    private String new_password;
    private SharedPreferences permissionStatus;
    private RadioButton rbFemale;
    private RadioButton rbMale;
    private RadioGroup rgGender;
    private boolean sentToSettings = false;
    private Toolbar toolbar;
    private TextView tvDob;
    private TextView tvForgotPass;
    private TextView tvGender;
    private TextView tvRegister;
    private String userGender;
    private boolean visiblePass = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference userSignupRef;
    private String imageUrl;

    @Override
    public void onActivityResult(int r1, int r2, Intent data) {
        Uri imageUri;
        switch (r1) {
            case 0:
                try {
                    imageUri = getImageUri(this, (Bitmap) data.getExtras().get("data"));

                    StringBuilder stringBuilder2 = new StringBuilder();
                    stringBuilder2.append(imageUri.toString());
                    stringBuilder2.append(" path");
                    Log.e("bitmap............", stringBuilder2.toString());

//                        inCamera = new Intent(getActivity(), ImageShow.class);
//                        inCamera.putExtra("byteimage", imageUri.toString());
//                        startActivityForResult(inCamera, 10);
                    uploadImageToFirebase(imageUri);
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            case 1:
                try {
                    imageUri = data.getData();

                    Log.e("imageUri", imageUri.toString());
//                        inCamera = new Intent(getActivity(), ImageShow.class);
//                        inCamera.putExtra("byteimage", imageUri.toString());
//                        startActivityForResult(inCamera, 11);
                    uploadImageToFirebase(imageUri);
                    break;
                } catch (Exception e2) {
                    e2.printStackTrace();
                    break;
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        database = ((App) this.getApplication()).getDatabase();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        tvDob = (TextView) findViewById(R.id.tvDob);
        tvGender = (TextView) findViewById(R.id.tvGender);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        ivProfileImage = (CircleImageView) findViewById(R.id.ivProfileImage);
        etFirstName = (EditText) findViewById(R.id.etFirstName);
        etLastName = (EditText) findViewById(R.id.etLastName);
        etUserName = (EditText) findViewById(R.id.etUserName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPass = (EditText) findViewById(R.id.etPass);
        etConfirmPass = (EditText) findViewById(R.id.etConfirmPass);
        etDescription = (EditText) findViewById(R.id.etDescription);
        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        rbMale = (RadioButton) findViewById(R.id.rbMale);
        rbFemale = (RadioButton) findViewById(R.id.rbFemale);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSignin = (Button) findViewById(R.id.btnSignin);
        gson = new Gson();
        gps = new GPSTracker(this);
        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        permissionStatus = getSharedPreferences("permissionStatus", 0);
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {

            if(mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
                mLocationManager.requestLocationUpdates("network", 0, 0.0f, this);

            }

            //LocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
            //mLocationManager.requestLocationUpdates("network", 0, 0.0f, this);
            etPass.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 1 || motionEvent.getRawX() < ((float) (etPass.getRight() - etPass.getCompoundDrawables()[2].getBounds().width()))) {
                        return false;
                    }
                    if (visiblePass) {
                        etPass.setInputType(GmsClientSupervisor.DEFAULT_BIND_FLAGS);
                        etPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        visiblePass = false;
                    } else {
                        etPass.setInputType(Errors.ALREADY_UNFAVORITED);
                        etPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        visiblePass = true;
                    }
                    return true;
                }
            });
            etConfirmPass.setOnTouchListener(new OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent motionEvent) {
                    if (motionEvent.getAction() != 1 || motionEvent.getRawX() < ((float) (etConfirmPass.getRight() - etConfirmPass.getCompoundDrawables()[2].getBounds().width()))) {
                        return false;
                    }
                    if (visiblePass) {
                        etConfirmPass.setInputType(GmsClientSupervisor.DEFAULT_BIND_FLAGS);
                        etConfirmPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        visiblePass = false;
                    } else {
                        etConfirmPass.setInputType(Errors.ALREADY_UNFAVORITED);
                        etConfirmPass.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        visiblePass = true;
                    }
                    return true;
                }
            });
            return;
        }
        locationPermission();
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

    public void registerUser(View view) {
        locationPermission();
        int gender = rgGender.getCheckedRadioButtonId();
        if (gender == R.id.rbMale) {
            userGender = "male";
        } else if (gender == R.id.rbFemale) {
            userGender = "female";
        }
        if (!Validation.isValidString(etFirstName.getText().toString().trim())) {
            etFirstName.requestFocus();
            Toast.makeText(this, R.string.enter_firest_name, Toast.LENGTH_SHORT).show();
        } else if (!Validation.isValidString(etLastName.getText().toString().trim())) {
            etLastName.requestFocus();
            Toast.makeText(this, R.string.enter_last_name, Toast.LENGTH_SHORT).show();
        } else if (!Validation.isValidString(etUserName.getText().toString().trim())) {
            etUserName.requestFocus();
            Toast.makeText(this, R.string.enter_username, Toast.LENGTH_SHORT).show();
        } else if (!Validation.isValidEmail(etEmail.getText().toString().trim())) {
            etEmail.requestFocus();
            Toast.makeText(this, R.string.enter_valid_email, Toast.LENGTH_SHORT).show();
        } else if (!Validation.isValidPassword(etPass.getText().toString().trim())) {
            etPass.requestFocus();
            Toast.makeText(this, R.string.enter_valid_password, Toast.LENGTH_SHORT).show();
        } else if (!etConfirmPass.getText().toString().trim().equals(etPass.getText().toString().trim())) {
            etConfirmPass.requestFocus();
            Toast.makeText(this, R.string.pass_not_match, Toast.LENGTH_SHORT).show();
        } else if (!Validation.isValidString(tvDob.getText().toString().trim())) {
            tvDob.requestFocus();
            Toast.makeText(this, R.string.enter_date_of_birth, Toast.LENGTH_SHORT).show();
        } else if (userGender == null) {
            Toast.makeText(this, R.string.select_gender, Toast.LENGTH_SHORT).show();
        } else{
            userSignup(imageUrl);
        }
    }

    public void selectDateofBirth(View view) {
        showDatePicker();
        DatePickerDialog dialog = new DatePickerDialog(this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();
    }

    public void alreadyUser(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void backPage(View view) {
        finish();
    }

    private void locationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            falg_camera_permission = "1";
            return;
        }
        Builder builder;
        Editor editor;
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermissions[0])) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermissions[1])) {
                if (permissionStatus.getBoolean(locationPermissions[0], false)) {
                    builder = new Builder(this);
                    builder.setTitle("Need Location Permissions");
                    builder.setMessage("This app needs Location permissions.");
                    builder.setPositiveButton("Grant", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivityForResult(intent, 101);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    ActivityCompat.requestPermissions(this, locationPermissions, 100);
                }
                editor = permissionStatus.edit();
                editor.putBoolean(locationPermissions[0], true);
                editor.commit();
            }
        }
        builder = new Builder(this);
        builder.setTitle("Need Location Permissions");
        builder.setMessage("This app needs Location permissions.");
        builder.setPositiveButton("Grant", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(SignupActivity.this, locationPermissions, 100);
            }
        });
        builder.setNegativeButton("Cancel", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        editor = permissionStatus.edit();
        editor.putBoolean(locationPermissions[0], true);
        editor.commit();
    }

    private void imagePermission() {
        if (ContextCompat.checkSelfPermission(this, imagePermissions[0]) == 0 || ContextCompat.checkSelfPermission(this, imagePermissions[1]) == 0) {
            falg_camera_permission = "1";
            choosePhoto();
            return;
        }
        Builder builder;
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, imagePermissions[0]) && ActivityCompat.shouldShowRequestPermissionRationale(this, imagePermissions[1])) {
            builder = new Builder(this);
            builder.setTitle("Need Multiple Permissions");
            builder.setMessage("This app needs Camera and Location permissions.");
            builder.setPositiveButton("Grant", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    ActivityCompat.requestPermissions(SignupActivity.this, imagePermissions, 100);
                }
            });
            builder.setNegativeButton("Cancel", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else if (permissionStatus.getBoolean(imagePermissions[0], false)) {
            builder = new Builder(this);
            builder.setTitle("Need Multiple Permissions");
            builder.setMessage("This app needs Camera and Location permissions.");
            builder.setPositiveButton("Grant", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    sentToSettings = true;
                    Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                    startActivityForResult(intent, 101);
                    Toast.makeText(getBaseContext(), "Go to Permissions to Grant  Camera and Location", Toast.LENGTH_LONG).show();
                }
            });
            builder.setNegativeButton("Cancel", new OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
        } else {
            ActivityCompat.requestPermissions(this, imagePermissions, 100);
        }
        Editor editor = permissionStatus.edit();
        editor.putBoolean(imagePermissions[0], true);
        editor.commit();
    }

    @SuppressLint({"Override"})
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length <= 0 || grantResults[0] != 0) {
                falg_camera_permission = "0";
            } else {
                falg_camera_permission = "1";
            }
        }
    }

    public void choosePhoto() {
        Builder ab = new Builder(this);
        View v = getLayoutInflater().inflate(R.layout.take_photo_layout, null);
        ab.setView(v);
        final AlertDialog ad = ab.show();
        ad.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        RelativeLayout camera = (RelativeLayout) v.findViewById(R.id.rl_camera);
        ((RelativeLayout) v.findViewById(R.id.rl_gallery)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                galleryimage();
                ad.dismiss();
            }
        });
        camera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cameraIntent();
                ad.dismiss();

            }
        });
    }

    private void updateLabel() {
        tvDob.setText(new SimpleDateFormat("dd/MM/yyyy", Locale.US).format(myCalendar.getTime()));
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private void userSignup(String image) {

        Utility.showProgressHUD(this, "Please wait..");

        final HashMap<String, String> hm = new HashMap<>();
        hm.put(SessionManager.KEY_FIRST_NAME, etFirstName.getText().toString().trim());
        hm.put(SessionManager.KEY_LAST_NAME, etLastName.getText().toString().trim());
        hm.put(SessionManager.KEY_USERNAME, etUserName.getText().toString().trim());
        hm.put("email", etEmail.getText().toString().trim());
        hm.put("image", image);
        hm.put(SessionManager.KEY_LATTITUDE, String.valueOf(gps.getLatitude()).trim());
        hm.put(SessionManager.KEY_LONGITUDE, String.valueOf(gps.getLongitude()).trim());
        hm.put("description", etDescription.getText().toString().trim());
        hm.put(SessionManager.KEY_DOB, tvDob.getText().toString().trim());
        hm.put(SessionManager.KEY_GENDER, userGender);
        hm.put("device_type", "android");
        hm.put("device_token", "device_token");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Story Request signup: ");
        stringBuilder.append(etEmail.getText().toString().trim());
        Log.e(TAG, stringBuilder.toString());

        final AllUsersModel newUser = new AllUsersModel();
        newUser.setUserId(FirebaseAuth.getInstance().getUid());
        newUser.setUserName(etUserName.getText().toString());
        newUser.setCreatedAt(System.currentTimeMillis()+"");
        newUser.setCurrentLatitude(String.valueOf(gps.getLatitude()).trim());
        newUser.setCurrentLongitude(String.valueOf(gps.getLongitude()).trim());
        newUser.setDescription(etDescription.getText().toString());
        newUser.setDeviceToken("device_token");
        newUser.setDeviceType("android");
        newUser.setDob(tvDob.getText().toString().trim());
        newUser.setGender(userGender);
        if(imageUrl == null){
            newUser.setImage("default");
        }else{
            newUser.setImage(image);
        }

        newUser.setEmail(etEmail.getText().toString());
        newUser.setFirstName(etFirstName.getText().toString());
        newUser.setLastName(etLastName.getText().toString());


        mAuth.createUserWithEmailAndPassword(etEmail.getText().toString().trim(), etPass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();

                            String uid = user.getUid();
                            userSignupRef = database.getReference().child("users").child(uid);
                            userSignupRef.setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Utility.hideProgressHud();
                                    createProfile();
                                    Toast.makeText(SignupActivity.this, "Sign Up complete. Verify email to continue.",
                                            Toast.LENGTH_LONG).show();

                                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                                    finish();
                                }
                            });


                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignupActivity.this, "Sign up failed. Please retry.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private String base64Convert(Bitmap picturePath) {
        try {
            ByteArrayOutputStream bao = new ByteArrayOutputStream();
            picturePath.compress(CompressFormat.JPEG, 40, bao);
            String ba1 = Base64.encodeToString(bao.toByteArray(), Base64.DEFAULT);
            Log.e("base64 ", ba1);
            return ba1;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void pickImage(View view) {
        imagePermission();
    }

    @Override
    public void onLocationChanged(Location location) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(String.valueOf(location.getLatitude()));
        stringBuilder.append(":lon ");
        stringBuilder.append(String.valueOf(location.getLongitude()));
        Log.e("location :let ", stringBuilder.toString());
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
    }

    @Override
    public void onProviderEnabled(String s) {
    }

    @Override
    public void onProviderDisabled(String s) {
    }

    public void userForgotPass(View view) {
        Builder builder = new Builder(this);
        View v = getLayoutInflater().inflate(R.layout.forgot_pass_popup, null);
        final EditText etEmailRegister = (EditText) v.findViewById(R.id.etRegisteredEmail);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
        Button btnSend = (Button) v.findViewById(R.id.btnSend);
        builder.setView(v);
        builder.setCancelable(false);
        builder.create();
        final AlertDialog dialog = builder.show();
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
                forgotPass(etEmailRegister.getText().toString().trim());
            }
        });
    }

    private void forgotPass(final String email) {

        Utility.showProgressHUD(this, "Please wait..");

        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "A Password reset email has been sent.", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(getApplicationContext(), "There was an error sending email. Please retry.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        Utility.hideProgressHud();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    private void createProfile(){

        ArrayList<String> followers = new ArrayList<String>();
        followers.add("demo");

        ProfileModel profile = new ProfileModel();
        profile.setCountArchieves(0);
        profile.setCountFeeds(0);
        profile.setCountFollowers(0);
        profile.setCountFollowings(0);
        profile.setFollowers(followers);
        profile.setFollowings(followers);

        FirebaseDatabase.getInstance().getReference()
                .child("profiles")
                .child(FirebaseAuth.getInstance().getUid())
                .setValue(profile);
    }

    public void galleryimage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction("android.intent.action.GET_CONTENT");
        startActivityForResult(Intent.createChooser(intent, "Select file to upload "), 1);
    }

    private void cameraIntent() {
        startActivityForResult(new Intent("android.media.action.IMAGE_CAPTURE"), 0);
    }

    public void uploadImageToFirebase(Uri data){
        Utility.showProgressHUD(this, "Uploading Image, please wait");
        final StorageReference fileRef = FirebaseStorage.getInstance().getReference().child("profileImages").child(System.currentTimeMillis() + FirebaseAuth.getInstance().getUid());

        UploadTask uploadTask = fileRef.putFile(data);


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Utility.hideProgressHud();
                    Toast.makeText(SignupActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    throw task.getException();
                }

                // Continue with the task to get the download URL

                return fileRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString();
                    Glide.with(SignupActivity.this).load(imageUrl).into(ivProfileImage);
                    Utility.hideProgressHud();
                    Log.e("downloadUrl", downloadUri.toString());
                } else {
                    // Handle failures
                    // ...
                }
            }
        });
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        inImage.compress(CompressFormat.JPEG, 100, new ByteArrayOutputStream());
        return Uri.parse(MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null));
    }
}
