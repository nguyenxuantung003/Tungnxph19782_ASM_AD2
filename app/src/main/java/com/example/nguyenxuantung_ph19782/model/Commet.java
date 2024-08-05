package com.example.nguyenxuantung_ph19782.model;

public class Commet {
    private String commentId;
    private String userId;
    private String username;
    private String content;
    private long createdAt;

    public Commet() {
    }

    public Commet(String commentId, String userId, String username, String content, long createdAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.createdAt = createdAt;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
