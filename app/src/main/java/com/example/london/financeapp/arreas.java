package com.example.london.financeapp;

public class arreas {
    private String membername;
    private String totalloan;
    private String installment;
    private String loanduration;
    private String loanbalance;
    private String dategiven;
    private String arreas;
    private String nextpayment;
    private String officerid;
    private String memberuid;
    private String loanid;

    public String getMembername() {
        return membername;
    }

    public void setMembername(String membername) {
        this.membername = membername;
    }

    public String getTotalloan() {
        return totalloan;
    }

    public void setTotalloan(String totalloan) {
        this.totalloan = totalloan;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public String getLoanduration() {
        return loanduration;
    }

    public void setLoanduration(String loanduration) {
        this.loanduration = loanduration;
    }

    public String getLoanbalance() {
        return loanbalance;
    }

    public void setLoanbalance(String loanbalance) {
        this.loanbalance = loanbalance;
    }

    public String getDategiven() {
        return dategiven;
    }

    public void setDategiven(String dategiven) {
        this.dategiven = dategiven;
    }

    public String getArreas() {
        return arreas;
    }

    public void setArreas(String arreas) {
        this.arreas = arreas;
    }

    public String getNextpayment() {
        return nextpayment;
    }

    public void setNextpayment(String nextpayment) {
        this.nextpayment = nextpayment;
    }


    public String getOfficerid() {
        return officerid;
    }

    public void setOfficerid(String officerid) {
        this.officerid = officerid;
    }

    public String getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(String memberuid) {
        this.memberuid = memberuid;
    }

    public String getLoanid() {
        return loanid;
    }

    public void setLoanid(String loanid) {
        this.loanid = loanid;
    }

    public arreas(String membername, String totalloan, String installment, String loanduration, String loanbalance, String dategiven, String arreas, String nextpayment, String officerid, String memberuid, String loanid) {
        this.membername = membername;
        this.totalloan = totalloan;
        this.installment = installment;
        this.loanduration = loanduration;
        this.loanbalance = loanbalance;
        this.dategiven = dategiven;
        this.arreas = arreas;
        this.nextpayment = nextpayment;
        this.officerid = officerid;
        this.memberuid = memberuid;
        this.loanid = loanid;
    }

    public arreas() {
    }
}
