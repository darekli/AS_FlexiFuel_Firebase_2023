package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface CommonMileagesFetched {
    void onCommonMileagesFetched(List<Integer> commonMileages);
    void onError(String errorMessage);
}


