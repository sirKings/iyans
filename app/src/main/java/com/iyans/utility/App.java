package com.iyans.utility;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.danikula.videocache.HttpProxyCacheServer;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.iyans.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class App extends MultiDexApplication {
    public static final String TAG = App.class.getSimpleName();
    private static App mInstance;
    private static String today;
    private SessionManager pref;
    private HttpProxyCacheServer proxy;
    private FirebaseDatabase database;
    private FirebaseStorage storage;

    public void setDatabase(){
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public void setStorage(){
        storage = FirebaseStorage.getInstance();
    }

    public static HttpProxyCacheServer getProxy(Context context) {
        App app = (App) context.getApplicationContext();
        if (app.proxy != null) {
            return app.proxy;
        }
        HttpProxyCacheServer newProxy = app.newProxy();
        app.proxy = newProxy;
        return newProxy;
    }

    private HttpProxyCacheServer newProxy() {
        return new HttpProxyCacheServer((Context) this);
    }

    public static synchronized App getInstance() {
        App app;
        synchronized (App.class) {
            app = mInstance;
        }
        return app;
    }

    public void onCreate() {
        super.onCreate();
        setDatabase();
        setStorage();
        today = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        mInstance = this;
    }

    public SessionManager getPrefManager() {
        if (this.pref == null) {
            this.pref = new SessionManager(this);
        }
        return this.pref;
    }

    public static String getTimeStamp(String dateStr) {
        String timestamp;
        if (!Validation.isValidString(dateStr)) {
            return null;
        }
        String stringBuilder;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp3 = "";
        Calendar cal2 = Calendar.getInstance();
        today = String.valueOf(cal2.get(Calendar.DAY_OF_MONTH));
        if (today.length() < 2) {
            StringBuilder stringBuilder2 = new StringBuilder();
            stringBuilder2.append("0");
            stringBuilder2.append(today);
            stringBuilder = stringBuilder2.toString();
        } else {
            stringBuilder = today;
        }
        today = stringBuilder;
        try {
            Date date = format.parse(dateStr);
            Date now = new Date();
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - date.getTime());
            timestamp = timestamp3;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - date.getTime());
            long calendar2 = TimeUnit.MILLISECONDS.toHours(now.getTime() - date.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - date.getTime());
            SimpleDateFormat format2;
            if (seconds < 60) {
                format2 = new SimpleDateFormat("HH:mm");
                return "Just now ";
            } else if (minutes < 60) {
                format2 = new SimpleDateFormat("HH:mm");
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append(minutes);
                stringBuilder3.append(getInstance().getResources().getQuantityString(R.plurals.minutes, (int) minutes));
                return stringBuilder3.toString();
            } else {
                long j = seconds;
                SimpleDateFormat format3;
                String timestamp4;
                StringBuilder stringBuilder4;
                if (calendar2 < 24) {
                    format3 = new SimpleDateFormat("HH:mm");
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(calendar2);
                    stringBuilder4.append(getInstance().getResources().getQuantityString(R.plurals.hours, (int) calendar2));
                    return stringBuilder4.toString();
                } else if (calendar2 < 24) {
                    format3 = new SimpleDateFormat("HH:mm");
                    timestamp4 = format3.format(date).toString();
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append("Today  ");
                    stringBuilder4.append(timestamp4);
                    return stringBuilder4.toString();
                } else {
                    format3 = new SimpleDateFormat("dd LLL, HH:mm");
                    stringBuilder4 = new StringBuilder();
                    stringBuilder4.append(days);
                    stringBuilder4.append(getInstance().getResources().getQuantityString(R.plurals.days, (int) days));
                    return stringBuilder4.toString();
                }
            }
        } catch (ParseException e2222222) {
            timestamp = timestamp3;
            return timestamp;
        }
    }
}
