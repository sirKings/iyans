package com.iyans.dashboard;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.iyans.R;
import com.iyans.adapter.CommentsAdapter;
import com.iyans.adapter.HypesAdapter;
import com.iyans.model.AllUsersModel;
import com.iyans.model.Comments;
import com.iyans.model.FeedDetailModel;
import com.iyans.model.FeedsModel;
import com.iyans.model.HypesLikes;
import com.iyans.utility.App;
import com.iyans.utility.Constant;
import com.iyans.utility.Utility;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayerStandard;
import de.hdodenhof.circleimageview.CircleImageView;

public class FeedDetailActivity extends AppCompatActivity {
    private static String today;
    private final String TAG = FeedDetailActivity.class.getSimpleName();
    Button btnSend;
    EditText etTypeComment;
    FeedDetailModel feed = new FeedDetailModel();
    FeedDetailModel feeddtl;
    ImageView ivFeedImage;
    ImageView ivMenu;
    CircleImageView ivProfileImg;
    List<HypesLikes> like = new ArrayList<>();
    ArrayList<Comments> mCommentList;
    private CommentsAdapter mCommentsAdapter;
    FeedsModel mFeedsModel;
    MediaPlayer mp;
    RecyclerView rcvComments;
    RecyclerView rcvHypes;
    TextView tvComments;
    TextView tvDecription;
    TextView tvFeedTitle;
    TextView tvHypes;
    TextView tvName;
    TextView tvTimeShow;
    TextView tvViews;
    AllUsersModel curentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFeedsModel = (FeedsModel) getIntent().getSerializableExtra(Constant.FEEDS_OBJECT);
        getCurrentUser();
        setContentView((int) R.layout.activity_feed_detail);
        ivProfileImg = (CircleImageView) findViewById(R.id.ivProfileImg);
        tvName = (TextView) findViewById(R.id.tvName);
        tvFeedTitle = (TextView) findViewById(R.id.tvFeedTitle);
        tvTimeShow = (TextView) findViewById(R.id.tvTimeShow);
        tvDecription = (TextView) findViewById(R.id.tvDecription);
        tvHypes = (TextView) findViewById(R.id.tvHypes);
        tvComments = (TextView) findViewById(R.id.tvComments);
        tvViews = (TextView) findViewById(R.id.tvViews);
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        btnSend = (Button) findViewById(R.id.btnSend);
        etTypeComment = (EditText) findViewById(R.id.etTypeComment);
        rcvHypes = (RecyclerView) findViewById(R.id.rcvHypes);
        rcvComments = (RecyclerView) findViewById(R.id.rcvComments);
        ivFeedImage = (ImageView) findViewById(R.id.ivFeedImage);
        like.clear();
        mCommentList = new ArrayList<>();
        rcvHypes.setLayoutManager(new LinearLayoutManager(getApplicationContext(), 0, false));
        rcvComments.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mCommentsAdapter = new CommentsAdapter(mCommentList, this, etTypeComment, btnSend);
        rcvComments.setAdapter(mCommentsAdapter);


        ivMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(FeedDetailActivity.this, ivMenu);
                popup.getMenuInflater().inflate(R.menu.feed_detail_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.itm_archive:
                                addArchive();
                                break;
                            case R.id.itm_delete:
                                deleteFeed();
                                break;
                            case R.id.itm_edit:
                                editFeed();
                                break;
                            case R.id.itm_hide:
                                changeHideActivity();
                                break;
                            default:
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        tvHypes.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mp = MediaPlayer.create(FeedDetailActivity.this, R.raw.song);
                mp.start();
                like();
            }
        });
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                userAddComment(mFeedsModel);
            }
        });
    }

    public void backPage(View view) {
        finish();
    }

    private void userFeedDetail() {
        //ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");

        DatabaseReference feedRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("feeds")
                .child(mFeedsModel.getFeedId());

        feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    feeddtl = dataSnapshot.getValue(FeedDetailModel.class);
                    setupFeedDetails(feeddtl);
                    Utility.hideProgressHud();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mFeedsModel.getFeedId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Story Request feeddetail");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.FeedDetail(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Story List :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        feeddtl = (FeedDetailModel) new Gson().fromJson(String.valueOf(object.getJSONObject("data")), FeedDetailModel.class);
//                        Type likeType = new TypeToken<ArrayList<HypesLikes>>() {
//                        }.getType();
//                        like = (List) new Gson().fromJson(String.valueOf(object.getJSONObject("data").getJSONArray("likeUsers")).toString(), likeType);
//                        Type commentType = new TypeToken<ArrayList<Comments>>() {
//                        }.getType();
//                        mCommentList.addAll((List) new Gson().fromJson(String.valueOf(object.getJSONObject("data").getJSONArray("comments")).toString(), commentType));
//                        StringBuilder stringBuilder2 = new StringBuilder();
//                        stringBuilder2.append(String.valueOf(mCommentList.size()));
//                        stringBuilder2.append(" ");
//                        stringBuilder2.append(String.valueOf(like.size()));
//                        Log.e("size", stringBuilder2.toString());
//                        mCommentsAdapter.notifyDataSetChanged();
//                        rcvHypes.setAdapter(new HypesAdapter(like, FeedDetailActivity.this));
//                        GlideRequests with = GlideApp.with(FeedDetailActivity.this);
//                        stringBuilder2 = new StringBuilder();
//                        stringBuilder2.append(ApiClient.BASE_URL);
//                        stringBuilder2.append(feeddtl.getUserImage());
//                        with.load(stringBuilder2.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(ivProfileImg);
//                        TextView textView = tvName;
//                        StringBuilder stringBuilder3 = new StringBuilder();
//                        stringBuilder3.append(feeddtl.getFirstName());
//                        stringBuilder3.append(" ");
//                        stringBuilder3.append(feeddtl.getLastName());
//                        textView.setText(stringBuilder3.toString());
//                        tvFeedTitle.setText(feeddtl.getTitle());
//                        tvTimeShow.setText(App.getTimeStamp(feeddtl.getCreatedAt()));
//                        tvDecription.setText(feeddtl.getDescription());
//                        textView = tvHypes;
//                        stringBuilder3 = new StringBuilder();
//                        stringBuilder3.append("Hype (");
//                        stringBuilder3.append(feeddtl.getLikeCount());
//                        stringBuilder3.append(")");
//                        textView.setText(stringBuilder3.toString());
//                        textView = tvComments;
//                        stringBuilder3 = new StringBuilder();
//                        stringBuilder3.append("(");
//                        stringBuilder3.append(feeddtl.getCommentCount());
//                        stringBuilder3.append(")");
//                        textView.setText(stringBuilder3.toString());
//                        tvViews.setText(String.valueOf(feeddtl.getViewCount()));
//                        if (feeddtl.getLikeStatus().equalsIgnoreCase("like")) {
//                            tvHypes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hype, 0, 0, 0);
//                        } else if (feeddtl.getLikeStatus().equalsIgnoreCase("unlike")) {
//                            tvHypes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unhype, 0, 0, 0);
//                        }
//                        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
//                        if (feeddtl.getImageType().equalsIgnoreCase("video")) {
//                            ivFeedImage.setVisibility(View.GONE);
//                            jzVideoPlayerStandard.setVisibility(View.VISIBLE);
//                            HttpProxyCacheServer proxy = App.getProxy(FeedDetailActivity.this);
//                            StringBuilder stringBuilder4 = new StringBuilder();
//                            stringBuilder4.append(ApiClient.BASE_URL);
//                            stringBuilder4.append(feeddtl.getImage());
//                            jzVideoPlayerStandard.setUp(proxy.getProxyUrl(stringBuilder4.toString()), 1, feeddtl.getTitle());
//                        } else {
//                            ivFeedImage.setVisibility(View.VISIBLE);
//                            jzVideoPlayerStandard.setVisibility(View.GONE);
//                            GlideRequests with2 = GlideApp.with(FeedDetailActivity.this);
//                            stringBuilder3 = new StringBuilder();
//                            stringBuilder3.append(ApiClient.BASE_URL);
//                            stringBuilder3.append(feeddtl.getImage());
//                            with2.load(stringBuilder3.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(ivFeedImage);
//                        }
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//            }
        //});
    }

    private void setupFeedDetails(FeedDetailModel feedDtl){
        HypesLikes hype = new HypesLikes();
        hype.setFeedId(mFeedsModel.getFeedId());
        hype.setFirstName(curentUser.getFirstName());
        hype.setLastName(curentUser.getLastName());
        hype.setUserImage(curentUser.getUserName());
        hype.setLikeId(mFeedsModel.getFeedId() + FirebaseAuth.getInstance().getUid());
        //feeddtl = (FeedDetailModel) new Gson().fromJson(String.valueOf(object.getJSONObject("data")), FeedDetailModel.class);
        //Type likeType = new TypeToken<ArrayList<HypesLikes>>() {
        //}.getType();

        //Type commentType = new TypeToken<ArrayList<Comments>>() {
        //}.getType();
        if(feedDtl.getCommentCount() > 0){
            mCommentList.addAll(feedDtl.getComments());
//        StringBuilder stringBuilder2 = new StringBuilder();
//        stringBuilder2.append(String.valueOf(mCommentList.size()));
//        stringBuilder2.append(" ");
//        stringBuilder2.append(String.valueOf(like.size()));
//        Log.e("size", stringBuilder2.toString());
            mCommentsAdapter.notifyDataSetChanged();
        }
        if(feedDtl.getLikeCount() > 0){
            like = feedDtl.getLikeUsers();
            rcvHypes.setAdapter(new HypesAdapter(like, FeedDetailActivity.this));
        }

//        GlideRequests with = GlideApp.with(FeedDetailActivity.this);
//        stringBuilder2 = new StringBuilder();
//        stringBuilder2.append(ApiClient.BASE_URL);
//        stringBuilder2.append(feeddtl.getUserImage());
//        with.load(stringBuilder2.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(ivProfileImg);
        if(feedDtl.getUserImage() == null || feedDtl.getUserImage().contentEquals("default")){
            ivProfileImg.setImageDrawable(getResources().getDrawable(R.drawable.user_placeholder_white));
        }else {
            Glide.with(this).load(feedDtl.getUserImage()).into(ivProfileImg);
        }
        //Glide.with(this).load(feedDtl.getUserImage()).into(ivProfileImg);
        TextView textView = tvName;
        StringBuilder stringBuilder3 = new StringBuilder();
        stringBuilder3.append(feeddtl.getFirstName());
        stringBuilder3.append(" ");
        stringBuilder3.append(feeddtl.getLastName());
        textView.setText(stringBuilder3.toString());
        tvFeedTitle.setText(feeddtl.getTitle());
        tvTimeShow.setText(App.getTimeStamp(feeddtl.getCreatedAt()));
        tvDecription.setText(feeddtl.getDescription());
        //textView = tvHypes;
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("Hype (");
        stringBuilder3.append(feeddtl.getLikeCount());
        stringBuilder3.append(")");
        textView.setText(stringBuilder3.toString());
        //textView = tvComments;
        stringBuilder3 = new StringBuilder();
        stringBuilder3.append("(");
        stringBuilder3.append(feeddtl.getCommentCount());
        stringBuilder3.append(")");
        textView.setText(stringBuilder3.toString());
        //tvViews.setText(String.valueOf(feeddtl.getViewCount()));
        if(!(feedDtl.getLikeUsers() == null)){
            if (feeddtl.getLikeUsers().contains(hype)) {
                //tvHypes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hype, 0, 0, 0);
            } else {
                //tvHypes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unhype, 0, 0, 0);
            }
        }else{
            //tvHypes.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unhype, 0, 0, 0);
        }

        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
        if (feeddtl.getImageType().equalsIgnoreCase("video")) {
            ivFeedImage.setVisibility(View.GONE);
            jzVideoPlayerStandard.setVisibility(View.VISIBLE);
//            HttpProxyCacheServer proxy = App.getProxy(FeedDetailActivity.this);
//            StringBuilder stringBuilder4 = new StringBuilder();
//            stringBuilder4.append(ApiClient.BASE_URL);
//            stringBuilder4.append(feeddtl.getImage());
            jzVideoPlayerStandard.setUp(feeddtl.getImage(), 1, feeddtl.getTitle());
        } else {
            ivFeedImage.setVisibility(View.VISIBLE);
            jzVideoPlayerStandard.setVisibility(View.GONE);
//            GlideRequests with2 = GlideApp.with(FeedDetailActivity.this);
//            stringBuilder3 = new StringBuilder();
//            stringBuilder3.append(ApiClient.BASE_URL);
//            stringBuilder3.append(feeddtl.getImage());
//            with2.load(stringBuilder3.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(ivFeedImage);
            Glide.with(this).load(feedDtl.getImage()).into(ivFeedImage);
        }
    }

    private void addArchive() {
        //ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(this, "Please wait..");

        DatabaseReference archiveRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child(FirebaseAuth.getInstance().getUid())
                .child("archives")
                .child(mFeedsModel.getFeedId());
        archiveRef.setValue(feeddtl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Utility.hideProgressHud();
                Toast.makeText(FeedDetailActivity.this, "Archived successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utility.hideProgressHud();
                Toast.makeText(FeedDetailActivity.this, "Archive failed", Toast.LENGTH_SHORT).show();
            }
        });


//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mFeedsModel.getFeedId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Archive Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.addArchive(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Archive Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }

    private void deleteFeed() {

        DatabaseReference feedRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("feeds")
                .child(mFeedsModel.getFeedId());
        feedRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Utility.hideProgressHud();
                Toast.makeText(FeedDetailActivity.this, "Delete successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utility.hideProgressHud();
                Toast.makeText(FeedDetailActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });


//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(this, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mFeedsModel.getFeedId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Delete Feed Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.deleteFeed(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Delete Feed Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }

    private void changeHideActivity() {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(this, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mFeedsModel.getFeedId());
//        hm.put("hide_activity", MessengerShareContentUtility.SHARE_BUTTON_HIDE);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Hide Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.changeHideActivity(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Hide Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }

    private void like() {
        HypesLikes hype = new HypesLikes();
        hype.setFeedId(mFeedsModel.getFeedId());
        hype.setFirstName(curentUser.getFirstName());
        hype.setLastName(curentUser.getLastName());
        hype.setUserImage(curentUser.getUserName());
        hype.setLikeId(mFeedsModel.getFeedId() + FirebaseAuth.getInstance().getUid());
        if (feeddtl.getLikeStatus().equalsIgnoreCase("like")) {
            //feeddtl.setLikeStatus("unlike");
            //feeddtl.setLikeCount(feeddtl.getLikeCount() + 1);
            //feeddtl.getLikeUsers().add(hype);
        } else if (feeddtl.getLikeStatus().equalsIgnoreCase("unlike")) {
            //feeddtl.setLikeStatus("unlike");
            //feeddtl.setLikeCount(feeddtl.getLikeCount() - 1);
            //feeddtl.getLikeUsers().remove(hype);
        }


        DatabaseReference feedRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("feeds")
                .child(mFeedsModel.getFeedId());
        feedRef.setValue(feeddtl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                userFeedDetail();
                //Utility.hideProgressHud();
                //Toast.makeText(FeedDetailActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Utility.hideProgressHud();
                //Toast.makeText(FeedDetailActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });

//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(this, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mFeedsModel.getFeedId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("status");
//        stringBuilder.append(feeddtl.getLikeStatus());
//        Log.e("status", stringBuilder.toString());
//        if (feeddtl.getLikeStatus().equalsIgnoreCase("like")) {
//            hm.put("status", "unlike");
//        } else if (feeddtl.getLikeStatus().equalsIgnoreCase("unlike")) {
//            hm.put("status", "like");
//        }
//        stringBuilder = new StringBuilder();
//        stringBuilder.append("Like Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.like(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Like Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                        userFeedDetail();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }

    public void editFeed() {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(this, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mFeedsModel.getFeedId());
//        hm.put("title", "Feed Post Title");
//        hm.put("description", "Feed Post Description");
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Edit Feed Request  ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.editFeed(hm).enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Edit Feed Response : ");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    private void userAddComment(FeedsModel cm) {

        final Comments hype = new Comments();
        hype.setFeedId(mFeedsModel.getFeedId());
        hype.setFirstName(curentUser.getFirstName());
        hype.setLastName(curentUser.getLastName());
        hype.setUserImage(curentUser.getUserName());
        hype.setComment(etTypeComment.getText().toString().trim());
        feeddtl.setCommentCount(feeddtl.getCommentCount() + 1);
        feeddtl.getComments().add(hype);


        DatabaseReference feedRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("feeds")
                .child(mFeedsModel.getFeedId());
        feedRef.setValue(feeddtl).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                mCommentList.add(hype);
                mCommentsAdapter.notifyDataSetChanged();
                etTypeComment.setText("");
                //Utility.hideProgressHud();
                //Toast.makeText(FeedDetailActivity.this, "", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Utility.hideProgressHud();
                //Toast.makeText(FeedDetailActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
            }
        });

//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(this, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", cm.getFeedId());
//        hm.put("comment", etTypeComment.getText().toString().trim());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("userAddComment");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.AddComment(hm).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("userAddComment ");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    Toast.makeText(FeedDetailActivity.this, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    if (object.getBoolean("status")) {
//                        mCommentList.add(new Gson().fromJson(String.valueOf(object.getJSONObject("data")), Comments.class));
//                        mCommentsAdapter.notifyDataSetChanged();
//                        etTypeComment.setText("");
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//            }
//        });
    }

    private void getCurrentUser(){
        String userId = FirebaseAuth.getInstance().getUid();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    curentUser = dataSnapshot.getValue(AllUsersModel.class);
                    userFeedDetail();
                    Log.e("User", curentUser.getDob());
                    Log.e("User", curentUser.getLastName());
                    Log.e("User", curentUser.getFirstName());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
