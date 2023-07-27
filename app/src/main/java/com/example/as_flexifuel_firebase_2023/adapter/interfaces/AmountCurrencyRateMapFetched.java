package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.Map;

public interface AmountCurrencyRateMapFetched {
    /**
     * Called when the amount-currency rate map is fetched successfully.
     *
     * @param amountCurrencyRateMap The map containing the amount as key and its corresponding currency rate as value.
     */
    void onAmountCurrencyRateMapFetched(Map<Double, Double> amountCurrencyRateMap);

    /**
     * Called when an error occurs while fetching the amount-currency rate map.
     *
     * @param errorMessage The error message describing the cause of the error.
     */
    void onError(String errorMessage);
}

