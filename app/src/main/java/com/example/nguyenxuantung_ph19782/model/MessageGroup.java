package com.example.nguyenxuantung_ph19782.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MessageGroup {
    private String senderId;
    private String messageText;
    private String timestamp;
    private String senderName;
    // Có thể thêm timestamp nếu cần


    public MessageGroup() {
    }

    public MessageGroup(String messageText, String senderId, String senderName, String timestamp) {
        this.messageText = messageText;
        this.senderId = senderId;
        this.senderName = senderName;
        this.timestamp = timestamp;
    }

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

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
