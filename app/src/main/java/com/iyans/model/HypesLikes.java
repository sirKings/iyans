package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HypesLikes {
    @SerializedName("created_at")
    @Expose
    private Object createdAt;
    @SerializedName("feed_id")
    @Expose
    private String feedId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("like_id")
    @Expose
    private String likeId;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_image")
    @Expose
    private String userImage;

    public String getLikeId() {
        return this.likeId;
    }

    public void setLikeId(String likeId) {
        this.likeId = likeId;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeedId() {
        return this.feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
    }

    public Object getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Object createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserImage() {
        return this.userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
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
}
