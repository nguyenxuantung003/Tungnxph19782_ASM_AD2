package com.example.nguyenxuantung_ph19782.model;

public class MessageGroup {
    private String userId;
    private String text;

    public MessageGroup() {
    }

    public MessageGroup(String userId, String text) {
        this.userId = userId;
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

