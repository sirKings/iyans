package com.iyans.imagefilter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.iyans.R;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class FiltersListFragment_ViewBinding implements Unbinder {
    private FiltersListFragment target;

    @UiThread
    public FiltersListFragment_ViewBinding(FiltersListFragment target, View source) {
        this.target = target;
        target.recyclerView = (RecyclerView) Utils.findRequiredViewAsType(source, R.id.recycler_view, "field 'recyclerView'", RecyclerView.class);
    }

    @CallSuper
    public void unbind() {
        FiltersListFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.recyclerView = null;
    }
}
