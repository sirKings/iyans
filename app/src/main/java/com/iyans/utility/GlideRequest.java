package com.iyans.utility;

import android.content.Context;
import android.content.res.Resources.Theme;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.CheckResult;
import android.support.annotation.DrawableRes;
import android.support.annotation.FloatRange;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.TransitionOptions;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.DownsampleStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.net.URL;

public class GlideRequest<TranscodeType> extends RequestBuilder<TranscodeType> implements Cloneable {
    GlideRequest(@NonNull Class<TranscodeType> transcodeClass, @NonNull RequestBuilder<?> other) {
        super(transcodeClass, other);
    }

    GlideRequest(@NonNull Glide glide, @NonNull RequestManager requestManager, @NonNull Class<TranscodeType> transcodeClass, @NonNull Context context) {
        super(glide, requestManager, transcodeClass, context);
    }

    @CheckResult
    @NonNull
    protected GlideRequest<File> getDownloadOnlyRequest() {
        return new GlideRequest(File.class, this).apply(DOWNLOAD_ONLY_OPTIONS);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> sizeMultiplier(@FloatRange(from = 0.0d, to = 1.0d) float value) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).sizeMultiplier(value);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).sizeMultiplier(value);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> useUnlimitedSourceGeneratorsPool(boolean flag) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).useUnlimitedSourceGeneratorsPool(flag);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).useUnlimitedSourceGeneratorsPool(flag);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> useAnimationPool(boolean flag) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).useAnimationPool(flag);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).useAnimationPool(flag);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> onlyRetrieveFromCache(boolean flag) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).onlyRetrieveFromCache(flag);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).onlyRetrieveFromCache(flag);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> diskCacheStrategy(@NonNull DiskCacheStrategy strategy) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).diskCacheStrategy(strategy);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).diskCacheStrategy(strategy);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> priority(@NonNull Priority priority) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).priority(priority);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).priority(priority);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> placeholder(@Nullable Drawable drawable) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).placeholder(drawable);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).placeholder(drawable);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> placeholder(@DrawableRes int id) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).placeholder(id);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).placeholder(id);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> fallback(@Nullable Drawable drawable) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fallback(drawable);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fallback(drawable);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> fallback(@DrawableRes int id) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fallback(id);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fallback(id);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> error(@Nullable Drawable drawable) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).error(drawable);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).error(drawable);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> error(@DrawableRes int id) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).error(id);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).error(id);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> theme(@Nullable Theme theme) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).theme(theme);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).theme(theme);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> skipMemoryCache(boolean skip) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).skipMemoryCache(skip);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).skipMemoryCache(skip);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> override(int width, int height) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).override(width, height);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).override(width, height);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> override(int size) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).override(size);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).override(size);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> signature(@NonNull Key key) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).signature(key);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).signature(key);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public <T> GlideRequest<TranscodeType> set(@NonNull Option<T> option, @NonNull T t) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).set((Option) option, (Object) t);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).set((Option) option, (Object) t);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> decode(@NonNull Class<?> clazz) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).decode((Class) clazz);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).decode((Class) clazz);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> encodeFormat(@NonNull CompressFormat format) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).encodeFormat(format);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).encodeFormat(format);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> encodeQuality(@IntRange(from = 0, to = 100) int value) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).encodeQuality(value);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).encodeQuality(value);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> frame(@IntRange(from = 0) long value) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).frame(value);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).frame(value);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> format(@NonNull DecodeFormat format) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).format(format);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).format(format);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> disallowHardwareConfig() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).disallowHardwareConfig();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).disallowHardwareConfig();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> downsample(@NonNull DownsampleStrategy strategy) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).downsample(strategy);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).downsample(strategy);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> timeout(@IntRange(from = 0) int value) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).timeout(value);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).timeout(value);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalCenterCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalCenterCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalCenterCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> centerCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).centerCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).centerCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalFitCenter() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalFitCenter();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalFitCenter();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> fitCenter() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).fitCenter();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).fitCenter();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalCenterInside() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalCenterInside();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalCenterInside();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> centerInside() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).centerInside();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).centerInside();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalCircleCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalCircleCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalCircleCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> circleCrop() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).circleCrop();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).circleCrop();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> transform(@NonNull Transformation<Bitmap> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).transform((Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).transform((Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> transforms(@NonNull Transformation<Bitmap>... transformations) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).transforms((Transformation[]) transformations);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).transforms((Transformation[]) transformations);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> optionalTransform(@NonNull Transformation<Bitmap> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalTransform((Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalTransform((Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public <T> GlideRequest<TranscodeType> optionalTransform(@NonNull Class<T> clazz, @NonNull Transformation<T> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).optionalTransform((Class) clazz, (Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).optionalTransform((Class) clazz, (Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public <T> GlideRequest<TranscodeType> transform(@NonNull Class<T> clazz, @NonNull Transformation<T> transformation) {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).transform((Class) clazz, (Transformation) transformation);
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).transform((Class) clazz, (Transformation) transformation);
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> dontTransform() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).dontTransform();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).dontTransform();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> dontAnimate() {
        if (getMutableOptions() instanceof GlideOptions) {
            this.requestOptions = ((GlideOptions) getMutableOptions()).dontAnimate();
        } else {
            this.requestOptions = new GlideOptions().apply(this.requestOptions).dontAnimate();
        }
        return this;
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> apply(@NonNull RequestOptions options) {
        return (GlideRequest) super.apply(options);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> transition(@NonNull TransitionOptions<?, ? super TranscodeType> options) {
        return (GlideRequest) super.transition(options);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> listener(@Nullable RequestListener<TranscodeType> listener) {
        return (GlideRequest) super.listener(listener);
    }

    @NonNull
    public GlideRequest<TranscodeType> error(@Nullable RequestBuilder<TranscodeType> builder) {
        return (GlideRequest) super.error(builder);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType> builder) {
        return (GlideRequest) super.thumbnail((RequestBuilder) builder);
    }

    @SafeVarargs
    @CheckResult
    @NonNull
    public final GlideRequest<TranscodeType> thumbnail(@Nullable RequestBuilder<TranscodeType>... builders) {
        return (GlideRequest) super.thumbnail((RequestBuilder[]) builders);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> thumbnail(float sizeMultiplier) {
        return (GlideRequest) super.thumbnail(sizeMultiplier);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Object o) {
        return (GlideRequest) super.load(o);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Bitmap bitmap) {
        return (GlideRequest) super.load(bitmap);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Drawable drawable) {
        return (GlideRequest) super.load(drawable);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable String string) {
        return (GlideRequest) super.load(string);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable Uri uri) {
        return (GlideRequest) super.load(uri);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable File file) {
        return (GlideRequest) super.load(file);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable @RawRes @DrawableRes Integer id) {
        return (GlideRequest) super.load(id);
    }

    @Deprecated
    @CheckResult
    public GlideRequest<TranscodeType> load(@Nullable URL url) {
        return (GlideRequest) super.load(url);
    }

    @CheckResult
    @NonNull
    public GlideRequest<TranscodeType> load(@Nullable byte[] bytes) {
        return (GlideRequest) super.load(bytes);
    }

    @CheckResult
    public GlideRequest<TranscodeType> clone() {
        return (GlideRequest) super.clone();
    }
}
