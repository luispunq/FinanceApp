package com.example.london.financeapp;

public class member {
    private String name;
    private String uid;
    private String image;
    private String idnum;

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

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public member(String name, String uid, String image, String idnum) {
        this.name = name;
        this.uid = uid;
        this.image = image;
        this.idnum = idnum;
    }

    public member() {
    }
}
