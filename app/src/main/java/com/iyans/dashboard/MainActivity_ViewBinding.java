package com.iyans.dashboard;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;

import com.iyans.R;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class MainActivity_ViewBinding implements Unbinder {
    private MainActivity target;

    @UiThread
    public MainActivity_ViewBinding(MainActivity target) {
        this(target, target.getWindow().getDecorView());
    }

    @UiThread
    public MainActivity_ViewBinding(MainActivity target, View source) {
        this.target = target;
        target.tvFeed = (ImageView) Utils.findOptionalViewAsType(source, R.id.tvFeed, "field 'tvFeed'", ImageView.class);
        target.tvProfile = (ImageView) Utils.findOptionalViewAsType(source, R.id.tvProfile, "field 'tvProfile'", ImageView.class);
        target.tvTelepost = (ImageView) Utils.findOptionalViewAsType(source, R.id.tvTelepost, "field 'tvTelepost'", ImageView.class);
        //target.tvChat = (TextView) Utils.findOptionalViewAsType(source, R.id.tvChat, "field 'tvChat'", TextView.class);
        target.tvNotifcation = (ImageView) Utils.findOptionalViewAsType(source, R.id.tvNotifcation, "field 'tvNotifcation'", ImageView.class);
    }

    @CallSuper
    public void unbind() {
        MainActivity target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.tvFeed = null;
        target.tvProfile = null;
        target.tvTelepost = null;
        //target.tvChat = null;
        target.tvNotifcation = null;
    }
}
