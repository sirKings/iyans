package com.iyans.dashboard.dashfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
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
import com.iyans.adapter.ArchiveAdapter;
import com.iyans.model.ArchiveModel;

import java.util.ArrayList;

public class ArchiveFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String TAG = ArchiveFragment.class.getSimpleName();
    private ArrayList<ArchiveModel> followsList;
    private ArchiveAdapter mFollowAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private TextView tvInfo;

    public static ArchiveFragment newInstance(String param1, String param2) {
        ArchiveFragment fragment = new ArchiveFragment();
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFollowAdapter = new ArchiveAdapter(getActivity(), followsList);
        recyclerView.setAdapter(mFollowAdapter);
        archives();
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                archives();
            }
        });
        return view;
    }

    private void archives() {
        mSwipeRefreshLayout.setRefreshing(true);

        DatabaseReference archiveRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FirebaseAuth.getInstance().getUid())
                .child("archives");
        archiveRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            mSwipeRefreshLayout.setRefreshing(false);
                            tvInfo.setVisibility(View.INVISIBLE);
                            for (DataSnapshot i: dataSnapshot.getChildren()){
                                ArchiveModel archive = i.getValue(ArchiveModel.class);
                                followsList.add(archive);
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
//        stringBuilder.append("archives ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//
//
//        apiService.archives(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                try {
//                    mSwipeRefreshLayout.setRefreshing(false);
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("archives = ");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        tvInfo.setVisibility(View.INVISIBLE);
//                        Type archiveType = new TypeToken<ArrayList<ArchiveModel>>() {
//                        }.getType();
//                        List<ArchiveModel> follows = (List) new Gson().fromJson(object.getJSONArray("data").toString(), archiveType);
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
