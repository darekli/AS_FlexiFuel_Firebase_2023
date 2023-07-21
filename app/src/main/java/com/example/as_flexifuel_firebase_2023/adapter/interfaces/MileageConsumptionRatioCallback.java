package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface MileageConsumptionRatioCallback {
    void onMileageConsumptionRatioCalculated(double ratio);

    void onError(String errorMessage);
}

