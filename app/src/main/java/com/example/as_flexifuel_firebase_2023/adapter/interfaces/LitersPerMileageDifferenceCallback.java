package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface LitersPerMileageDifferenceCallback {
    void onResultFetched(double litersPerMileageDifference);
    void onError(String errorMessage);
}
