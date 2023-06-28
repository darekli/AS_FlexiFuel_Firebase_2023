package com.example.as_flexifuel_firebase_2023;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RefuelingDao {
    private DatabaseReference refuelingsRef;
    private SimpleDateFormat dateFormat;

    public RefuelingDao() {
        // Initialize Firebase database reference
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        refuelingsRef = database.getReference("refuelings");

        // Initialize date format
        dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    }

    public void addRefueling(String vehicle, Calendar date, FuelType fuelType, FuelFP fuelFP,String liters) {
        String id = refuelingsRef.push().getKey();
        String dateString = dateFormat.format(date.getTime());

        Refueling refueling = new Refueling(id, vehicle, dateString, fuelType, fuelFP,liters);
        refuelingsRef.child(id).setValue(refueling);
    }
}

