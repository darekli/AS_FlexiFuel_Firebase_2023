package com.example.as_flexifuel_firebase_2023.adapter;

import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.as_flexifuel_firebase_2023.FuelFP;
import com.example.as_flexifuel_firebase_2023.FuelType;
import com.example.as_flexifuel_firebase_2023.MainActivity;
import com.example.as_flexifuel_firebase_2023.R;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AdjustedAmountFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AdjustedAmountPerMileageDifferenceFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AmountCurrencyRateMapFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AmountCurrencyRateMapFetchedString;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.AverageFuelConsumptionCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.CommonMileagesFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.DaysDifferenceCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.HighestCommonMileageFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastIdFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LastMileageCallback;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LitersListFetched;
import com.example.as_flexifuel_firebase_2023.adapter.interfaces.LitersPerMileageDifferenceCallback;
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
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class Ask extends AppCompatActivity {
    public TextView tv_answer_01, tv_answer_02, tv_answer_03, tv_answer_04, tv_answer_05, tv_answer_06, tv_answer_07, tv_answer_08, tv_answer_09, tv_answer_10, tv_answer_11, tv_answer_12, tv_answer_13, tv_answer_14, tv_answer_15, tv_answer_16, tv_answer_17, tv_answer_18, tv_answer_19, tv_answer_20;
    public TextView tv_answer_21, tv_answer_22, tv_answer_23, tv_answer_24, tv_answer_25, tv_answer_26, tv_answer_27, tv_answer_28, tv_answer_29, tv_answer_30, tv_answer_31, tv_answer_32, tv_answer_33, tv_answer_34;
    public TextView tv_answer_35, tv_answer_36, tv_answer_37, tv_answer_38, tv_answer_39, tv_answer_40, tv_answer_41, tv_answer_42, tv_answer_43, tv_answer_44, tv_answer_45, tv_answer_46, tv_answer_47, tv_answer_48, tv_answer_49;
    public TextView tv_answer_50, tv_answer_51, tv_answer_52, tv_answer_53, tv_answer_54, tv_answer_55, tv_answer_56, tv_answer_57, tv_answer_58, tv_answer_59;
    public TextView tv_answer_60, tv_answer_61, tv_answer_62, tv_answer_63, tv_answer_64, tv_answer_65, tv_answer_66, tv_answer_67, tv_answer_68, tv_answer_69;
    public TextView tv_answer_70, tv_answer_71, tv_answer_72, tv_answer_73, tv_answer_74;
    public Button buttonLast, button3Last, buttonLastAll, button_back_main, button_nbp_page;
    public EditText vehicleEditText;
    public DatabaseReference databaseRef;
    public Spinner fuelTypeSpinner;
    String lastCountableKm = "", secondLastCountableKm = "";
    private static final String SHARED_PREF_NAME = "MySharedPrefs";
    private static final String VEHICLE_PREF_KEY = "vehicle";

    GradeGaugeView gaugeView_avg_l_cons_once, gaugeView_avg_l_cons_last_pb, gaugeView_avg_l_cons_last_lpg, gaugeView_avg_l_cons_all_pb, gaugeView_avg_l_cons_all_lpg;
    TextView tvDistanceOnce, tvCost100kmOnce, tvDistanceLast, tvCost100kmLast, tvDistanceAll, tvCost100kmAll;

    private Last last;
    private All all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ask_data);

        last = new Last();
        all = new All();

        tvDistanceLast = findViewById(R.id.tv_distance_last);
        tvCost100kmLast = findViewById(R.id.tv_cost_100km_last);
        tvDistanceOnce = findViewById(R.id.tv_distance_once);
        tvCost100kmOnce = findViewById(R.id.tv_cost_100km_once);
        tvDistanceAll = findViewById(R.id.tv_distance_all);
        tvCost100kmAll = findViewById(R.id.tv_cost_100km_all);


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
        // button_nbp_page = findViewById(R.id.button_nbp_page);
//        button_nbp_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), NbpPage.class));
//            }
//        });


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
        tv_answer_45 = findViewById(R.id.tv_answer_45);
        tv_answer_46 = findViewById(R.id.tv_answer_46);
        tv_answer_47 = findViewById(R.id.tv_answer_47);
        tv_answer_48 = findViewById(R.id.tv_answer_48);
        tv_answer_49 = findViewById(R.id.tv_answer_49);
        tv_answer_50 = findViewById(R.id.tv_answer_50);
        tv_answer_51 = findViewById(R.id.tv_answer_51);
        tv_answer_52 = findViewById(R.id.tv_answer_52);
        tv_answer_53 = findViewById(R.id.tv_answer_53);
        tv_answer_54 = findViewById(R.id.tv_answer_54);
        tv_answer_55 = findViewById(R.id.tv_answer_55);
        tv_answer_56 = findViewById(R.id.tv_answer_56);
        tv_answer_57 = findViewById(R.id.tv_answer_57);
        tv_answer_58 = findViewById(R.id.tv_answer_58);
        tv_answer_59 = findViewById(R.id.tv_answer_59);
        tv_answer_60 = findViewById(R.id.tv_answer_60);
        tv_answer_61 = findViewById(R.id.tv_answer_61);
        tv_answer_62 = findViewById(R.id.tv_answer_62);
        tv_answer_63 = findViewById(R.id.tv_answer_63);
        tv_answer_64 = findViewById(R.id.tv_answer_64);
        tv_answer_65 = findViewById(R.id.tv_answer_65);
        tv_answer_66 = findViewById(R.id.tv_answer_66);
        tv_answer_67 = findViewById(R.id.tv_answer_67);
        tv_answer_68 = findViewById(R.id.tv_answer_68);
        tv_answer_69 = findViewById(R.id.tv_answer_69);
        tv_answer_70 = findViewById(R.id.tv_answer_70);
        tv_answer_71 = findViewById(R.id.tv_answer_71);
        tv_answer_72 = findViewById(R.id.tv_answer_72);
        tv_answer_73 = findViewById(R.id.tv_answer_73);
        tv_answer_74 = findViewById(R.id.tv_answer_74);

        buttonLast = findViewById(R.id.button_last);
        button3Last = findViewById(R.id.button_3last);
        buttonLastAll = findViewById(R.id.button_last_all);

        ArrayAdapter<FuelType> fuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FuelType.values());
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(fuelTypeAdapter);
        databaseRef = FirebaseDatabase.getInstance().getReference("refuelings");

        //tv_answer_01 = findViewById(R.id.tv_answer_01);
        //   buttonAsk = findViewById(R.id.button_ask);
        buttonLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /**
                 * GAUGE
                 */

                //shifting gauge to new page
                // setContentView(R.layout.gauge_fuel_cons);
                //only Pb, lpg or on
                gaugeView_avg_l_cons_once = findViewById(R.id.gaugeview_fuel_0);
                gaugeView_avg_l_cons_once.setLabel("l/100km");
                gaugeView_avg_l_cons_once.setAdapter(new GradeGaugeView.Adapter0Test());

//pb last
                gaugeView_avg_l_cons_last_pb = findViewById(R.id.gaugeview_fuel_1);
                gaugeView_avg_l_cons_last_pb.setLabel("l/100km");
                gaugeView_avg_l_cons_last_pb.setAdapter(new GradeGaugeView.Adapter4Test());
//lpg last
                gaugeView_avg_l_cons_last_lpg = findViewById(R.id.gaugeview_fuel_2);
                gaugeView_avg_l_cons_last_lpg.setLabel("l/100km");
                gaugeView_avg_l_cons_last_lpg.setAdapter(new GradeGaugeView.Adapter5Test());

//pb all
                gaugeView_avg_l_cons_all_pb = findViewById(R.id.gaugeview_fuel_all_1);
                gaugeView_avg_l_cons_all_pb.setLabel("l/100km");
                gaugeView_avg_l_cons_all_pb.setAdapter(new GradeGaugeView.AdapterAll1());
//lpg all
                gaugeView_avg_l_cons_all_lpg = findViewById(R.id.gaugeview_fuel_all_2);
                gaugeView_avg_l_cons_all_lpg.setLabel("l/100km");
                gaugeView_avg_l_cons_all_lpg.setAdapter(new GradeGaugeView.AdapterAll2());


                String vehicle = vehicleEditText.getText().toString();
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(VEHICLE_PREF_KEY, vehicle);
                editor.apply();


                tv_answer_15.setText("------------SECOND---LAST-------------------------");
                tv_answer_01.setText("click!");
                System.out.println(">>> todayFormatDate: " + todayFormatDate());
                tv_answer_03.setText(">>> todayFormatDate: " + todayFormatDate());

                last.getFindLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new LastIdFetched() {

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

                last.findAllMileagesIfFueledfp_FULLAndFuelTypeIsPBAndVehicleIs(vehicleEditText, new MileageListFetched() {


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
                last.findAllMileagesIfFueledfp_FULLAndFuelTypeIsLPGAndVehicleIs(vehicleEditText, new MileageListFetched() {


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


                last.findCommonMileagesIfFueledfp_FULLAndVehicleIs(vehicleEditText, new CommonMileagesFetched() {

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

                last.findHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new HighestCommonMileageFetched() {

                    @Override
                    public void onHighestCommonMileageFetched(int highestMileage) {
                        tv_answer_21.setText("21. PB==LPG mileage the highest " + highestMileage);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.findSecondHighestCommonMileageIfFueledfp_FULLAndVehicleIs(vehicleEditText, new SecondHighestCommonMileageFetched() {


                    @Override
                    public void onSecondHighestCommonMileageFetched(int secondHighestMileage) {
                        tv_answer_22.setText("22. PB==LPG mileage 2nd highest " + secondHighestMileage);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findMileageDifferenceIfFueledfp_FULLAndVehicleIs(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        tv_answer_23.setText("23. PB==LPG mileage diff highest and 2nd highest: " + mileageDifference + " kms");
                        tvDistanceLast.setText(mileageDifference + " km");
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findAllMileageLitersIfFuelTypeIsPB(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_24.setText("24. PB mileage-liters: " + mileageLitersMap + " elements size " + mileageLitersMap.size());

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });
                last.findAllMileageLitersIfFuelTypeIsPB_RemoveFirstRefueling(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_25.setText("25. PB mileage-liters:remove first " + mileageLitersMap + " elements size " + mileageLitersMap.size());

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });
                last.findAllMileageLitersIfFuelTypeIsLPG(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_26.setText("26. LPG mileage-liters: " + mileageLitersMap + " elements size: " + mileageLitersMap.size());
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });
                last.findAllMileageLitersIfFuelTypeIsLPG_RemoveFirstRefueling(vehicleEditText, new MileageLitersMapFetched() {

                    @Override
                    public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {
                        tv_answer_27.setText("27. LPG mileage-liters: correct: " + mileageLitersMap + " elements size: " + mileageLitersMap.size());
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }

                });

                last.sumAllLitersIfFuelTypeIsPB(vehicleEditText, new SumAllLitersCallback() {


                    @Override
                    public void onSumAllLiters(double sumLiters) {
                        tv_answer_28.setText("28. PB sum liters: " + sumLiters);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.sumAllLitersIfFuelTypeIsLPG(vehicleEditText, new SumAllLitersCallback() {


                    @Override
                    public void onSumAllLiters(double sumLiters) {
                        tv_answer_29.setText("29. LPG sum liters: " + sumLiters);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.calculateMileageConsumptionRatioPb(vehicleEditText, new MileageConsumptionRatioCallback() {


                    @Override
                    public void onMileageConsumptionRatioCalculated(double ratio) {
                        DecimalFormat decimalFormat = new DecimalFormat("#.###");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formattedSumLiters = decimalFormat.format(ratio);
                        tv_answer_30.setText("30. PB avg fuel consumption: " + formattedSumLiters + " liters");

                        //gauge
                        gaugeView_avg_l_cons_last_pb.setCurrent(Float.parseFloat(formattedSumLiters));
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.calculateMileageConsumptionRatioLpg(vehicleEditText, new MileageConsumptionRatioCallback() {


                    @Override
                    public void onMileageConsumptionRatioCalculated(double ratio) {
                        DecimalFormat decimalFormat = new DecimalFormat("#.###");
                        decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
                        String formattedSumLiters = decimalFormat.format(ratio);
                        tv_answer_31.setText("31. LPG avg fuel consumption: " + formattedSumLiters + " liters");
                        //gauge
                        gaugeView_avg_l_cons_last_lpg.setCurrent(Float.parseFloat(formattedSumLiters));

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyPB(vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_32.setText("32. PB MileageAmountCurrencyDate: " + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyLPG(vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_33.setText("33. LPG MileageAmountCurrencyDate: " + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findTotalAmountOfMoneySpendLastCountableByFuelTypePB(vehicleEditText, new MileageAmountCurrencySumFetched() {


                    @Override
                    public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum) {
                        tv_answer_34.setText("34.Amount of money last countable PB: " + sum);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.findTotalAmountOfMoneySpendLastCountableByFuelTypeLPG(vehicleEditText, new MileageAmountCurrencySumFetched() {


                    @Override
                    public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum) {
                        tv_answer_35.setText("35. Amount of money last countable LPG: " + sum);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyPB_RemoveLowestMileage(vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_36.setText("36. 34. removed first where mileage is the lowestPB" + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });


                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencyLPG_RemoveLowestMileage(vehicleEditText, new MileageAmountCurrencyListFetched() {

                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {
                        tv_answer_37.setText("37. 35. removed first where mileage is the lowestLPG" + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findTotalAmountOfMoneySpendLastCountableByFuelTypePB_PLN(vehicleEditText, new MileageAmountCurrencySumFetched() {

                    @Override
                    public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum) {
                        tv_answer_38.setText("38. 36. PB=PLN" + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.findTotalAmountOfMoneySpendLastCountableByFuelTypeLPG_PLN(vehicleEditText, new MileageAmountCurrencySumFetched() {

                    @Override
                    public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum) {
                        tv_answer_39.setText("39. 37. LPG=PLN" + mileageAmountCurrencyList);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.countFinalSumPB_PLN(vehicleEditText, new AmountCurrencyRateMapFetched() {


                    @Override
                    public void onAmountCurrencyRateMapFetched(Map<Double, Double> amountCurrencyRateMap) {
                        tv_answer_40.setText("40. 38. PB=PLN" + amountCurrencyRateMap);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.countFinalFinalSumPB_PLN(vehicleEditText, new TotalSumCalculated2() {


                    @Override
                    public void onTotalSumCalculated(String totalSum) {
                        tv_answer_41.setText("41. 38. PB=PLN" + totalSum);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.countFinalSumLPG_PLN(vehicleEditText, new AmountCurrencyRateMapFetched() {


                    @Override
                    public void onAmountCurrencyRateMapFetched(Map<Double, Double> amountCurrencyRateMap) {
                        tv_answer_42.setText("42. 39. LPG=PLN" + amountCurrencyRateMap);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.countFinalFinalSumLPG_PLN(vehicleEditText, new TotalSumCalculated2() {


                    @Override
                    public void onTotalSumCalculated(String totalSum) {
                        tv_answer_43.setText("43.  LPG=PLN: " + totalSum);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.sumFinalSumInPLNPbAndLPG(vehicleEditText, new SumCalculated() {
                    @Override
                    public void onSumCalculated(double totalSum) {
                        tv_answer_44.setText("44. Final PLN Pb+LPG: " + totalSum);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.sumTotalAmountOfMoneySpentByFuelTypeForBothPBAndLpgLastCountable(vehicleEditText, new SumCalculated() {
                    @Override
                    public void onSumCalculated(double totalSum) {
                        tv_answer_45.setText("45.(34.+35.) Final in PLN Pb+LPG: " + totalSum + " PLN");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.calculateRatioAndSumForLastCountablePbLPG(vehicleEditText, new SumCalculated() {
                    @Override
                    public void onSumCalculated(double totalSum) {
                        tv_answer_46.setText("46.(23.45.*100) PLN/100km: " + totalSum + " PLN/100km");
                        tvCost100kmLast.setText(totalSum + " PLN/100km");
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                last.findTotalAmountOfMoneySpendLastCountableByFuelTypexxx(fuelTypeSpinner, vehicleEditText, new MileageAmountCurrencySumFetched() {


                    @Override
                    public void onMileageAmountCurrencySumFetched(List<List<Object>> mileageAmountCurrencyList, double sum) {
                        tv_answer_47.setText("47. " + mileageAmountCurrencyList + " PLN/100km" + " >>> sum: " + sum);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.calculateLastPln100kmOnce(fuelTypeSpinner, vehicleEditText, new MileageCalculationCallback() {

                    @Override
                    public void onMileageCalculationComplete(double sum, double difference, double result) {
                        tv_answer_48.setText("48. " + difference + " kms" + " >>> sum: " + sum + "PLN; cons. l/100km: " + result + " PLN");

                    }

                    @Override
                    public void onMileageCalculationError(String errorMessage) {

                    }
                });

                last.calculateTotalLitersSpentLastCountableByFuelType(fuelTypeSpinner, vehicleEditText, new TotalLitersFetched() {

                    @Override
                    public void onTotalLitersFetched(double totalLiters) {
                        tv_answer_49.setText("49. " + totalLiters + " l; liters total");

                    }

                    @Override
                    public void onTotalLitersFetchedError(String errorMessage) {

                    }
                });
                last.calculateLitersPerMileage(fuelTypeSpinner, vehicleEditText, new LitersPerMileageFetched() {

                    @Override
                    public void onLiterPerMileageFetched(String literPerMileage) {
                        tv_answer_50.setText("50. " + literPerMileage + " liters/100km");
                        gaugeView_avg_l_cons_once.setCurrent(Float.parseFloat(literPerMileage));

                    }

                    @Override
                    public void onLiterPerMileageError(String errorMessage) {

                    }
                });

                tv_answer_51.setText("-------ALL--------------");


                all.findLowestMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {

                    @Override
                    public void onLastIdFetched(String lastId) {
                        tv_answer_52.setText("52. all, first smallest mileage: " + lastId + " kms");
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.findLastMileageIfFueledfp_FULLAndBothFuelTypeIsLPGAndPb(vehicleEditText, new LastIdFetched() {

                    @Override
                    public void onLastIdFetched(String lastId) {
                        tv_answer_53.setText("53. all, last highest mileage: " + lastId + " kms");
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.calculateDifferenceBetweenLastAndSmallestMileage(vehicleEditText, new MileageDifferenceFetched() {

                    @Override
                    public void onMileageDifferenceFetched(int mileageDifference) {
                        tv_answer_54.setText("54. all, mileage difference: " + mileageDifference + " kms");
                        tvDistanceAll.setText(mileageDifference + " km");

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                all.findRecordsBetweenLowestAndHighestMileageForPB(vehicleEditText, new MileageAmountCurrencyListFetched() {
                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {

                        tv_answer_55.setText("55.pb 1st record is removed  " + mileageAmountCurrencyList.toString() + " size: " + mileageAmountCurrencyList.size());
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.findRecordsBetweenLowestAndHighestMileageForLPG(vehicleEditText, new MileageAmountCurrencyListFetched() {
                    @Override
                    public void onMileageAmountCurrencyListFetched(List<List<Object>> mileageAmountCurrencyList) {

                        tv_answer_56.setText("56.lpg 1st record is removed " + mileageAmountCurrencyList.toString() + " size: " + mileageAmountCurrencyList.size());
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.findAmountToCurrencyRateMappingPB(vehicleEditText, new AmountCurrencyRateMapFetchedString() {
                    @Override
                    public void onMileageAmountCurrencyMapFetched(Map<String, String> amountToCurrencyRateMap) {


                        tv_answer_57.setText("57. pb " + amountToCurrencyRateMap.toString());
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.findAmountToCurrencyRateMappingLPG(vehicleEditText, new AmountCurrencyRateMapFetchedString() {
                    @Override
                    public void onMileageAmountCurrencyMapFetched(Map<String, String> amountToCurrencyRateMap) {


                        tv_answer_58.setText("58. lpg " + amountToCurrencyRateMap.toString());
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                all.computeTotalAdjustedAmountPB(vehicleEditText, new AdjustedAmountFetched() {
                    @Override
                    public void onAdjustedAmountFetched(double adjustedAmount) {
                        tv_answer_59.setText("59. PB PLN total: " + adjustedAmount);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.computeTotalAdjustedAmountLPG(vehicleEditText, new AdjustedAmountFetched() {
                    @Override
                    public void onAdjustedAmountFetched(double adjustedAmount) {
                        String pln = String.format("%.2f", (adjustedAmount));
                        tv_answer_60.setText("60. LPG PLN total: " + pln);
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });


                all.computeAmountPerMileageForPB(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        String pln100km = String.format("%.2f", (adjustedAmountPerMileage));

                        tv_answer_61.setText("61. PB total PLN/100km: " + pln100km);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.computeAmountPerMileageForLPG(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        String pln100km = String.format("%.2f", (adjustedAmountPerMileage));

                        tv_answer_62.setText("62. PB total PLN/100km: " + pln100km);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.computeTotalAmountPerMileage(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {
                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        String pln100km = String.format("%.2f", (adjustedAmountPerMileage));
                        tv_answer_63.setText("63. PB+LPG total PLN/100km " + pln100km);
                        tvCost100kmAll.setText(pln100km + " PLN/100km");
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.computeAmountPerMileageForPB(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        String pln100km = String.format("%.2f", (adjustedAmountPerMileage));
                        tv_answer_61.setText("61. for PB total PLN/100km: " + pln100km);
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
                all.computeTotalLitersBetweenMileagesForPB(vehicleEditText, new TotalLitersFetched() {
                    @Override
                    public void onTotalLitersFetched(double totalLiters) {
                        tv_answer_64.setText("64. PB " + String.valueOf(totalLiters) + " l total liters all PB");
                    }

                    @Override
                    public void onTotalLitersFetchedError(String errorMessage) {

                    }
                });
                all.computeTotalLitersBetweenMileagesForLPG(vehicleEditText, new TotalLitersFetched() {
                    @Override
                    public void onTotalLitersFetched(double totalLiters) {
                        tv_answer_65.setText("65. LPG " + String.valueOf(totalLiters) + " l total liters all LPG");
                    }

                    @Override
                    public void onTotalLitersFetchedError(String errorMessage) {

                    }
                });

                all.computeLitersPer100kmDifferencePB(vehicleEditText, new LitersPerMileageDifferenceCallback() {
                    @Override
                    public void onResultFetched(double adjustedAmountPerLiter) {
                        tv_answer_66.setText("66. PB " + String.valueOf(adjustedAmountPerLiter) + " l/100km");
                        String l100km = String.format("%.3f", (adjustedAmountPerLiter));
                        //gauge
                        gaugeView_avg_l_cons_all_pb.setCurrent(Float.parseFloat(l100km));
                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                all.computeLitersPer100kmDifferenceLPG(vehicleEditText, new LitersPerMileageDifferenceCallback() {
                    @Override
                    public void onResultFetched(double adjustedAmountPerLiter) {
                        tv_answer_67.setText("67. LPG " + String.valueOf(adjustedAmountPerLiter) + " l/100km");
                        String l100km = String.format("%.3f", (adjustedAmountPerLiter));
                        //gauge
                        gaugeView_avg_l_cons_all_lpg.setCurrent(Float.parseFloat(l100km));

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });

                all.computeDaysDifferenceBetweenLowestAndHighestMileageCountableAll(vehicleEditText, new DaysDifferenceCallback() {


                    @Override
                    public void onDaysDifferenceComputed(long daysDifference) {
                        tv_answer_68.setText("68. Days all: " + daysDifference);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });
                last.computeDaysDifferenceBetweenLowestAndHighestMileageCountableLast(vehicleEditText, new Last.DaysDifferenceCallback() {

                    @Override
                    public void onDaysDifferenceComputed(long daysDifference) {
                        tv_answer_69.setText("69. Days last: " + daysDifference);

                    }

                    @Override
                    public void onError(String errorMessage) {

                    }
                });


                all.computeAmountPerMileageForLPG(vehicleEditText, new AdjustedAmountPerMileageDifferenceFetched() {

                    @Override
                    public void onAdjustedAmountPerMileageDifferenceFetched(double adjustedAmountPerMileage) {
                        String pln100km = String.format("%.2f", (adjustedAmountPerMileage));
                        tv_answer_62.setText("62. for LPG total PLN/100km: " + pln100km);
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });


                last.getFindSecondLastMileageIfFueledfp_FULLAndFuelTypeIsAndVehicleIs(fuelTypeSpinner, vehicleEditText, new

                        LastIdFetched() {

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
                last.findAllLitersBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new

                        LitersListFetched() {
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
                last.findAllMileageLitersBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new

                        MileageLitersMapFetched() {
                            @Override
                            public void onMileageLitersMapFetched(Map<Integer, Double> mileageLitersMap) {

                                tv_answer_11.setText("11. mileage-liters: " + mileageLitersMap);

                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                last.averageFuelConsumptionLastCountable(fuelTypeSpinner, vehicleEditText, new

                        AverageFuelConsumptionCallback() {


                            @Override
                            public void onAverageFuelConsumptionCalculated(double averageFuelConsumption) {
                                tv_answer_13.setText("13. avg cons. L last countable: " + String.valueOf(averageFuelConsumption + " L"));
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new

                        MileageAmountCurrencyListFetched() {

                            @Override
                            public void onMileageAmountCurrencyListFetched
                                    (List<List<Object>> mileageAmountCurrencyList) {
                                tv_answer_14.setText("14. " + mileageAmountCurrencyList);
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrency(fuelTypeSpinner, vehicleEditText, new

                        MileageAmountCurrencyListFetched() {

                            @Override
                            public void onMileageAmountCurrencyListFetched
                                    (List<List<Object>> mileageAmountCurrencyList) {
                                tv_answer_15.setText("15. order by currency: " + mileageAmountCurrencyList);
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                last.findAllMileageAmountCurrencyDateBetweenLastAndSecondLastOrderByCurrencySumByCurrency(fuelTypeSpinner, vehicleEditText, new

                        MileageAmountCurrencyListFetched() {

                            @Override
                            public void onMileageAmountCurrencyListFetched
                                    (List<List<Object>> mileageAmountCurrencyList) {
                                tv_answer_16.setText("16. order by currency: " + mileageAmountCurrencyList);
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });
                last.findAllMileageIfFueledfp_FULLAndFuelTypeIsAndLastCountable(fuelTypeSpinner, vehicleEditText, new

                        MileageListFetched() {


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
                last.calculateDifferenceBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText, new

                        MileageDifferenceFetched() {
                            @Override
                            public void onMileageDifferenceFetched(int mileageDifference) {
                                tv_answer_08.setText("8. " + String.valueOf(mileageDifference));
                                tvDistanceOnce.setText((mileageDifference + " km"));
                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });

                last.getLastCountableMileageDistance(fuelTypeSpinner, vehicleEditText, new

                        MileageListFetched() {
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

                last.findAllLitersIfFueledfp_FULLAndFuelTypeIsAndLastCountable(fuelTypeSpinner, vehicleEditText, new

                        LitersListFetched() {


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

                last.getFindLastIdIsFueledfp_FULLAndFuelTypeIs(String.valueOf(fuelTypeSpinner), new

                        LastIdCallback() {
                            @Override
                            public void onLastIdFetched(String lastId) {
                                if (lastId != null) {
                                    // Do something with the lastId value
                                    //  tv_answer_04.setText("lastId: " + lastId);
                                    last.getRefuelingDetailsById_test(lastId);
                                    last.getRefuelingDetailsById(lastId, "mileage");
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

                last.getMileageBetweenLastAndSecondLast(fuelTypeSpinner, vehicleEditText, new

                        MileageListCallback() {

                            @Override
                            public void onMileageListFetched(List<Integer> ids) {

                            }

                            @Override
                            public void onError(String errorMessage) {

                            }
                        });

                last.calculateDifferenceBetweenLastAndSecondLastMileage(fuelTypeSpinner, vehicleEditText);

            }
        });
    }


    //todo lastMileageFromTodayIfFueledFPIsFull(View view) {


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


}