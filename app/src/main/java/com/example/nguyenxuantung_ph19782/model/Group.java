package com.example.nguyenxuantung_ph19782.model;

import java.util.HashMap;
import java.util.Map;

public class Group {
    private String groupId;
    private String groupName;
    private String description;
    private Map<String, Boolean> members;
    private String creatorId;

    public Group() {
    }

    public Group(String groupId, String groupName, String description, Map<String, Boolean> members, String creatorId) {
        this.groupId = groupId;
        this.groupName = groupName;
        this.description = description;
        this.members = members;
        this.creatorId = creatorId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, Boolean> getMembers() {
        return members;
    }

    public void setMembers(Map<String, Boolean> members) {
        this.members = members;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}