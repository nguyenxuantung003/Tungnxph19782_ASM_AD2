package com.example.nguyenxuantung_ph19782.model;

public class Loibieton {
    private String userId ;
    private String date;
    private String gratitudeNotes;
    private String createdAt;
    private String updatedAt;
    private  String activityId;
    public Loibieton() {
    }

    public Loibieton(String userId, String date, String gratitudeNotes, String createdAt, String updatedAt, String activityId) {
        this.userId = userId;
        this.date = date;
        this.gratitudeNotes = gratitudeNotes;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.activityId = activityId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGratitudeNotes() {
        return gratitudeNotes;
    }

    public void setGratitudeNotes(String gratitudeNotes) {
        this.gratitudeNotes = gratitudeNotes;
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }
}
