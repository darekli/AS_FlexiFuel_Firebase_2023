package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface MileageListFetched {
    void onMileageListFetched(List<Integer> mileageList);
    void onError(String errorMessage);
    void onLastIdFetched(int finalResult);
}
