package com.iyans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.iyans.R;
import com.iyans.model.AllUsersModel;

import java.util.ArrayList;

public class AllUserAdapter extends Adapter<AllUserAdapter.MyViewHolder> {
    private static final String TAG = AllUserAdapter.class.getSimpleName();
    Context context;
    private ArrayList<AllUsersModel> followsList;
    private ArrayList<String> followers;

    public class MyViewHolder extends ViewHolder {
        private ImageView imgViewUser;
        private ImageView ivFollowUnFollow;
        private TextView tvName;
        private TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            imgViewUser = (ImageView) itemView.findViewById(R.id.imgViewUser);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            ivFollowUnFollow = (ImageView) itemView.findViewById(R.id.ivFollowUnFollow);
            ivFollowUnFollow.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    followUnFollow((AllUsersModel) followsList.get(getAdapterPosition()));
                }
            });
        }
    }

    public AllUserAdapter(Context context, ArrayList<AllUsersModel> followsList, ArrayList<String> followers) {
        this.context = context;
        this.followsList = followsList;
        this.followers = followers;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        AllUsersModel followModel = (AllUsersModel) followsList.get(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(followModel.getFirstName());
        stringBuilder.append(" ");
        stringBuilder.append(followModel.getLastName());
        holder.tvName.setText(stringBuilder.toString());
        if (!followers.contains(followModel.getUserId())) {
            holder.ivFollowUnFollow.setImageResource(R.drawable.btn_plus);
        } else  {
            holder.ivFollowUnFollow.setImageResource(R.drawable.btn_minus);
        }
        Glide.with(this.context).load(followModel.getImage()).into(holder.imgViewUser);
    }

    @Override
    public int getItemCount() {
        return followsList.size();
    }

    private void followUnFollow(final AllUsersModel allUsersModel) {

        if(!followers.contains(allUsersModel.getUserId())){
            follow(true, allUsersModel);
        }else{
            follow(false, allUsersModel);
        }


//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("receiver_id", allUsersModel.getUserId());
//        hm.put("status", allUsersModel.getFollowStatus());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("followUnFollow ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.follow(hm).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("followUnFollow :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (allUsersModel.getFollowStatus().equalsIgnoreCase(Constant.FOLLOW)) {
//                        allUsersModel.setFollowStatus(Constant.UNFOLLOW);
//                    } else {
//                        allUsersModel.setFollowStatus(Constant.FOLLOW);
//                    }
//                    notifyDataSetChanged();
//                    Toast.makeText(context, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
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

    private void follow(final Boolean status, final AllUsersModel allUsersModel){

        DatabaseReference followRef = FirebaseDatabase
                .getInstance()
                .getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getUid())
                .child("followings")
                .child(allUsersModel.getUserId());
        if(status){
            followRef.setValue(allUsersModel).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    followers.add(allUsersModel.getUserId());
                    notifyDataSetChanged();
                    updateProfile(followers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Utility.hideProgressHud();
                    //Toast.makeText(FeedDetailActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            followRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    followers.remove(allUsersModel.getUserId());
                    notifyDataSetChanged();
                    updateProfile(followers);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Utility.hideProgressHud();
                    //Toast.makeText(FeedDetailActivity.this, "Delete failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateProfile(ArrayList<String> follower){
        DatabaseReference profileRef = FirebaseDatabase.getInstance()
                .getReference()
                .child("profiles")
                .child(FirebaseAuth.getInstance().getUid())
                .child("followers");
        profileRef.setValue(follower);


    }
}
