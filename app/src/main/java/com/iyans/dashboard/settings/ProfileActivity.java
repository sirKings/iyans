package com.iyans.dashboard.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.iyans.R;
import com.iyans.adapter.AllUserAdapter;
import com.iyans.model.AllUsersModel;
import com.iyans.model.ProfileModel;
import com.iyans.service.ApiClient;
import com.iyans.service.ApiInterface;
import com.iyans.utility.Constant;
import com.iyans.utility.GlideApp;
import com.iyans.utility.GlideRequests;
import com.iyans.utility.Utility;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    private ArrayList<AllUsersModel> followsList;
    private ImageView imgVArchive;
    private ImageView imgVFollow;
    private ImageView imgVFollowing;
    private ImageView imgVUsers;
    private ImageView imgViewProfile;
    private AllUserAdapter mFollowAdapter;
    int numberOfColumns = 3;
    private RecyclerView recyclerView;
    private RelativeLayout relativeEditProfile;
    private TextView tvArchiveValue;
    private TextView tvFollowValue;
    private TextView tvFollowingValue;
    private TextView tvTelePostValue;
    private ProfileModel mProfileModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_profile);
        followsList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        imgViewProfile = (ImageView) findViewById(R.id.imgViewProfile);
        tvTelePostValue = (TextView) findViewById(R.id.tvTelePostValue);
        tvFollowValue = (TextView) findViewById(R.id.tvFollowValue);
        tvFollowingValue = (TextView) findViewById(R.id.tvFollowingValue);
        tvArchiveValue = (TextView) findViewById(R.id.tvArchiveValue);
        relativeEditProfile = (RelativeLayout) findViewById(R.id.relativeEditProfile);
        relativeEditProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, UpdateProfileActivity.class));
            }
        });
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        //ArrayList<String> followings = new ArrayList<String>(mProfileModel.getFollowers().values());
        mFollowAdapter = new AllUserAdapter(this, followsList, mProfileModel.getFollowers());
        recyclerView.setAdapter(mFollowAdapter);
        getUserProfile();
    }

    private void getUserProfile() {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", "1");
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
                        mProfileModel = (ProfileModel) new Gson().fromJson(String.valueOf(object.getJSONObject("data")), ProfileModel.class);
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("getCountFeeds");
                        stringBuilder2.append(mProfileModel.getCountFeeds());
                        Log.e(TAG, stringBuilder2.toString());
                        tvTelePostValue.setText(String.valueOf(mProfileModel.getCountFeeds()));
                        tvFollowValue.setText(String.valueOf(mProfileModel.getCountFollowers()));
                        tvFollowingValue.setText(String.valueOf(mProfileModel.getCountFollowings()));
                        tvArchiveValue.setText(String.valueOf(mProfileModel.getCountArchieves()));
                        try {
                            GlideRequests with = GlideApp.with(ProfileActivity.this);
                            stringBuilder2 = new StringBuilder();
                            stringBuilder2.append(ApiClient.BASE_URL);
                            stringBuilder2.append(mProfileModel.getImage());
                            with.load(stringBuilder2.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(imgViewProfile);
                        } catch (IndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                        Type userType = new TypeToken<ArrayList<AllUsersModel>>() {
                        }.getType();
                        List<AllUsersModel> follows = (List) new Gson().fromJson(object.getJSONObject("data").getJSONArray(Constant.FOLLOW).toString(), userType);
                        followsList.clear();
                        followsList.addAll(follows);
                        mFollowAdapter.notifyDataSetChanged();
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
}
