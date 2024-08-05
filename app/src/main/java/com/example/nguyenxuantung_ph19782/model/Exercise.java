package com.example.nguyenxuantung_ph19782.model;

public class Exercise {
    private String id;
    private String name;
    private String details;
    private String benefits;
    private String instructions;

    // Constructor rỗng
    public Exercise() {}

    // Constructor với tham số
    public Exercise(String id, String name, String details, String benefits, String instructions) {
        this.id = id;
        this.name = name;
        this.details = details;
        this.benefits = benefits;
        this.instructions = instructions;
    }

    // Getter và Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getBenefits() {
        return benefits;
    }

    public void setBenefits(String benefits) {
        this.benefits = benefits;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }
}
