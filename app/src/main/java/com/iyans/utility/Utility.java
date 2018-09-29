package com.iyans.utility;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ParseException;
import android.os.Build.VERSION;
import android.provider.Settings.Secure;
import android.support.graphics.drawable.PathInterpolatorCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog.Builder;
import android.support.v7.widget.helper.ItemTouchHelper.Callback;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.iyans.R;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;
import java.util.regex.Pattern;

public class Utility {
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private static String PREFERENCES = "user";
    private static final String TAG = Utility.class.getSimpleName();
    private static final int TWO_MINUTES = 120000;
    private static boolean cached = false;
    private static Dialog dialog;
    private static boolean hasImmersive;

    public static void hideSoftKeyboard(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getDeviceId(Context context) {
        return Secure.getString(context.getContentResolver(), "android_id");
    }

    public static boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            return true;
        }
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > 120000;
        boolean isSignificantlyOlder = timeDelta < -120000;
        boolean isNewer = timeDelta > 0;
        if (isSignificantlyNewer) {
            return true;
        }
        if (isSignificantlyOlder) {
            return false;
        }
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 6;
        boolean isSignificantlyLessAccurate = accuracyDelta > Callback.DEFAULT_DRAG_ANIMATION_DURATION;
        boolean isFromSameProvider = isSameProvider(location.getProvider(), currentBestLocation.getProvider());
        if (isMoreAccurate) {
            return true;
        }
        return ((!isNewer || isLessAccurate) && isNewer && !isSignificantlyLessAccurate && isFromSameProvider) ? false : false;
    }

    private static boolean isSameProvider(String provider1, String provider2) {
        if (provider1 != null) {
            return provider1.equals(provider2);
        }
        return provider2 == null;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String timeLeft(int delivery_time, String updatedDate) {
        try {
            long totalMinutes = ((long) ((delivery_time * 60) * 1000)) + new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(updatedDate).getTime();
            new SimpleDateFormat().setTimeZone(TimeZone.getTimeZone("UTC"));
            long leftMinutes = totalMinutes - getUTCDateTimeAslong();
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(leftMinutes);
            stringBuilder.append("");
            return stringBuilder.toString();
        } catch (Exception e) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(e);
            Log.e("Error in Utility 63", stringBuilder2.toString());
            e.printStackTrace();
            return "";
        }
    }

    public static long getUTCDateTimeAslong() {
        Date dateTime1 = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("IST"));
            Date date = new Date();
            dateTime1 = null;
            try {
                dateTime1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(format.format(date));
            } catch (ParseException e) {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("");
                stringBuilder.append(e);
                Log.e("Error in Utility 84", stringBuilder.toString());
                e.printStackTrace();
            }
        } catch (java.text.ParseException e2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("");
            stringBuilder2.append(e2);
            Log.e("Error in Utility 88", stringBuilder2.toString());
            e2.printStackTrace();
        }
        return dateTime1.getTime();
    }

    public static void logLargeString(String str) {
        if (str.length() > PathInterpolatorCompat.MAX_NUM_POINTS) {
            System.out.print(str.substring(0, PathInterpolatorCompat.MAX_NUM_POINTS));
            logLargeString(str.substring(PathInterpolatorCompat.MAX_NUM_POINTS));
            return;
        }
        System.out.print(str);
    }

    public static int getNavBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return context.getResources().getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    @SuppressLint({"NewApi"})
    public static boolean hasImmersive(Context ctx) {
        if (!cached) {
            boolean z = false;
            if (VERSION.SDK_INT < 19) {
                hasImmersive = false;
                cached = true;
                return false;
            }
            Display d = ((WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            DisplayMetrics realDisplayMetrics = new DisplayMetrics();
            d.getRealMetrics(realDisplayMetrics);
            int realHeight = realDisplayMetrics.heightPixels;
            int realWidth = realDisplayMetrics.widthPixels;
            DisplayMetrics displayMetrics = new DisplayMetrics();
            d.getMetrics(displayMetrics);
            int displayHeight = displayMetrics.heightPixels;
            if (realWidth <= displayMetrics.widthPixels) {
                if (realHeight <= displayHeight) {
                    hasImmersive = z;
                    cached = true;
                }
            }
            z = true;
            hasImmersive = z;
            cached = true;
        }
        return hasImmersive;
    }

    public static String getDeviceType(Context mContext) {
        if (new WebView(mContext).getSettings().getUserAgentString().contains("Mobile")) {
            System.out.println("Type:Mobile");
            return "ANDROID MOBILE";
        }
        System.out.println("Type:TAB");
        return "ANDROID TAB";
    }

    public static String getFormatedDate(String strDate, String sourceFormate, String destinyFormate) {
        Date date = null;
        try {
            date = new SimpleDateFormat(sourceFormate).parse(strDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SimpleDateFormat(destinyFormate).format(date);
    }

    public static String getFirstDayofWeek() {
        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        try {
            myDate = sdf.parse(str);
        } catch (ParseException e) {
        } catch (java.text.ParseException e2) {
            e2.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("getFirstDayofWeek:");
        stringBuilder.append(sdf.format(cal.getTime()));
        printStream.println(stringBuilder.toString());
        return sdf.format(cal.getTime());
    }

    public static String getDBCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public static String getCurrentDateMMMddyyyy() {
        return new SimpleDateFormat("MMM dd, yyyy").format(new Date());
    }

    public static String getCurrentMMddyy() {
        return new SimpleDateFormat("MM-dd-yy").format(new Date());
    }

    public static String getCurrentTime() {
        return new SimpleDateFormat("HH:mm").format(new Date());
    }

    public static String getLastDayofWeek() {
        String str = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date myDate = new Date();
        try {
            myDate = sdf.parse(str);
        } catch (ParseException e) {
        } catch (java.text.ParseException e2) {
            e2.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(myDate);
        cal.set(Calendar.DAY_OF_WEEK, 7);
        return sdf.format(cal.getTime());
    }

    public static void setBooleanPreferences(Context context, String key, boolean isCheck) {
        Editor editor = context.getSharedPreferences(PREFERENCES, 0).edit();
        editor.putBoolean(key, isCheck);
        editor.commit();
    }

    public static boolean getBooleanPreferences(Context context, String key) {
        return context.getSharedPreferences(PREFERENCES, 0).getBoolean(key, false);
    }

    public static void setStringPreferences(Context context, String key, String value) {
        Editor editor = context.getSharedPreferences(PREFERENCES, 0).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(PREFERENCES, 0).getString(name, null);
    }

    public static String getStringPreferences(Context context, String key) {
        return context.getSharedPreferences(PREFERENCES, 0).getString(key, "");
    }

    public static void setIntegerPreferences(Context context, String key, int value) {
        Editor editor = context.getSharedPreferences(PREFERENCES, 0).edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static void clearAllSharedPreferences(Context context) {
        Editor editor = context.getSharedPreferences(PREFERENCES, 0).edit();
        editor.clear();
        editor.commit();
    }

    public static int getIntegerPreferences(Context context, String key) {
        return context.getSharedPreferences(PREFERENCES, 0).getInt(key, 0);
    }


    public static void showProgressHUD(Context context, String message) {
        try {
            dialog = ProgressDialog.show(context, context.getString(R.string.app_name), message);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(true);
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("showProgressHUD ");
            stringBuilder.append(e.toString());
            Log.e(str, stringBuilder.toString());
            e.printStackTrace();
        }
    }

    public static void hideProgressHud() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("hideProgressHud ");
            stringBuilder.append(e.toString());
            Log.e(str, stringBuilder.toString());
            e.printStackTrace();
        }
    }

    public static String getCurrentMMddyyyy() {
        return new SimpleDateFormat("MM-dd-yyyy").format(new Date());
    }

    public static void Alert_NoFilter() {
    }

    public static String encodeToBase64(Bitmap bitmap, CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), 2);
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedBytes = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    @TargetApi(16)
    public static boolean checkPermission(final Context context) {
        if (VERSION.SDK_INT < 23 || ContextCompat.checkSelfPermission(context, "android.permission.READ_EXTERNAL_STORAGE") == 0) {
            return true;
        }
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, "android.permission.READ_EXTERNAL_STORAGE")) {
            Builder alertBuilder = new Builder(context);
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle((CharSequence) "Permission necessary");
            alertBuilder.setMessage((CharSequence) "External storage permission is necessary");
            alertBuilder.setPositiveButton(R.string.positive_button_datetime_picker, new OnClickListener() {
                @TargetApi(16)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
            });
            alertBuilder.create().show();
        } else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        }
        return false;
    }

    public static String getCompleteAddressthroughLatLngString(Context mContext, double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        try {
            List<Address> addresses = new Geocoder(mContext, Locale.getDefault()).getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                int i = 0;
                Address returnedAddress = (Address) addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");
                while (i < returnedAddress.getMaxAddressLineIndex()) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i));
                    strReturnedAddress.append(" ");
                    i++;
                }
                strAdd = strReturnedAddress.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (VERSION.SDK_INT > 20) {
            for (RunningAppProcessInfo processInfo : am.getRunningAppProcesses()) {
                if (processInfo.importance == 100) {
                    boolean isInBackground2 = isInBackground;
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground2 = false;
                        }
                    }
                    isInBackground = isInBackground2;
                }
            }
            return isInBackground;
        } else if (((RunningTaskInfo) am.getRunningTasks(1).get(0)).topActivity.getPackageName().equals(context.getPackageName())) {
            return false;
        } else {
            return true;
        }
    }

    public static HashMap<String, Object> getModeltoMap(Object object) {
        Gson gson = new Gson();
        return (HashMap) gson.fromJson(gson.toJson(object), new HashMap().getClass());
    }

    public static boolean isValidEmailAddress(String emailAddress) {
        return Pattern.compile("[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})", Pattern.CASE_INSENSITIVE).matcher(emailAddress).matches();
    }

}
