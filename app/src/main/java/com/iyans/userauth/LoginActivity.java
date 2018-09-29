package com.iyans.userauth;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.CallbackManager.Factory;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphRequest.GraphJSONObjectCallback;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.internal.NativeProtocol;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.internal.ShareConstants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.iyans.R;
import com.iyans.dashboard.MainActivity;
import com.iyans.utility.App;
import com.iyans.utility.GPSTracker;
import com.iyans.utility.SessionManager;
import com.iyans.utility.Utility;
import com.iyans.utility.Validation;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig.Builder;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements LocationListener {
    private static final String EMAIL = "email";
    private static final int PERMISSION_CALLBACK_CONSTANT = 100;
    private static final String PROFILE = "public_profile";
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private final String TAG = LoginActivity.class.getSimpleName();
    private LoginButton btnFacebook;
    private TwitterLoginButton btnTwitter;
    private Button btn_facebook;
    private Button btn_twitter;
    private CallbackManager callbackManager;
    private EditText et_email;
    private EditText et_pass;
    private String falg_camera_permission = "0";
    long fb_id;
    private GPSTracker gps;
    Gson gson;
    private ImageView ib_back;
    private String[] locationPermissions = new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"};
    private FirebaseAuth mAuth;
    LocationManager mLocationManager;
    private SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private String socialEmail;
    private String socialId;
    private String socialImage;
    private String socialName;
    private TextView tv_forgotpass;
    private TextView tv_header_login;
    private Button tv_signup;
    private Button tv_submit;

    private FirebaseDatabase database;
    private DatabaseReference userSignupRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        database = ((App) this.getApplication()).getDatabase();

        ib_back = (ImageView) findViewById(R.id.backImageView);
        tv_header_login = (TextView) findViewById(R.id.tvLogin);
        tv_submit = (Button) findViewById(R.id.btnSubmit);
        tv_forgotpass = (TextView) findViewById(R.id.tvForgotPass);
        tv_signup = (Button) findViewById(R.id.btnSignup);
        btn_facebook = (Button) findViewById(R.id.btnFacebook);
        btn_twitter = (Button) findViewById(R.id.btnTwitter);
        btnFacebook = (LoginButton) findViewById(R.id.facebook);
        btnTwitter = (TwitterLoginButton) findViewById(R.id.twitter);
        et_email = (EditText) findViewById(R.id.etEmail);
        et_pass = (EditText) findViewById(R.id.etPass);
        permissionStatus = getSharedPreferences(NativeProtocol.RESULT_ARGS_PERMISSIONS, 0);
        gson = new Gson();
        callbackManager = Factory.create();
        gps = new GPSTracker(this);


        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {

            if(mLocationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER) && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                //mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);
                mLocationManager.requestLocationUpdates("gps", 0, 0.0f, this);
                mLocationManager.requestLocationUpdates("network", 0, 0.0f, this);

            }

            mAuth = FirebaseAuth.getInstance();

            btn_facebook.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnFacebook.performClick();
                }
            });

            Twitter.initialize(new Builder(this).twitterAuthConfig(new TwitterAuthConfig(getString(R.string.com_twitter_sdk_android_CONSUMER_KEY),
                    getString(R.string.com_twitter_sdk_android_CONSUMER_SECRET))).build());

            btnFacebook.setReadPermissions(Arrays.asList(new String[]{"email", PROFILE}));

            btnFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    handleFacebookAccessToken(loginResult);
                }

                @Override
                public void onCancel() {
                    Log.e("loginCancel ", "onCancel");
                }

                @Override
                public void onError(FacebookException error) {
                    Log.e("loginError ", error.toString());
                }
            });

            btn_twitter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnTwitter.performClick();
                }
            });



            btnTwitter.setCallback(new com.twitter.sdk.android.core.Callback<TwitterSession>() {
                @Override
                public void success(Result<TwitterSession> result) {

                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("twitterLogin:success ");
                    stringBuilder.append(((TwitterSession) result.data).getUserName());
                    Log.e(TAG, stringBuilder.toString());

                    handleTwitterSession((TwitterSession) result.data);
                }

                @Override
                public void failure(TwitterException exception) {
                    Log.e(TAG, "twitterLogin:failure", exception);
                }
            });

            //logOutFromTwitter();
            //logoutFromFacebook();
            return;
        }
        locationPermission();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        btnTwitter.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(final LoginResult loginResult) {
        AccessToken token = loginResult.getAccessToken();

        // [START_EXCLUDE silent]
        Utility.showProgressHUD(this, "Please wait..");
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        Log.d("xxx_token",credential.toString());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            getUserDetails(loginResult);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    protected void getUserDetails(LoginResult loginResult) {
        GraphRequest data_request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject json_object, GraphResponse response) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(json_object.toString());
                stringBuilder.append("");
                Log.e("loginResult data", stringBuilder.toString());
                try {
                    JSONObject object1 = json_object.getJSONObject("picture").getJSONObject("data");
                    socialId = json_object.getString(ShareConstants.WEB_DIALOG_PARAM_ID);
                    socialName = json_object.getString("name");
                    socialImage = object1.getString("url");
                    if (json_object.has("email")) {
                        socialEmail = json_object.getString("email");
                    } else {
                        socialEmail = "";
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                socialSignup(socialId, socialEmail, socialName, socialImage, "facebook");
            }
        });
        Bundle permission_param = new Bundle();
        permission_param.putString(GraphRequest.FIELDS_PARAM, "id,name,email,picture,first_name,last_name");
        data_request.setParameters(permission_param);
        data_request.executeAsync();
    }

    private void handleTwitterSession(TwitterSession session) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("handleTwitterSession: ");
        stringBuilder.append(session.toString());
        Log.e(TAG, stringBuilder.toString());

        AuthCredential credential = TwitterAuthProvider.getCredential(
                session.getAuthToken().token,
                session.getAuthToken().secret);

        Log.e("xxx_t", credential.toString());

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            TwitterCore.getInstance().getApiClient().getAccountService().verifyCredentials(true, false, true).enqueue(new com.twitter.sdk.android.core.Callback<User>() {
                                @Override
                                public void success(Result<User> userResult) {

                                    socialId = String.valueOf(((User) userResult.data).id);
                                    socialName = ((User) userResult.data).name;
                                    socialEmail = ((User) userResult.data).email;
                                    socialImage = ((User) userResult.data).profileImageUrl;

                                    StringBuilder stringBuilder = new StringBuilder();
                                    stringBuilder.append("Verify Credentials ");
                                    stringBuilder.append(socialName);
                                    stringBuilder.append(" ");
                                    stringBuilder.append(socialEmail);
                                    stringBuilder.append(" ");
                                    stringBuilder.append(socialImage);
                                    Log.e("TwitterKit", stringBuilder.toString());

                                    String photoUrlBiggerSize = ((User) userResult.data).profileImageUrl.replace("_normal", "_bigger");
                                    String photoUrlMiniSize = ((User) userResult.data).profileImageUrl.replace("_normal", "_mini");
                                    String photoUrlOriginalSize = ((User) userResult.data).profileImageUrl.replace("_normal", "");
                                    socialSignup(socialId, socialEmail, socialName, socialImage, "twitter");
                                }

                                @Override
                                public void failure(TwitterException exception) {
                                    Log.d("TwitterKit", "Verify Credentials Failure.", exception);
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    public void userSignin(View view) {
        if (!Validation.isValidString(et_email.getText().toString().trim())) {
            Toast.makeText(this, R.string.valid_username_email, Toast.LENGTH_SHORT).show();
        } else if (Validation.isValidPassword(et_pass.getText().toString().trim())) {
            userLogin();
        } else {
            Toast.makeText(this, R.string.enter_valid_password, Toast.LENGTH_SHORT).show();
        }
    }

    public void backPage(View view) {
        finish();
    }

    public void registerUser(View view) {
        startActivity(new Intent(this, SignupActivity.class));
    }

    private void userLogin() {

        Utility.showProgressHUD(this, "Please wait..");

        mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithEmailAndPassword(et_email.getText().toString().trim(), et_pass.getText().toString().trim())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Utility.hideProgressHud();
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //if(!user.isEmailVerified()) {
                                App.getInstance().getPrefManager().setLoginSession(true);
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            //}
                            //else {
//                                et_email.setText("");
//                                et_pass.setText("");
//                                Toast.makeText(getApplicationContext(), "Please verify your email to continue.",
//                                        Toast.LENGTH_LONG).show();
                            //}
                        } else {
                            // If sign in fails, display a message to the user.
                            Utility.hideProgressHud();
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Error: Invalid email or password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void userForgotPass(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = getLayoutInflater().inflate(R.layout.forgot_pass_popup, null);
        final EditText etEmailRegister = (EditText) v.findViewById(R.id.etRegisteredEmail);
        Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
        Button btnSend = (Button) v.findViewById(R.id.btnSend);
        builder.setView(v);
        builder.setCancelable(false);
        builder.create();
        final AlertDialog dialog = builder.show();
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btnSend.setOnClickListener(new OnClickListener() {
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

    private void logOutFromTwitter() {
        mAuth.signOut();
        TwitterCore.getInstance().getSessionManager().clearActiveSession();
    }

    private void logoutFromFacebook() {
        try {
            if (AccessToken.getCurrentAccessToken() != null) {
                AccessToken currentAccessToken = AccessToken.getCurrentAccessToken();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("/ ");
                stringBuilder.append(fb_id);
                stringBuilder.append("/permissions/");
                new GraphRequest(currentAccessToken, stringBuilder.toString(), null, HttpMethod.DELETE, new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();
                    }
                }).executeAsync();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void socialSignup(String id, String email, String name, String image, String type) {

        Utility.showProgressHUD(this, "Please wait..");

        HashMap<String, String> hm = new HashMap<>();
        hm.put(SessionManager.KEY_SOCIAL_ID, id);
        hm.put("email", email);
        hm.put(SessionManager.KEY_FIRST_NAME, name);
        hm.put(SessionManager.KEY_LOGIN_TYPE, type);
        hm.put("description", "");
        hm.put(SessionManager.KEY_LATTITUDE, String.valueOf(gps.getLatitude()).trim());
        hm.put(SessionManager.KEY_LONGITUDE, String.valueOf(gps.getLongitude()).trim());
        hm.put("device_type", "android");
        hm.put("device_token", "device_token");

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Story Request signup");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());

        FirebaseUser user = mAuth.getCurrentUser();

        String uid = user.getUid();
        userSignupRef = database.getReference(uid);
        userSignupRef.setValue(hm).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Utility.hideProgressHud();

                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });
    }

    private void locationPermission() {
        if (ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_FINE_LOCATION") == 0 || ContextCompat.checkSelfPermission(this, "android.permission.ACCESS_COARSE_LOCATION") == 0) {
            falg_camera_permission = "1";
            return;
        }
        AlertDialog.Builder builder;
        Editor editor;
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermissions[0])) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this, locationPermissions[1])) {
                if (permissionStatus.getBoolean(locationPermissions[0], false)) {
                    builder = new AlertDialog.Builder(this);
                    builder.setTitle("Need Location Permissions");
                    builder.setMessage("This app needs Location permissions.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
                            intent.setData(Uri.fromParts("package", getPackageName(), null));
                            startActivityForResult(intent, 101);
                            Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
        builder = new AlertDialog.Builder(this);
        builder.setTitle("Need Location Permissions");
        builder.setMessage("This app needs Location permissions.");
        builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                ActivityCompat.requestPermissions(LoginActivity.this, locationPermissions, 100);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
        editor = permissionStatus.edit();
        editor.putBoolean(locationPermissions[0], true);
        editor.commit();
    }

    @Override
    public void onLocationChanged(Location location) {
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
}
