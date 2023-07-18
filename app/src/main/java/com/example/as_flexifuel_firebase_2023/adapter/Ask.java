package com.example.as_flexifuel_firebase_2023.adapter;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.as_flexifuel_firebase_2023.Currency;
import com.example.as_flexifuel_firebase_2023.FuelFP;
import com.example.as_flexifuel_firebase_2023.FuelType;
import com.example.as_flexifuel_firebase_2023.MainActivity;
import com.example.as_flexifuel_firebase_2023.R;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AverageFuelConsumptionCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.CommonMileagesFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.HighestCommonMileageFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastMileageCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LitersListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageAmountCurrencyListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageConsumptionRatioCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageDifferenceFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageListCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.MileageLitersMapFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.SecondHighestCommonMileageFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.SumAllLitersCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Ask extends AppCompatActivity {
    public DatePicker askDateDatePicker;
    public TextView tv_answer_01, tv_answer_02, tv_answer_03, tv_answer_04, tv_answer_05, tv_answer_06, tv_answer_07, tv_answer_08, tv_answer_09, tv_answer_10, tv_answer_11, tv_answer_12, tv_answer_13, tv_answer_14, tv_answer_15, tv_answer_16, tv_answer_17, tv_answer_18, tv_answer_19, tv_answer_20;
    public TextView tv_answer_21, tv_answer_22, tv_answer_23, tv_answer_24, tv_answer_25, tv_answer_26, tv_answer_27, tv_answer_28, tv_answer_29, tv_answer_30, tv_answer_31, tv_answer_32, tv_answer_33, tv_answer_34;
    public TextView tv_answer_35, tv_answer_36, tv_answer_37, tv_answer_38, tv_answer_39, tv_answer_40, tv_answer_41, tv_answer_42, tv_answer_43, tv_answer_44;
    public Button buttonAsk, button_back_main;
    public EditText vehicleEditText;
    public DatabaseReference databaseRef;
    public Spinner fuelTypeSpinner;
    String lastCountableKm = "", secondLastCountableKm = "";
    private static final String SHARED_PREF_NAME = "MySharedPrefs";
    private static final String VEHICLE_PREF_KEY = "vehicle";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_data);

/**
 * BACK TO MAIN ACTIVITY
 */
        button_back_main = findViewById(R.id.button_back);
        button_back_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        vehicleEditText = findViewById(R.id.et_ask_vehicle);
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String lastSavedVehicle = sharedPreferences.getString(VEHICLE_PREF_KEY, "");
        vehicleEditText.setText(lastSavedVehicle);

        fuelTypeSpinner = findViewById(R.id.ask_fuel_type_spinner);
        tv_answer_01 = findViewById(R.id.tv_answer_01);
        tv_answer_02 = findViewById(R.id.tv_answer_02);
        tv_answer_03 = findViewById(R.id.tv_answer_03);
        tv_answer_04 = findViewById(R.id.tv_answer_04);
        tv_answer_05 = findViewById(R.id.tv_answer_05);
        tv_answer_06 = findViewById(R.id.tv_answer_06);
        tv_answer_07 = findViewById(R.id.tv_answer_07);
        tv_answer_08 = findViewById(R.id.tv_answer_08);
        tv_answer_09 = findViewById(R.id.tv_answer_09);
        tv_answer_10 = findViewById(R.id.tv_answer_10);
        tv_answer_11 = findViewById(R.id.tv_answer_11);
        tv_answer_12 = findViewById(R.id.tv_answer_12);
        tv_answer_13 = findViewById(R.id.tv_answer_13);
        tv_answer_14 = findViewById(R.id.tv_answer_14);
        tv_answer_15 = findViewById(R.id.tv_answer_15);
        tv_answer_16 = findViewById(R.id.tv_answer_16);
        tv_answer_17 = findViewById(R.id.tv_answer_17);
        tv_answer_18 = findViewById(R.id.tv_answer_18);
        tv_answer_19 = findViewById(R.id.tv_answer_19);
        tv_answer_20 = findViewById(R.id.tv_answer_20);
        tv_answer_21 = findViewById(R.id.tv_answer_21);
        tv_answer_22 = findViewById(R.id.tv_answer_22);
        tv_answer_23 = findViewById(R.id.tv_answer_23);
        tv_answer_24 = findViewById(R.id.tv_answer_24);
        tv_answer_25 = findViewById(R.id.tv_answer_25);
        tv_answer_26 = findViewById(R.id.tv_answer_26);
        tv_answer_27 = findViewById(R.id.tv_answer_27);
        tv_answer_28 = findViewById(R.id.tv_answer_28);
        tv_answer_29 = findViewById(R.id.tv_answer_29);
        tv_answer_30 = findViewById(R.id.tv_answer_30);
        tv_answer_31 = findViewById(R.id.tv_answer_31);
        tv_answer_32 = findViewById(R.id.tv_answer_32);
        tv_answer_33 = findViewById(R.id.tv_answer_33);
        tv_answer_34 = findViewById(R.id.tv_answer_34);
        tv_answer_35 = findViewById(R.id.tv_answer_35);
        tv_answer_36 = findViewById(R.id.tv_answer_36);
        tv_answer_37 = findViewById(R.id.tv_answer_37);
        tv_answer_38 = findViewById(R.id.tv_answer_38);
        tv_answer_39 = findViewById(R.id.tv_answer_39);
        tv_answer_40 = findViewById(R.id.tv_answer_40);
        tv_answer_41 = findViewById(R.id.tv_answer_41);
        tv_answer_42 = findViewById(R.id.tv_answer_42);
        tv_answer_43 = findViewById(R.id.tv_answer_43);
        tv_answer_44 = findViewById(R.id.tv_answer_44);
        askDateDatePicker = findViewById(R.id.askDateDatePicker);

        buttonAsk = findViewById(R.id.button_ask);
        ArrayAdapter<FuelType> fuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FuelType.values());
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(fuelTypeAdapter);
        databaseRef = FirebaseDatabase.getInstance().getReference("refuelings");

        //tv_answer_01 = findViewById(R.id.tv_answer_01);
        //   buttonAsk = findViewById(R.id.button_ask);
        buttonAsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String vehicle = vehicleEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(VEHICLE_PREF_KEY, vehicle);
                editor.apply();


                tv_answer_15.setText("------------SECOND---LAST-------------------------");
                lastMileageBySelectedDate(v);
                tv_answer_01.setText("click!");
                System.out.println(">>> userDatePickerFormatDate: " + userDatePickerFormatDate());
                System.out.println(">>> todayFormatDate: " + todayFormatDate());
                tv_answer_02.setText(">>> userDatePickerFormatDate: " + userDatePickerFormatDate());
                tv_answer_03.setText(">>> todayFormatDate: " + todayFormatDate());

                getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {

                    @Override
                    public void onLastIdFetched(String lastId) {
                        if (lastId != null) {
                            tv_answer_04.setText("4. lastId: " + lastId);
                            secondLastCountableKm = lastId;
                        } else {

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                findAllMileagesIfFueledfp_FULLAndFuelTypeIsPBAndVehicleIs(vehicleEditText, new MileageListFetched() {


                    @Override
                    public void onMileageListFetched(List<Integer> mileageList) {
                        tv_answer_17.setText("17.all mileage PB full " + mileageList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onLastIdFetched(int finalResult) {

                    }
                });
                findAllMileagesIfFueledfp_FULLAndFuelTypeIsLPGAndVehicleIs(vehicleEditText, new MileageListFetched() {


                    @Override
                    public void onMileageListFetched(List<Integer> mileageList) {
                        tv_answer_18.setText("18.all mileage LPG full " + mileageList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onLastIdFetched(int finalResult) {

                    }
                });


                findCommonMileagesIfFueledfp_FULLAndVehicleIs(vehicleEditText, new CommonMileagesFetched() {

                    @Override
                    public void onCommonMileagesFetched(List<Integer> commonMileages) {
                        tv_answer_19.setText("19.all mileage PB==LPG full " + commonMileages);
                        int countable = commonMileages.size() - 1;
                        tv_answer_20.setText("20. PB==LPG size " + commonMileages.size() + " countable: " + countable);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {

                    @Override
                    public void onHighestCommonMileageFetched(int highestMileage) {
                        tv_answer_21.setText("21. PB==LPG mileage the highest " + highestMileage);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {


                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        tv_answer_22.setText("22. PB==LPG mileage 2nd highest " + secondHighestMileage);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                findMileageDifferenceIfFueledfp_FULLAndVehicleIs(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        tv_answer_23.setText("23. PB==LPG mileage diff highest and 2nd highest: " + mileageDifference + " kms");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                findAllMileageLitersIfFuelTypeIsPB(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_24.setText("24. PB mileage-liters: " + mileageLitersMap);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });
                findAllMileageLitersIfFuelTypeIsPB_RemoveFirstRefueling(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_25.setText("25. PB mileage-liters:remove first " + mileageLitersMap);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });
                findAllMileageLitersIfFuelTypeIsLPG(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_26.setText("26. LPG mileage-liters: " + mileageLitersMap);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });
                findAllMileageLitersIfFuelTypeIsLPG_RemoveFirstRefueling(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_27.setText("27. LPG mileage-liters:remove first: " + mileageLitersMap);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });

                sumAllLitersIfFuelTypeIsPB(vehicleEditText, new SumAllLitersCallback() {


                    @Override
                    public void onSumAllLiters(double sumLiters) {
                        tv_answer_28.setText("28. PB sum liters: " + sumLiters);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                sumAllLitersIfFuelTypeIsLPG(vehicleEditText, new SumAllLitersCallback() {


                    @Override
                    public void onSumAllLiters(double sumLiters) {
                        tv_answer_29.setText("29. LPG sum liters: " + sumLiters);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                calculateMileageConsumptionRatioPb(vehicleEditText, new MileageConsumptionRatioCallback() {


                    @Override
                    public void onMileageConsumptionRatioCalculated(double ratio) {
                        DecimalFormat decimalFormat = new DecimalFormat("#.###");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formattedSumLiters = decimalFormat.format(ratio);
                        tv_answer_30.setText("30. PB avg fuel consumption: " + formattedSumLiters + " liters");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                calculateMileageConsumptionRatioLpg(vehicleEditText, new MileageConsumptionRatioCallback() {


                    @Override
                    public void onMileageConsumptionRatioCalculated(double ratio) {
                        DecimalFormat decimalFormat = new DecimalFormat("#.###");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formattedSumLiters = decimalFormat.format(ratio);
                        tv_answer_31.setText("31. LPG avg fuel consumption: " + formattedSumLiters + " liters");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyPB(vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_32.setText("32. PB MileageAmountCurrencyDate: " + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyLPG(vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_33.setText("33. LPG MileageAmountCurrencyDate: " + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {

                    @Override
                    public void onLastIdFetched(String lastId) {
                        if (lastId != null) {
                            tv_answer_05.setText("5. second last: " + lastId);
                            lastCountableKm = lastId;


                        } else {

                        }
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findAllLitersBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new LitersListFetched() {
                    @Override
                    public void onLitersListFetched(List<Double> litersList) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (double liters : litersList) {
                            stringBuilder.append(liters).append(", ");
                        }
                        String litersString = stringBuilder.toString();
                        // Remove the trailing comma and space
                        if (litersString.length() > 2) {
                            litersString = litersString.substring(0, litersString.length() - 2);
                        }
                        tv_answer_10.setText("10. total L last countable: " + litersString);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onLastIdFetched(int finalResult) {

                    }
                });
                findAllMileageLitersBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new MileageLitersMapFetched() {
                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {

                        tv_answer_11.setText("11. mileage-liters: " + mileageLitersMap);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                averageFuelConsumptionLastCountable(fuelTypeSpinner, vehicleEditText, new AverageFuelConsumptionCallback() {


                    @Override
                    public void onAverageFuelConsumptionCalculated(double averageFuelConsumption) {
                        tv_answer_13.setText("13. avg cons. L last countable: " + String.valueOf(averageFuelConsumption));
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findAllMileageAmountCurrencyDateBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_14.setText("14. " + mileageAmountCurrencyList);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrency(fuelTypeSpinner, vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_15.setText("15. order by currency: " + mileageAmountCurrencyList);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencySumByCurrency(fuelTypeSpinner, vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_16.setText("16. order by currency: " + mileageAmountCurrencyList);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                findAllMileageIfFueledfp_FULLAndFuelTypeIsAndLastCountable(fuelTypeSpinner, vehicleEditText, new MileageListFetched() {


                    @Override
                    public void onMileageListFetched(List<Integer> mileageList) {
                        tv_answer_06.setText("6.All FULL in DB " + mileageList.toString());
                        tv_answer_07.setText("7. " + String.valueOf(mileageList.size()));
                        // tv_answer_08.setText("." + String.valueOf(mileageList.get(1)));
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onLastIdFetched(int finalResult) {

                    }
                });


                getLastCountableMileageDistance(fuelTypeSpinner, vehicleEditText, new MileageListFetched() {
                    @Override
                    public void onMileageListFetched(List<Integer> mileageList) {
                        tv_answer_12.setText(String.valueOf("12. " + mileageList));

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onLastIdFetched(int finalResult) {

                    }
                });

                findAllLitersIfFueledfp_FULLAndFuelTypeIsAndLastCountable(fuelTypeSpinner, vehicleEditText, new LitersListFetched() {


                    @Override
                    public void onLitersListFetched(List<Double> litersList) {
                        tv_answer_09.setText("9.all liters " + litersList.toString() + " ?");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                    @Override
                    public void onLastIdFetched(int finalResult) {

                    }
                });

                getFindLastIdIsFueledfp_FULLAndFuelTypeIs(String.valueOf(fuelTypeSpinner), new

                        LastIdCallback() {
                            @Override
                            public void onLastIdFetched(String lastId) {
                                if (lastId != null) {
                                    // Do something with the lastId value
                                    //  tv_answer_04.setText("lastId: " + lastId);
                                    getRefuelingDetailsById_test(lastId);
                                    getRefuelingDetailsById(lastId, "mileage");
                                    // tv_answer_05.setText(getRefuelingDetailsById(lastId,"kilometers"));
                                } else {
                                    // No matching entry found, handle accordingly
                                    // tv_answer_04.setText("No matching entry found");
                                }
                            }


                            @Override
                            public void onError(String errorMessage) {
                                // Handle the error
                                // tv_answer_04.setText("Error: " + errorMessage);
                            }
                        });

                getMileageBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new MileageListCallback() {

                    @Override
                    public void onMileageListFetched(List<Integer> ids) {

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                calculateDifferenceBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText);

            }
        });
    }


    public void lastMileageBySelectedDate(View view) {
        databaseRef = FirebaseDatabase.getInstance().getReference("refuelings");
        Query query = databaseRef.orderByChild("date").equalTo(userDatePickerFormatDate()).limitToFirst(1);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String kilometers = snapshot.child("mileage").getValue(String.class);
                        tv_answer_01.setText("todo probably wrong: " + kilometers);
                        break;  // Only retrieve the first result
                    }
                } else {
                    tv_answer_01.setText("No data found for the given date");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Query canceled with error: " + databaseError.getMessage());
            }
        });
    }
    //todo lastMileageFromTodayIfFueledFPIsFull(View view) {

    public String userDatePickerFormatDate() {
        int updatedDay = askDateDatePicker.getDayOfMonth();
        int updatedMonth = askDateDatePicker.getMonth() + 1; // Months are zero-based
        int updatedYear = askDateDatePicker.getYear();
        String updatedDate = updatedDay + "/" + updatedMonth + "/" + updatedYear;
        return updatedDate;
    }

    public String todayFormatDate() {
        Calendar c = Calendar.getInstance();
        String year = String.valueOf(c.get(Calendar.YEAR));
        String month = String.valueOf(c.get(Calendar.MONTH) + 1);
        String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
        String updatedDate = day + "/" + month + "/" + year;
        return updatedDate;
    }

//    public interface LastMileageCallback {
//        void onLastMileageFetched(String lastMileage);
//    }

    public void getFindLastIdIsFueledfp_FULL(final LastMileageCallback callback) {
        databaseRef = FirebaseDatabase.getInstance().getReference("refuelings");
        Query fuelFPQuery = databaseRef.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());
        fuelFPQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String lastMileage = null;
//                String lastLiters = null;
                String lastId = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String mileage = snapshot.child("mileage").getValue(String.class);
//                    if (lastMileage == null || mileage.compareTo(lastMileage) > 0) {
//                        lastMileage = mileage;
//                    }
//
//                    String liters = snapshot.child("liters").getValue(String.class);
//                    if (lastLiters == null || liters.compareTo(lastLiters) > 0) {
//                        lastLiters = liters;
//                    }
                    String id = snapshot.child("id").getValue(String.class);
                    if (lastId == null || lastId.compareTo(lastId) > 0) {
                        lastId = id;
                    }
                }

                // Invoke the callback with the last mileage value
                callback.onLastMileageFetched(lastId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

    public void getFindCountableId(final LastMileageCallback callback) {
        databaseRef = FirebaseDatabase.getInstance().getReference("refuelings");
        Query fuelFPQuery = databaseRef.orderByChild("fuelFP").equalTo(FuelFP.FULL.toString());
        fuelFPQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                String lastMileage = null;
//                String lastLiters = null;
                String lastId = null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String mileage = snapshot.child("mileage").getValue(String.class);
//                    if (lastMileage == null || mileage.compareTo(lastMileage) > 0) {
//                        lastMileage = mileage;
//                    }
//
//                    String liters = snapshot.child("liters").getValue(String.class);
//                    if (lastLiters == null || liters.compareTo(lastLiters) > 0) {
//                        lastLiters = liters;
//                    }
                    String id = snapshot.child("id").getValue(String.class);
                    if (lastId == null || lastId.compareTo(lastId) > 0) {
                        lastId = id;
                    }
                }

                // Invoke the callback with the last mileage value
                callback.onLastMileageFetched(lastId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error
            }
        });
    }

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
                    if (snapshotFuelType != null && snapshotFuelType.equals(fuelTypeSpinner.getSelectedItem().toString())
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
                                tv_answer_08.setText("8. Difference between last and second last mileage: " + difference);

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

    public void averageFuelConsumptionLastCountablePbLPG(AverageFuelConsumptionCallback callback) {
        findAllLitersBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new LitersListFetched() {
            @Override
            public void onLitersListFetched(List<Double> litersList) {
                double sumLiters = 0;
                for (double liters : litersList) {
                    sumLiters += liters;
                }

                double finalSumLiters = sumLiters;
                findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {
                    @Override
                    public void onHighestCommonMileageFetched(int highestMileage) {
                        findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {
                            @Override
                            public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                                int difference;
                                if (highestMileage > 0 && secondHighestMileage > 0) {
                                    difference = Math.abs(highestMileage - secondHighestMileage);
                                } else {
                                    difference = 0;
                                }
                                double averageConsumption = (finalSumLiters / difference) * 100.0;

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
                                mileageLitersMap.remove(lowestMileage);

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
                                mileageLitersMap.remove(lowestMileage);

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
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.PB.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);
                                        double amount = Double.parseDouble(amountStr);
                                        String currency = currencyStr;
                                        String date = dateStr;

                                        if (mileage > secondHighestMileage && mileage <= highestMileage && currency != null) {
                                            List<Object> entry = new ArrayList<>();
                                            entry.add(mileage);
                                            entry.add(liters);
                                            entry.add(amount);
                                            entry.add(currency);
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
                                    String dateStr = snapshot.child("date").getValue(String.class);

                                    if (snapshotFuelType != null && snapshotFuelType.equals(FuelType.LPG.toString())
                                            && snapshotVehicle != null && snapshotVehicle.equals(vehicleEditText.getText().toString())) {
                                        int mileage = Integer.parseInt(mileageStr);
                                        double liters = Double.parseDouble(litersStr);
                                        double amount = Double.parseDouble(amountStr);
                                        String currency = currencyStr;
                                        String date = dateStr;

                                        if (mileage > secondHighestMileage && mileage <= highestMileage && currency != null) {
                                            List<Object> entry = new ArrayList<>();
                                            entry.add(mileage);
                                            entry.add(liters);
                                            entry.add(amount);
                                            entry.add(currency);
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

}