package com.example.nguyenxuantung_ph19782.model;

import org.w3c.dom.Comment;

import java.util.HashMap;
import java.util.Map;

public class Post {
    private String postId;
    private String userId;
    private String username;
    private String content;
    private String imageUrl;
    private long createdAt;
    private Map<String, Boolean> likes = new HashMap<>();
    private Map<String, Commet> comments = new HashMap<>();

    public Post() {
    }

    public Post(String postId, String userId, String username, String content,String imageUrl, long createdAt) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }

    public Post(String postId, String userId, String username, String content, String imageUrl, long createdAt, Map<String, Boolean> likes, Map<String, Commet> comments) {
        this.postId = postId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.likes = likes;
        this.comments = comments;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public Map<String, Boolean> getLikes() {
        return likes;
    }

    public void setLikes(Map<String, Boolean> likes) {
        this.likes = likes;
    }

    public Map<String, Commet> getComments() {
        return comments;
    }

    public void setComments(Map<String, Commet> comments) {
        this.comments = comments;
    }
}
