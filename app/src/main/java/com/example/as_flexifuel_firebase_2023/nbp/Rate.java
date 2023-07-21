package com.example.as_flexifuel_firebase_2023.nbp;

import java.util.List;

public class Rate {

    private String id;
    private String no;
    private String effectiveDate;
    private double mid;
    private List<Rate> rates;

    public Rate() {
    }

    public Rate(String id, String no, String effectiveDate, double mid, List<Rate> rates) {
        this.id = id;
        this.no = no;
        this.effectiveDate = effectiveDate;
        this.mid = mid;
        this.rates = rates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(String effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

    public double getMid() {
        return mid;
    }

    public void setMid(double mid) {
        this.mid = mid;
    }

    public List<Rate> getRates() {
        return rates;
    }

    public void setRates(List<Rate> rates) {
        this.rates = rates;
    }
}
