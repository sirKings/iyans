package com.iyans.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.allattentionhere.autoplayvideos.AAH_CustomViewHolder;
import com.allattentionhere.autoplayvideos.AAH_VideosAdapter;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.iyans.R;
import com.iyans.dashboard.FeedDetailActivity;
import com.iyans.model.FeedsModel;
import com.iyans.utility.Constant;

import java.util.List;

public class FeedAdapter extends AAH_VideosAdapter {
    private static String TAG = FeedAdapter.class.getSimpleName();
    private static final int TYPE_IMAGE = 11;
    private static final int TYPE_VIDEO = 10;
    private Context mContext;
    private List<FeedsModel> mList;
    private TextView tvInfo;

    public class MyImageViewHolder extends AAH_CustomViewHolder {
        public ImageView ivFeedImage;
        public ImageView ivMore;
        public ImageView ivProfileImage;
        public RelativeLayout parent;
        public TextView tvComments;
        public TextView tvFeedDescription;
        public TextView tvHype;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvTitle;
        public TextView tvView;

        public MyImageViewHolder(View view) {
            super(view);
            ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
            ivMore = (ImageView) view.findViewById(R.id.ivMore);
            ivFeedImage = (ImageView) view.findViewById(R.id.ivFeedImage);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvFeedDescription = (TextView) view.findViewById(R.id.tvFeedDescription);
            tvHype = (TextView) view.findViewById(R.id.tvHype);
            tvComments = (TextView) view.findViewById(R.id.tvComments);
            tvView = (TextView) view.findViewById(R.id.tvView);
            parent = (RelativeLayout) view.findViewById(R.id.parent);
            parent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, FeedDetailActivity.class).putExtra(Constant.FEEDS_OBJECT, (FeedsModel) mList.get(getAdapterPosition())));
                }
            });
            ivMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, ivMore);
                    popup.getMenuInflater().inflate(R.menu.feed_detail_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.itm_archive:
                                    addArchive((FeedsModel) mList.get(getAdapterPosition()));
                                    break;
                                case R.id.itm_delete:
                                    deleteFeed((FeedsModel) mList.get(getAdapterPosition()));
                                    break;
                                case R.id.itm_edit:
                                    break;
                                case R.id.itm_hide:
                                    changeHideActivity((FeedsModel) mList.get(getAdapterPosition()));
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
        }
    }

    public class MyVideoViewHolder extends AAH_CustomViewHolder {
        public ImageView img_playback;
        public ImageView img_vol;
        boolean isMuted;
        public ImageView ivMore;
        public ImageView ivProfileImage;
        public RelativeLayout parent;
        public TextView tvComments;
        public TextView tvFeedDescription;
        public TextView tvHype;
        public TextView tvName;
        public TextView tvTime;
        public TextView tvTitle;
        public TextView tvView;

        public MyVideoViewHolder(View view) {
            super(view);
            ivProfileImage = (ImageView) view.findViewById(R.id.ivProfileImage);
            ivMore = (ImageView) view.findViewById(R.id.ivMore);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            tvFeedDescription = (TextView) view.findViewById(R.id.tvFeedDescription);
            tvHype = (TextView) view.findViewById(R.id.tvHype);
            tvComments = (TextView) view.findViewById(R.id.tvComments);
            tvView = (TextView) view.findViewById(R.id.tvView);
            img_vol = (ImageView) view.findViewById(R.id.img_vol);
            img_playback = (ImageView) view.findViewById(R.id.img_playback);
            parent = (RelativeLayout) view.findViewById(R.id.parent);
            parent.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(new Intent(mContext, FeedDetailActivity.class).putExtra(Constant.FEEDS_OBJECT, (FeedsModel) mList.get(getAdapterPosition())));
                }
            });
            ivMore.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(mContext, ivMore);
                    popup.getMenuInflater().inflate(R.menu.feed_detail_menu, popup.getMenu());
                    popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.itm_archive:
                                    addArchive((FeedsModel) mList.get(getAdapterPosition()));
                                    break;
                                case R.id.itm_delete:
                                    deleteFeed((FeedsModel) mList.get(getAdapterPosition()));
                                    break;
                                case R.id.itm_edit:
                                    break;
                                case R.id.itm_hide:
                                    changeHideActivity((FeedsModel) mList.get(getAdapterPosition()));
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
        }

        public void videoStarted() {
            super.videoStarted();
            img_playback.setImageResource(R.drawable.ic_pause);
            if (isMuted) {
                muteVideo();
                img_vol.setImageResource(R.drawable.ic_mute);
                return;
            }
            unmuteVideo();
            img_vol.setImageResource(R.drawable.ic_unmute);
        }

        public void pauseVideo() {
            super.pauseVideo();
            img_playback.setImageResource(R.drawable.ic_play);
        }
    }

    public FeedAdapter(Context mContext, List<FeedsModel> mList, TextView tvInfo) {
        this.mContext = mContext;
        this.mList = mList;
        this.tvInfo = tvInfo;
    }

    @Override
    public AAH_CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 11) {
            return new MyImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_row_image, parent, false));
        }
        return new MyVideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.feeds_row_video, parent, false));
    }

    @Override
    public void onBindViewHolder(final AAH_CustomViewHolder holder, final int position) {
        FeedsModel mDto = (FeedsModel) mList.get(position);
        TextView textView;
        StringBuilder stringBuilder;
        StringBuilder stringBuilder2;


        if (holder instanceof MyImageViewHolder) {
            MyImageViewHolder mMyImageViewHolder = (MyImageViewHolder) holder;

            //mMyImageViewHolder.ivFeedImage.layout(0, 0, 0, 0);
            textView = mMyImageViewHolder.tvName;
            stringBuilder = new StringBuilder();
            stringBuilder.append(mDto.getFirstName());
            stringBuilder.append(" ");
            stringBuilder.append(mDto.getLastName());
            textView.setText(stringBuilder.toString());
            mMyImageViewHolder.tvTitle.setText(mDto.getTitle());
            mMyImageViewHolder.tvTime.setText(mDto.getCreatedAt());
            mMyImageViewHolder.tvFeedDescription.setText(mDto.getDescription());

            mMyImageViewHolder.tvView.setText(String.valueOf(mDto.getViewCount()));
            //mMyImageViewHolder.tvComments.setText(String.valueOf(mDto.getCommentCount()));
            //mMyImageViewHolder.tvHype.setText(String.valueOf(mDto.getLikeCount()));
            if (mDto.getLikeStatus().equalsIgnoreCase("like")) {
                //mMyImageViewHolder.tvHype.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hype, 0, 0, 0);
            } else if (mDto.getLikeStatus().equalsIgnoreCase("unlike")) {
                //mMyImageViewHolder.tvHype.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unhype, 0, 0, 0);
            }

            //stringBuilder2 = new StringBuilder();
//            stringBuilder2.append(ApiClient.BASE_URL);
//            stringBuilder2.append(mDto.getUserImage());
            if(mDto.getUserImage() == null || mDto.getUserImage().contentEquals("default")){
                mMyImageViewHolder.ivProfileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.user_placeholder_white));
            }else {
                Glide.with(mContext).load(mDto.getUserImage()).into(mMyImageViewHolder.ivProfileImage);
            }
            if (mDto.getImageType().equalsIgnoreCase("image")) {
//                GlideRequests with2 = GlideApp.with(mContext);
//                StringBuilder stringBuilder3 = new StringBuilder();
//                stringBuilder3.append(ApiClient.BASE_URL);
//                stringBuilder3.append(mDto.getImage());
//                with2.load(stringBuilder3.toString()).centerCrop().placeholder((int) R.drawable.image_placeholder).dontAnimate().into(mMyImageViewHolder.ivFeedImage);
                Glide.with(mContext).load(mDto.getImage()).into(mMyImageViewHolder.ivFeedImage);
            }
            if (FirebaseAuth.getInstance().getUid().equalsIgnoreCase(mDto.getUserId())) {
                mMyImageViewHolder.ivMore.setVisibility(View.VISIBLE);
            } else {
                mMyImageViewHolder.ivMore.setVisibility(View.INVISIBLE);
            }
            mMyImageViewHolder.tvHype.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    like((FeedsModel) mList.get(position));
                }
            });
        } else if (holder instanceof MyVideoViewHolder) {
            final MyVideoViewHolder mMyVideoViewHolder = (MyVideoViewHolder) holder;
//            StringBuilder stringBuilder4 = new StringBuilder();
//            stringBuilder4.append(ApiClient.BASE_URL);
//            stringBuilder4.append(mDto.getImage());
            //Uri video = Uri.parse(mDto.getImage());
            holder.setVideoUrl(mDto.getImage());
            mMyVideoViewHolder.setLooping(true);
            textView = mMyVideoViewHolder.tvName;
            stringBuilder = new StringBuilder();
            stringBuilder.append(mDto.getFirstName());
            stringBuilder.append(" ");
            stringBuilder.append(mDto.getLastName());
            textView.setText(stringBuilder.toString());
            mMyVideoViewHolder.tvTitle.setText(mDto.getTitle());
            mMyVideoViewHolder.tvTime.setText(mDto.getCreatedAt());
            mMyVideoViewHolder.tvFeedDescription.setText(mDto.getDescription());
            mMyVideoViewHolder.tvView.setText(String.valueOf(mDto.getViewCount()));
            //mMyVideoViewHolder.tvComments.setText(String.valueOf(mDto.getCommentCount()));
            //mMyVideoViewHolder.tvHype.setText(String.valueOf(mDto.getLikeCount()));
            if (mDto.getLikeStatus().equalsIgnoreCase("like")) {
                //mMyVideoViewHolder.tvHype.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hype, 0, 0, 0);
            } else if (mDto.getLikeStatus().equalsIgnoreCase("unlike")) {
                //mMyVideoViewHolder.tvHype.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unhype, 0, 0, 0);
            }
//            with = GlideApp.with(mContext);
//            stringBuilder2 = new StringBuilder();
//            stringBuilder2.append(ApiClient.BASE_URL);
//            stringBuilder2.append(mDto.getUserImage());
//            with.load(stringBuilder2.toString()).centerCrop().placeholder((int) R.drawable.user_placeholder_white).dontAnimate().into(mMyVideoViewHolder.ivProfileImage);
            if( mDto.getUserImage() == null || mDto.getUserImage().contentEquals("default")){
                mMyVideoViewHolder.ivProfileImage.setImageDrawable(mContext.getResources().getDrawable(R.drawable.user_placeholder_white));
            }else{
                Glide.with(mContext).load(mDto.getUserImage()).into(mMyVideoViewHolder.ivProfileImage);
            }

            if (mDto.getImageType().equalsIgnoreCase("video")) {
                mMyVideoViewHolder.img_playback.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (holder.isPlaying()) {
                            holder.pauseVideo();
                            holder.setPaused(true);
                            return;
                        }
                        holder.playVideo();
                        holder.setPaused(false);
                    }
                });
                mMyVideoViewHolder.img_vol.setOnClickListener(new OnClickListener() {
                    public void onClick(View v) {
                        if (mMyVideoViewHolder.isMuted) {
                            holder.unmuteVideo();
                            mMyVideoViewHolder.img_vol.setImageResource(R.drawable.ic_unmute);
                        } else {
                            holder.muteVideo();
                            mMyVideoViewHolder.img_vol.setImageResource(R.drawable.ic_mute);
                        }
                        mMyVideoViewHolder.isMuted = !mMyVideoViewHolder.isMuted;
                    }
                });
                if (mDto.getImage() == null) {
                    mMyVideoViewHolder.img_vol.setVisibility(View.GONE);
                    mMyVideoViewHolder.img_playback.setVisibility(View.GONE);
                } else {
                    mMyVideoViewHolder.img_vol.setVisibility(View.VISIBLE);
                    mMyVideoViewHolder.img_playback.setVisibility(View.VISIBLE);
                }
            }
            if (FirebaseAuth.getInstance().getUid().equalsIgnoreCase(mDto.getUserId())) {
                mMyVideoViewHolder.ivMore.setVisibility(View.VISIBLE);
            } else {
                mMyVideoViewHolder.ivMore.setVisibility(View.INVISIBLE);
            }
            mMyVideoViewHolder.tvHype.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    like((FeedsModel) mList.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public int getItemViewType(int position) {
        if (((FeedsModel) mList.get(position)).getImageType().equalsIgnoreCase("image")) {
            return 11;
        }
        return 10;
    }

    private void addArchive(FeedsModel mDto) {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(mContext,"Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mDto.getFeedId());
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
//                        Toast.makeText(mContext, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(mContext, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                        notifyDataSetChanged();
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

    public void editFeed(FeedsModel mDto) {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(mContext, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mDto.getFeedId());
//        hm.put("title", "Feed Post Title");
//        hm.put("description", "Feed Post Description");
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Edit Feed Request  ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        //apiService.editFeed(hm).enqueue(new Callback);
    }

    private void changeHideActivity(final FeedsModel mdto) {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(mContext, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mdto.getFeedId());
//        hm.put("hide_activity", MessengerShareContentUtility.SHARE_BUTTON_HIDE);
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Hide Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.changeHideActivity(hm).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Hide Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(mContext, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                        mList.remove(mdto);
//                        notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }

    private void deleteFeed(final FeedsModel mdto) {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(mContext, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mdto.getFeedId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Delete Feed Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.deleteFeed(hm).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Delete Feed Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(mContext, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                        mList.remove(mdto);
//                        notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }

    private void like(final FeedsModel mdto) {
//        ApiInterface apiService = (ApiInterface) ApiClient.getClient().create(ApiInterface.class);
//        Utility.showProgressHUD(mContext, "Please wait..");
//        HashMap<String, String> hm = new HashMap<>();
//        hm.put("user_id", App.getInstance().getPrefManager().getUser().getUserId());
//        hm.put("feed_id", mdto.getFeedId());
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("status");
//        stringBuilder.append(mdto.getLikeStatus());
//        Log.e("status", stringBuilder.toString());
//        if (mdto.getLikeStatus().equalsIgnoreCase("like")) {
//            hm.put("status", "unlike");
//        } else if (mdto.getLikeStatus().equalsIgnoreCase("unlike")) {
//            hm.put("status", "like");
//        }
//        stringBuilder = new StringBuilder();
//        stringBuilder.append("Like Request ");
//        stringBuilder.append(String.valueOf(hm));
//        Log.e(TAG, stringBuilder.toString());
//        apiService.like(hm).enqueue(new Callback<ResponseBody>() {
//            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
//                Utility.hideProgressHud();
//                try {
//                    JSONObject object = new JSONObject(((ResponseBody) response.body()).string());
//                    StringBuilder stringBuilder = new StringBuilder();
//                    stringBuilder.append("Like Response :");
//                    stringBuilder.append(object.toString());
//                    Log.e(TAG, stringBuilder.toString());
//                    if (object.getBoolean("status")) {
//                        Toast.makeText(mContext, object.getString(ShareConstants.WEB_DIALOG_PARAM_MESSAGE), Toast.LENGTH_SHORT).show();
//                        if (mdto.getLikeStatus().equalsIgnoreCase("like")) {
//                            mdto.setLikeStatus("unlike");
//                        } else if (mdto.getLikeStatus().equalsIgnoreCase("unlike")) {
//                            mdto.setLikeStatus("like");
//                        }
//                        notifyDataSetChanged();
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
//                t.printStackTrace();
//                Utility.hideProgressHud();
//            }
//        });
    }
}
