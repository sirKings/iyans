package com.iyans.dashboard.dashfragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.TabLayoutOnPageChangeListener;
import android.support.design.widget.TabLayout.ViewPagerOnTabSelectedListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyans.R;
import com.iyans.dashboard.settings.UpdateProfileActivity;
import com.iyans.model.AllUsersModel;
import com.iyans.model.ProfileModel;
import com.iyans.utility.Utility;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static ArrayList<String> followers = new ArrayList<String>();
    public static ArrayList<String> followings = new ArrayList<String>();
    //public static ArrayList<String> followers;


    String TAG = ProfileFragment.class.getSimpleName();
    private ImageView imgVArchive;
    private ImageView imgVFollow;
    private ImageView imgVFollowing;
    private ImageView imgVUsers;
    private ImageView imgViewProfile;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    int numberOfColumns = 3;
    private TextView tvArchiveValue;
    private TextView tvEditProfile;
    private TextView tvFollowValue;
    private TextView tvFollowingValue;
    private TextView tvTelePostValue;
    private AllUsersModel curentUser;

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        getCurrentUser();
        imgViewProfile = (ImageView) view.findViewById(R.id.imgViewProfile);
        tvTelePostValue = (TextView) view.findViewById(R.id.tvTelePostValue);
        tvFollowValue = (TextView) view.findViewById(R.id.tvFollowValue);
        tvFollowingValue = (TextView) view.findViewById(R.id.tvFollowingValue);
        tvArchiveValue = (TextView) view.findViewById(R.id.tvArchiveValue);
        tvEditProfile = (TextView) view.findViewById(R.id.tvEditProfile);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager.addOnPageChangeListener(new TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new ViewPagerOnTabSelectedListener(mViewPager));

        tvEditProfile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdateProfileActivity.class));
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getUserProfile() {
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("profiles").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    ProfileModel profile = dataSnapshot.getValue(ProfileModel.class);
                    setUpProfile(profile);
                    Utility.hideProgressHud();
                }else{
                    Toast.makeText(getActivity(), "Unable to load profile", Toast.LENGTH_SHORT).show();
                    Utility.hideProgressHud();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Unable to load profile", Toast.LENGTH_SHORT).show();
                Utility.hideProgressHud();
            }
        });


//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(getActivity(), "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Profile Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.getUserProfile(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Profile Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        ProfileModel mProfileModel = (ProfileModel) new Gson().fromJson(String.valueOf(object.getJSONObject("data")), ProfileModel.class);
//                        StringBuilder stringBuilder2 = new StringBuilder();
//                        stringBuilder2.append("getCountFeeds");
//                        stringBuilder2.append(mProfileModel.getCountFeeds());
//                        Log.e("getCountFeeds", stringBuilder2.toString());
//
//                    }
//                } catch (Exception e2) {
//                    e2.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                Utility.hideProgressHud();
//                t.printStackTrace();
//            }
//        });
    }

    private void setUpProfile(ProfileModel profile){
        tvTelePostValue.setText(String.valueOf(profile.getCountFeeds()));
        tvFollowValue.setText(String.valueOf(profile.getCountFollowers()));
        tvFollowingValue.setText(String.valueOf(profile.getCountFollowings()));
        tvArchiveValue.setText(String.valueOf(profile.getCountArchieves()));
        Glide.with(getActivity()).load(curentUser.getImage()).into(imgViewProfile);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return UsersFragment.newInstance("", "");
                case 1:
                    return FollowerFragment.newInstance("", "");
                case 2:
                    return FollowingFragment.newInstance("", "");
                case 3:
                    return ArchiveFragment.newInstance("", "");
                default:
                    return UsersFragment.newInstance("", "");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    private void getCurrentUser(){
        String userId = FirebaseAuth.getInstance().getUid();
        Utility.showProgressHUD(getContext(), "Loading your profile");

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    curentUser = dataSnapshot.getValue(AllUsersModel.class);
                    getUserProfile();
                    getUserFollowings();
                    getUserFollowers();
                    //Utility.hideProgressHud();
                    Log.e("User", curentUser.getDob());
                    Log.e("User", curentUser.getLastName());
                    Log.e("User", curentUser.getFirstName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                    Utility.hideProgressHud();
            }
        });

    }

    private void getUserFollowings(){
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("profiles")
                .child(userId)
                .child("followings");


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot i: dataSnapshot.getChildren()){
                        followings.add(i.getValue(String.class));

                    }
                }else{
                    Toast.makeText(getActivity(), "Unable to load profile", Toast.LENGTH_SHORT).show();
                    Utility.hideProgressHud();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Unable to load profile", Toast.LENGTH_SHORT).show();
                Utility.hideProgressHud();
            }
        });
    }

    private void getUserFollowers(){
        String userId = FirebaseAuth.getInstance().getUid();
        DatabaseReference userRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("profiles")
                .child(userId)
                .child("followers");


        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for(DataSnapshot i: dataSnapshot.getChildren()){
                        followers.add(i.getValue(String.class));
                    }
                }else{
                    Toast.makeText(getActivity(), "Unable to load profile", Toast.LENGTH_SHORT).show();
                    Utility.hideProgressHud();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Unable to load profile", Toast.LENGTH_SHORT).show();
                Utility.hideProgressHud();
            }
        });
    }

    public void refreshProfile(){
        getUserProfile();
        getUserFollowers();
        getUserFollowings();
    }
}
