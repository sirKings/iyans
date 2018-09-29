package com.iyans.imagefilter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.iyans.R;
import com.iyans.imagefilter.ThumbnailsAdapter.ThumbnailsAdapterListener;
import com.iyans.utility.ThumbnailItem;
import com.iyans.utility.ThumbnailsManager;
import com.zomato.photofilters.imageprocessors.Filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class FiltersListFragment extends Fragment implements ThumbnailsAdapterListener {
    Uri imageBytes;
    FiltersListFragmentListener listener;
    ThumbnailsAdapter mAdapter;
    Bitmap originalImage;
    RecyclerView recyclerView;
    List<ThumbnailItem> thumbnailItemList;

    public interface FiltersListFragmentListener {
        void onFilterSelected(Filter filter);
    }

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filters_list, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            imageBytes = Uri.parse(bundle.getString("byteimage"));
            try {
                originalImage = Media.getBitmap(getActivity().getContentResolver(), imageBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ButterKnife.bind((Object) this, view);
        thumbnailItemList = new ArrayList();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mAdapter = new ThumbnailsAdapter(getActivity(), thumbnailItemList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), 0, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new SpacesItemDecoration((int) TypedValue.applyDimension(1, 8.0f, getResources().getDisplayMetrics())));
        recyclerView.setAdapter(mAdapter);
        prepareThumbnail(originalImage);
        return view;
    }

    public void prepareThumbnail(final Bitmap bitmap) {
        new Thread(new Runnable() {
            public void run() {
                Bitmap thumbImage;
                if (bitmap == null) {
                    thumbImage = BitmapUtils.getBitmapFromAssets(getActivity(), ImageShow.IMAGE_NAME, 100, 100);
                } else {
                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                }
                if (thumbImage != null) {
                    ThumbnailsManager.clearThumbs();
                    thumbnailItemList.clear();
                    ThumbnailItem thumbnailItem = new ThumbnailItem();
                    thumbnailItem.image = thumbImage;
                    thumbnailItem.filterName = getString(R.string.filter_normal);
                    ThumbnailsManager.addThumb(thumbnailItem);
                    for (Filter filter : FilterPack.getFilterPack(getActivity())) {
                        ThumbnailItem tI = new ThumbnailItem();
                        tI.image = thumbImage;
                        tI.filter = filter;
                        tI.filterName = "";
                        ThumbnailsManager.addThumb(tI);
                    }
                    thumbnailItemList.addAll(ThumbnailsManager.processThumbs(getActivity()));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        }).start();
    }

    public void onFilterSelected(Filter filter) {
        if (this.listener != null) {
            this.listener.onFilterSelected(filter);
        }
    }
}
