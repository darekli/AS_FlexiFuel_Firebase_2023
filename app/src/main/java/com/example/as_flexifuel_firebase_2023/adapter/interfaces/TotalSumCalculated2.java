package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

public interface TotalSumCalculated2 {
    /**
     * Called when the final total sum is calculated successfully.
     *
     * @param totalSum The final total sum as a formatted string.
     */
    void onTotalSumCalculated(String totalSum);

    /**
     * Called when an error occurs during the calculation of the final total sum.
     *
     * @param errorMessage The error message describing the cause of the error.
     */
    void onError(String errorMessage);
}
