package com.example.as_flexifuel_firebase_2023.adapter;

import androidx.annotation.NonNull;

import com.example.as_flexifuel_firebase_2023.adapter.interfaces.VehiclesFetched;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Search {

    public void findAllAvailableVehicles(VehiclesFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> vehiclesSet = new HashSet<>(); // Using a Set to automatically handle uniqueness

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String vehicle = snapshot.child("vehicle").getValue(String.class);
                    if (vehicle != null && !vehicle.trim().isEmpty()) {
                        vehiclesSet.add(vehicle);
                    }
                }

                ArrayList<String> vehiclesList = new ArrayList<>(vehiclesSet);
                callback.onVehiclesFetched(vehiclesList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }


}
