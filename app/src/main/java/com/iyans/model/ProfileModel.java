package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileModel {
    @SerializedName("count_archieves")
    @Expose
    private Integer countArchieves;
    @SerializedName("followers")
    @Expose
    private ArrayList<String> followers;

    @SerializedName("followings")
    @Expose
    private ArrayList<String> followings;
    @SerializedName("count_feeds")
    @Expose
    private Integer countFeeds;
    @SerializedName("count_followers")
    @Expose
    private Integer countFollowers;
    @SerializedName("count_followings")
    @Expose
    private Integer countFollowings;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("current_latitude")
    @Expose
    private String currentLatitude;
    @SerializedName("current_longitude")
    @Expose
    private String currentLongitude;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("device_type")
    @Expose
    private String deviceType;
    @SerializedName("dob")
    @Expose
    private String dob;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("login_type")
    @Expose
    private Object loginType;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("mobile_otp")
    @Expose
    private String mobileOtp;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("social_id")
    @Expose
    private Object socialId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private Object updatedAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("verification_status")
    @Expose
    private String verificationStatus;

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDob() {
        return this.dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return this.mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMobileOtp() {
        return this.mobileOtp;
    }

    public void setMobileOtp(String mobileOtp) {
        this.mobileOtp = mobileOtp;
    }

    public String getVerificationStatus() {
        return this.verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getDeviceToken() {
        return this.deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getDeviceType() {
        return this.deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public Object getSocialId() {
        return this.socialId;
    }

    public void setSocialId(Object socialId) {
        this.socialId = socialId;
    }

    public Object getLoginType() {
        return this.loginType;
    }

    public void setLoginType(Object loginType) {
        this.loginType = loginType;
    }

    public String getCurrentLatitude() {
        return this.currentLatitude;
    }

    public void setCurrentLatitude(String currentLatitude) {
        this.currentLatitude = currentLatitude;
    }

    public String getCurrentLongitude() {
        return this.currentLongitude;
    }

    public void setCurrentLongitude(String currentLongitude) {
        this.currentLongitude = currentLongitude;
    }


    public ArrayList<String> getFollowings() {
        return followings;
    }

    public void setFollowings(ArrayList<String> followings) {
        this.followings = followings;
    }


    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public Object getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Object updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Integer getCountArchieves() {
        return this.countArchieves;
    }

    public void setCountArchieves(Integer countArchieves) {
        this.countArchieves = countArchieves;
    }

    public Integer getCountFeeds() {
        return this.countFeeds;
    }

    public void setCountFeeds(Integer countFeeds) {
        this.countFeeds = countFeeds;
    }

    public Integer getCountFollowers() {
        return this.countFollowers;
    }

    public void setCountFollowers(Integer countFollowers) {
        this.countFollowers = countFollowers;
    }

    public Integer getCountFollowings() {
        return this.countFollowings;
    }

    public void setCountFollowings(Integer countFollowings) {
        this.countFollowings = countFollowings;
    }
}
