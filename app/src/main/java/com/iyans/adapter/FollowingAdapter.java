package com.iyans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.share.internal.ShareConstants;
import com.iyans.R;
import com.iyans.model.FollowingModel;
import com.iyans.service.ApiClient;
import com.iyans.service.ApiInterface;
import com.iyans.utility.App;
import com.iyans.utility.Constant;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingAdapter extends Adapter<FollowingAdapter.MyViewHolder> {
    private static final String TAG = FollowingAdapter.class.getSimpleName();
    Context context;
    private ArrayList<FollowingModel> mList;

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
                    followUnFollow((FollowingModel) mList.get(getAdapterPosition()));
                }
            });
        }
    }

    public FollowingAdapter(Context context, ArrayList<FollowingModel> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        FollowingModel followModel = (FollowingModel) mList.get(position);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(followModel.getFirstName());
        stringBuilder.append(" ");
        stringBuilder.append(followModel.getLastName());
        holder.tvName.setText(stringBuilder.toString());
        //if (followModel.getFollowStatus().equalsIgnoreCase(Constant.FOLLOW)) {
            //holder.ivFollowUnFollow.setImageResource(R.drawable.btn_plus);
       // } else if (followModel.getFollowStatus().equalsIgnoreCase(Constant.UNFOLLOW)) {
            holder.ivFollowUnFollow.setImageResource(R.drawable.btn_minus);
        //}
//        if (followModel.getImage() != null) {
//            GlideRequests with = GlideApp.with(context);
//            stringBuilder = new StringBuilder();
//            stringBuilder.append(ApiClient.BASE_URL);
//            stringBuilder.append(followModel.getImage());
//            with.load(stringBuilder.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(holder.imgViewUser);
//        }
        Glide.with(context).load(followModel.getImage()).into(holder.imgViewUser);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    private void followUnFollow(final FollowingModel allUsersModel) {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
        hm.put("receiver_id", allUsersModel.getUserId());
        hm.put("status", allUsersModel.getFollowStatus());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("followUnFollow ");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.follow(hm).enqueue(new Callback<ResponseBody>() {
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("followUnFollow :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    if (allUsersModel.getFollowStatus().equalsIgnoreCase(Constant.FOLLOW)) {
                        allUsersModel.setFollowStatus(Constant.UNFOLLOW);
                    } else {
                        allUsersModel.setFollowStatus(Constant.FOLLOW);
                    }
                    mList.remove(allUsersModel);
                    notifyDataSetChanged();
                    Toast.makeText(context, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
