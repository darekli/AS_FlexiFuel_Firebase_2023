package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.Map;

public interface AmountCurrencyRateMapFetchedString {
    void onMileageAmountCurrencyMapFetched(Map<String, String> amountToCurrencyRateMap);
    void onError(String errorMessage);
}
