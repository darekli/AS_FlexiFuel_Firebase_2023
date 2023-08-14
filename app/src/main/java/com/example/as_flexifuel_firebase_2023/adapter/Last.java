package com.example.as_flexifuel_firebase_2023.adapter;

import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;

import com.example.as_flexifuel_firebase_2023.Currency;
import com.example.as_flexifuel_firebase_2023.FuelFP;
import com.example.as_flexifuel_firebase_2023.FuelType;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AmountCurrencyRateMapFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AverageFuelConsumptionCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.CommonMileagesFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.HighestCommonMileageFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LitersListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LitersPerMileageFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageAmountCurrencyListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageAmountCurrencySumFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageCalculationCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageConsumptionRatioCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageDifferenceFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageListCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageLitersMapFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.SecondHighestCommonMileageFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.SumAllLitersCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.SumCalculated;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.TotalLitersFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.TotalSumCalculated2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Last {
    public void getFindLastIdIsFueledfp_FULLAndFuelTypeIs(String fuelTypeSpinner, LastIdCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelType
        Query fuelTypeQuery = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        fuelTypeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastId = null;
                int maxMileage = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("fuelType").getValue(String.class).equals(fuelTypeSpinner)) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        if (mileage > maxMileage) {
                            maxMileage = mileage;
                            lastId = snapshot.child("id").getValue(String.class);
                        }
                    }
                }

                // Invoke the callback with the lastId value
                callback.onLastIdFetched(lastId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }


    public void getFindSecondLastIdIsFueledfp_FULLAndFuelTypeIs(FuelType
                                                                        fuelType, LastIdCallback callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelType
        Query fuelTypeQuery = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        fuelTypeQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> mileageList = new ArrayList<>();
                List<String> idList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.child("fuelType").getValue(String.class).equals(fuelType.toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        mileageList.add(mileage);
                        idList.add(snapshot.child("mileage").getValue(String.class));
                    }
                }

                // Sort the mileage list in descending order
                Collections.sort(mileageList, Collections.reverseOrder());

                // Find the second highest mileage
                if (mileageList.size() >= 2) {
                    int secondLastMileage = mileageList.get(1);

                    // Find the corresponding id
                    for (int i = 0; i < mileageList.size(); i++) {
                        if (mileageList.get(i) == secondLastMileage) {
                            String secondLastId = idList.get(i);

                            // Invoke the callback with the second last id value
                            callback.onLastIdFetched(secondLastId);
                            return;
                        }
                    }
                }

                // No matching entry found or not enough entries, handle accordingly
                callback.onLastIdFetched("No second last id found");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }


    public void getRefuelingDetailsById_test(String refuelingId) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings").child(refuelingId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Retrieve the necessary variables
                String liters = dataSnapshot.child("liters").getValue(String.class);
                Currency currency = dataSnapshot.child("currency").getValue(Currency.class);
                String timeworn = dataSnapshot.child("timeworn").getValue(String.class);
                String notes = dataSnapshot.child("notes").getValue(String.class);

                // Use the retrieved variables as needed
                System.out.println("Liters: " + liters);
                System.out.println("Currency: " + currency);
                System.out.println("Timeworn: " + timeworn);
                System.out.println("Notes: " + notes);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    public void getRefuelingDetailsById(String refuelingId, String value) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings").child(refuelingId);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String val = dataSnapshot.child(value).getValue(String.class);
                System.out.println("getRefuelingDetailsById: " + val);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    public void getMileageBetweenLastAndSecondLast(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageListCallback callback) {
        FuelType fuelType = FuelType.valueOf(fuelTypeSpinner.getSelectedItem().toString());
        String vehicle = vehicleEditText.getText().toString();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelType, fuelFP, and vehicle
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> mileageList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String currentFuelType = snapshot.child("fuelType").getValue(String.class);
                    String currentVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (currentFuelType != null && currentFuelType.equals(fuelType.toString()) &&
                            currentVehicle != null && currentVehicle.equals(vehicle)) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        mileageList.add(mileage);
                    }
                }

                // Sort the mileage list in descending order
                Collections.sort(mileageList, Collections.reverseOrder());

                // Find the last mileage
                if (!mileageList.isEmpty()) {
                    int lastMileage = mileageList.get(0);

                    // Find the second last mileage
                    if (mileageList.size() >= 2) {
                        int secondLastMileage = mileageList.get(1);

                        // Find all mileage values between the last and second last entries
                        List<Integer> mileageBetweenLastAndSecondLast = new ArrayList<>();
                        for (int mileage : mileageList) {
                            if (mileage < lastMileage && mileage > secondLastMileage) {
                                mileageBetweenLastAndSecondLast.add(mileage);
                            }
                        }

                        // Invoke the callback with the list of mileage values
                        callback.onMileageListFetched(mileageBetweenLastAndSecondLast);
                    } else {
                        // Not enough entries to find the second last mileage, handle accordingly
                        callback.onMileageListFetched(Collections.emptyList());
                    }
                } else {
                    // No matching entry found, handle accordingly
                    callback.onMileageListFetched(Collections.emptyList());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }


    public void findMileageIfLastAndSecondLastMatch(String vehicle, MileageListCallback
            callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by vehicle, fuelType, and fuelFP
        Query query = ref.orderByChild("vehicle").equalTo(vehicle);

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> mileageList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String fuelType = snapshot.child("fuelType").getValue(String.class);
                    String fuelFP = snapshot.child("fuelFP").getValue(String.class);
                    if (fuelType != null && fuelType.equals("LPG") && fuelFP != null && fuelFP.equals("FULL")) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        mileageList.add(mileage);
                    }
                }

                // Invoke the callback with the list of mileage values
                callback.onMileageListFetched(mileageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public void getLastCountableMileageDistance(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageListFetched callback) {
        FuelType fuelType = FuelType.valueOf(fuelTypeSpinner.getSelectedItem().toString());
        String vehicle = vehicleEditText.getText().toString();

        // Call the method to fetch the mileage list
        findAllMileageIfFueledfp_FULLAndFuelTypeIsAndLastCountable(fuelTypeSpinner, vehicleEditText, new MileageListFetched() {
            @Override
            public void onMileageListFetched(List<Integer> mileageList) {
                // Check if the mileage list has at least two elements
                if (mileageList.size() >= 2) {
                    // Get the last and second-last mileage
                    int lastMileage = mileageList.get(mileageList.size() - 1);
                    int secondLastMileage = mileageList.get(mileageList.size() - 2);

                    // Calculate the final result
                    int finalResult = lastMileage - secondLastMileage;

                    // Invoke the callback with the final result
                    callback.onLastIdFetched(finalResult);
                } else {
                    callback.onError("Insufficient mileage data to calculate the distance.");
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Invoke the callback with the error message
                callback.onError(errorMessage);
            }

            @Override
            public void onLastIdFetched(int finalResult) {

            }
        });

    }

    public void getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(Spinner fuelTypeSpinner, EditText vehicleEditText, LastIdFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and fuelType
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMileage = null;
                int maxMileage = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.toString())
                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        if (mileage > maxMileage) {
                            maxMileage = mileage;
                            lastMileage = snapshot.child("mileage").getValue(String.class);
                        }
                    }
                }

                // Invoke the callback with the lastMileage value
                callback.onLastIdFetched(lastMileage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * FIND LAST MILEAGE WHERE FuelFP ==FULL and Pb and LPG has the same mileage
     */
    public void findAllMileagesIfFueledfp_FULLAndFuelTypeIsPBAndVehicleIs(EditText vehicleEditText, MileageListFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and fuelType
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> mileageList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        mileageList.add(mileage);
                    }
                }

                // Invoke the callback with the mileageList
                callback.onMileageListFetched(mileageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public void findAllMileagesIfFueledfp_FULLAndFuelTypeIsLPGAndVehicleIs(EditText vehicleEditText, MileageListFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and fuelType
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> mileageList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        mileageList.add(mileage);
                    }
                }

                // Invoke the callback with the mileageList
                callback.onMileageListFetched(mileageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * COMPARED LISTS WHERE FuelType.PB and FUEL.Type.LPG AND mileage is the same in BOTH
     */
    public void findCommonMileagesIfFueledfp_FULLAndVehicleIs(EditText vehicleEditText, CommonMileagesFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> pbMileages = new ArrayList<>();
                List<Integer> lpgMileages = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                    int mileage = Integer.parseInt(mileageStr);

                    if (snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())) {
                            pbMileages.add(mileage);
                        } else if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())) {
                            lpgMileages.add(mileage);
                        }
                    }
                }

                // Find the common mileages
                List<Integer> commonMileages = new ArrayList<>();
                for (Integer pbMileage : pbMileages) {
                    if (lpgMileages.contains(pbMileage)) {
                        commonMileages.add(pbMileage);
                    }
                }

                // Invoke the callback with the commonMileages list
                callback.onCommonMileagesFetched(commonMileages);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    /**
     * PB=LPG LAST_THE_HIGHEST MILEAGE
     */
    public void findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(EditText vehicleEditText, HighestCommonMileageFetched callback) {
        findCommonMileagesIfFueledfp_FULLAndVehicleIs(vehicleEditText, new CommonMileagesFetched() {
            @Override
            public void onCommonMileagesFetched(List<Integer> commonMileages) {
                int highestMileage = Integer.MIN_VALUE;

                for (int mileage : commonMileages) {
                    if (mileage > highestMileage) {
                        highestMileage = mileage;
                    }
                }

                // Invoke the callback with the highestMileage value
                callback.onHighestCommonMileageFetched(highestMileage);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });
    }


    /**
     * PB=LPG SECOND LAST MILEAGE
     */
    public void findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(EditText vehicleEditText, SecondHighestCommonMileageFetched callback) {
        findCommonMileagesIfFueledfp_FULLAndVehicleIs(vehicleEditText, new CommonMileagesFetched() {
            @Override
            public void onCommonMileagesFetched(List<Integer> commonMileages) {
                int highestMileage = Integer.MIN_VALUE;
                int secondHighestMileage = Integer.MIN_VALUE;

                for (int mileage : commonMileages) {
                    if (mileage > highestMileage) {
                        secondHighestMileage = highestMileage;
                        highestMileage = mileage;
                    } else if (mileage > secondHighestMileage && mileage < highestMileage) {
                        secondHighestMileage = mileage;
                    }
                }

                // Invoke the callback with the secondHighestMileage value
                callback.onSecondHighestCommonMileageFetched(secondHighestMileage);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });
    }

    /**
     * LPG= PB DIFFERENCE
     */

    public void findMileageDifferenceIfFueledfp_FULLAndVehicleIs(EditText vehicleEditText, MileageDifferenceFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        int mileageDifference = highestMileage - secondHighestMileage;

                        // Invoke the callback with the mileageDifference value
                        callback.onMileageDifferenceFetched(mileageDifference);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });
    }


    public void getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(Spinner fuelTypeSpinner, EditText vehicleEditText, LastIdFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and fuelType
        Query query = ref.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String lastMileage = null;
                int maxMileage = 0;
                int secondMaxMileage = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        if (mileage > maxMileage) {
                            secondMaxMileage = maxMileage;
                            maxMileage = mileage;
                            lastMileage = snapshot.child("mileage").getValue(String.class);
                        } else if (mileage > secondMaxMileage) {
                            secondMaxMileage = mileage;
                        }
                    }
                }

                // Invoke the callback with the secondMaxMileage value

                callback.onLastIdFetched(String.valueOf(secondMaxMileage));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }


    public void findAllMileageIfFueledfp_FULLAndFuelTypeIsAndLastCountable(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageListFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and vehicle
        Query query = ref.orderByChild("fuelFP").equalTo("FULL");

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Integer> mileageList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (snapshotFuelType != null && snapshotFuelType.contains(fuelTypeSpinner.getSelectedItem().toString())
                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String mileageStr = snapshot.child("mileage").getValue(String.class);
                        int mileage = Integer.parseInt(mileageStr);
                        mileageList.add(mileage);
                    }
                }

                // Sort the mileageList increasingly
                Collections.sort(mileageList);

                callback.onMileageListFetched(mileageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }

    public void findAllLitersIfFueledfp_FULLAndFuelTypeIsAndLastCountable(Spinner fuelTypeSpinner, EditText vehicleEditText, LitersListFetched callback) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

        // Query to filter by fuelFP and vehicle
        Query query = ref.orderByChild("fuelFP").equalTo("FULL");

        // Get the filtered data
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Double> litersList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                    if (snapshotFuelType != null && snapshotFuelType.contains(fuelTypeSpinner.getSelectedItem().toString())
                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                        String litersStr = snapshot.child("liters").getValue(String.class);
                        double liters = Double.parseDouble(litersStr);
                        litersList.add(liters);
                    }
                }

                // Sort the litersList increasingly
                Collections.sort(litersList);

                callback.onLitersListFetched(litersList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
                callback.onError(databaseError.getMessage());
            }
        });
    }


    public void calculateDifferenceBetweenLastAndSecondLastMileage(Spinner fuelTypeSpinner, EditText vehicleEditText) {
        String fuelType = fuelTypeSpinner.getSelectedItem().toString();
        String vehicle = vehicleEditText.getText().toString();

        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondLastMileage = Integer.parseInt(lastId);

                                // Calculate the difference between the last and second last mileage
                                int difference = 0;
                                if (lastMileage > 0 && secondLastMileage > 0) {
                                    difference = Math.abs(lastMileage - secondLastMileage);
                                }

                                // Print the difference
                                //tv_answer_08.setText("8xxx. Difference between last and second last mileage: " + difference);
                                //tvDistanceOnce.setText(difference + " km");
                                System.out.println("Difference between last and second last mileage: " + difference);
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Handle the error
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
            }
        });

    }

    public void calculateDifferenceBetweenLastAndSecondLastMileage(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageDifferenceFetched callback) {
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondLastMileage = Integer.parseInt(lastId);

                                // Calculate the difference between the last and second last mileage
                                int difference = 0;
                                if (lastMileage > 0 && secondLastMileage > 0) {
                                    difference = Math.abs(lastMileage - secondLastMileage);
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

    public void findAllLitersBetweenLastAndSecondLastMileage(Spinner fuelTypeSpinner, EditText vehicleEditText, LitersListFetched callback) {
        findAllMileageLitersBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new MileageLitersMapFetched() {
            @Override
            public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                    @Override
                    public void onLastIdFetched(String lastId) {
                        if (lastId != null) {
                            int lastMileage = Integer.parseInt(lastId);

                            getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                                @Override
                                public void onLastIdFetched(String lastId) {
                                    if (lastId != null) {
                                        int secondLastMileage = Integer.parseInt(lastId);

                                        List<Double> litersList = new ArrayList<>();

                                        for (Map.Entry<Integer, Double> entry : mileageLitersMap.entrySet()) {
                                            int mileage = entry.getKey();
                                            double liters = entry.getValue();

                                            if (mileage > secondLastMileage && mileage <= lastMileage && liters != secondLastMileage) {
                                                litersList.add(liters);
                                            }
                                        }

                                        callback.onLitersListFetched(litersList);
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    callback.onError(errorMessage);
                                }
                            });
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


    public void findAllMileageLitersBetweenLastAndSecondLast(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageLitersMapFetched callback) {
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondLastMileage = Integer.parseInt(lastId);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Map<Integer, Double> mileageLitersMap = new HashMap<>();

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                            String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                            String mileageStr = snapshot.child("mileage").getValue(String.class);
                                            String litersStr = snapshot.child("liters").getValue(String.class);

                                            if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                                                    && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                                int mileage = Integer.parseInt(mileageStr);
                                                double liters = Double.parseDouble(litersStr);

                                                if (mileage > secondLastMileage && mileage <= lastMileage) {
                                                    mileageLitersMap.put(mileage, liters);
                                                }
                                            }
                                        }

                                        callback.onMileageLitersMapFetched(mileageLitersMap);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.onError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onError(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void averageFuelConsumptionLastCountable(Spinner fuelTypeSpinner, EditText vehicleEditText, AverageFuelConsumptionCallback callback) {
        findAllLitersBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new LitersListFetched() {
            @Override
            public void onLitersListFetched(List<Double> litersList) {
                double sumLiters = 0;
                for (double liters : litersList) {
                    sumLiters += liters;
                }

                double finalSumLiters = sumLiters;
                getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                    @Override
                    public void onLastIdFetched(String lastId) {
                        if (lastId != null) {
                            int lastMileage = Integer.parseInt(lastId);

                            getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                                @Override
                                public void onLastIdFetched(String lastId) {
                                    if (lastId != null) {
                                        double averageConsumption;
                                        int secondLastMileage = Integer.parseInt(lastId);
                                        int difference;
                                        if (lastMileage > 0 && secondLastMileage > 0) {
                                            difference = Math.abs(lastMileage - secondLastMileage);
                                        } else {
                                            difference = 0;
                                        }
                                        averageConsumption = (finalSumLiters / difference) * 100.0;

                                        // Check if averageConsumption is infinite
                                        if (Double.isInfinite(averageConsumption)) {
                                            // Handle the infinity case
                                            callback.onAverageFuelConsumptionCalculated(0.0); // or provide a custom value or message
                                        } else {
                                            // Format the averageConsumption value
                                            DecimalFormat decimalFormat = new DecimalFormat("#.###");
                                            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                                            String formattedAverage = decimalFormat.format(averageConsumption);
                                            callback.onAverageFuelConsumptionCalculated(Double.parseDouble(formattedAverage));
                                        }
                                    }
                                }

                                @Override
                                public void onError(String errorMessage) {
                                    callback.onError(errorMessage);
                                }
                            });
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

            @Override
            public void onLastIdFetched(int finalResult) {
                // Handle the onLastIdFetched(int) method if required
            }
        });
    }

    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLast(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondLastMileage = Integer.parseInt(lastId);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                            String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                            String mileageStr = snapshot.child("mileage").getValue(String.class);
                                            String litersStr = snapshot.child("liters").getValue(String.class);

                                            String amountStr = snapshot.child("amount").getValue(String.class);
                                            String currencyStr = snapshot.child("currency").getValue(String.class);
                                            String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                            String dateStr = snapshot.child("date").getValue(String.class);


                                            if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                                                    && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                                int mileage = Integer.parseInt(mileageStr);
                                                double liters = Double.parseDouble(litersStr);
                                                double amount = Double.parseDouble(amountStr);
                                                String date = dateStr;

                                                if (mileage > secondLastMileage && mileage <= lastMileage && currencyStr != null) {
                                                    List<Object> entry = new ArrayList<>();
                                                    entry.add(mileage);
                                                    entry.add(liters);
                                                    entry.add(amount);
                                                    entry.add(currencyStr);
                                                    entry.add(currencyRateStr);
                                                    mileageAmountCurrencyList.add(entry);
                                                    entry.add(date);
                                                }
                                            }
                                        }

                                        callback.onMileageAmountCurrencyListFetched(mileageAmountCurrencyList);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.onError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onError(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrency(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondLastMileage = Integer.parseInt(lastId);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                            String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                            String mileageStr = snapshot.child("mileage").getValue(String.class);
                                            String litersStr = snapshot.child("liters").getValue(String.class);
                                            String amountStr = snapshot.child("amount").getValue(String.class);
                                            String currencyStr = snapshot.child("currency").getValue(String.class);
                                            String dateStr = snapshot.child("date").getValue(String.class);

                                            if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                                                    && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                                int mileage = Integer.parseInt(mileageStr);
                                                double liters = Double.parseDouble(litersStr);
                                                double amount = Double.parseDouble(amountStr);
                                                String date = dateStr;

                                                if (mileage > secondLastMileage && mileage <= lastMileage && currencyStr != null) {
                                                    List<Object> entry = new ArrayList<>();
                                                    entry.add(mileage);
                                                    entry.add(liters);
                                                    entry.add(amount);
                                                    entry.add(currencyStr);
                                                    entry.add(date);
                                                    mileageAmountCurrencyList.add(entry);
                                                }
                                            }
                                        }

                                        // Separate the list by currency
                                        Map<String, List<List<Object>>> mileageAmountCurrencyMap = new HashMap<>();
                                        for (List<Object> entry : mileageAmountCurrencyList) {
                                            String currency = (String) entry.get(3); // Assuming currency is stored at index 3
                                            List<List<Object>> currencyList = mileageAmountCurrencyMap.get(currency);
                                            if (currencyList == null) {
                                                currencyList = new ArrayList<>();
                                                mileageAmountCurrencyMap.put(currency, currencyList);
                                            }
                                            currencyList.add(entry);
                                        }

                                        // Extract the separated lists by currency
                                        // List<List<Object>> separatedLists = new ArrayList<>(mileageAmountCurrencyMap.values());
// Separate the list by currency
                                        // Map<String, List<List<Object>>> mileageAmountCurrencyMap = new HashMap<>();
                                        for (List<Object> entry : mileageAmountCurrencyList) {
                                            String currency = (String) entry.get(3); // Assuming currency is stored at index 3
                                            List<List<Object>> currencyList = mileageAmountCurrencyMap.get(currency);
                                            if (currencyList == null) {
                                                currencyList = new ArrayList<>();
                                                mileageAmountCurrencyMap.put(currency, currencyList);
                                            }
                                            currencyList.add(entry);
                                        }

// Extract the separated lists by currency
                                        List<List<Object>> separatedLists = new ArrayList<>();
                                        for (String currency : mileageAmountCurrencyMap.keySet()) {
                                            List<List<Object>> currencyList = mileageAmountCurrencyMap.get(currency);
                                            separatedLists.add(Collections.singletonList(currencyList));
                                        }

// Pass the separated lists to the callback
                                        callback.onMileageAmountCurrencyListFetched(separatedLists);

                                        // Pass the separated lists to the callback
                                        callback.onMileageAmountCurrencyListFetched(separatedLists);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.onError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onError(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencySumByCurrency(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int lastMileage = Integer.parseInt(lastId);

                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondLastMileage = Integer.parseInt(lastId);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        Map<String, Double> currencyAmountMap = new HashMap<>();

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                            String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                            String mileageStr = snapshot.child("mileage").getValue(String.class);
                                            String litersStr = snapshot.child("liters").getValue(String.class);
                                            String amountStr = snapshot.child("amount").getValue(String.class);
                                            String currencyStr = snapshot.child("currency").getValue(String.class);
                                            String dateStr = snapshot.child("date").getValue(String.class);

                                            if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                                                    && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                                int mileage = Integer.parseInt(mileageStr);
                                                double liters = Double.parseDouble(litersStr);
                                                double amount = Double.parseDouble(amountStr);
                                                String date = dateStr;

                                                if (mileage > secondLastMileage && mileage <= lastMileage && currencyStr != null) {
                                                    double currentAmount = currencyAmountMap.getOrDefault(currencyStr, 0.0);
                                                    currentAmount += amount;
                                                    currencyAmountMap.put(currencyStr, currentAmount);
                                                }
                                            }
                                        }

                                        // Create a list to hold the summarized amounts by currency
                                        List<List<Object>> summarizedAmountsByCurrency = new ArrayList<>();

                                        // Iterate through the currencyAmountMap and create entries for each currency and its corresponding sum amount
                                        for (Map.Entry<String, Double> entry : currencyAmountMap.entrySet()) {
                                            String currency = entry.getKey();
                                            double amount = entry.getValue();
                                            List<Object> currencyEntry = new ArrayList<>();
                                            currencyEntry.add(currency);
                                            currencyEntry.add(amount);
                                            summarizedAmountsByCurrency.add(currencyEntry);
                                        }

                                        // Pass the summarized amounts by currency to the callback
                                        callback.onMileageAmountCurrencyListFetched(summarizedAmountsByCurrency);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.onError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onError(errorMessage);
                        }
                    });
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }

        });

    }

//    public void averageFuelConsumptionLastCountablePbLPG(AverageFuelConsumptionCallback callback) {
//        findAllLitersBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new LitersListFetched() {
//            @Override
//            public void onLitersListFetched(List<Double> litersList) {
//                double sumLiters = 0;
//                for (double liters : litersList) {
//                    sumLiters += liters;
//                }
//
//                double finalSumLiters = sumLiters;
//                findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
//                    @Override
//                    public void onHighestCommonMileageFetched(int highestMileage) {
//                        findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
//                            @Override
//                            public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
//                                int difference;
//                                if (highestMileage > 0 && secondHighestMileage > 0) {
//                                    difference = Math.abs(highestMileage - secondHighestMileage);
//                                } else {
//                                    difference = 0;
//                                }
//                                double averageConsumption = (finalSumLiters / difference) * 100.0;
//
//                                // Check if averageConsumption is infinite
//                                if (Double.isInfinite(averageConsumption)) {
//                                    // Handle the infinity case
//                                    callback.onAverageFuelConsumptionCalculated(0.0); // or provide a custom value or message
//                                } else {
//                                    // Format the averageConsumption value
//                                    DecimalFormat decimalFormat = new DecimalFormat("#.###");
//                                    decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
//                                    String formattedAverage = decimalFormat.format(averageConsumption);
//                                    callback.onAverageFuelConsumptionCalculated(Double.parseDouble(formattedAverage));
//                                }
//                            }
//
//                            @Override
//                            public void onError(String errorMessage) {
//                                callback.onError(errorMessage);
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onError(String errorMessage) {
//                        callback.onError(errorMessage);
//                    }
//                });
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                callback.onError(errorMessage);
//            }
//
//            @Override
//            public void onLastIdFetched(int finalResult) {
//                // Handle the onLastIdFetched(int) method if required
//            }
//        });
//    }

    /**
     * FIND ALL liters-mileage BETWEEN if PB==LPG
     */
    public void findAllMileageLitersIfFuelTypeIsPB(EditText vehicleEditText, MileageLitersMapFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Integer, Double> mileageLitersMap = new HashMap<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);

                                        if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                            mileageLitersMap.put(mileage, liters);
                                        }
                                    }
                                }

                                callback.onMileageLitersMapFetched(mileageLitersMap);
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

    /**
     * FIND ALL liters-mileage BETWEEN if PB==LPG
     */
    public void findAllMileageLitersIfFuelTypeIsLPG(EditText vehicleEditText, MileageLitersMapFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Integer, Double> mileageLitersMap = new HashMap<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);

                                        if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                            mileageLitersMap.put(mileage, liters);
                                        }
                                    }
                                }

                                callback.onMileageLitersMapFetched(mileageLitersMap);
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

    public void findAllMileageLitersIfFuelTypeIsPB_RemoveFirstRefueling(EditText vehicleEditText, MileageLitersMapFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Integer, Double> mileageLitersMap = new HashMap<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);

                                        if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                            mileageLitersMap.put(mileage, liters);
                                        }
                                    }
                                }

                                // Find the lowest mileage
                                int lowestMileage = Integer.MAX_VALUE;
                                for (int mileage : mileageLitersMap.keySet()) {
                                    if (mileage < lowestMileage) {
                                        lowestMileage = mileage;
                                    }
                                }

                                // Remove the lowest mileage from the map
                                //todo not remove on this moment
                                //  mileageLitersMap.remove(lowestMileage);

                                callback.onMileageLitersMapFetched(mileageLitersMap);
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

    public void findAllMileageLitersIfFuelTypeIsLPG_RemoveFirstRefueling(EditText vehicleEditText, MileageLitersMapFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Integer, Double> mileageLitersMap = new HashMap<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);

                                        if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                            mileageLitersMap.put(mileage, liters);
                                        }
                                    }
                                }

                                // Find the lowest mileage
                                int lowestMileage = Integer.MAX_VALUE;
                                for (int mileage : mileageLitersMap.keySet()) {
                                    if (mileage < lowestMileage) {
                                        lowestMileage = mileage;
                                    }
                                }

                                // Remove the lowest mileage from the map
                                //todo not remove becouse couse problem with correct counting
                                // mileageLitersMap.remove(lowestMileage);

                                callback.onMileageLitersMapFetched(mileageLitersMap);
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

    public void sumAllLitersIfFuelTypeIsPB(EditText vehicleEditText, SumAllLitersCallback callback) {
        findAllMileageLitersIfFuelTypeIsPB_RemoveFirstRefueling(vehicleEditText, new MileageLitersMapFetched() {
            @Override
            public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                double sumLiters = 0.0;

                for (double liters : mileageLitersMap.values()) {
                    sumLiters += liters;
                }

                callback.onSumAllLiters(sumLiters);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void sumAllLitersIfFuelTypeIsLPG(EditText vehicleEditText, SumAllLitersCallback callback) {
        findAllMileageLitersIfFuelTypeIsLPG_RemoveFirstRefueling(vehicleEditText, new MileageLitersMapFetched() {
            @Override
            public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                double sumLiters = 0.0;

                for (double liters : mileageLitersMap.values()) {
                    sumLiters += liters;
                }

                callback.onSumAllLiters(sumLiters);
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        });
    }

    public void calculateMileageConsumptionRatioPb(EditText vehicleEditText, MileageConsumptionRatioCallback callback) {
        findMileageDifferenceIfFueledfp_FULLAndVehicleIs(vehicleEditText, new MileageDifferenceFetched() {
            @Override
            public void onMileageDifferenceFetched(int mileageDifference) {
                sumAllLitersIfFuelTypeIsPB(vehicleEditText, new SumAllLitersCallback() {
                    @Override
                    public void onSumAllLiters(double sumLiters) {
                        // Calculate the mileage consumption ratio
                        double ratio = mileageDifference / 100.0;
                        if (sumLiters != 0) {
                            sumLiters /= ratio;
                        }

                        callback.onMileageConsumptionRatioCalculated(sumLiters);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });
    }

    public void calculateMileageConsumptionRatioLpg(EditText vehicleEditText, MileageConsumptionRatioCallback callback) {
        findMileageDifferenceIfFueledfp_FULLAndVehicleIs(vehicleEditText, new MileageDifferenceFetched() {
            @Override
            public void onMileageDifferenceFetched(int mileageDifference) {
                sumAllLitersIfFuelTypeIsLPG(vehicleEditText, new SumAllLitersCallback() {
                    @Override
                    public void onSumAllLiters(double sumLiters) {
                        // Calculate the mileage consumption ratio
                        double ratio = mileageDifference / 100.0;
                        if (sumLiters != 0) {
                            sumLiters /= ratio;

                        }

                        callback.onMileageConsumptionRatioCalculated(sumLiters);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });

    }

    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyPB(EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();

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

                                        if (mileage > secondHighestMileage && mileage <= highestMileage && currency != null) {
                                            List<Object> entry = new ArrayList<>();
                                            entry.add(mileage);
                                            entry.add(liters);
                                            entry.add(amount);
                                            entry.add(currency);
                                            entry.add(currRate);
                                            entry.add(date);
                                            mileageAmountCurrencyList.add(entry);
                                        }
                                    }
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                // Pass the sorted list to the callback
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

    //remove last where mileage is the lowest
    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyPB_RemoveLowestMileage(EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                List<Object> lowestMileageEntry = null;

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

                                        if (mileage > secondHighestMileage && mileage <= highestMileage && currency != null) {
                                            List<Object> entry = new ArrayList<>();
                                            entry.add(mileage);
                                            entry.add(liters);
                                            entry.add(amount);
                                            entry.add(currency);
                                            entry.add(currRate);
                                            entry.add(date);
                                            mileageAmountCurrencyList.add(entry);

                                            // Update the lowest mileage value and its corresponding entry
                                            if (mileage < lowestMileage) {
                                                lowestMileage = mileage;
                                                lowestMileageEntry = entry;
                                            }
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the list
                                if (lowestMileageEntry != null) {
                                    mileageAmountCurrencyList.remove(lowestMileageEntry);
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                // Pass the sorted list to the callback
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

    //total cost PB=PLN
    public void findTotalAmountOfMoneySpendLastCountableByFuelTypePB_PLN(EditText vehicleEditText, MileageAmountCurrencySumFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                double finalSum = 0.0;

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
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double liters = Double.parseDouble(litersStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currency = currencyStr;
                                            String currRate = currencyRateStr;
                                            String date = dateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                List<Object> entry = new ArrayList<>();
                                                entry.add(mileage);
                                                entry.add(liters);
                                                entry.add(amount);
                                                entry.add(currency);
                                                entry.add(currRate);
                                                entry.add(date);
                                                mileageAmountCurrencyList.add(entry);

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                finalSum += (amount * currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, liters, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                // Remove the entry with the lowest mileage from the list
                                int lowestMileage = Integer.MAX_VALUE;
                                List<Object> lowestMileageEntry = null;
                                for (List<Object> entry : mileageAmountCurrencyList) {
                                    int mileage = (int) entry.get(0); // Assuming mileage is stored at index 0
                                    if (mileage < lowestMileage) {
                                        lowestMileage = mileage;
                                        lowestMileageEntry = entry;
                                    }
                                }

                                if (lowestMileageEntry != null) {
                                    mileageAmountCurrencyList.remove(lowestMileageEntry);
                                }

                                callback.onMileageAmountCurrencySumFetched(mileageAmountCurrencyList, finalSum);
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

    //count final sum PB=PLN

    public void countFinalSumPB_PLN(EditText vehicleEditText, AmountCurrencyRateMapFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Double, Double> amountCurrencyRateMap = new HashMap<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                DataSnapshot lowestMileageSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyRateStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currRate = currencyRateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                if (mileage < lowestMileage) {
                                                    lowestMileage = mileage;
                                                    lowestMileageSnapshot = snapshot;
                                                }

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                amountCurrencyRateMap.put(amount, currencyRate);

                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the map
                                if (lowestMileageSnapshot != null) {
                                    String amountStr = lowestMileageSnapshot.child("amount").getValue(String.class);
                                    double amountToRemove = Double.parseDouble(amountStr);
                                    amountCurrencyRateMap.remove(amountToRemove);
                                }

                                callback.onAmountCurrencyRateMapFetched(amountCurrencyRateMap);
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

    public void countFinalFinalSumPB_PLN(EditText vehicleEditText, TotalSumCalculated2 callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Double, Double> amountCurrencyRateMap = new HashMap<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                DataSnapshot lowestMileageSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyRateStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currRate = currencyRateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                if (mileage < lowestMileage) {
                                                    lowestMileage = mileage;
                                                    lowestMileageSnapshot = snapshot;
                                                }

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                amountCurrencyRateMap.put(amount, currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the map
                                if (lowestMileageSnapshot != null) {
                                    String amountStr = lowestMileageSnapshot.child("amount").getValue(String.class);
                                    double amountToRemove = Double.parseDouble(amountStr);
                                    amountCurrencyRateMap.remove(amountToRemove);
                                }

                                // Calculate the total sum by multiplying keys (amounts) with values (currency rates)
                                double totalSum = calculateTotalSumByMultiplyingKeysWithValues(amountCurrencyRateMap);

                                // Convert the total sum to a formatted string
                                String totalSumString = String.format("%.2f", totalSum);

                                // Call the callback to return the totalSum as a string
                                callback.onTotalSumCalculated(totalSumString);
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

    public void countFinalFinalSumLPG_PLN(EditText vehicleEditText, TotalSumCalculated2 callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Double, Double> amountCurrencyRateMap = new HashMap<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                DataSnapshot lowestMileageSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyRateStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currRate = currencyRateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                if (mileage < lowestMileage) {
                                                    lowestMileage = mileage;
                                                    lowestMileageSnapshot = snapshot;
                                                }

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                amountCurrencyRateMap.put(amount, currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the map
                                if (lowestMileageSnapshot != null) {
                                    String amountStr = lowestMileageSnapshot.child("amount").getValue(String.class);
                                    double amountToRemove = Double.parseDouble(amountStr);
                                    amountCurrencyRateMap.remove(amountToRemove);
                                }

                                // Calculate the total sum by multiplying keys (amounts) with values (currency rates)
                                double totalSum = calculateTotalSumByMultiplyingKeysWithValues(amountCurrencyRateMap);

                                // Convert the total sum to a formatted string
                                String totalSumString = String.format("%.2f", totalSum);

                                // Call the callback to return the totalSum as a string
                                callback.onTotalSumCalculated(totalSumString);
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

    // Method to sum the results of countFinalFinalSumPB_PLN and countFinalFinalSumLPG_PLN
    public void sumFinalSumInPLNPbAndLPG(EditText vehicleEditText, SumCalculated callback) {
        // Callbacks for countFinalFinalSumPB_PLN
        TotalSumCalculated2 callbackPB_PLN = new TotalSumCalculated2() {
            @Override
            public void onTotalSumCalculated(String totalSumPB_PLN) {
                // Call the method to get the LPG_PLN total sum
                countFinalFinalSumLPG_PLN(vehicleEditText, new TotalSumCalculated2() {
                    @Override
                    public void onTotalSumCalculated(String totalSumLPG_PLN) {
                        try {
                            double sum = Double.parseDouble(totalSumPB_PLN) + Double.parseDouble(totalSumLPG_PLN);
                            String sumString = String.format("%.2f", sum);
                            callback.onSumCalculated(Double.parseDouble(sumString));
                        } catch (NumberFormatException e) {
                            // Handle the exception if parsing total sums fails
                            // You can display an error message or take appropriate action
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error if needed
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error if needed
            }
        };

        // Call the method to get the PB_PLN total sum
        countFinalFinalSumPB_PLN(vehicleEditText, callbackPB_PLN);
    }


    // Method to calculate the total sum by multiplying keys (amounts) with values (currency rates)
    private double calculateTotalSumByMultiplyingKeysWithValues(Map<Double, Double> amountCurrencyRateMap) {
        double totalSum = 0.0;

        for (Map.Entry<Double, Double> entry : amountCurrencyRateMap.entrySet()) {
            double amount = entry.getKey();
            double currencyRate = entry.getValue();
            double result = amount * currencyRate;
            totalSum += result;
        }

        return totalSum;
    }


//count final sum LPG=PLN

    public void countFinalSumLPG_PLN(EditText vehicleEditText, AmountCurrencyRateMapFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Double, Double> amountCurrencyRateMap = new HashMap<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                DataSnapshot lowestMileageSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyRateStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currRate = currencyRateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                if (mileage < lowestMileage) {
                                                    lowestMileage = mileage;
                                                    lowestMileageSnapshot = snapshot;
                                                }

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                amountCurrencyRateMap.put(amount, currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the map
                                if (lowestMileageSnapshot != null) {
                                    String amountStr = lowestMileageSnapshot.child("amount").getValue(String.class);
                                    double amountToRemove = Double.parseDouble(amountStr);
                                    amountCurrencyRateMap.remove(amountToRemove);
                                }

                                callback.onAmountCurrencyRateMapFetched(amountCurrencyRateMap);
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

    public void findTotalSumByMultiplyingKeysWithValues(Map<Double, Double> amountCurrencyRateMap, SumCalculated callback) {
        if (amountCurrencyRateMap == null || amountCurrencyRateMap.isEmpty()) {
            callback.onSumCalculated(0.0); // Return 0 if the map is null or empty
            return;
        }

        double totalSum = 0.0;

        for (Map.Entry<Double, Double> entry : amountCurrencyRateMap.entrySet()) {
            double amount = entry.getKey();
            double currencyRate = entry.getValue();
            double result = amount * currencyRate;
            totalSum += result;
        }

        callback.onSumCalculated(totalSum);
    }

    public void findAmountCurrencyRateMapByFuelTypePB_PLN(EditText vehicleEditText, AmountCurrencyRateMapFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Map<Double, Double> amountCurrencyRateMap = new HashMap<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                DataSnapshot lowestMileageSnapshot = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyRateStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currRate = currencyRateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                if (mileage < lowestMileage) {
                                                    lowestMileage = mileage;
                                                    lowestMileageSnapshot = snapshot;
                                                }

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                amountCurrencyRateMap.put(amount, currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the map
                                if (lowestMileageSnapshot != null) {
                                    String amountStr = lowestMileageSnapshot.child("amount").getValue(String.class);
                                    double amountToRemove = Double.parseDouble(amountStr);
                                    amountCurrencyRateMap.remove(amountToRemove);
                                }

                                callback.onAmountCurrencyRateMapFetched(amountCurrencyRateMap);
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


    public void findTotalAmountOfMoneySpendLastCountableByFuelTypeLPG_PLN(EditText vehicleEditText, MileageAmountCurrencySumFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                double finalSum = 0.0;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double liters = Double.parseDouble(litersStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currency = currencyStr;
                                            String currRate = currencyRateStr;
                                            String date = dateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                List<Object> entry = new ArrayList<>();
                                                entry.add(mileage);
                                                entry.add(liters);
                                                entry.add(amount);
                                                entry.add(currency);
                                                entry.add(currRate);
                                                entry.add(date);
                                                mileageAmountCurrencyList.add(entry);

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                finalSum += (amount * currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, liters, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                // Remove the entry with the lowest mileage from the list
                                int lowestMileage = Integer.MAX_VALUE;
                                List<Object> lowestMileageEntry = null;
                                for (List<Object> entry : mileageAmountCurrencyList) {
                                    int mileage = (int) entry.get(0); // Assuming mileage is stored at index 0
                                    if (mileage < lowestMileage) {
                                        lowestMileage = mileage;
                                        lowestMileageEntry = entry;
                                    }
                                }

                                if (lowestMileageEntry != null) {
                                    mileageAmountCurrencyList.remove(lowestMileageEntry);
                                }

                                callback.onMileageAmountCurrencySumFetched(mileageAmountCurrencyList, finalSum);
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

    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyLPG(EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);
                                        double amount = Double.parseDouble(amountStr);
                                        String currency = currencyStr;
                                        String currRate = currencyRateStr;
                                        String date = dateStr;

                                        if (mileage > secondHighestMileage && mileage <= highestMileage && currency != null) {
                                            List<Object> entry = new ArrayList<>();
                                            entry.add(mileage);
                                            entry.add(liters);
                                            entry.add(amount);
                                            entry.add(currency);
                                            entry.add(currRate);
                                            entry.add(date);
                                            mileageAmountCurrencyList.add(entry);
                                        }
                                    }
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                // Pass the sorted list to the callback
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

    //remove last where mileage is the lowest when LPG
    public void findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyLPG_RemoveLowestMileage(EditText vehicleEditText, MileageAmountCurrencyListFetched callback) {
        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                int lowestMileage = Integer.MAX_VALUE;
                                List<Object> lowestMileageEntry = null;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);
                                        double amount = Double.parseDouble(amountStr);
                                        String currency = currencyStr;
                                        String currRate = currencyRateStr;
                                        String date = dateStr;

                                        if (mileage > secondHighestMileage && mileage <= highestMileage && currency != null) {
                                            List<Object> entry = new ArrayList<>();
                                            entry.add(mileage);
                                            entry.add(liters);
                                            entry.add(amount);
                                            entry.add(currency);
                                            entry.add(currRate);
                                            entry.add(date);
                                            mileageAmountCurrencyList.add(entry);

                                            // Update the lowest mileage value and its corresponding entry
                                            if (mileage < lowestMileage) {
                                                lowestMileage = mileage;
                                                lowestMileageEntry = entry;
                                            }
                                        }
                                    }
                                }

                                // Remove the entry with the lowest mileage from the list
//                                if (lowestMileageEntry != null) {
//                                    mileageAmountCurrencyList.remove(lowestMileageEntry);
//                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                // Pass the sorted list to the callback
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

    public double calculateSumWithCurrencyRatesFixedNA(List<List<Object>> mileageAmountCurrencyList) {
        double sum = 0.0;

        for (List<Object> entry : mileageAmountCurrencyList) {
            String currency = (String) entry.get(3); // Assuming currency is stored at index 3
            double currRate = 0.0;
            double amount = 0.0;

            try {
                currRate = Double.parseDouble((String) entry.get(4)); // Assuming currency rate is stored at index 4
                String amountStr = String.valueOf(entry.get(2));
                if (!amountStr.equalsIgnoreCase("NA")) {
                    amount = Double.parseDouble(amountStr); // Assuming amount is stored at index 2
                } else {
                    amount = 1.0; // Treat "NA" as 1
                }
            } catch (NumberFormatException e) {
                // Handle the exception when "NA" or invalid number format is encountered
                continue; // Skip this entry and continue with the loop
            }

            double currencyValue = currRate * amount;
            sum += currencyValue;
            double totalSum = calculateSumWithCurrencyRatesFixedNA(mileageAmountCurrencyList);
            System.out.println("Total Sum: " + totalSum);
        }

        return sum;
    }

    public void findTotalAmountOfMoneySpendLastCountableByFuelTypePB(EditText vehicleEditText, MileageAmountCurrencySumFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                double finalSum = 0.0;

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
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double liters = Double.parseDouble(litersStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currency = currencyStr;
                                            String currRate = currencyRateStr;
                                            String date = dateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                List<Object> entry = new ArrayList<>();
                                                entry.add(mileage);
                                                entry.add(liters);
                                                entry.add(amount);
                                                entry.add(currency);
                                                entry.add(currRate);
                                                entry.add(date);
                                                mileageAmountCurrencyList.add(entry);

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                finalSum += (amount * currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, liters, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                callback.onMileageAmountCurrencySumFetched(mileageAmountCurrencyList, finalSum);
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

    public void findTotalAmountOfMoneySpendLastCountableByFuelTypeLPG(EditText vehicleEditText, MileageAmountCurrencySumFetched callback) {
        if (vehicleEditText == null) {
            callback.onError("vehicleEditText is null");
            return;
        }

        findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
            @Override
            public void onHighestCommonMileageFetched(int highestMileage) {
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                double finalSum = 0.0;

                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                    String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                    String mileageStr = snapshot.child("mileage").getValue(String.class);
                                    String litersStr = snapshot.child("liters").getValue(String.class);
                                    String amountStr = snapshot.child("amount").getValue(String.class);
                                    String currencyStr = snapshot.child("currency").getValue(String.class);
                                    String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                            && mileageStr != null && litersStr != null && amountStr != null && currencyStr != null) {

                                        try {
                                            int mileage = Integer.parseInt(mileageStr);
                                            double liters = Double.parseDouble(litersStr);
                                            double amount = Double.parseDouble(amountStr);
                                            String currency = currencyStr;
                                            String currRate = currencyRateStr;
                                            String date = dateStr;

                                            if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                List<Object> entry = new ArrayList<>();
                                                entry.add(mileage);
                                                entry.add(liters);
                                                entry.add(amount);
                                                entry.add(currency);
                                                entry.add(currRate);
                                                entry.add(date);
                                                mileageAmountCurrencyList.add(entry);

                                                double currencyRate = 1.0; // Default value for currRate if NA
                                                if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                    currencyRate = Double.parseDouble(currRate);
                                                }

                                                finalSum += (amount * currencyRate);
                                            }
                                        } catch (NumberFormatException e) {
                                            // Handle the exception if parsing mileage, liters, amount, or currency rate fails
                                            // You can log the error if needed or skip the entry
                                        }
                                    }
                                }

                                // Sort the list by currency
                                mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                    @Override
                                    public int compare(List<Object> entry1, List<Object> entry2) {
                                        String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                        String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                        return currency1.compareTo(currency2);
                                    }
                                });

                                callback.onMileageAmountCurrencySumFetched(mileageAmountCurrencyList, finalSum);
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

    public void sumTotalAmountOfMoneySpentByFuelTypeForBothPBAndLpgLastCountable(EditText vehicleEditText, SumCalculated callback) {
        findTotalAmountOfMoneySpendLastCountableByFuelTypePB(vehicleEditText, new MileageAmountCurrencySumFetched() {
            @Override
            public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyListPB, double finalSumPB) {
                findTotalAmountOfMoneySpendLastCountableByFuelTypeLPG(vehicleEditText, new MileageAmountCurrencySumFetched() {
                    @Override
                    public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyListLPG, double finalSumLPG) {
                        double totalSum = finalSumPB + finalSumLPG;
                        callback.onSumCalculated(Double.parseDouble(String.format("%.2f", totalSum)));
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error if needed
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error if needed
                callback.onError(errorMessage);
            }
        });
    }

    public void calculateRatioAndSumForLastCountablePbLPG(EditText vehicleEditText, SumCalculated callback) {
        sumTotalAmountOfMoneySpentByFuelTypeForBothPBAndLpgLastCountable(vehicleEditText, new SumCalculated() {
            @Override
            public void onSumCalculated(double totalSum) {
                findMileageDifferenceIfFueledfp_FULLAndVehicleIs(vehicleEditText, new MileageDifferenceFetched() {
                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        // Check if mileageDifference is not zero to avoid division by zero
                        if (mileageDifference != 0) {
                            double ratio = totalSum / mileageDifference * 100.;
                            callback.onSumCalculated(Double.parseDouble(String.format("%.2f", ratio)));

                        } else {
                            // Handle the division by zero scenario
                            callback.onError("Division by zero");
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error
                        callback.onError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onError(errorMessage);
            }
        });
    }

    public void findTotalAmountOfMoneySpendLastCountableByFuelTypexxx(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageAmountCurrencySumFetched callback) {
        if (fuelTypeSpinner == null || vehicleEditText == null) {
            callback.onError("fuelTypeSpinner or vehicleEditText is null");
            return;
        }

        // Define a callback for the first query
        LastIdFetched firstQueryCallback = new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int highestMileage = Integer.parseInt(lastId);

                    // Define a callback for the second query
                    LastIdFetched secondQueryCallback = new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondHighestMileage = Integer.parseInt(lastId);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        List<List<Object>> mileageAmountCurrencyList = new ArrayList<>();
                                        double finalSum = 0.0;

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                            String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                            String mileageStr = snapshot.child("mileage").getValue(String.class);
                                            String litersStr = snapshot.child("liters").getValue(String.class);
                                            String amountStr = snapshot.child("amount").getValue(String.class);
                                            String currencyStr = snapshot.child("currency").getValue(String.class);
                                            String currencyRateStr = snapshot.child("currencyRate").getValue(String.class);
                                            String dateStr = snapshot.child("date").getValue(String.class);

                                            if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                                                    && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                                    && mileageStr != null && litersStr != null && amountStr != null && currencyStr != null) {

                                                try {
                                                    int mileage = Integer.parseInt(mileageStr);
                                                    double liters = Double.parseDouble(litersStr);
                                                    double amount = Double.parseDouble(amountStr);
                                                    String currency = currencyStr;
                                                    String currRate = currencyRateStr;
                                                    String date = dateStr;

                                                    if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                        List<Object> entry = new ArrayList<>();
                                                        entry.add(mileage);
                                                        entry.add(liters);
                                                        entry.add(amount);
                                                        entry.add(currency);
                                                        entry.add(currRate);
                                                        entry.add(date);
                                                        mileageAmountCurrencyList.add(entry);

                                                        double currencyRate = 1.0; // Default value for currRate if NA
                                                        if (currRate != null && !currRate.equalsIgnoreCase("NA") && !currRate.equalsIgnoreCase("")) {
                                                            currencyRate = Double.parseDouble(currRate);
                                                        }

                                                        finalSum += (amount * currencyRate);
                                                    }
                                                } catch (NumberFormatException e) {
                                                    // Handle the exception if parsing mileage, liters, amount, or currency rate fails
                                                    // You can log the error if needed or skip the entry
                                                }
                                            }
                                        }

                                        // Sort the list by currency
                                        mileageAmountCurrencyList.sort(new Comparator<List<Object>>() {
                                            @Override
                                            public int compare(List<Object> entry1, List<Object> entry2) {
                                                String currency1 = (String) entry1.get(3); // Assuming currency is stored at index 3
                                                String currency2 = (String) entry2.get(3); // Assuming currency is stored at index 3
                                                return currency1.compareTo(currency2);
                                            }
                                        });

                                        // Remove the entry with the lowest mileage from the list
                                        int lowestMileage = Integer.MAX_VALUE;
                                        List<Object> lowestMileageEntry = null;
                                        for (List<Object> entry : mileageAmountCurrencyList) {
                                            int mileage = (int) entry.get(0); // Assuming mileage is stored at index 0
                                            if (mileage < lowestMileage) {
                                                lowestMileage = mileage;
                                                lowestMileageEntry = entry;
                                            }
                                        }

                                        if (lowestMileageEntry != null) {
                                            // mileageAmountCurrencyList.remove(lowestMileageEntry);
                                        }

                                        callback.onMileageAmountCurrencySumFetched(mileageAmountCurrencyList, finalSum);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.onError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onError(errorMessage);
                        }
                    };

                    // Call the second query method
                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, secondQueryCallback);
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onError(errorMessage);
            }
        };

        // Call the first query method
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, firstQueryCallback);
    }

    public void calculateLastPln100kmOnce(Spinner fuelTypeSpinner, EditText vehicleEditText, MileageCalculationCallback callback) {
        // Calculate the sum value
        findTotalAmountOfMoneySpendLastCountableByFuelTypexxx(fuelTypeSpinner, vehicleEditText, new MileageAmountCurrencySumFetched() {
            @Override
            public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum) {
                // Calculate the difference value
                calculateDifferenceBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new MileageDifferenceFetched() {
                    @Override
                    public void onMileageDifferenceFetched(int difference) {
                        // Divide the sum by the difference and invoke the callback
                        if (difference != 0) {
                            double result = sum / difference * 100.;
                            callback.onMileageCalculationComplete(sum, difference, result);
                            String resultS = String.format("%.2f", (result));
                            //tvCost100kmOnce.setText(resultS + " PLN/100km");
                        } else {
                            // Handle the case where difference is zero (to avoid division by zero)
                            callback.onMileageCalculationError("Difference is zero, cannot divide by zero.");
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        // Handle the error
                        callback.onMileageCalculationError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                // Handle the error
                callback.onMileageCalculationError(errorMessage);
            }
        });
    }

    public void calculateTotalLitersSpentLastCountableByFuelType(Spinner fuelTypeSpinner, EditText vehicleEditText, TotalLitersFetched callback) {
        if (fuelTypeSpinner == null || vehicleEditText == null) {
            callback.onTotalLitersFetchedError("fuelTypeSpinner or vehicleEditText is null");
            return;
        }

        // Define a callback for the first query
        LastIdFetched firstQueryCallback = new LastIdFetched() {
            @Override
            public void onLastIdFetched(String lastId) {
                if (lastId != null) {
                    int highestMileage = Integer.parseInt(lastId);

                    // Define a callback for the second query
                    LastIdFetched secondQueryCallback = new LastIdFetched() {
                        @Override
                        public void onLastIdFetched(String lastId) {
                            if (lastId != null) {
                                int secondHighestMileage = Integer.parseInt(lastId);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("refuelings");

                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        double totalLiters = 0.0;

                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            String snapshotFuelType = snapshot.child("fuelType").getValue(String.class);
                                            String snapshotVehicle = snapshot.child("vehicle").getValue(String.class);
                                            String mileageStr = snapshot.child("mileage").getValue(String.class);
                                            String litersStr = snapshot.child("liters").getValue(String.class);

                                            if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
                                                    && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())
                                                    && mileageStr != null && litersStr != null) {

                                                try {
                                                    int mileage = Integer.parseInt(mileageStr);
                                                    double liters = Double.parseDouble(litersStr);

                                                    if (mileage > secondHighestMileage && mileage <= highestMileage) {
                                                        totalLiters += liters;
                                                    }
                                                } catch (NumberFormatException e) {
                                                    // Handle the exception if parsing mileage or liters fails
                                                    // You can log the error if needed or skip the entry
                                                }
                                            }
                                        }

                                        callback.onTotalLitersFetched(totalLiters);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        callback.onTotalLitersFetchedError(databaseError.getMessage());
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(String errorMessage) {
                            callback.onTotalLitersFetchedError(errorMessage);
                        }
                    };

                    // Call the second query method
                    getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, secondQueryCallback);
                }
            }

            @Override
            public void onError(String errorMessage) {
                callback.onTotalLitersFetchedError(errorMessage);
            }
        };

        // Call the first query method
        getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, firstQueryCallback);
    }

    public void calculateLitersPerMileage(Spinner fuelTypeSpinner, EditText vehicleEditText, LitersPerMileageFetched callback) {
        calculateDifferenceBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new MileageDifferenceFetched() {
            @Override
            public void onMileageDifferenceFetched(int difference) {
                calculateTotalLitersSpentLastCountableByFuelType(fuelTypeSpinner, vehicleEditText, new TotalLitersFetched() {
                    @Override
                    public void onTotalLitersFetched(double totalLiters) {
                        if (difference != 0) {
                            double litersPerMileage = totalLiters / difference * 100.0;
                            String lpmFormatted = String.format("%.3f", litersPerMileage);

                            callback.onLiterPerMileageFetched(lpmFormatted);
                        } else {
                            callback.onLiterPerMileageError("Difference is zero, cannot divide by zero.");
                        }
                    }

                    @Override
                    public void onTotalLitersFetchedError(String errorMessage) {
                        callback.onLiterPerMileageError(errorMessage);
                    }
                });
            }

            @Override
            public void onError(String errorMessage) {
                callback.onLiterPerMileageError(errorMessage);
            }
        });
    }

    public void computeDaysDifferenceBetweenLowestAndHighestMileageCountableLast(EditText vehicleEditText, DaysDifferenceCallback callback) {

        findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyLPG_RemoveLowestMileage(vehicleEditText, new MileageAmountCurrencyListFetched() {

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

    interface DaysDifferenceCallback {
        void onDaysDifferenceComputed(long daysDifference);

        void onError(String errorMessage);
    }
}
