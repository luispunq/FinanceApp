package com.example.london.financeapp;

public class transaction {
    private String name;
    private String type;
    private String amount;
    private String status;
    private String date;
    private String officer;
    private long timestamp;
    private String memberuid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(String memberuid) {
        this.memberuid = memberuid;
    }

    public transaction(String name, String type, String amount, String status, String date, String officer, long timestamp, String memberuid) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.status = status;
        this.date = date;
        this.officer = officer;
        this.timestamp = timestamp;
        this.memberuid = memberuid;
    }

    public transaction() {
    }
}
