package com.example.london.financeapp;

public class loanapps {
    private String requestdate;
    private String membername;
    private String amountrequested;
    private String memberuid;
    private String period;
    private String monthlypay;
    private String status;
    private String image;
    private String installmenttype;
    private String state;
    private String officeruid;
    private String loanpurpose;
    private String comment;
    private String amountcash;
    private String interest;
    private String officername;

    public String getRequestdate() {
        return requestdate;
    }

    public void setRequestdate(String requestdate) {
        this.requestdate = requestdate;
    }

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getAmountrequested() {
        return amountrequested;
    }

    public void setAmountrequested(String amountrequested) {
        this.amountrequested = amountrequested;
    }

    public String getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(String memberuid) {
        this.memberuid = memberuid;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMonthlypay() {
        return monthlypay;
    }

    public void setMonthlypay(String monthlypay) {
        this.monthlypay = monthlypay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getInstallmenttype() {
        return installmenttype;
    }

    public void setInstallmenttype(String installmenttype) {
        this.installmenttype = installmenttype;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getOfficeruid() {
        return officeruid;
    }

    public void setOfficeruid(String officeruid) {
        this.officeruid = officeruid;
    }

    public String getLoanpurpose() {
        return loanpurpose;
    }

    public void setLoanpurpose(String loanpurpose) {
        this.loanpurpose = loanpurpose;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public loanapps(String requestdate, String membername, String amountrequested, String memberuid, String period, String monthlypay, String status, String image, String installmenttype, String state, String officeruid, String loanpurpose, String comment, String amountcash, String interest, String officername) {
        this.requestdate = requestdate;
        this.membername = membername;
        this.amountrequested = amountrequested;
        this.memberuid = memberuid;
        this.period = period;
        this.monthlypay = monthlypay;
        this.status = status;
        this.image = image;
        this.installmenttype = installmenttype;
        this.state = state;
        this.officeruid = officeruid;
        this.loanpurpose = loanpurpose;
        this.comment = comment;
        this.amountcash = amountcash;
        this.interest = interest;
        this.officername = officername;
    }

    public loanapps() {
    }
}
