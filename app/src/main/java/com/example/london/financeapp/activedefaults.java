package com.example.london.financeapp;

public class activedefaults {
    private String memberid;
    private String membername;
    private String days;
    private String fee;
    private String officerid;
    private String officername;
    private String loanid;
    private String loan;
    private String lastinst;
    private long timestamp;
    private String daterecorded;

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getOfficerid() {
        return officerid;
    }

    public void setOfficerid(String officerid) {
        this.officerid = officerid;
    }

    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getDaterecorded() {
        return daterecorded;
    }

    public void setDaterecorded(String daterecorded) {
        this.daterecorded = daterecorded;
    }

    public activedefaults(String memberid, String membername, String days, String fee, String officerid, String officername, String loanid, String loan, String lastinst, long timestamp, String daterecorded) {
        this.memberid = memberid;
        this.membername = membername;
        this.days = days;
        this.fee = fee;
        this.officerid = officerid;
        this.officername = officername;
        this.loanid = loanid;
        this.loan = loan;
        this.lastinst = lastinst;
        this.timestamp = timestamp;
        this.daterecorded = daterecorded;
    }

    public activedefaults() {
    }

    public String getLastinst() {
        return lastinst;
    }

    public void setLastinst(String lastinst) {
        this.lastinst = lastinst;
    }

    public String getLoan() {
        return loan;
    }

    public void setLoan(String loan) {
        this.loan = loan;
    }

    public String getOfficername() {
        return officername;
    }

    public void setOfficername(String officername) {
        this.officername = officername;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }
}
