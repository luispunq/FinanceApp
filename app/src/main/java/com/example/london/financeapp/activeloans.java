package com.example.london.financeapp;

public class activeloans {
    private String balance;
    private String memberid;
    private String officerid;
    private String amount;
    private String installment;
    private long datedisbursed;
    private long lastinstalmentdate;
    private String installmenttype;
    private String nextinstalmentdate;
    private String arreas;
    private long  timestamp;
    private String amountcash;
    private String interest;
    private String officername;
    private String location;
    private String contact;
    private String membername;
    private long checkdate;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getOfficerid() {
        return officerid;
    }

    public void setOfficerid(String officerid) {
        this.officerid = officerid;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public long getDatedisbursed() {
        return datedisbursed;
    }

    public void setDatedisbursed(long datedisbursed) {
        this.datedisbursed = datedisbursed;
    }

    public long getLastinstalmentdate() {
        return lastinstalmentdate;
    }

    public void setLastinstalmentdate(long lastinstalmentdate) {
        this.lastinstalmentdate = lastinstalmentdate;
    }

    public String getInstallmenttype() {
        return installmenttype;
    }

    public void setInstallmenttype(String installmenttype) {
        this.installmenttype = installmenttype;
    }

    public String getNextinstalmentdate() {
        return nextinstalmentdate;
    }

    public void setNextinstalmentdate(String nextinstalmentdate) {
        this.nextinstalmentdate = nextinstalmentdate;
    }

    public String getArreas() {
        return arreas;
    }

    public void setArreas(String arreas) {
        this.arreas = arreas;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAmountcash() {
        return amountcash;
    }

    public void setAmountcash(String amountcash) {
        this.amountcash = amountcash;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getOfficername() {
        return officername;
    }

    public void setOfficername(String officername) {
        this.officername = officername;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public activeloans(String balance, String memberid, String officerid, String amount, String installment, long datedisbursed, long lastinstalmentdate, String installmenttype, String nextinstalmentdate, String arreas, long timestamp, String amountcash, String interest, String officername, String location, String contact, String membername, long checkdate) {
        this.balance = balance;
        this.memberid = memberid;
        this.officerid = officerid;
        this.amount = amount;
        this.installment = installment;
        this.datedisbursed = datedisbursed;
        this.lastinstalmentdate = lastinstalmentdate;
        this.installmenttype = installmenttype;
        this.nextinstalmentdate = nextinstalmentdate;
        this.arreas = arreas;
        this.timestamp = timestamp;
        this.amountcash = amountcash;
        this.interest = interest;
        this.officername = officername;
        this.location = location;
        this.contact = contact;
        this.membername = membername;
        this.checkdate = checkdate;
    }

    public activeloans() {
    }

    public long getCheckdate() {
        return checkdate;
    }

    public void setCheckdate(long checkdate) {
        this.checkdate = checkdate;
    }
}
