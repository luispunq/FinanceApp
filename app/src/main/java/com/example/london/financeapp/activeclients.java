package com.example.london.financeapp;

public class activeclients {
    private String name;
    private String uid;
    private String image;
    private String officer;
    private String date;
    private String paiddate;
    private String loanid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getOfficer() {
        return officer;
    }

    public void setOfficer(String officer) {
        this.officer = officer;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public activeclients(String name, String uid, String image, String officer, String date, String paiddate, String loanid) {
        this.name = name;
        this.uid = uid;
        this.image = image;
        this.officer = officer;
        this.date = date;
        this.paiddate = paiddate;
        this.loanid = loanid;
    }

    public activeclients() {
    }

    public String getPaiddate() {
        return paiddate;
    }

    public void setPaiddate(String paiddate) {
        this.paiddate = paiddate;
    }
}
