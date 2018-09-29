package com.iyans.imagefilter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.iyans.R;

public class EditImageFragment extends Fragment implements OnSeekBarChangeListener {
    private EditImageFragmentListener listener;
    SeekBar seekBarBrightness;
    SeekBar seekBarContrast;
    SeekBar seekBarSaturation;

    public interface EditImageFragmentListener {
        void onBrightnessChanged(int i);

        void onContrastChanged(float f);

        void onEditCompleted();

        void onEditStarted();

        void onSaturationChanged(float f);
    }

    public void setListener(EditImageFragmentListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_image, container, false);
        seekBarBrightness = (SeekBar) view.findViewById(R.id.seekbar_brightness);
        seekBarContrast = (SeekBar) view.findViewById(R.id.seekbar_contrast);
        seekBarSaturation = (SeekBar) view.findViewById(R.id.seekbar_saturation);
        this.seekBarBrightness.setMax(Callback.DEFAULT_DRAG_ANIMATION_DURATION);
        this.seekBarBrightness.setProgress(100);
        this.seekBarContrast.setMax(20);
        this.seekBarContrast.setProgress(0);
        this.seekBarSaturation.setMax(30);
        this.seekBarSaturation.setProgress(10);
        this.seekBarBrightness.setOnSeekBarChangeListener(this);
        this.seekBarContrast.setOnSeekBarChangeListener(this);
        this.seekBarSaturation.setOnSeekBarChangeListener(this);
        return view;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
        if (this.listener != null) {
            if (seekBar.getId() == R.id.seekbar_brightness) {
                this.listener.onBrightnessChanged(progress - 100);
            }
            if (seekBar.getId() == R.id.seekbar_contrast) {
                progress += 10;
                this.listener.onContrastChanged(((float) progress) * 0.1f);
            }
            if (seekBar.getId() == R.id.seekbar_saturation) {
                this.listener.onSaturationChanged(0.1f * ((float) progress));
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        if (this.listener != null) {
            this.listener.onEditStarted();
        }
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (this.listener != null) {
            this.listener.onEditCompleted();
        }
    }

    public void resetControls() {
        this.seekBarBrightness.setProgress(100);
        this.seekBarContrast.setProgress(0);
        this.seekBarSaturation.setProgress(10);
    }
}
