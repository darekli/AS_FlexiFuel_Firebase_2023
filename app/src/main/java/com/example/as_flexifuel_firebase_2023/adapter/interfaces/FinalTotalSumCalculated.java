package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface FinalTotalSumCalculated {
    /**
     * Called when the final total sum is calculated successfully.
     *
     * @param finalTotalSum The final total sum as a formatted string.
     */
    void onFinalTotalSumCalculated(String finalTotalSum);

    /**
     * Called when an error occurs during the calculation of the final total sum.
     *
     * @param errorMessage The error message describing the cause of the error.
     */
    void onError(String errorMessage);
}