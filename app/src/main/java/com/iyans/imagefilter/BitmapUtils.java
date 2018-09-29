package com.iyans.imagefilter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class BitmapUtils {
    private static final String TAG = BitmapUtils.class.getSimpleName();

    public static Bitmap getBitmapFromAssets(Context context, String fileName, int width, int height) {
        AssetManager assetManager = context.getAssets();
        Bitmap bitmap = null;
        try {
            Options options = new Options();
            options.inJustDecodeBounds = true;
            InputStream istr = assetManager.open(fileName);
            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(istr, null, options);
        } catch (IOException e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exception: ");
            stringBuilder.append(e.getMessage());
            Log.e(str, stringBuilder.toString());
            return null;
        }
    }

    public static Bitmap getBitmapFromGallery(Context context, Uri path, int width, int height) {
        String[] filePathColumn = new String[]{"_data"};
        Cursor cursor = context.getContentResolver().query(path, filePathColumn, null, null, null);
        cursor.moveToFirst();
        String picturePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
        cursor.close();
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);
        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);
    }

    private static int calculateInSampleSize(Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;
            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        Options options = new Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static final String insertImage(ContentResolver cr, Bitmap source, String title, String description) {
        Uri url;
        Exception e;
        Exception e2;
        ContentResolver contentResolver = cr;
        Bitmap bitmap = source;
        String str = title;
        ContentValues values = new ContentValues();
        values.put("title", str);
        values.put("_display_name", str);
        values.put("description", description);
        values.put("mime_type", "image/jpeg");
        values.put("date_added", Long.valueOf(System.currentTimeMillis()));
        values.put("datetaken", Long.valueOf(System.currentTimeMillis()));
        String stringUrl = null;
        try {
            url = contentResolver.insert(Media.EXTERNAL_CONTENT_URI, values);
            if (bitmap != null) {
                OutputStream imageOut = null;
                try {
                    imageOut = contentResolver.openOutputStream(url);
                    bitmap.compress(CompressFormat.JPEG, 50, imageOut);
                    imageOut.close();
                    long id = ContentUris.parseId(url);
                    storeThumbnail(contentResolver, Thumbnails.getThumbnail(contentResolver, id, 1, null), id, 50.0f, 50.0f, 3);
                } catch (Exception e3) {
                    e = e3;
                    e2 = e;
                    if (url != null) {
                        contentResolver.delete(url, null, null);
                        url = null;
                    }
                    if (url != null) {
                        return url.toString();
                    }
                    return stringUrl;
                } catch (Throwable th) {
                    Throwable th2 = th;
                    imageOut.close();
                }
            }
            contentResolver.delete(url, null, null);
            url = null;
        } catch (Exception e4) {
            e = e4;
            url = null;
            e2 = e;
            if (url != null) {
                contentResolver.delete(url, null, null);
                url = null;
            }
            if (url != null) {
                return stringUrl;
            }
            return url.toString();
        }
        if (url != null) {
            return url.toString();
        }
        return stringUrl;
    }

    private static final Bitmap storeThumbnail(ContentResolver cr, Bitmap source, long id, float width, float height, int kind) {
        ContentResolver contentResolver = cr;
        Matrix matrix = new Matrix();
        matrix.setScale(width / ((float) source.getWidth()), height / ((float) source.getHeight()));
        Bitmap thumb = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
        ContentValues values = new ContentValues(4);
        values.put("kind", Integer.valueOf(kind));
        values.put("image_id", Integer.valueOf((int) id));
        values.put("height", Integer.valueOf(thumb.getHeight()));
        values.put("width", Integer.valueOf(thumb.getWidth()));
        try {
            OutputStream thumbOut = contentResolver.openOutputStream(contentResolver.insert(Thumbnails.EXTERNAL_CONTENT_URI, values));
            thumb.compress(CompressFormat.JPEG, 100, thumbOut);
            thumbOut.close();
            return thumb;
        } catch (FileNotFoundException e) {
            FileNotFoundException ex = e;
            return null;
        } catch (IOException e2) {
            IOException ex2 = e2;
            return null;
        }
    }
}
