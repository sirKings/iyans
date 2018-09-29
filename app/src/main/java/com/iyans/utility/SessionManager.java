package com.iyans.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.iyans.model.UserModel;

import java.util.HashMap;

public class SessionManager {
    public static final String CURRENT_CHAT_ID = "current_id";
    public static final String CURRENT_CHAT_TYPE = "current_type";
    public static final String FCM_REGISTRATION_ID = "fcm_reg_id";
    private static final String IS_LOGIN = "IsLoggedIn";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_DOB = "dob";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_IMGPREV = "key_img_prev";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_LATTITUDE = "current_latitude";
    public static final String KEY_LOGIN_TYPE = "login_type";
    public static final String KEY_LONGITUDE = "current_longitude";
    public static final String KEY_MOBILE = "mobile_no";
    public static final String KEY_NOTIFICATION = "key_notification";
    public static final String KEY_SOCIAL_ID = "social_id";
    public static final String KEY_SOUNDEFFECT = "key_sound_effect";
    public static final String KEY_USERNAME = "user_name";
    public static final String KEY_USER_ID = "user_id";
    private static final String PREF_NAME = "IyANSPref";
    int PRIVATE_MODE = 0;
    Context _context;
    Editor editor;
    SharedPreferences pref;

    public SessionManager(Context context) {
        this._context = context;
        this.pref = this._context.getSharedPreferences(PREF_NAME, this.PRIVATE_MODE);
        this.editor = this.pref.edit();
    }

    public void setCurrentChat(String sender_id, String type) {
        this.editor.putString(CURRENT_CHAT_ID, sender_id);
        this.editor.putString(CURRENT_CHAT_TYPE, type);
        this.editor.commit();
    }

    public HashMap<String, String> getCurrentChat() {
        HashMap<String, String> user = new HashMap<>();
        user.put(CURRENT_CHAT_ID, this.pref.getString(CURRENT_CHAT_ID, null));
        user.put(CURRENT_CHAT_TYPE, this.pref.getString(CURRENT_CHAT_TYPE, null));
        return user;
    }

    public void setUser(UserModel mUser) {
        this.editor.putString("user_id", mUser.getUserId());
        this.editor.putString(KEY_FIRST_NAME, mUser.getFirstName());
        this.editor.putString(KEY_LAST_NAME, mUser.getLastName());
        this.editor.putString(KEY_DOB, mUser.getDob());
        this.editor.putString(KEY_GENDER, mUser.getGender());
        this.editor.putString(KEY_MOBILE, mUser.getMobileNo());
        this.editor.putString("email", mUser.getEmail());
        this.editor.putString(KEY_USERNAME, mUser.getUserName());
        this.editor.putString("image", mUser.getImage());
        this.editor.putString(KEY_SOCIAL_ID, mUser.getSocialId());
        this.editor.putString(KEY_LOGIN_TYPE, mUser.getLoginType());
        this.editor.putString(KEY_LATTITUDE, mUser.getCurrentLatitude());
        this.editor.putString(KEY_LONGITUDE, mUser.getCurrentLongitude());
        this.editor.putString("description", mUser.getDescription());
        this.editor.commit();
    }

    public UserModel getUser() {
        if (this.pref.getString("user_id", null) == null) {
            return null;
        }
        String user_id = this.pref.getString("user_id", null);

        UserModel user = new UserModel();
        //user.setCreatedAt(this.pref.getString());
        user.setCurrentLatitude(this.pref.getString(KEY_LATTITUDE, null));
        user.setCurrentLongitude(this.pref.getString(KEY_LONGITUDE, null));
        user.setDescription(this.pref.getString("description", null));
        user.setDob(this.pref.getString(KEY_DOB, null));
        user.setEmail(this.pref.getString("email", null));
        user.setFirstName(this.pref.getString(KEY_FIRST_NAME, null));
        user.setGender(this.pref.getString(KEY_GENDER, null));
        user.setImage(this.pref.getString(KEY_IMAGE, null));
        user.setLastName(this.pref.getString(KEY_LAST_NAME, null));
        user.setLoginType(this.pref.getString(KEY_LOGIN_TYPE, null));
        user.setMobileNo(this.pref.getString(KEY_MOBILE, null));
        user.setSocialId(this.pref.getString(KEY_SOCIAL_ID, null));
        user.setUserName(this.pref.getString(KEY_USERNAME, null));
        user.setUserId(this.pref.getString("user_id", null));

        //this.pref.getString(KEY_FIRST_NAME, null), this.pref.getString(KEY_LAST_NAME, null), this.pref.getString(KEY_DOB, null), this.pref.getString(KEY_GENDER, null), this.pref.getString(KEY_MOBILE, null), this.pref.getString("email", null), this.pref.getString(KEY_USERNAME, null), this.pref.getString("image", null), this.pref.getString(KEY_SOCIAL_ID, null), this.pref.getString(KEY_LOGIN_TYPE, null), this.pref.getString(KEY_LATTITUDE, null), this.pref.getString(KEY_LONGITUDE, null), this.pref.getString("description", null), user_id
        return user;
    }

    public void setSoundEffectState(Boolean b) {
        this.editor.putBoolean(KEY_SOUNDEFFECT, b);
        this.editor.commit();
    }

    public boolean getSoundEffectState() {
        return this.pref.getBoolean(KEY_SOUNDEFFECT, false);
    }

    public void setNotificationState(Boolean b) {
        this.editor.putBoolean(KEY_NOTIFICATION, b);
        this.editor.commit();
    }

    public boolean getNotificationState() {
        return this.pref.getBoolean(KEY_NOTIFICATION, false);
    }

    public void setImgPrevState(Boolean b) {
        this.editor.putBoolean(KEY_IMGPREV, b);
        this.editor.commit();
    }

    public boolean getImgPrevState() {
        return this.pref.getBoolean(KEY_IMGPREV, false);
    }

    public void logoutUser() {
        this.editor.clear();
        this.editor.commit();
    }

    public void setFcmRegistrationId(String id) {
        this.editor.putString(FCM_REGISTRATION_ID, id);
        this.editor.commit();
    }

    public HashMap<String, String> getFcmRegistrationId() {
        HashMap<String, String> user = new HashMap<>();
        user.put(FCM_REGISTRATION_ID, this.pref.getString(FCM_REGISTRATION_ID, null));
        return user;
    }

    public void setLoginSession(boolean bool) {
        this.editor.putBoolean(IS_LOGIN, bool);
        this.editor.commit();
    }

    public boolean isLoggedIn() {
        return this.pref.getBoolean(IS_LOGIN, false);
    }
}
