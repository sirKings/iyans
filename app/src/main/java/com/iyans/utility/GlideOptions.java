package com.iyans.utility;

import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.support.annotation.CheckResult;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestOptions;

public final class GlideOptions extends RequestOptions implements Cloneable {
    private static GlideOptions centerCropTransform2;
    private static GlideOptions centerInsideTransform1;
    private static GlideOptions circleCropTransform3;
    private static GlideOptions fitCenterTransform0;
    private static GlideOptions noAnimation5;
    private static GlideOptions noTransformation4;

    @CheckResult
    @NonNull
    public static GlideOptions sizeMultiplierOf(@FloatRange(from = 0.0d, to = 1.0d) float value) {
        return new GlideOptions().sizeMultiplier(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions diskCacheStrategyOf(@NonNull DiskCacheStrategy strategy) {
        return new GlideOptions().diskCacheStrategy(strategy);
    }

    @CheckResult
    @NonNull
    public static GlideOptions priorityOf(@NonNull Priority priority) {
        return new GlideOptions().priority(priority);
    }

    @CheckResult
    @NonNull
    public static GlideOptions placeholderOf(@Nullable Drawable drawable) {
        return new GlideOptions().placeholder(drawable);
    }

    @CheckResult
    @NonNull
    public static GlideOptions placeholderOf(@DrawableRes int id) {
        return new GlideOptions().placeholder(id);
    }

    @CheckResult
    @NonNull
    public static GlideOptions errorOf(@Nullable Drawable drawable) {
        return new GlideOptions().error(drawable);
    }

    @CheckResult
    @NonNull
    public static GlideOptions errorOf(@DrawableRes int id) {
        return new GlideOptions().error(id);
    }

    @CheckResult
    @NonNull
    public static GlideOptions skipMemoryCacheOf(boolean skipMemoryCache) {
        return new GlideOptions().skipMemoryCache(skipMemoryCache);
    }

    @CheckResult
    @NonNull
    public static GlideOptions overrideOf(@IntRange(from = 0) int value0, @IntRange(from = 0) int value1) {
        return new GlideOptions().override(value0, value1);
    }

    @CheckResult
    @NonNull
    public static GlideOptions overrideOf(@IntRange(from = 0) int value) {
        return new GlideOptions().override(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions signatureOf(@NonNull Key key) {
        return new GlideOptions().signature(key);
    }

    @CheckResult
    @NonNull
    public static GlideOptions fitCenterTransform() {
        if (fitCenterTransform0 == null) {
            fitCenterTransform0 = new GlideOptions().fitCenter().autoClone();
        }
        return fitCenterTransform0;
    }

    @CheckResult
    @NonNull
    public static GlideOptions centerInsideTransform() {
        if (centerInsideTransform1 == null) {
            centerInsideTransform1 = new GlideOptions().centerInside().autoClone();
        }
        return centerInsideTransform1;
    }

    @CheckResult
    @NonNull
    public static GlideOptions centerCropTransform() {
        if (centerCropTransform2 == null) {
            centerCropTransform2 = new GlideOptions().centerCrop().autoClone();
        }
        return centerCropTransform2;
    }

    @CheckResult
    @NonNull
    public static GlideOptions circleCropTransform() {
        if (circleCropTransform3 == null) {
            circleCropTransform3 = new GlideOptions().circleCrop().autoClone();
        }
        return circleCropTransform3;
    }

    @CheckResult
    @NonNull
    public static GlideOptions bitmapTransform(@NonNull Transformation<Bitmap> transformation) {
        return new GlideOptions().transform((Transformation) transformation);
    }

    @CheckResult
    @NonNull
    public static GlideOptions noTransformation() {
        if (noTransformation4 == null) {
            noTransformation4 = new GlideOptions().dontTransform().autoClone();
        }
        return noTransformation4;
    }

    @CheckResult
    @NonNull
    public static <T> GlideOptions option(@NonNull Option<T> option, @NonNull T t) {
        return new GlideOptions().set((Option) option, (Object) t);
    }

    @CheckResult
    @NonNull
    public static GlideOptions decodeTypeOf(@NonNull Class<?> clazz) {
        return new GlideOptions().decode((Class) clazz);
    }

    @CheckResult
    @NonNull
    public static GlideOptions formatOf(@NonNull DecodeFormat format) {
        return new GlideOptions().format(format);
    }

    @CheckResult
    @NonNull
    public static GlideOptions frameOf(@IntRange(from = 0) long value) {
        return new GlideOptions().frame(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions downsampleOf(@NonNull DownsampleStrategy strategy) {
        return new GlideOptions().downsample(strategy);
    }

    @CheckResult
    @NonNull
    public static GlideOptions timeoutOf(@IntRange(from = 0) int value) {
        return new GlideOptions().timeout(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions encodeQualityOf(@IntRange(from = 0, to = 100) int value) {
        return new GlideOptions().encodeQuality(value);
    }

    @CheckResult
    @NonNull
    public static GlideOptions encodeFormatOf(@NonNull CompressFormat format) {
        return new GlideOptions().encodeFormat(format);
    }

    @CheckResult
    @NonNull
    public static GlideOptions noAnimation() {
        if (noAnimation5 == null) {
            noAnimation5 = new GlideOptions().dontAnimate().autoClone();
        }
        return noAnimation5;
    }

    @CheckResult
    @NonNull
    public final GlideOptions sizeMultiplier(@FloatRange(from = 0.0d, to = 1.0d) float value) {
        return (GlideOptions) super.sizeMultiplier(value);
    }

    @CheckResult
    @NonNull
    public final GlideOptions useUnlimitedSourceGeneratorsPool(boolean flag) {
        return (GlideOptions) super.useUnlimitedSourceGeneratorsPool(flag);
    }

    @CheckResult
    @NonNull
    public final GlideOptions useAnimationPool(boolean flag) {
        return (GlideOptions) super.useAnimationPool(flag);
    }

    @CheckResult
    @NonNull
    public final GlideOptions onlyRetrieveFromCache(boolean flag) {
        return (GlideOptions) super.onlyRetrieveFromCache(flag);
    }

    @CheckResult
    @NonNull
    public final GlideOptions diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        return (GlideOptions) super.diskCacheStrategy(strategy);
    }

    @CheckResult
    @NonNull
    public final GlideOptions priority(@NonNull Priority priority) {
        return (GlideOptions) super.priority(priority);
    }

    @CheckResult
    @NonNull
    public final GlideOptions placeholder(@Nullable Drawable drawable) {
        return (GlideOptions) super.placeholder(drawable);
    }

    @CheckResult
    @NonNull
    public final GlideOptions placeholder(@DrawableRes int id) {
        return (GlideOptions) super.placeholder(id);
    }

    @CheckResult
    @NonNull
    public final GlideOptions fallback(@Nullable Drawable drawable) {
        return (GlideOptions) super.fallback(drawable);
    }

    @CheckResult
    @NonNull
    public final GlideOptions fallback(@DrawableRes int id) {
        return (GlideOptions) super.fallback(id);
    }

    @CheckResult
    @NonNull
    public final GlideOptions error(@Nullable Drawable drawable) {
        return (GlideOptions) super.error(drawable);
    }

    @CheckResult
    @NonNull
    public final GlideOptions error(@DrawableRes int id) {
        return (GlideOptions) super.error(id);
    }

    @CheckResult
    @NonNull
    public final GlideOptions theme(@Nullable Theme theme) {
        return (GlideOptions) super.theme(theme);
    }

    @CheckResult
    @NonNull
    public final GlideOptions skipMemoryCache(boolean skip) {
        return (GlideOptions) super.skipMemoryCache(skip);
    }

    @CheckResult
    @NonNull
    public final GlideOptions override(int width, int height) {
        return (GlideOptions) super.override(width, height);
    }

    @CheckResult
    @NonNull
    public final GlideOptions override(int size) {
        return (GlideOptions) super.override(size);
    }

    @CheckResult
    @NonNull
    public final GlideOptions signature(@NonNull Key key) {
        return (GlideOptions) super.signature(key);
    }

    @CheckResult
    public final GlideOptions clone() {
        return (GlideOptions) super.clone();
    }

    @CheckResult
    @NonNull
    public final <T> GlideOptions set(@NonNull Option<T> option, @NonNull T t) {
        return (GlideOptions) super.set(option, t);
    }

    @CheckResult
    @NonNull
    public final GlideOptions decode(@NonNull Class<?> clazz) {
        return (GlideOptions) super.decode(clazz);
    }

    @CheckResult
    @NonNull
    public final GlideOptions encodeFormat(@NonNull CompressFormat format) {
        return (GlideOptions) super.encodeFormat(format);
    }

    @CheckResult
    @NonNull
    public final GlideOptions encodeQuality(@IntRange(from = 0, to = 100) int value) {
        return (GlideOptions) super.encodeQuality(value);
    }

    @CheckResult
    @NonNull
    public final GlideOptions frame(@IntRange(from = 0) long value) {
        return (GlideOptions) super.frame(value);
    }

    @CheckResult
    @NonNull
    public final GlideOptions format(@NonNull DecodeFormat format) {
        return (GlideOptions) super.format(format);
    }

    @CheckResult
    @NonNull
    public final GlideOptions disallowHardwareConfig() {
        return (GlideOptions) super.disallowHardwareConfig();
    }

    @CheckResult
    @NonNull
    public final GlideOptions downsample(@NonNull DownsampleStrategy strategy) {
        return (GlideOptions) super.downsample(strategy);
    }

    @CheckResult
    @NonNull
    public final GlideOptions timeout(@IntRange(from = 0) int value) {
        return (GlideOptions) super.timeout(value);
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalCenterCrop() {
        return (GlideOptions) super.optionalCenterCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions centerCrop() {
        return (GlideOptions) super.centerCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalFitCenter() {
        return (GlideOptions) super.optionalFitCenter();
    }

    @CheckResult
    @NonNull
    public final GlideOptions fitCenter() {
        return (GlideOptions) super.fitCenter();
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalCenterInside() {
        return (GlideOptions) super.optionalCenterInside();
    }

    @CheckResult
    @NonNull
    public final GlideOptions centerInside() {
        return (GlideOptions) super.centerInside();
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalCircleCrop() {
        return (GlideOptions) super.optionalCircleCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions circleCrop() {
        return (GlideOptions) super.circleCrop();
    }

    @CheckResult
    @NonNull
    public final GlideOptions transform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideOptions) super.transform(transformation);
    }

    @SafeVarargs
    @CheckResult
    @NonNull
    public final GlideOptions transforms(@NonNull Transformation<Bitmap>... transformations) {
        return (GlideOptions) super.transforms(transformations);
    }

    @CheckResult
    @NonNull
    public final GlideOptions optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        return (GlideOptions) super.optionalTransform(transformation);
    }

    @CheckResult
    @NonNull
    public final <T> GlideOptions optionalTransform(@NonNull Class<T> clazz, @NonNull Transformation<T> transformation) {
        return (GlideOptions) super.optionalTransform((Class) clazz, (Transformation) transformation);
    }

    @CheckResult
    @NonNull
    public final <T> GlideOptions transform(@NonNull Class<T> clazz, @NonNull Transformation<T> transformation) {
        return (GlideOptions) super.transform((Class) clazz, (Transformation) transformation);
    }

    @CheckResult
    @NonNull
    public final GlideOptions dontTransform() {
        return (GlideOptions) super.dontTransform();
    }

    @CheckResult
    @NonNull
    public final GlideOptions dontAnimate() {
        return (GlideOptions) super.dontAnimate();
    }

    @CheckResult
    @NonNull
    public final GlideOptions apply(@NonNull RequestOptions options) {
        return (GlideOptions) super.apply(options);
    }

    @NonNull
    public final GlideOptions lock() {
        return (GlideOptions) super.lock();
    }

    @NonNull
    public final GlideOptions autoClone() {
        return (GlideOptions) super.autoClone();
    }
}
