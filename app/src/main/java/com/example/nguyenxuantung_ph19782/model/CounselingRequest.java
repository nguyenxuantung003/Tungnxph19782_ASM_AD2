package com.example.nguyenxuantung_ph19782.model;

public class CounselingRequest {
    private String userId;
    private String username;
    private String email;
    private String field;
    private String content;
    private String createdAt;

    public CounselingRequest() {
    }

    public CounselingRequest(String userId, String username, String email, String field, String content, String createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.field = field;
        this.content = content;
        this.createdAt = createdAt;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
