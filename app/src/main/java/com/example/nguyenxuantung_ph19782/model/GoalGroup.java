package com.example.nguyenxuantung_ph19782.model;

public class GoalGroup {
    private String title;
    private String description;
    private String creatorId;

    public GoalGroup() {
    }

    public GoalGroup(String title, String description, String creatorId) {
        this.title = title;
        this.description = description;
        this.creatorId = creatorId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
