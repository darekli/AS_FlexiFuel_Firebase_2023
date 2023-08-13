package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface DaysDifferenceCallback {
    void onDaysDifferenceComputed(long daysDifference);
    void onError(String errorMessage);
}
