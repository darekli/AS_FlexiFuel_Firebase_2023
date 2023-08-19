package com.example.as_flexifuel_firebase_2023.adapter.interfaces;

import java.util.ArrayList;

public interface VehiclesFetched {
    void onVehiclesFetched(ArrayList<String> vehicles);
    void onError(String errorMessage);
}
