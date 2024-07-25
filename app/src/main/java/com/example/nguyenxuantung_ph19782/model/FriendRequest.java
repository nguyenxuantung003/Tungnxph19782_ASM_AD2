package com.example.nguyenxuantung_ph19782.model;

public class FriendRequest {
    private String requestId;
    private String from;
    private String to;
    private String status;

    // Constructors, getters, and setters

    public FriendRequest() {
        // Default constructor
    }

    public FriendRequest(String requestId, String from, String to, String status) {
        this.requestId = requestId;
        this.from = from;
        this.to = to;
        this.status = status;
    }

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
}
