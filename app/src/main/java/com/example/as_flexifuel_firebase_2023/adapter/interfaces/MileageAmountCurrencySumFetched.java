package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface MileageAmountCurrencySumFetched {
    void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum);
    void onError(String errorMessage);
}

