package com.example.nguyenxuantung_ph19782.model;

public class Buocchan {
    private String userId;
    private String activityId;
    private long date;
    private int stepsCount;
    private int goal;
    private boolean goalAchieved;
    private long createdAt;
    private long updatedAt;

    public Buocchan() {
    }

    public Buocchan(String userId, String activityId, long date, int stepsCount, int goal, boolean goalAchieved, long createdAt, long updatedAt) {
        this.userId = userId;
        this.activityId = activityId;
        this.date = date;
        this.stepsCount = stepsCount;
        this.goal = goal;
        this.goalAchieved = goalAchieved;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getStepsCount() {
        return stepsCount;
    }

    public void setStepsCount(int stepsCount) {
        this.stepsCount = stepsCount;
    }

    public int getGoal() {
        return goal;
    }

    public void setGoal(int goal) {
        this.goal = goal;
    }

    public boolean isGoalAchieved() {
        return goalAchieved;
    }

    public void setGoalAchieved(boolean goalAchieved) {
        this.goalAchieved = goalAchieved;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

}
