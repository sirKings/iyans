package com.iyans.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyans.R;
import com.iyans.dashboard.dashfragments.FeedsFragment;
import com.iyans.dashboard.dashfragments.NotificationFragment;
import com.iyans.dashboard.dashfragments.ProfileFragment;
import com.iyans.dashboard.dashfragments.TelePostFragment;
import com.iyans.dashboard.settings.SettingActivity;
import com.iyans.model.UserModel;
import com.iyans.utility.Constant;

public class MainActivity extends AppCompatActivity implements OnClickListener {
    //TextView tvChat;
    ImageView tvFeed;
    ImageView tvNotifcation;
    ImageView tvProfile;
    ImageView tvTelepost;
    private DatabaseReference userRef;
    public UserModel currentUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_main);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        getCurrentUser();
        tvFeed = (ImageView) findViewById(R.id.tvFeed);
        tvProfile = (ImageView) findViewById(R.id.tvProfile);
        tvTelepost = (ImageView) findViewById(R.id.tvTelepost);
        //tvChat = (TextView) findViewById(R.id.tvChat);
        tvNotifcation = (ImageView) findViewById(R.id.tvNotifcation);
        this.tvFeed.setOnClickListener(this);
        this.tvProfile.setOnClickListener(this);
        this.tvTelepost.setOnClickListener(this);
        //this.tvChat.setOnClickListener(this);
        this.tvNotifcation.setOnClickListener(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FeedsFragment()).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, FeedsFragment.newInstance("", ""));
        transaction.commit();
    }

    @Override
    public void onClick(View view) {
        Fragment selectedFragment = null;
        switch (view.getId()) {
            //case R.id.tvChat:
            //   selectedFragment = ChatFragment.newInstance("", "");
            //   break;
            case R.id.tvFeed:
                selectedFragment = FeedsFragment.newInstance("", "");
                break;
            case R.id.tvNotifcation:
                selectedFragment = NotificationFragment.newInstance("", "");
                break;
            case R.id.tvProfile:
                selectedFragment = ProfileFragment.newInstance("", "");
                break;
            case R.id.tvTelepost:
                selectedFragment = TelePostFragment.newInstance("", "");
                break;
            default:
                break;
        }
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, selectedFragment);
        transaction.commit();
    }

    private void getCurrentUser(){
        String userId = FirebaseAuth.getInstance().getUid();

        userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               if (dataSnapshot.exists()){
                   Constant.currentUser = dataSnapshot.getValue(UserModel.class);
                   currentUserInfo = dataSnapshot.getValue(UserModel.class);
                   Log.e("User", Constant.currentUser.getDob());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void goHome(View view) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FeedsFragment()).commit();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, FeedsFragment.newInstance("", ""));
        transaction.commit();
    }

    public void goSetting(View view) {
        startActivity(new Intent(this, SettingActivity.class));
    }
}
