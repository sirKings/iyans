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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.internal.ShareConstants;
import com.google.gson.Gson;
import com.iyans.R;
import com.iyans.model.Comments;
import com.iyans.service.ApiClient;
import com.iyans.service.ApiInterface;
import com.iyans.utility.App;
import com.iyans.utility.Constant;
import com.iyans.utility.GlideApp;
import com.iyans.utility.GlideRequests;
import com.iyans.utility.Utility;
import com.iyans.utility.Validation;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentsAdapter extends Adapter<CommentsAdapter.CommentViewHolder> {
    private static final String TAG = CommentsAdapter.class.getSimpleName();
    Button btnUpdate;
    List<Comments> commentsList;
    Context context;
    EditText etComment;
    boolean isUpdatingComment;
    int pos;

    class CommentViewHolder extends ViewHolder {
        ImageView ivEdit;
        CircleImageView ivUserImage;
        TextView tvUserMessage;
        TextView tvUserName;

        public CommentViewHolder(View itemView) {
            super(itemView);
            ivUserImage = (CircleImageView) itemView.findViewById(R.id.ivUserImage);
            ivEdit = (ImageView) itemView.findViewById(R.id.ivEdit);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvUserMessage = (TextView) itemView.findViewById(R.id.tvUserMessage);
            btnUpdate.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Validation.isValidString(etComment.getText().toString().trim())) {
                        return;
                    }
                    if (isUpdatingComment) {
                        userEditComment((Comments) commentsList.get(getAdapterPosition()));
                        etComment.setText("");
                        btnUpdate.setText(Constant.REQUEST_STATUS_SEND);
                        isUpdatingComment = false;
                        return;
                    }
                    userAddComment((Comments) commentsList.get(getAdapterPosition()));
                    etComment.setText("");
                }
            });
        }
    }

    public CommentsAdapter(List<Comments> commentsList, Context context) {
        this.commentsList = commentsList;
        this.context = context;
    }

    public CommentsAdapter(List<Comments> commentsList, Context context, EditText etComment, Button btnUpdate) {
        this.commentsList = commentsList;
        this.context = context;
        this.etComment = etComment;
        this.btnUpdate = btnUpdate;
    }

    @NonNull
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.comments_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, final int position) {
        final Comments cm = (Comments) commentsList.get(position);
        GlideRequests with = GlideApp.with(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiClient.BASE_URL);
        stringBuilder.append(cm.getUserImage());
        with.load(stringBuilder.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(holder.ivUserImage);
        stringBuilder = new StringBuilder();
        stringBuilder.append(cm.getFirstName());
        stringBuilder.append(" ");
        stringBuilder.append(cm.getLastName());
        holder.tvUserName.setText(stringBuilder.toString());
        holder.tvUserMessage.setText(cm.getComment());
        holder.ivEdit.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                etComment.setText(cm.getComment());
                btnUpdate.setText("Update");
                isUpdatingComment = true;
                pos = position;
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(cm.getCommentId());
                stringBuilder.append(" ");
                stringBuilder.append(cm.getFeedId());
                stringBuilder.append(" ");
                stringBuilder.append(cm.getUserId());
                Log.e("edit click: ", stringBuilder.toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    private void userEditComment(final Comments cm) {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(context, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
        hm.put("feed_id", cm.getFeedId());
        hm.put("comment_id", cm.getCommentId());
        hm.put("comment", etComment.getText().toString().trim());
        apiService.EditComment(hm).enqueue(new Callback<ResponseBody>() {
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    JSONObject obj = object.getJSONObject("data");
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("Story List :");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    Toast.makeText(context, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
                    cm.setComment(object.getJSONObject("data").getString("comment"));
                    commentsList.set(pos, cm);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void userAddComment(Comments cm) {
        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
        Utility.showProgressHUD(context, "Please wait..");
        HashMap<String, String> hm = new HashMap<>();
        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
        hm.put("feed_id", cm.getFeedId());
        hm.put("comment", etComment.getText().toString().trim());
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("userAddComment");
        stringBuilder.append(String.valueOf(hm));
        Log.e(TAG, stringBuilder.toString());
        apiService.AddComment(hm).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                Utility.hideProgressHud();
                try {
                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("userAddComment ");
                    stringBuilder.append(object.toString());
                    Log.e(TAG, stringBuilder.toString());
                    Toast.makeText(context, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
                    if (object.getBoolean("status")) {
                        commentsList.add(new Gson().fromJson(String.valueOf(object.getJSONObject("data")), Comments.class));
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
