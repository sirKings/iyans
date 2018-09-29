package com.iyans.utility;

import com.iyans.model.UserModel;

public class Constant {
    public static final String ANDROID = "android";
    public static final String BASE_URL = "http://www.mmdnow.com/sweettooth/";
    public static int CALL_IN = 1;
    public static int CALL_OUT = 2;
    public static final String CHAT_OBJECT = "com.carimov.app.utility.chatobj";
    public static final String FEEDS_OBJECT = "com.carimov.app.utility.feedsmodel";
    public static final String FOLLOW = "follow";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String NOTIFICATION_TYPE_CHAT = "chat";
    public static final String NOTIFICATION_TYPE_READ = "read_status";
    public static final String NOTIFICATION_TYPE_REQUEST = "request";
    public static final String NOTIFICATION_TYPE_TYPING = "type";
    public static final String NOT_TYPING = "0";
    public static final int PERMISSION_REQUEST = 100;
    public static final String PUSH_MESSAGE = "com.carimov.app.utility.push_msg";
    public static final String PUSH_NOTIFICATION = "com.carimov.app.utility.pushNotification";
    public static final String READ = "1";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String REQUEST_STATUS_ACCEPTED = "Accepted";
    public static final String REQUEST_STATUS_PENDING = "Pending";
    public static final String REQUEST_STATUS_SEND = "Send";
    public static final String TOPIC_GLOBAL = "global";
    public static final String TYPE_GROUP = "group";
    public static final String TYPE_IMAGE = "image";
    public static final String TYPE_SINGLE = "single";
    public static final String TYPE_TEXT = "text";
    public static final String TYPE_VIDEO = "video";
    public static final String TYPING = "1";
    public static final String UNFOLLOW = "unfollow";
    public static final String UNREAD = "0";
    public static final String USERLIST = "com.carimov.app.utility.userList";
    private static long timeLast;

    public static boolean isFastlyClick() {
        if (System.currentTimeMillis() - timeLast < 1500) {
            timeLast = System.currentTimeMillis();
            return true;
        }
        timeLast = System.currentTimeMillis();
        return false;
    }

    public static UserModel currentUser;
}
