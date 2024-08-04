package com.example.nguyenxuantung_ph19782.model;

import java.io.Serializable;

public class CounselingRequest implements Serializable {
    private String requestId;
    private String content;
    private String createdAt;
    private String email;
    private String field;
    private String userId;
    private String username;
    private String response;
    private String status;
    private String expertId;

    public CounselingRequest() {
    }

    public CounselingRequest(String requestId, String content, String createdAt, String email, String field, String userId, String username, String response, String status, String expertId) {
        this.requestId = requestId;
        this.content = content;
        this.createdAt = createdAt;
        this.email = email;
        this.field = field;
        this.userId = userId;
        this.username = username;
        this.response = response;
        this.status = status;
        this.expertId = expertId;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExpertId() {
        return expertId;
    }

    public void setExpertId(String expertId) {
        this.expertId = expertId;
    }
}