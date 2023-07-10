package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface MileageAmountCurrencyListFetched {
    void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList);

    void onError(String errorMessage);
}
