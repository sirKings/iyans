package com.iyans.utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.manager.Lifecycle;
import com.bumptech.glide.manager.RequestManagerTreeNode;
import com.bumptech.glide.request.RequestOptions;
import java.io.File;
import java.net.URL;

public class GlideRequests extends RequestManager {
    public GlideRequests(@NonNull Glide glide, @NonNull Lifecycle lifecycle, @NonNull RequestManagerTreeNode treeNode, @NonNull Context context) {
        super(glide, lifecycle, treeNode, context);
    }

    @CheckResult
    @NonNull
    public <ResourceType> GlideRequest<ResourceType> as(@NonNull Class<ResourceType> resourceClass) {
        return new GlideRequest(this.glide, this, resourceClass, this.context);
    }

    @NonNull
    public GlideRequests applyDefaultRequestOptions(@NonNull RequestOptions options) {
        return (GlideRequests) super.applyDefaultRequestOptions(options);
    }

    @NonNull
    public GlideRequests setDefaultRequestOptions(@NonNull RequestOptions options) {
        return (GlideRequests) super.setDefaultRequestOptions(options);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Bitmap> asBitmap() {
        return (GlideRequest) super.asBitmap();
    }

    @CheckResult
    @NonNull
    public GlideRequest<GifDrawable> asGif() {
        return (GlideRequest) super.asGif();
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> asDrawable() {
        return (GlideRequest) super.asDrawable();
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Bitmap bitmap) {
        return (GlideRequest) super.load(bitmap);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Drawable drawable) {
        return (GlideRequest) super.load(drawable);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable String string) {
        return (GlideRequest) super.load(string);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Uri uri) {
        return (GlideRequest) super.load(uri);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable File file) {
        return (GlideRequest) super.load(file);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable @RawRes @DrawableRes Integer id) {
        return (GlideRequest) super.load(id);
    }

    @Deprecated
    @CheckResult
    public GlideRequest<Drawable> load(@Nullable URL url) {
        return (GlideRequest) super.load(url);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable byte[] bytes) {
        return (GlideRequest) super.load(bytes);
    }

    @CheckResult
    @NonNull
    public GlideRequest<Drawable> load(@Nullable Object o) {
        return (GlideRequest) super.load(o);
    }

    @CheckResult
    @NonNull
    public GlideRequest<File> downloadOnly() {
        return (GlideRequest) super.downloadOnly();
    }

    @CheckResult
    @NonNull
    public GlideRequest<File> download(@Nullable Object o) {
        return (GlideRequest) super.download(o);
    }

    @CheckResult
    @NonNull
    public GlideRequest<File> asFile() {
        return (GlideRequest) super.asFile();
    }

    protected void setRequestOptions(@NonNull RequestOptions toSet) {
        if (toSet instanceof GlideOptions) {
            super.setRequestOptions(toSet);
        } else {
            super.setRequestOptions(new GlideOptions().apply(toSet));
        }
    }
}
