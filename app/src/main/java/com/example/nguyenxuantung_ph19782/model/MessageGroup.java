package com.example.nguyenxuantung_ph19782.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageGroup {
    private String senderId;
    private String messageText;
    private String timestamp; // Có thể thêm timestamp nếu cần

    public MessageGroup() {
        // Constructor không tham số là bắt buộc
    }

    public MessageGroup(String senderId, String messageText) {
        this.senderId = senderId;
        this.messageText = messageText;
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
    }

    // Getter và Setter
    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
