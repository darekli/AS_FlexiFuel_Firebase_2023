package com.example.as_flexifuel_firebase_2023;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Refueling {
    private String id;
    private String vehicle;
    private String date;
    private String mileage;
    private FuelType fuelType;
    private FuelFP fuelFP;
    private String liters;
    private String amount;
    private Country country;
    private Currency currency;
    private String currencyRate;
    private String timeworn;
    private String notes;
    private String poi;
    private String lat;
    private String lng;

    public Refueling() {

    }

    public Refueling(String id, String vehicle, String date, String mileage, FuelType fuelType, FuelFP fuelFP, String liters, String amount, Country country, Currency currency, String currencyRate, String timeworn, String notes, String poi, String lat, String lng) {
        this.id = id;
        this.vehicle = vehicle;
        this.date = date;
        this.mileage = mileage;
        this.fuelType = fuelType;
        this.fuelFP = fuelFP;
        this.liters = liters;
        this.amount = amount;
        this.country = country;
        this.currency = currency;
        this.currencyRate = currencyRate;
        this.timeworn = timeworn;
        this.notes = notes;
        this.poi = poi;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public FuelType getFuelType() {
        return fuelType;
    }

    public void setFuelType(FuelType fuelType) {
        this.fuelType = fuelType;
    }

    public FuelFP getFuelFP() {
        return fuelFP;
    }

    public void setFuelFP(FuelFP fuelFP) {
        this.fuelFP = fuelFP;
    }

    public String getLiters() {
        return liters;
    }

    public void setLiters(String liters) {
        this.liters = liters;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public String getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(String currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getTimeworn() {
        return timeworn;
    }

    public void setTimeworn(String timeworn) {
        this.timeworn = timeworn;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPoi() {
        return poi;
    }

    public void setPoi(String poi) {
        this.poi = poi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }
}


