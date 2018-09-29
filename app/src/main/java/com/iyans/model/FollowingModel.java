package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FollowingModel {
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("follow_id")
    @Expose
    private String followId;
    @SerializedName("follow_status")
    @Expose
    private String followStatus;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("user_id")
    @Expose
    private String userId;

    public String getFollowId() {
        return this.followId;
    }

    public void setFollowId(String followId) {
        this.followId = followId;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFollowStatus() {
        return this.followStatus;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }
}
