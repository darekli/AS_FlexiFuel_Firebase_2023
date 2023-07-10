package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface AverageFuelConsumptionCallback {
    void onAverageFuelConsumptionCalculated(double averageFuelConsumption);

    void onError(String errorMessage);
}
