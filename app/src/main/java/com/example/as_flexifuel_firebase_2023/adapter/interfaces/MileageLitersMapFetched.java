package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.Map;

public interface MileageLitersMapFetched {
    void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap);

    void onError(String errorMessage);
}

