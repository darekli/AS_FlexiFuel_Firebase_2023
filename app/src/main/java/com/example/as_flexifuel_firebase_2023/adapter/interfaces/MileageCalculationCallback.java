package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface MileageCalculationCallback {
    void onMileageCalculationComplete(double sum, double difference, double result);
    void onMileageCalculationError(String errorMessage);
}
