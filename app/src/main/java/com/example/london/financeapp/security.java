package com.example.london.financeapp;

public class security {
    private String itemname;
    private String marketvalue;
    private String compvalue;

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public String getMarketvalue() {
        return marketvalue;
    }

    public void setMarketvalue(String marketvalue) {
        this.marketvalue = marketvalue;
    }

    public String getCompvalue() {
        return compvalue;
    }

    public void setCompvalue(String compvalue) {
        this.compvalue = compvalue;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public security(String itemname, String marketvalue, String compvalue, String sno) {

        this.itemname = itemname;
        this.marketvalue = marketvalue;
        this.compvalue = compvalue;
        this.sno = sno;
    }

    public security() {

    }

    private String sno;
}
