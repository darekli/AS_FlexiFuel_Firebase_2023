package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface AdjustedAmountPerLiterCallback {
    void onResultFetched(double adjustedAmountPerLiter);
    void onError(String errorMessage);
}
