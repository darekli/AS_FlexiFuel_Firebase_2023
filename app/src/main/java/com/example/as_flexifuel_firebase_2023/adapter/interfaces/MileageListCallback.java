package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface MileageListCallback {
    void onMileageListFetched(List<Integer> ids);
    void onError(String errorMessage);
}
