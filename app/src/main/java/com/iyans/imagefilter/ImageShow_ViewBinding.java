package com.iyans.imagefilter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.iyans.R;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class ImageShow_ViewBinding implements Unbinder {
    private ImageShow target;

    @UiThread
    public ImageShow_ViewBinding(ImageShow target) {
        this(target, target.getWindow().getDecorView());
    }

    @UiThread
    public ImageShow_ViewBinding(ImageShow target, View source) {
        this.target = target;
        target.imagePreview = (ImageView) Utils.findRequiredViewAsType(source, R.id.image_preview, "field 'imagePreview'", ImageView.class);
        target.tabLayout = (TabLayout) Utils.findRequiredViewAsType(source, R.id.tabs, "field 'tabLayout'", TabLayout.class);
        target.viewPager = (ViewPager) Utils.findRequiredViewAsType(source, R.id.viewpager, "field 'viewPager'", ViewPager.class);
        target.coordinatorLayout = (CoordinatorLayout) Utils.findRequiredViewAsType(source, R.id.coordinator_layout, "field 'coordinatorLayout'", CoordinatorLayout.class);
    }

    @CallSuper
    public void unbind() {
        ImageShow target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.imagePreview = null;
        target.tabLayout = null;
        target.viewPager = null;
        target.coordinatorLayout = null;
    }
}
