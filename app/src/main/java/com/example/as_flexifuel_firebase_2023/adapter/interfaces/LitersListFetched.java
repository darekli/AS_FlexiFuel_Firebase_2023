package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface LitersListFetched {
    void onLitersListFetched(List<Double> litersList);

    void onError(String errorMessage);

    void onLastIdFetched(int finalResult);

}

