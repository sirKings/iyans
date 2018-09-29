package com.iyans.dashboard.dashfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyans.R;
import com.iyans.adapter.FollowerAdapter;
import com.iyans.model.FollowerModel;

import java.util.ArrayList;

public class FollowerFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String TAG = FollowerFragment.class.getSimpleName();
    private ArrayList<FollowerModel> followsList;
    private FollowerAdapter mFollowAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    int numberOfColumns = 3;
    private RecyclerView recyclerView;
    private TextView tvInfo;

    public static FollowerFragment newInstance(String param1, String param2) {
        FollowerFragment fragment = new FollowerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        followsList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        mFollowAdapter = new FollowerAdapter(getActivity(), followsList);
        recyclerView.setAdapter(mFollowAdapter);
        getAllUsers();
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                getAllUsers();
            }
        });
        return view;
    }

    private void getAllUsers() {
        mSwipeRefreshLayout.setRefreshing(true);
        followsList.clear();
        DatabaseReference archiveRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("followers");
        archiveRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    mSwipeRefreshLayout.setRefreshing(false);
                    tvInfo.setVisibility(View.INVISIBLE);
                    for (DataSnapshot i: dataSnapshot.getChildren()){
                        FollowerModel follower = i.getValue(FollowerModel.class);
                        followsList.add(follower);
                        mFollowAdapter.notifyDataSetChanged();
                    }
                }else{
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("followers ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.followers(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                try {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("followers :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        tvInfo.setVisibility(View.INVISIBLE);
//                        Type followType = new TypeToken<ArrayList<FollowerModel>>() {
//                        }.getType();
//                        List<FollowerModel> follows = (List) new Gson().fromJson(object.getJSONArray("data").toString(), followType);
//                        followsList.clear();
//                        followsList.addAll(follows);
//                        mFollowAdapter.notifyDataSetChanged();
//                    } else {
//                        tvInfo.setVisibility(View.VISIBLE);
//                        tvInfo.setText(object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE));
//                    }
//                } catch (Exception e) {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                mSwipeRefreshLayout.setRefreshing(false);
//                t.printStackTrace();
//            }
//        });
    }
}
