package com.iyans.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FeedsModel implements Serializable {
    @SerializedName("comment_count")
    @Expose
    private int commentCount;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("feed_id")
    @Expose
    private String feedId;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("image_preview_status")
    @Expose
    private String imagePreviewStatus;
    @SerializedName("image_type")
    @Expose
    private String imageType;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("like_count")
    @Expose
    private int likeCount;
    @SerializedName("like_status")
    @Expose
    private String likeStatus;
    @SerializedName("post_feed_date")
    @Expose
    private Object postFeedDate;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("view_count")
    @Expose
    private int viewCount;

    public int getLikeCount() {
        return this.likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public int getViewCount() {
        return this.viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getFeedId() {
        return this.feedId;
    }

    public void setFeedId(String feedId) {
        this.feedId = feedId;
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

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String imageType) {
        this.imageType = imageType;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagePreviewStatus() {
        return this.imagePreviewStatus;
    }

    public void setImagePreviewStatus(String imagePreviewStatus) {
        this.imagePreviewStatus = imagePreviewStatus;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getPostFeedDate() {
        return this.postFeedDate;
    }

    public void setPostFeedDate(Object postFeedDate) {
        this.postFeedDate = postFeedDate;
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

    public String getLikeStatus() {
        return this.likeStatus;
    }

    public void setLikeStatus(String likeStatus) {
        this.likeStatus = likeStatus;
    }
}
