package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

interface ResultCallback {
    void onSuccess(double result);
    void onError(String errorMessage);
}