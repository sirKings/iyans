package com.iyans.utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;

import java.io.File;

public final class GlideApp {
    private GlideApp() {
    }

    @Nullable
    public static File getPhotoCacheDir(@NonNull Context context) {
        return Glide.getPhotoCacheDir(context);
    }

    @Nullable
    public static File getPhotoCacheDir(@NonNull Context context, @NonNull String string) {
        return Glide.getPhotoCacheDir(context, string);
    }

    @NonNull
    public static Glide get(@NonNull Context context) {
        return Glide.get(context);
    }

    @VisibleForTesting
    @SuppressLint({"VisibleForTests"})
    @Deprecated
    public static void init(Glide glide) {
        Glide.init(glide);
    }

    @VisibleForTesting
    @SuppressLint({"VisibleForTests"})
    public static void init(@NonNull Context context, @NonNull GlideBuilder builder) {
        Glide.init(context, builder);
    }

    @VisibleForTesting
    @SuppressLint({"VisibleForTests"})
    public static void tearDown() {
        Glide.tearDown();
    }

    @NonNull
    public static GlideRequests with(@NonNull Context context) {
        return (GlideRequests) Glide.with(context);
    }

    @NonNull
    public static GlideRequests with(@NonNull Activity activity) {
        return (GlideRequests) Glide.with(activity);
    }

    @NonNull
    public static GlideRequests with(@NonNull FragmentActivity activity) {
        return (GlideRequests) Glide.with(activity);
    }

    @NonNull
    public static GlideRequests with(@NonNull Fragment fragment) {
        return (GlideRequests) Glide.with(fragment);
    }

    @Deprecated
    @NonNull
    public static GlideRequests with(@NonNull android.app.Fragment fragment) {
        return (GlideRequests) Glide.with(fragment);
    }

    @NonNull
    public static GlideRequests with(@NonNull View view) {
        return (GlideRequests) Glide.with(view);
    }
}
