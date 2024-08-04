package com.example.nguyenxuantung_ph19782.model;

public class Users {
    private String userId;
    private String username;
    private String role;
    private String email;
    private String createdAt;
    private String updatedAt;
    private String profileUrl;

    public Users() {
    }

    public Users(String userId, String username, String role, String email, String createdAt, String updatedAt, String profileUrl) {
        this.userId = userId;
        this.username = username;
        this.role = role;
        this.email = email;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.profileUrl = profileUrl;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }
}
