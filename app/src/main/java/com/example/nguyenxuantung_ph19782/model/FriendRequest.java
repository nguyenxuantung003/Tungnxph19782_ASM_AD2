package com.example.nguyenxuantung_ph19782.model;

public class FriendRequest {
    private String requestId;
    private String from;
    private String to;
    private String status;
    private String userName; // Thêm trường này để lưu tên người dùng

    public FriendRequest() {
        // Required empty constructor for Firebase
    }

    public FriendRequest(String requestId, String from, String to, String status) {
        this.requestId = requestId;
        this.from = from;
        this.to = to;
        this.status = status;
    }

    // Getter and Setter methods
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}