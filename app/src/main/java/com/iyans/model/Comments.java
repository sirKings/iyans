package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Comments implements Serializable{
    @SerializedName("comment")
    @Expose
    private String comment;
    @SerializedName("comment_id")
    @Expose
    private String commentId;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
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
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_image")
    @Expose
    private String userImage;

    //public Comments(String comment) {
      //  this.comment = comment;
    //}

    public String getCommentId() {
        return this.commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
