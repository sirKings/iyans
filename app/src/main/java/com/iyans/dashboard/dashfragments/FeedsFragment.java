package com.iyans.dashboard.dashfragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomRecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.iyans.R;
import com.iyans.adapter.FeedAdapter;
import com.iyans.model.FeedsModel;
import com.iyans.utility.App;
import com.iyans.utility.DividerItemDecoration;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FeedsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = FeedsFragment.class.getSimpleName();
    private FeedAdapter mChatListAdapter;
    private List<FeedsModel> mList;
    private String mParam1;
    private String mParam2;
    private AAH_CustomRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tvInfo;

    private FirebaseDatabase database;
    private DatabaseReference feedsRef;
    private Query query;

    public static FeedsFragment newInstance(String param1, String param2) {
        FeedsFragment fragment = new FeedsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mList = new ArrayList<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_feeds, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        database = ((App) this.getActivity().getApplication()).getDatabase();
        feedsRef = database.getReference("feeds");
        query = feedsRef.orderByChild("timestamp");
        query.keepSynced(true);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.mSwipeRefreshLayout);
        mSwipeRefreshLayout.setColorScheme(R.color.colorPrimary, R.color.colorPrimary, R.color.colorAccent, R.color.colorAccent);
        mRecyclerView = (AAH_CustomRecyclerView) view.findViewById(R.id.mRecyclerView);
        tvInfo = (TextView) view.findViewById(R.id.tvInfo);
        mChatListAdapter = new FeedAdapter(getActivity(), mList, tvInfo);
        mRecyclerView.setActivity(getActivity());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getResources().getDrawable(R.drawable.divider), false, true));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setPlayOnlyFirstVideo(true);
        mRecyclerView.setCheckForMp4(false);
        mRecyclerView.setVisiblePercent(100.0f);
        mRecyclerView.setAdapter(mChatListAdapter);
        mRecyclerView.smoothScrollBy(0, 1);
        mRecyclerView.smoothScrollBy(0, -1);
        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefreshLayout.setRefreshing(true);
                Log.d("Swipe", "Refreshing Number");
                getFeedList();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getFeedList();
    }

    @Override
    public void onPause() {
        super.onPause();
        mRecyclerView.stopVideos();
    }

    public void getFeedList() {
        mSwipeRefreshLayout.setRefreshing(true);
        mList.clear();
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mList.clear();
                mSwipeRefreshLayout.setRefreshing(false);

                if (dataSnapshot.exists()) {
                    tvInfo.setVisibility(View.INVISIBLE);
                    for (DataSnapshot feed_snapshot : dataSnapshot.getChildren()) {

                        JSONArray ja = new JSONArray();

//                        ja.put(feed_snapshot.child("comment_count").toString());
//                        ja.put(feed_snapshot.child("created_at").toString());
//                        ja.put(feed_snapshot.child("description").toString());
//                        ja.put(feed_snapshot.child("feed_id").toString());
//                        ja.put(feed_snapshot.child("first_name").toString());
//                        ja.put(feed_snapshot.child("id").toString());
//                        ja.put(feed_snapshot.child("image").toString());
//                        ja.put(feed_snapshot.child("image_preview_status").toString());
//                        ja.put(feed_snapshot.child("image_type").toString());
//                        ja.put(feed_snapshot.child("last_name").toString());
//                        ja.put(feed_snapshot.child("like_count").toString());
//                        ja.put(feed_snapshot.child("like_status").toString());
//                        ja.put(feed_snapshot.child("post_feed_date").toString());
//                        ja.put(feed_snapshot.child("status").toString());
//                        ja.put(feed_snapshot.child("title").toString());
//                        ja.put(feed_snapshot.child("updated_at").toString());
//                        ja.put(feed_snapshot.child("user_id").toString());
//                        ja.put(feed_snapshot.child("user_image").toString());
//                        ja.put(feed_snapshot.child("view_count").toString());
//
//                        Type feedType = new TypeToken<ArrayList<FeedsModel>>() {}.getType();
                        FeedsModel feed = feed_snapshot.getValue(FeedsModel.class);
                        mList.add(feed);
                        Log.e("Feed", feed.toString());

                        Log.e("Time", feed.getCreatedAt());
                        Log.e("Time", App.getTimeStamp(feed.getCreatedAt()).toString());
                        mChatListAdapter.notifyDataSetChanged();
                    }
                } else {
                    tvInfo.setVisibility(View.VISIBLE);
                    tvInfo.setText("message");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}
