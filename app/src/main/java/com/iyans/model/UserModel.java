package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable{
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
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("login_type")
    @Expose
    private String loginType;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
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

//    public UserModel(String firstName, String lastName, String dob, String gender, String mobileNo, String email, String userName, String image, String socialId, String loginType, String currentLatitude, String currentLongitude, String description, String userId) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.dob = dob;
//        this.gender = gender;
//        this.mobileNo = mobileNo;
//        this.email = email;
//        this.userName = userName;
//        this.image = image;
//        this.socialId = socialId;
//        this.loginType = loginType;
//        this.currentLatitude = currentLatitude;
//        this.currentLongitude = currentLongitude;
//        this.description = description;
//        this.userId = userId;
//    }

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

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
