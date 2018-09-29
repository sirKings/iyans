package com.iyans.utility;

import android.content.Context;
import android.graphics.Bitmap;

import com.iyans.R;

import java.util.ArrayList;
import java.util.List;

public final class ThumbnailsManager {
    private static List<ThumbnailItem> filterThumbs = new ArrayList(10);
    private static List<ThumbnailItem> processedThumbs = new ArrayList(10);

    private ThumbnailsManager() {
    }

    public static void addThumb(ThumbnailItem thumbnailItem) {
        filterThumbs.add(thumbnailItem);
    }

    public static List<ThumbnailItem> processThumbs(Context context) {
        for (ThumbnailItem thumb : filterThumbs) {
            float size = context.getResources().getDimension(R.dimen.thumbnail_size);
            thumb.image = Bitmap.createScaledBitmap(thumb.image, (int) size, (int) size, false);
            thumb.image = thumb.filter.processFilter(thumb.image);
            processedThumbs.add(thumb);
        }
        return processedThumbs;
    }

    public static void clearThumbs() {
        filterThumbs = new ArrayList();
        processedThumbs = new ArrayList();
    }
}