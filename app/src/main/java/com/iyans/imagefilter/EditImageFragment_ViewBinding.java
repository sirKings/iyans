package com.iyans.imagefilter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.SeekBar;

import com.iyans.R;

import butterknife.Unbinder;
import butterknife.internal.Utils;

public class EditImageFragment_ViewBinding implements Unbinder {
    private EditImageFragment target;

    @UiThread
    public EditImageFragment_ViewBinding(EditImageFragment target, View source) {
        this.target = target;
        target.seekBarBrightness = (SeekBar) Utils.findRequiredViewAsType(source, R.id.seekbar_brightness, "field 'seekBarBrightness'", SeekBar.class);
        target.seekBarContrast = (SeekBar) Utils.findRequiredViewAsType(source, R.id.seekbar_contrast, "field 'seekBarContrast'", SeekBar.class);
        target.seekBarSaturation = (SeekBar) Utils.findRequiredViewAsType(source, R.id.seekbar_saturation, "field 'seekBarSaturation'", SeekBar.class);
    }

    @CallSuper
    public void unbind() {
        EditImageFragment target = this.target;
        if (target == null) {
            throw new IllegalStateException("Bindings already cleared.");
        }
        this.target = null;
        target.seekBarBrightness = null;
        target.seekBarContrast = null;
        target.seekBarSaturation = null;
    }
}
