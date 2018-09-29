package com.iyans.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iyans.R;
import com.iyans.model.ArchiveModel;
import com.iyans.utility.App;
import com.iyans.utility.Validation;

import java.util.ArrayList;

public class ArchiveAdapter extends Adapter<ArchiveAdapter.MyViewHolder> {
    private Context mContext;
    private ArrayList<ArchiveModel> modelArrayList;

    public class MyViewHolder extends ViewHolder {
        TextView tvDescription;
        TextView tvTime;
        TextView tvTitle;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
            tvTime = (TextView) itemView.findViewById(R.id.tvTime);
        }
    }

    public ArchiveAdapter(Context context, ArrayList<ArchiveModel> archiveModels) {
        this.mContext = context;
        this.modelArrayList = archiveModels;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.archive_row, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ArchiveModel mModel = (ArchiveModel) modelArrayList.get(position);
        if (Validation.isValidString(mModel.getTitle())) {
            holder.tvTitle.setText(mModel.getTitle());
        }
        if (Validation.isValidString(mModel.getDescription())) {
            holder.tvDescription.setText(mModel.getDescription());
        }
        if (Validation.isValidString(mModel.getCreatedAt())) {
            holder.tvTime.setText(App.getTimeStamp(mModel.getCreatedAt()));
        }
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }
}
