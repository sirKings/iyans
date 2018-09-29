package com.iyans.service;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {
    @FormUrlEncoded
    @POST("feeds/add_comment")
    Call<ResponseBody> AddComment(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/edit_comment")
    Call<ResponseBody> EditComment(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/detail")
    Call<ResponseBody> FeedDetail(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/forgot_password")
    Call<ResponseBody> ForgotPassword(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/login")
    Call<ResponseBody> Login(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/notification_list")
    Call<ResponseBody> Notifications(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/registration")
    Call<ResponseBody> Registration(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/reset_password")
    Call<ResponseBody> ResetPassword(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/verify")
    Call<ResponseBody> VerifyEmail(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/add_archive")
    Call<ResponseBody> addArchive(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/all_users")
    Call<ResponseBody> allUsers(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/archives")
    Call<ResponseBody> archives(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/change_hide_activity")
    Call<ResponseBody> changeHideActivity(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/change_password")
    Call<ResponseBody> changePassword(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/delete")
    Call<ResponseBody> deleteFeed(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/edit_feed")
    Call<ResponseBody> editFeed(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/list")
    Call<ResponseBody> feedsList(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/follow")
    Call<ResponseBody> follow(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/followers")
    Call<ResponseBody> followers(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/followings")
    Call<ResponseBody> followings(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/show_profile")
    Call<ResponseBody> getUserProfile(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/like")
    Call<ResponseBody> like(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("feeds/add")
    Call<ResponseBody> postImage(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/image_preview_status")
    Call<ResponseBody> saveImgPrevState(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/change_notification_status")
    Call<ResponseBody> saveNotificationState(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST("users/edit_profile")
    Call<ResponseBody> updateProfile(@FieldMap HashMap<String, String> hashMap);
}
