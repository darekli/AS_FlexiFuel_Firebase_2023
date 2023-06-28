package com.example.as_flexifuel_firebase_2023;

import androidx.annotation.NonNull;

import java.util.Objects;

public class Refueling {
    private String id;
    private String vehicle;
    private String date;
    private FuelType fuelType;
    private FuelFP fuelFP;
    private String liters;
    private String amount;

    private Currency currency;

    public Refueling() {
        // Default constructor required for Firebase
    }

    public Refueling(String id, String vehicle, String date, FuelType fuelType, FuelFP fuelFP, String liters, String amount, Currency currency) {
        this.id = id;
        this.vehicle = vehicle;
        this.date = date;
        this.fuelType = fuelType;
        this.fuelFP = fuelFP;
        this.liters = liters;
        this.amount = amount;
        this.currency = currency;
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

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }
}

