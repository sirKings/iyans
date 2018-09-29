package com.iyans.imagefilter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iyans.R;
import com.iyans.utility.ThumbnailItem;
import com.zomato.photofilters.imageprocessors.Filter;

import java.util.List;

public class ThumbnailsAdapter extends Adapter<ThumbnailsAdapter.MyViewHolder> {
    private ThumbnailsAdapterListener listener;
    private Context mContext;
    private int selectedIndex = 0;
    private List<ThumbnailItem> thumbnailItemList;

    public interface ThumbnailsAdapterListener {
        void onFilterSelected(Filter filter);
    }

    public class MyViewHolder extends ViewHolder {
        TextView filterName;
        ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            filterName = (TextView) view.findViewById(R.id.filter_name);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
        }
    }

    public ThumbnailsAdapter(Context context, List<ThumbnailItem> thumbnailItemList, ThumbnailsAdapterListener listener) {
        this.mContext = context;
        this.thumbnailItemList = thumbnailItemList;
        this.listener = listener;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_list_item, parent, false));
    }

    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ThumbnailItem thumbnailItem = (ThumbnailItem) thumbnailItemList.get(position);
        holder.thumbnail.setImageBitmap(thumbnailItem.image);
        holder.thumbnail.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
               listener.onFilterSelected(thumbnailItem.filter);
               selectedIndex = position;
               notifyDataSetChanged();
            }
        });
        holder.filterName.setText(thumbnailItem.filterName);
        if (selectedIndex == position) {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_selected));
        } else {
            holder.filterName.setTextColor(ContextCompat.getColor(mContext, R.color.filter_label_normal));
        }
    }

    public int getItemCount() {
        return thumbnailItemList.size();
    }
}
