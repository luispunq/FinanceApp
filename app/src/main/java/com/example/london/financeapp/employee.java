package com.example.london.financeapp;

public class employee {
    private String name;
    private String ID;
    private String phone;
    private String position;
    private String status;
    private String startdate;
    private String salary;
    private String image;
    private String sex;
    private String fieldid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public employee() {

    }

    public employee(String name, String ID, String phone, String position, String status, String startdate, String salary, String image, String sex, String fieldid) {
        this.name = name;
        this.ID = ID;
        this.phone = phone;
        this.position = position;
        this.status = status;
        this.startdate = startdate;
        this.salary = salary;
        this.image = image;
        this.sex = sex;
        this.fieldid = fieldid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getFieldid() {
        return fieldid;
    }

    public void setFieldid(String fieldid) {
        this.fieldid = fieldid;
    }
}
