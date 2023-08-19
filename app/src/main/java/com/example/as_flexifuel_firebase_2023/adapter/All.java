package com.example.as_flexifuel_firebase_2023.adapter;

import android.widget.EditText;

import androidx.annotation.NonNull;

import com.example.as_flexifuel_firebase_2023.enums.FuelFP;
import com.example.as_flexifuel_firebase_2023.enums.FuelType;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AdjustedAmountFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AdjustedAmountPerMileageDifferenceFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AmountCurrencyRateMapFetchedString;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.DaysDifferenceCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LitersPerMileageDifferenceCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageAmountCurrencyListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageDifferenceFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.TotalLitersFetched;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class All {


    public void calculateDifferenceBetweenLastAndSmallestMileage(EditText vehicleEditText, MileageDifferenceFetched callback) {
        findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int smallestMileage = Integer.parseInt(lastId);

                                // Calculate the difference between the last and smallest mileage
                                int difference = 0;
                                if (lastMileage > 0 && smallestMileage > 0) {
                                    difference = Math.abs(lastMileage - smallestMileage);
                                }

                                // Invoke the callback with the difference value
                                callback.onMileageDifferenceFetched(difference);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Handle the error
                            callback.onError(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });
    }


    public void findRecordsBetweenLowestAndHighestMileageForPB(EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {

        // First, get the lowest mileage.
        findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {

            @Override
            public void onLastIdFetched(String lowestMileagePB) {

                // Then, get the highest mileage.
                findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {

                    @Override
                    public void onLastIdFetched(String highestMileagePB) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");
                        int minMileage = Integer.parseInt(lowestMileagePB);
                        int maxMileage = Integer.parseInt(highestMileagePB);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> recordList = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals("PB")
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {

                                        int mileage = Integer.parseInt(mileageStr);

                                        if (mileage >= minMileage && mileage <= maxMileage) {
                                            List<Object> entry = Arrays.asList(mileageStr, litersStr, amountStr, currencyStr, currencyRateStr, dateStr);
                                            recordList.add(entry);
                                        }
                                    }
                                }

                                // Sort the recordList by mileage
                                Collections.sort(recordList, new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> o1, List<Object> o2) {
                                        return Integer.compare(Integer.parseInt((String) o1.get(0)), Integer.parseInt((String) o2.get(0)));
                                    }
                                });
                                //removed first record
                                if (!recordList.isEmpty()) {
                                    recordList.remove(0);
                                }

                                callback.onMileageAmountCurrencyListFetched(recordList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void findRecordsBetweenLowestAndHighestMileageForLPG(EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {

        // First, get the lowest mileage.
        findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {

            @Override
            public void onLastIdFetched(String lowestMileageLPG) {

                // Then, get the highest mileage.
                findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {

                    @Override
                    public void onLastIdFetched(String highestMileageLPG) {

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");
                        int minMileage = Integer.parseInt(lowestMileageLPG);
                        int maxMileage = Integer.parseInt(highestMileageLPG);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> recordList = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals("LPG")
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {

                                        int mileage = Integer.parseInt(mileageStr);

                                        if (mileage >= minMileage && mileage <= maxMileage) {
                                            List<Object> entry = Arrays.asList(mileageStr, litersStr, amountStr, currencyStr, currencyRateStr, dateStr);
                                            recordList.add(entry);
                                        }
                                    }
                                }

                                // Sort the recordList by mileage
                                Collections.sort(recordList, new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> o1, List<Object> o2) {
                                        return Integer.compare(Integer.parseInt((String) o1.get(0)), Integer.parseInt((String) o2.get(0)));
                                    }
                                });
                                //removed first record
                                if (!recordList.isEmpty()) {
                                    recordList.remove(0);
                                }
                                callback.onMileageAmountCurrencyListFetched(recordList);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyPB_RemoveLowestMileage(
            EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {

        findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText,
                new LastIdFetched() {
                    @Override
                    public void onLastIdFetched(String highestMileage) {

                        findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText,
                                new LastIdFetched() {
                                    @Override
                                    public void onLastIdFetched(String smallestMileage) {

                                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                                int lowestMileage = Integer.MAX_VALUE;
                                                List<Object> lowestMileageEntry = null;

                                                int highestMileageValue = Integer.parseInt(highestMileage);
                                                int secondHighestMileage = Integer.parseInt(smallestMileage);

                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                                    String dateStr = snapshot.child("date").getValue(String.class);

                                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                                        int mileage = Integer.parseInt(mileageStr);
                                                        double liters = Double.parseDouble(litersStr);
                                                        double amount = Double.parseDouble(amountStr);
                                                        String currency = currencyStr;
                                                        String currRate = currencyRateStr;
                                                        String date = dateStr;

                                                        if (mileage > secondHighestMileage && mileage <= highestMileageValue && currency != null) {
                                                            List<Object> entry = new ArrayList<>();
                                                            entry.add(mileage);
                                                            entry.add(liters);
                                                            entry.add(amount);
                                                            entry.add(currency);
                                                            entry.add(currRate);
                                                            entry.add(date);
                                                            mileageAmountCurrencyList.add(entry);

                                                            if (mileage < lowestMileage) {
                                                                lowestMileage = mileage;
                                                                lowestMileageEntry = entry;
                                                            }
                                                        }
                                                    }
                                                }

                                                if (lowestMileageEntry != null) {
                                                    mileageAmountCurrencyList.remove(lowestMileageEntry);
                                                }

                                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                                    @Override
                                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                                        String currency1 = (String) entry1.get(3);
                                                        String currency2 = (String) entry2.get(3);
                                                        return currency1.compareTo(currency2);
                                                    }
                                                });

                                                callback.onMileageAmountCurrencyListFetched(mileageAmountCurrencyList);
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                callback.onError(databaseError.getMessage());
                                            }
                                        });
                                    }

                                    @Override
                                    public void onError(String errorMessage) {
                                        callback.onError(errorMessage);
                                    }
                                });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
    }

    public void findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(EditText vehicleEditText, LastIdFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and fuelType
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lowestMileagePB = null;
                String lowestMileageLPG = null;
                int minMileagePB = Integer.MAX_VALUE;
                int minMileageLPG = Integer.MAX_VALUE;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    String snapshotFuelFP = snapshot.child("fuelFP").getValue(String.class);

                    if (snapshotFuelType != null && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);

                        if (snapshotFuelType.equals("PB") && snapshotFuelFP.equals(FuelFP.FULL.toString())) {
                            if (mileage < minMileagePB) {
                                minMileagePB = mileage;
                                lowestMileagePB = mileageStr;
                            }
                        }

                        if (snapshotFuelType.equals("LPG") && snapshotFuelFP.equals(FuelFP.FULL.toString())) {
                            if (mileage < minMileageLPG) {
                                minMileageLPG = mileage;
                                lowestMileageLPG = mileageStr;
                            }
                        }
                    }
                }

                // Check if both lowest mileages are the same
                if (lowestMileagePB != null && lowestMileageLPG != null && lowestMileagePB.equals(lowestMileageLPG)) {
                    // Invoke the callback with the lowest mileage value
                    callback.onLastIdFetched(lowestMileagePB);
                } else {
                    // No matching condition found
                    callback.onError("No matching records found.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public void findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(EditText vehicleEditText, LastIdFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMileagePB = null;
                String lastMileageLPG = null;

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);

                    if (snapshotFuelType != null && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);

                        if (mileageStr != null && mileageStr.matches("^\\d+$")) {
                            if (snapshotFuelType.equals("PB")) {
                                lastMileagePB = mileageStr; // Update to this mileage as it's the most recent for PB so far
                            }

                            if (snapshotFuelType.equals("LPG")) {
                                lastMileageLPG = mileageStr; // Update to this mileage as it's the most recent for LPG so far
                            }
                        }
                    }
                }

                // Check if both last mileages are retrieved
                if (lastMileagePB != null && lastMileageLPG != null) {
                    // Here you can decide what you want to do. For example:
                    // If you want to return both, you can create a custom object or use a pair.
                    // If you want to return one, decide on your conditions and return accordingly.
                    // For this example, I'll just return the PB mileage:
                    callback.onLastIdFetched(lastMileagePB);
                } else {
                    callback.onError("Failed to retrieve last mileage for both fuel types.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                callback.onError(databaseError.getMessage());
            }
        });
    }





    public void findAmountToCurrencyRateMappingPB(EditText vehicleEditText, AmountCurrencyRateMapFetchedString callback) {
        // First, get the lowest mileage.
        findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lowestMileagePB) {
                // Then, get the highest mileage.
                findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {
                    @Override
                    public void onLastIdFetched(String highestMileagePB) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");
                        int minMileage = Integer.parseInt(lowestMileagePB);
                        int maxMileage = Integer.parseInt(highestMileagePB);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, String> amountToCurrencyRateMap = new HashMap<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals("PB")
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);

                                        if (mileage >= minMileage && mileage <= maxMileage) {
                                            if ("NA".equals(currencyRateStr) || currencyRateStr == null) {
                                                currencyRateStr = "1";
                                            }
                                            amountToCurrencyRateMap.put(amountStr, currencyRateStr);
                                        }
                                    }
                                }

                                callback.onMileageAmountCurrencyMapFetched(amountToCurrencyRateMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void findAmountToCurrencyRateMappingLPG(EditText vehicleEditText, AmountCurrencyRateMapFetchedString callback) {
        // First, get the lowest mileage.
        findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lowestMileageLPG) {
                // Then, get the highest mileage.
                findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {
                    @Override
                    public void onLastIdFetched(String highestMileageLPG) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");
                        int minMileage = Integer.parseInt(lowestMileageLPG);
                        int maxMileage = Integer.parseInt(highestMileageLPG);

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<String, String> amountToCurrencyRateMap = new HashMap<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals("LPG")
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);

                                        if (mileage >= minMileage && mileage <= maxMileage) {
                                            if ("NA".equals(currencyRateStr) || currencyRateStr == null) {
                                                currencyRateStr = "1";
                                            }
                                            amountToCurrencyRateMap.put(amountStr, currencyRateStr);
                                        }
                                    }
                                }

                                callback.onMileageAmountCurrencyMapFetched(amountToCurrencyRateMap);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                callback.onError(databaseError.getMessage());
                            }
                        });
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void computeTotalAdjustedAmountPB(EditText vehicleEditText, AdjustedAmountFetched callback) {
        findAmountToCurrencyRateMappingPB(vehicleEditText, new AmountCurrencyRateMapFetchedString() {

            @Override
            public void onMileageAmountCurrencyMapFetched(Map<String, String> amountToCurrencyRateMap) {
                double totalAdjustedAmount = 0.0;

                for (Map.Entry<String, String> entry : amountToCurrencyRateMap.entrySet()) {
                    double amount = Double.parseDouble(entry.getKey());
                    double currencyRate = Double.parseDouble(entry.getValue());

                    totalAdjustedAmount += (amount * currencyRate);
                }

                callback.onAdjustedAmountFetched(totalAdjustedAmount);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void computeTotalAdjustedAmountLPG(EditText vehicleEditText, AdjustedAmountFetched callback) {
        findAmountToCurrencyRateMappingLPG(vehicleEditText, new AmountCurrencyRateMapFetchedString() {

            @Override
            public void onMileageAmountCurrencyMapFetched(Map<String, String> amountToCurrencyRateMap) {
                double totalAdjustedAmount = 0.0;

                for (Map.Entry<String, String> entry : amountToCurrencyRateMap.entrySet()) {
                    double amount = Double.parseDouble(entry.getKey());
                    double currencyRate = Double.parseDouble(entry.getValue());

                    totalAdjustedAmount += (amount * currencyRate);
                }

                callback.onAdjustedAmountFetched(totalAdjustedAmount);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void computeTotalLitersPB(EditText vehicleEditText, TotalLitersFetched callback) {
        findAmountToCurrencyRateMappingPB(vehicleEditText, new AmountCurrencyRateMapFetchedString() {

            @Override
            public void onMileageAmountCurrencyMapFetched(Map<String, String> amountToCurrencyRateMap) {
                double totalLiters = 0.0;

                for (Map.Entry<String, String> entry : amountToCurrencyRateMap.entrySet()) {
                    double amount = Double.parseDouble(entry.getKey());
                    totalLiters += amount;
                }

                callback.onTotalLitersFetched(totalLiters);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onTotalLitersFetchedError(errorMessage);
            }
        });
    }

    public void computeTotalLitersBetweenMileagesForPB(EditText vehicleEditText, TotalLitersFetched callback) {
        findRecordsBetweenLowestAndHighestMileageForPB(vehicleEditText, new MileageAmountCurrencyListFetched() {

            @Override
            public void onMileageAmountCurrencyListFetched(List<List<Object>> recordList) {
                double totalLiters = 0.0;

                for (List<Object> record : recordList) {
                    String litersStr = (String) record.get(1); // Assuming liters is the second item in the record list
                    double liters = Double.parseDouble(litersStr);
                    totalLiters += liters;
                }

                callback.onTotalLitersFetched(totalLiters);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onTotalLitersFetchedError(errorMessage);
            }
        });
    }

    public void computeTotalLitersBetweenMileagesForLPG(EditText vehicleEditText, TotalLitersFetched callback) {
        findRecordsBetweenLowestAndHighestMileageForLPG(vehicleEditText, new MileageAmountCurrencyListFetched() {

            @Override
            public void onMileageAmountCurrencyListFetched(List<List<Object>> recordList) {
                double totalLiters = 0.0;

                for (List<Object> record : recordList) {
                    String litersStr = (String) record.get(1); // Assuming liters is the second item in the record list
                    double liters = Double.parseDouble(litersStr);
                    totalLiters += liters;
                }

                callback.onTotalLitersFetched(totalLiters);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onTotalLitersFetchedError(errorMessage);
            }
        });
    }

    public void computeLitersPer100kmDifferencePB(EditText vehicleEditText, LitersPerMileageDifferenceCallback callback) {
        computeTotalLitersBetweenMileagesForPB(vehicleEditText, new TotalLitersFetched() {

            @Override
            public void onTotalLitersFetched(double totalLiters) {
                calculateDifferenceBetweenLastAndSmallestMileage(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        if (mileageDifference != 0) {
                            double litersPerMileageDifference = totalLiters / mileageDifference * 100.;
                            callback.onResultFetched(litersPerMileageDifference);
                        } else {
                            callback.onError("Mileage difference is zero, division not possible.");
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onTotalLitersFetchedError(String errorMessage) {
                callback.onError(errorMessage);

            }

        });
    }

    public void computeLitersPer100kmDifferenceLPG(EditText vehicleEditText, LitersPerMileageDifferenceCallback callback) {
        computeTotalLitersBetweenMileagesForLPG(vehicleEditText, new TotalLitersFetched() {

            @Override
            public void onTotalLitersFetched(double totalLiters) {
                calculateDifferenceBetweenLastAndSmallestMileage(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        if (mileageDifference != 0) {
                            double litersPerMileageDifference = totalLiters / mileageDifference * 100.;
                            callback.onResultFetched(litersPerMileageDifference);
                        } else {
                            callback.onError("Mileage difference is zero, division not possible.");
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onTotalLitersFetchedError(String errorMessage) {
                callback.onError(errorMessage);

            }

        });
    }


    public void computeAmountPerMileageForLPG(EditText vehicleEditText, AdjustedAmountPerMileageDifferenceFetched callback) {

        // First, compute the total adjusted amount for LPG
        computeTotalAdjustedAmountLPG(vehicleEditText, new AdjustedAmountFetched() {

            @Override
            public void onAdjustedAmountFetched(double adjustedAmount) {

                // Next, calculate the difference between last and smallest mileage
                calculateDifferenceBetweenLastAndSmallestMileage(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        if (mileageDifference != 0) {
                            double adjustedAmountPerMileage = adjustedAmount / mileageDifference * 100.;
                            callback.onAdjustedAmountPerMileageDifferenceFetched(adjustedAmountPerMileage);
                        } else {
                            callback.onError("Mileage difference is zero, division not possible.");
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });

            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });

    }

    public void computeAmountPerMileageForPB(EditText vehicleEditText, AdjustedAmountPerMileageDifferenceFetched callback) {

        // First, compute the total adjusted amount for LPG
        computeTotalAdjustedAmountPB(vehicleEditText, new AdjustedAmountFetched() {

            @Override
            public void onAdjustedAmountFetched(double adjustedAmount) {

                // Next, calculate the difference between last and smallest mileage
                calculateDifferenceBetweenLastAndSmallestMileage(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        if (mileageDifference != 0) {
                            double adjustedAmountPerMileage = adjustedAmount / mileageDifference * 100.;
                            callback.onAdjustedAmountPerMileageDifferenceFetched(adjustedAmountPerMileage);
                        } else {
                            callback.onError("Mileage difference is zero, division not possible.");
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        callback.onError(errorMessage);
                    }
                });

            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });

    }

    public void computeTotalAmountPerMileage(EditText vehicleEditText, AdjustedAmountPerMileageDifferenceFetched callback) {

        final double[] totalAdjustedAmounts = new double[2];
        final String[] errorMessages = new String[2];

        // First, get the amount per mileage for LPG
        computeAmountPerMileageForLPG(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

            @Override
            public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                totalAdjustedAmounts[0] = adjustedAmountPerMileage;

                // Next, get the amount per mileage for PB
                computeAmountPerMileageForPB(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        totalAdjustedAmounts[1] = adjustedAmountPerMileage;

                        double summedAmountPerMileage = totalAdjustedAmounts[0] + totalAdjustedAmounts[1];

                        callback.onAdjustedAmountPerMileageDifferenceFetched(summedAmountPerMileage);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        errorMessages[1] = errorMessage;

                        // In case of an error in any of the methods, provide an appropriate response
                        if (errorMessages[0] != null || errorMessages[1] != null) {
                            callback.onError("Errors encountered: " + errorMessages[0] + " " + errorMessages[1]);
                        }
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                errorMessages[0] = errorMessage;

                // Even if there's an error in LPG calculation, still proceed with PB calculation
                computeAmountPerMileageForPB(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        totalAdjustedAmounts[1] = adjustedAmountPerMileage;

                        if (errorMessages[0] != null) {
                            callback.onError("Errors encountered: " + errorMessages[0]);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        errorMessages[1] = errorMessage;
                        if (errorMessages[0] != null || errorMessages[1] != null) {
                            callback.onError("Errors encountered: " + errorMessages[0] + " " + errorMessages[1]);
                        }
                    }
                });
            }
        });
    }

    public void computeDaysDifferenceBetweenLowestAndHighestMileageCountableAll(EditText vehicleEditText, DaysDifferenceCallback callback) {


        findRecordsBetweenLowestAndHighestMileageForLPG(vehicleEditText, new MileageAmountCurrencyListFetched() {

            @Override
            public void onMileageAmountCurrencyListFetched(List<List<Object>> recordList) {
                if (recordList.isEmpty()) {
                    callback.onError("List is empty, cannot compute difference");
                    return;
                }

                // Assuming the 6th item in the record is the date
                String lowestMileageDateStr = (String) recordList.get(0).get(5);
                String highestMileageDateStr = (String) recordList.get(recordList.size() - 1).get(5);

                Date start = parseDate(lowestMileageDateStr);
                Date end = parseDate(highestMileageDateStr);

                if (start == null || end == null) {
                    callback.onError("Error parsing dates");
                    return;
                }

                long daysDifference = TimeUnit.DAYS.convert(end.getTime() - start.getTime(), TimeUnit.MILLISECONDS);

                callback.onDaysDifferenceComputed(daysDifference);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    private Date parseDate(String dateStr) {
        List<SimpleDateFormat> knownPatterns = new ArrayList<>();
        knownPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));
        knownPatterns.add(new SimpleDateFormat("M/d/yy"));

        for (SimpleDateFormat format : knownPatterns) {
            try {
                return format.parse(dateStr);
            } catch (ParseException ignored) {
            }
        }

        return null;

    }


}