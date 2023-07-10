package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.List;

public interface RefuelingListFetched {
    void onLitersListFetched(List<Double> refuelingList);

    void onError(String message);
}
