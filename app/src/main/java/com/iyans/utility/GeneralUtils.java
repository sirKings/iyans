package com.iyans.utility;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;

public final class GeneralUtils {
    private GeneralUtils() {
    }

    public static Bitmap generateCircularBitmap(Bitmap input) {
        int width = input.getWidth();
        int height = input.getHeight();
        Bitmap outputBitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Path path = new Path();
        path.addCircle((float) (width / 2), (float) (height / 2), (float) Math.min(width, height / 2), Direction.CCW);
        Canvas canvas = new Canvas(outputBitmap);
        canvas.clipPath(path);
        canvas.drawBitmap(input, 0.0f, 0.0f, null);
        return outputBitmap;
    }
}
