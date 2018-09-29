package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AllUsersModel {
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
    @SerializedName("follow_status")
    @Expose
    private String followStatus;
    @SerializedName("forgot_otp")
    @Expose
    private String forgotOtp;
    @SerializedName("gender")
    @Expose
    private String gender;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_preview_status")
    @Expose
    private String imagePreviewStatus;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("login_type")
    @Expose
    private String loginType;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("mobile_otp")
    @Expose
    private String mobileOtp;
    @SerializedName("notification_status")
    @Expose
    private String notificationStatus;
    @SerializedName("social_id")
    @Expose
    private String socialId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
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

    public String getForgotOtp() {
        return this.forgotOtp;
    }

    public void setForgotOtp(String forgotOtp) {
        this.forgotOtp = forgotOtp;
    }

    public String getVerificationStatus() {
        return this.verificationStatus;
    }

    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getNotificationStatus() {
        return this.notificationStatus;
    }

    public void setNotificationStatus(String notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public String getImagePreviewStatus() {
        return this.imagePreviewStatus;
    }

    public void setImagePreviewStatus(String imagePreviewStatus) {
        this.imagePreviewStatus = imagePreviewStatus;
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

    public String getSocialId() {
        return this.socialId;
    }

    public void setSocialId(String socialId) {
        this.socialId = socialId;
    }

    public String getLoginType() {
        return this.loginType;
    }

    public void setLoginType(String loginType) {
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

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getFollowStatus() {
        return this.followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }
}
