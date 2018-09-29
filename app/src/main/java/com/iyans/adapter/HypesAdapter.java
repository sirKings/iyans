package com.iyans.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyans.R;
import com.iyans.model.HypesLikes;
import com.iyans.service.ApiClient;
import com.iyans.utility.GlideApp;
import com.iyans.utility.GlideRequests;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HypesAdapter extends Adapter<HypesAdapter.HypesViewHolder> {
    Context context;
    List<HypesLikes> hypesList;

    class HypesViewHolder extends ViewHolder {
        CircleImageView ivProfile;

        public HypesViewHolder(View itemView) {
            super(itemView);
            ivProfile = (CircleImageView) itemView.findViewById(R.id.ivProfileImage);
        }
    }

    public HypesAdapter(List<HypesLikes> hypesList, Context context) {
        this.hypesList = hypesList;
        this.context = context;
    }

    @NonNull
    public HypesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HypesViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.hypes_view, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HypesViewHolder holder, int position) {
        HypesLikes hl = (HypesLikes) hypesList.get(position);
        GlideRequests with = GlideApp.with(context);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ApiClient.BASE_URL);
        stringBuilder.append(hl.getUserImage());
        with.load(stringBuilder.toString()).centerCrop().placeholder((int) R.drawable.user_placeholder_white).dontAnimate().into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return hypesList.size();
    }
}
