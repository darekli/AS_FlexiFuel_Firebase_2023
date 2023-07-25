package com.example.as_flexifuel_firebase_2023;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as_flexifuel_firebase_2023.adapter.Ask;
import com.example.as_flexifuel_firebase_2023.nbp.NbpApiService;
import com.example.as_flexifuel_firebase_2023.nbp.Rate;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    public EditText vehicleEditText;
    public DatePicker dateDatePicker;
    //public DatePicker editDatePicker;
    public EditText mileageEditText;

    public Spinner fuelTypeSpinner;
    public Spinner fuelFPSpinner;
    public EditText litersEditText;
    public EditText amountEditText;
    public Spinner countrySpinner;
    public Spinner currencySpinner;
    public TextView currencyRateEditText;

    public TextView timeWornTextView;
    public EditText notesEditText;
    public EditText poiEditText;

    public EditText latEditText;
    public EditText lngEditText;


    public Button addRefuelingButton;

    public DatabaseReference refuelingsRef;
    private List<Refueling> refuelingsList;
    private RefuelingAdapter refuelingAdapter;

    //    private TextView tv_answer_01;
    private Button buttonAsk, buttonAskPage;
//    private DatabaseReference databaseRef;

    public NumberPicker np_number_hours, np_number_minutes;
    private TextView tvVersion;
    private PackageUpdateReceiver receiver;
    private String currentVersionName;


    String updatedDate;
    Currency updatedCurrency;
    String updatedCurrencyRate;
    //public NbpPage nbpPage;
    private static final String BASE_URL = "https://api.nbp.pl/api/";

    private NbpApiService apiService;
    private Handler handler;

    //Button editAddCurrencyRateButton;


    private Runnable versionCheckRunnable = new Runnable() {
        @Override
        public void run() {
            checkForVersionUpdate();
            handler.postDelayed(this, 1000); // Check for update every 1 second
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //  editAddCurrencyRateButton = findViewById(R.id.updateButton_currency_rate);
//        editAddCurrencyRateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fetchExchangeRate();
//            }
//        });
        /**
         * NBP API
         */
        Gson gson = new GsonBuilder().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        apiService = retrofit.create(NbpApiService.class);
/**
 * CODE VERSION
 */
        tvVersion = findViewById(R.id.tv_version);

        // Retrieve the initial version name
        currentVersionName = getAppVersionName();
        tvVersion.setText("Version " + currentVersionName);

        // Start checking for version updates periodically
        handler = new Handler();
        handler.postDelayed(versionCheckRunnable, 1000); // Start checking after 1 second
////


        /**
         * PAGE ASK
         */
        buttonAskPage = findViewById(R.id.button_ask_page);

        buttonAskPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Ask.class));
            }
        });


        vehicleEditText = findViewById(R.id.vehicleEditText);
        dateDatePicker = findViewById(R.id.dateDatePicker);
        mileageEditText = findViewById(R.id.mileageEditText);
        mileageEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        InputFilter mileageFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regex = "-?[0-9]+"; // Wyrażenie regularne dopasowujące tylko liczby całkowite, włączając wartości ujemne
                if (!source.toString().matches(regex)) {
                    return ""; // Odrzuć wprowadzone znaki
                }
                return null; // Zaakceptuj wprowadzone znaki
            }
        };

        mileageEditText.setFilters(new InputFilter[]{mileageFilter});

        fuelTypeSpinner = findViewById(R.id.fuelTypeSpinner);
        fuelFPSpinner = findViewById(R.id.fuelFPSpinner);
        litersEditText = findViewById(R.id.litersEditText);

        InputFilter litersFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regex = "-?[0-9]*(\\.[0-9]*)?"; // Wyrażenie regularne dopasowujące tylko liczby zmiennoprzecinkowe, włączając wartości ujemne
                if (!source.toString().matches(regex)) {
                    return ""; // Odrzuć wprowadzone znaki
                }
                return null; // Zaakceptuj wprowadzone znaki
            }
        };

        litersEditText.setFilters(new InputFilter[]{litersFilter});

        amountEditText = findViewById(R.id.amountEditText);
        InputFilter amountFilter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String regex = "-?[0-9]*(\\.[0-9]*)?"; // Wyrażenie regularne dopasowujące tylko liczby zmiennoprzecinkowe, włączając wartości ujemne
                if (!source.toString().matches(regex)) {
                    return ""; // Odrzuć wprowadzone znaki
                }
                return null; // Zaakceptuj wprowadzone znaki
            }
        };
        amountEditText.setFilters(new InputFilter[]{amountFilter});
        countrySpinner = findViewById(R.id.countrySpinner);
        currencySpinner = findViewById(R.id.currencySpinner);
        currencyRateEditText = findViewById(R.id.currencyRateEditText);
        np_number_hours = findViewById(R.id.np_number_hours);
        np_number_hours.setMinValue(0);
        np_number_hours.setMaxValue(9);
        np_number_minutes = findViewById(R.id.np_number_minutes);
        np_number_minutes.setMinValue(0);
        np_number_minutes.setMaxValue(59);
        np_number_minutes.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%02d", i);
            }
        });
        int hours = np_number_hours.getValue();
        int minutes = np_number_minutes.getValue();
        int totalMinutes = hours * 60 + minutes;
        timeWornTextView = findViewById(R.id.timeWornTextView);
        //todo
       timeWornTextView.setText(String.valueOf(totalMinutes));
        np_number_hours.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateTimeWorn();
            }
        });
        np_number_minutes.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                updateTimeWorn();
            }
        });

        notesEditText = findViewById(R.id.notesEditText);
        poiEditText = findViewById(R.id.poiEditText);
        latEditText = findViewById(R.id.latEditText);
        lngEditText = findViewById(R.id.lngEditText);
        addRefuelingButton = findViewById(R.id.addButton);

        // Initialize Firebase database reference
        refuelingsRef = FirebaseDatabase.getInstance().getReference("refuelings");

        // Set up the spinner adapters
        List<FuelType> fuelTypeList = Arrays.asList(FuelType.values());

        ArrayAdapter<FuelType> fuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FuelType.values());
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(fuelTypeAdapter);

        ArrayAdapter<FuelFP> fuelFPAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, FuelFP.values());
        fuelFPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelFPSpinner.setAdapter(fuelFPAdapter);

        ArrayAdapter<Country> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Country.values());
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);

        ArrayAdapter<Currency> currencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Currency.values());
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        currencySpinner.setAdapter(currencyAdapter);

        refuelingsList = new ArrayList<>();
        refuelingAdapter = new RefuelingAdapter(refuelingsList);
        RecyclerView refuelingsRecyclerView = findViewById(R.id.refuelingsRecyclerView);
        refuelingsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        refuelingsRecyclerView.setAdapter(refuelingAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //reverse mode
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        refuelingsRecyclerView.setLayoutManager(layoutManager);
        addRefuelingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRefueling();
                fetchExchangeRate();
                System.out.println("               fetchExchangeRate() " + fetchExchangeRate());


            }
        });


        refuelingAdapter.setOnItemClickListener(new RefuelingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Refueling selectedRefueling = refuelingsList.get(position);
                showOptionsDialog(selectedRefueling);
            }
        });
        retrieveRefuelings();
    }

    private void addRefueling() {
        String vehicle = vehicleEditText.getText().toString().trim();
        String date = userDatePickerFormatDate();

        if (vehicle.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Please enter vehicle and date", Toast.LENGTH_SHORT).show();
            return;
        }
        String mileage = mileageEditText.getText().toString().trim();

        FuelType fuelType = (FuelType) fuelTypeSpinner.getSelectedItem();
        FuelFP fuelFP = (FuelFP) fuelFPSpinner.getSelectedItem();
        String liters = litersEditText.getText().toString().trim();
        String amount = amountEditText.getText().toString().trim();
        Country country = (Country) countrySpinner.getSelectedItem();
        Currency currency = (Currency) currencySpinner.getSelectedItem();
        String currencyRate = currencyRateEditText.getText().toString().trim();
        //todo error treat
        //String currencyRate;
//
//        if (fetchExchangeRate() != null) {
//            currencyRate = fetchExchangeRateEdit(date).trim();
//        } else {
//            // Handle the case where fetchExchangeRate() returns null
//            // For example, you can set a default value or display an error message.
//            currencyRate = "N/A"; // Default value
//        }
        String timeworn = timeWornTextView.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String poi = poiEditText.getText().toString().trim();
        String lat = latEditText.getText().toString().trim();
        String lng = lngEditText.getText().toString().trim();

        String refuelingId = refuelingsRef.push().getKey();
        if (refuelingId != null) {
            Refueling refueling = new Refueling(refuelingId, vehicle, date, mileage, fuelType, fuelFP, liters, amount, country, currency, currencyRate, timeworn, notes, poi, lat, lng);
            refuelingsRef.child(refuelingId).setValue(refueling)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Refueling added", Toast.LENGTH_SHORT).show();
                            clearFields();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed to add refueling", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    private void clearFields() {
        vehicleEditText.setText("");
        mileageEditText.setText("");
        fuelTypeSpinner.setSelection(0);
        fuelFPSpinner.setSelection(0);
        litersEditText.setText("");
        amountEditText.setText("");
        countrySpinner.setSelection(0);
        currencySpinner.setSelection(0);
        currencyRateEditText.setText("");
        timeWornTextView.setText("");
        notesEditText.setText("");
        poiEditText.setText("");
        latEditText.setText("");
        lngEditText.setText("");
    }

    private void retrieveRefuelings() {
        refuelingsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                refuelingsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Refueling refueling = snapshot.getValue(Refueling.class);
                    if (refueling != null) {
                        refuelingsList.add(refueling);
                    }
                }
                refuelingAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to retrieve refuelings", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showOptionsDialog(final Refueling refueling) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Options")
                .setItems(new CharSequence[]{"Edit", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // Edit option selected
                                openEditDialog(refueling);
                                break;
                            case 1:
                                // Delete option selected
                                deleteRefueling(refueling);
                                break;
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * EDIT REFUELING
     *
     * @param refueling
     */
    private void openEditDialog(final Refueling refueling) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Refueling");


        // Inflate the layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.edit_refueling, null);
        builder.setView(dialogView);

        final EditText editVehicleEditText = dialogView.findViewById(R.id.updateVehicleEditText);

        final EditText editDateEditText = dialogView.findViewById(R.id.updateDateEt);
        final EditText editMileageEditText = dialogView.findViewById(R.id.updateMileageEditText);
        final Spinner editFuelTypeSpinner = dialogView.findViewById(R.id.updateFuelTypeSpinner);
        final Spinner editFuelFPSpinner = dialogView.findViewById(R.id.updateFuelFPSpinner);
        final EditText editLitersEditText = dialogView.findViewById(R.id.updateLitersEditText);
        final EditText editAmountEditText = dialogView.findViewById(R.id.updateAmountEditText);
        final Spinner editCountrySpinner = dialogView.findViewById(R.id.updateCountrySpinner);
        final Spinner editCurrencySpinner = dialogView.findViewById(R.id.updateCurrencySpinner);
        final TextView editCurrencyRateTextView = dialogView.findViewById(R.id.updateCurrencyRateEditText);
        final EditText editTimeWornEditText = dialogView.findViewById(R.id.updateTimeWornEditText);

        final EditText editNotesEditText = dialogView.findViewById(R.id.updateNotesEditText);
        final EditText editPoiEditText = dialogView.findViewById(R.id.updatePoiEditText);
        final EditText editLatEditText = dialogView.findViewById(R.id.updateLatEditText);
        final EditText editLngEditText = dialogView.findViewById(R.id.updateLngEditText);


        // Set initial values for the dialog fields
        // String updatedDate = userDatePickerFormatDate();
        editVehicleEditText.setText(refueling.getVehicle());
        // String dateString = dateTextView.getText().toString();

// Split the date string into year, month, and day values
        editDateEditText.setText(refueling.getDate());
//       String dateString = editDateEditText.getText().toString();
//        if (!dateString.isEmpty()) {
//            // Split the date string into year, month, and day values
//            String[] dateParts = dateString.split("-");
//
//            // Check if the date has all three parts (year, month, day)
//            if (dateParts.length == 3) {
//                int year = Integer.parseInt(dateParts[0]);
//                int month = Integer.parseInt(dateParts[1]) - 1; // Months are zero-based
//                int day = Integer.parseInt(dateParts[2]);
//
//                // Find the DatePicker in your layout (assuming its id is datePicker)
//                DatePicker datePicker = findViewById(R.id.updateDatePicker);
//
//                // Set the initial date of the DatePicker
//                datePicker.updateDate(year, month, day);
//            } else {
//                // Handle invalid date format in the TextView
//                // For example, show an error message or use a default date
//            }
//        } else {
//            // Handle empty date string in the TextView
//            // For example, show an error message or use a default date
//        }


        editMileageEditText.setText(refueling.getMileage());

        // Create the fuel type adapter and set the selection
        List<FuelType> fuelTypeList = Arrays.asList(FuelType.values());
        ArrayAdapter<FuelType> fuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fuelTypeList);
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editFuelTypeSpinner.setAdapter(fuelTypeAdapter);
        FuelType refuelingFuelType = refueling.getFuelType();
        if (refuelingFuelType != null) {
            int fuelTypeSelection = fuelTypeAdapter.getPosition(refuelingFuelType);
            if (fuelTypeSelection != Spinner.INVALID_POSITION) {
                editFuelTypeSpinner.setSelection(fuelTypeSelection);
            } else {
                // Handle the case where the fuel type is not found in the adapter
            }
        } else {
            // Handle the case where the fuel type is null
            // Set a default selection or perform any other desired action
        }

        // Create the fuel FP adapter and set the selection
        List<FuelFP> fuelFPList = Arrays.asList(FuelFP.values());
        ArrayAdapter<FuelFP> fuelFPAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fuelFPList);
        fuelFPAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editFuelFPSpinner.setAdapter(fuelFPAdapter);
        FuelFP refuelingFuelFP = refueling.getFuelFP();
        if (refuelingFuelFP != null) {
            int fuelFPSelection = fuelFPAdapter.getPosition(refuelingFuelFP);
            if (fuelFPSelection != Spinner.INVALID_POSITION) {
                editFuelFPSpinner.setSelection(fuelFPSelection);
            } else {
                // Handle the case where the fuel FP is not found in the adapter
            }
        } else {
            // Handle the case where the fuel FP is null
            // Set a default selection or perform any other desired action
        }
        editLitersEditText.setText(refueling.getLiters());
        editAmountEditText.setText(refueling.getAmount());

        List<Country> countryList = Arrays.asList(Country.values());
        ArrayAdapter<Country> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCountrySpinner.setAdapter(countryAdapter);
        Country refuelingCountry = refueling.getCountry();
        if (refuelingCountry != null) {
            int countrySelection = countryAdapter.getPosition(refuelingCountry);
            if (countrySelection != Spinner.INVALID_POSITION) {
                editCountrySpinner.setSelection(countrySelection);
            } else {
                // Handle the case where the fuel FP is not found in the adapter
            }
        } else {
            // Handle the case where the fuel FP is null
            // Set a default selection or perform any other desired action
        }

        List<Currency> currencyList = Arrays.asList(Currency.values());
        ArrayAdapter<Currency> currencyAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, currencyList);
        currencyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        editCurrencySpinner.setAdapter(currencyAdapter);
        Currency refuelingCurrency = refueling.getCurrency();
        if (refuelingCurrency != null) {
            int currencySelection = currencyAdapter.getPosition(refuelingCurrency);
            if (currencySelection != Spinner.INVALID_POSITION) {
                editCurrencySpinner.setSelection(currencySelection);
            } else {
                // Handle the case where the fuel FP is not found in the adapter
            }
        } else {
            // Handle the case where the fuel FP is null
            // Set a default selection or perform any other desired action
        }

        editCurrencyRateTextView.setText(refueling.getCurrencyRate());
        editTimeWornEditText.setText(refueling.getTimeworn());

        editNotesEditText.setText(refueling.getNotes());
        editPoiEditText.setText(refueling.getPoi());


        /**
         *  SAVE
         */
        builder.setPositiveButton("Savex", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the updated values from the dialog fields
                String updatedVehicle = editVehicleEditText.getText().toString().trim();
//                int updatedDay = dateDatePicker.getDayOfMonth();
//                int updatedMonth = dateDatePicker.getMonth() + 1; // Months are zero-based
//                int updatedYear = dateDatePicker.getYear();
                updatedDate = editDateEditText.getText().toString().trim();//updatedDay + "/" + updatedMonth + "/" + updatedYear;
                String updatedMileage = editMileageEditText.getText().toString().trim();

                FuelType updatedFuelType = (FuelType) editFuelTypeSpinner.getSelectedItem();
                FuelFP updatedFuelFP = (FuelFP) editFuelFPSpinner.getSelectedItem();
                String updatedLiters = editLitersEditText.getText().toString().trim();
                String updatedAmount = editAmountEditText.getText().toString().trim();
                Country updateCountry = (Country) editCountrySpinner.getSelectedItem();
                updatedCurrency = (Currency) editCurrencySpinner.getSelectedItem();
                updatedCurrencyRate = fetchExchangeRateEdit(editCurrencyRateTextView.getText().toString());
                // String updatedCurrencyRate = updateCurrencyRateEdit.trim();
                //  System.out.println(">>>CURR " + updatedDate.toString());
                System.out.println("curr " + updatedCurrencyRate);
                //String updatedCurrencyRate = fetchExchangeRateEdit(updatedDate).trim();
                // System.out.println("updatedCurrencyRate " + updatedCurrencyRate);

                String updateTimeWorn = editTimeWornEditText.getText().toString().trim();
                String updatedNotes = editNotesEditText.getText().toString().trim();
                String updatePoi = editPoiEditText.getText().toString().trim();
                String updateLat = editLatEditText.getText().toString().trim();
                String updateLng = editLngEditText.getText().toString().trim();
                // Update the refueling object
                refueling.setVehicle(updatedVehicle);
                refueling.setDate(updatedDate);
                refueling.setMileage(updatedMileage);

                refueling.setFuelType(updatedFuelType);
                refueling.setFuelFP(updatedFuelFP);
                refueling.setLiters(updatedLiters);
                refueling.setAmount(updatedAmount);
                refueling.setCountry(updateCountry);
                refueling.setCurrency(updatedCurrency);
                refueling.setCurrencyRate(updatedCurrencyRate);
                refueling.setTimeworn(updateTimeWorn);
                refueling.setNotes(updatedNotes);
                refueling.setPoi(updatePoi);
                refueling.setLat(updateLat);
                refueling.setLng(updateLng);

                // Update the refueling in the Firebase database
                refuelingsRef.child(refueling.getId()).setValue(refueling)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MainActivity.this, "Refueling updated", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainActivity.this, "Failed to update refueling", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        builder.setNegativeButton("Cancel", null);

        builder.show();
    }


    private void deleteRefueling(final Refueling refueling) {
        refuelingsRef.child(refueling.getId()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Refueling deleted", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Failed to delete refueling", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateTimeWorn() {
        int hours = np_number_hours.getValue();
        int minutes = np_number_minutes.getValue();
        String combinedValue = hours + ":" + minutes;
        timeWornTextView.setText(combinedValue);
    }

    public String userDatePickerFormatDate() {
        int updatedDay = dateDatePicker.getDayOfMonth();
        int updatedMonth = dateDatePicker.getMonth() + 1; // Months are zero-based
        int updatedYear = dateDatePicker.getYear();
        String updatedDate = String.format("%04d-%02d-%02d", updatedYear, updatedMonth, updatedDay);
        return updatedDate;

    }

//    public String userUpdateDatePickerFormatDate() {
//        int updatedDay = eddate.getDayOfMonth();
//        int updatedMonth = editDatePicker.getMonth() + 1; // Months are zero-based
//        int updatedYear = editDatePicker.getYear();
//        String updatedDate = String.format("%04d-%02d-%02d", updatedYear, updatedMonth, updatedDay);
//
//        return updatedDate;
//    }

    private String getAppVersionName() {
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();
        String versionName = "";
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void checkForVersionUpdate() {
        String newVersionName = getAppVersionName();
        if (!currentVersionName.equals(newVersionName)) {
            currentVersionName = newVersionName;
            tvVersion.setText("Version " + currentVersionName);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the version checking when the activity is destroyed
        handler.removeCallbacks(versionCheckRunnable);
    }

    //    @Override
//    protected void onPause() {
//        super.onPause();
//        unregisterReceiver(receiver);
//    }
    @Override
    protected void onPause() {
        super.onPause();
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
    }

    /**
     * EXCHANGE RATE NBP API
     */
    private String fetchExchangeRate() {
        String table = "a";
        String currency = getSelectedCurrency();
        Date selectedDate = getDateFromDatePicker(dateDatePicker);
        String selectedDateString = formatDate(selectedDate);
        System.out.println("User date: " + selectedDateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);

        ArrayList<String> rateStrings = new ArrayList<>();
        boolean rateFound = false;

        for (int i = 0; i < 5; i++) {
            Date currentDate = calendar.getTime();

            String currentDateString = formatDate(currentDate);
            FetchExchangeRateTask task = new FetchExchangeRateTask();
            task.execute(table, currency, currentDateString);

            try {
                String result = task.get(); // Wait for the task to complete and get the result
                if (result != null && !result.isEmpty()) {
                    if (result.equals("Failed")) {
                        System.out.println("! Failed to fetch exchange rate data for " + currentDateString);
                    } else {
                        double rate = Double.parseDouble(result);
                        if (rate > 0) {
                            rateStrings.add(String.valueOf(rate));
                            rateFound = true;
                            break; // Stop fetching rates if a valid rate is found
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Move to the previous date
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        Double rate = null;

        try {
            Date currentDate = calendar.getTime();
            String currentDateString = formatDate(currentDate);
            String rateString = rateStrings.get(0);
            // String rateFinal = rateString.substring(1, rateString.length() - 1);


            if (rateFound) {
                System.out.println("Rates: " + rateStrings + " for " + currentDateString);
                System.out.println("RATE to db " + rateStrings);
                String resultString = rateString.replace("[", "").replace("]", "");

                return String.valueOf(resultString);
                // Get the first (and only) string element
                //exchangeRateTv.setText("date: " + currentDateString + ", rate: " + String.valueOf(rate) + " " + currency);
                // Display or process the rates as needed
            } else {
                System.out.println("No valid rates found.");
                rate = 0.0;
                //exchangeRateTv.setText("invalid rate. " + rate + " " + currency);
            }
        } catch (IndexOutOfBoundsException e) {
            rate = null;
            //exchangeRateTv.setText("invalid rate. " + rate + " " + currency);

        }
        return null;
    }

    private String fetchExchangeRateEdit(String date) {
        String table = "a";
        String currency = String.valueOf(updatedCurrency);
        //  Date selectedDate = getDateFromDatePicker(date);
        //  String selectedDateString = formatDate(selectedDate);
        System.out.println("User date: " + date);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(convertStringToDate(updatedDate));

        ArrayList<String> rateStrings = new ArrayList<>();
        boolean rateFound = false;

        for (int i = 0; i < 5; i++) {
            Date currentDate = calendar.getTime();

            String currentDateString = formatDate(currentDate);
            FetchExchangeRateTask task = new FetchExchangeRateTask();
            task.execute(table, currency, currentDateString);

            try {
                String result = task.get(); // Wait for the task to complete and get the result
                if (result != null && !result.isEmpty()) {
                    if (result.equals("Failed")) {
                        System.out.println("! Failed to fetch exchange rate data for " + currentDateString);
                    } else {
                        double rate = Double.parseDouble(result);
                        if (rate > 0) {
                            rateStrings.add(String.valueOf(rate));
                            rateFound = true;
                            break; // Stop fetching rates if a valid rate is found
                        }
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }

            // Move to the previous date
            calendar.add(Calendar.DAY_OF_MONTH, -1);
        }
        Double rate = null;

        try {
            Date currentDate = calendar.getTime();
            String currentDateString = formatDate(currentDate);
            String rateString = rateStrings.get(0);
            // String rateFinal = rateString.substring(1, rateString.length() - 1);


            if (rateFound) {
                System.out.println("Rates: " + rateStrings + " for " + currentDateString);
                System.out.println("RATE to db " + rateStrings);
                String resultString = rateString.replace("[", "").replace("]", "");

                return String.valueOf(resultString);
                // Get the first (and only) string element
                //exchangeRateTv.setText("date: " + currentDateString + ", rate: " + String.valueOf(rate) + " " + currency);
                // Display or process the rates as needed
            } else {
                System.out.println("No valid rates found.");
                rate = 0.0;
                //exchangeRateTv.setText("invalid rate. " + rate + " " + currency);
            }
        } catch (IndexOutOfBoundsException e) {
            rate = null;
            //exchangeRateTv.setText("invalid rate. " + rate + " " + currency);

        }
        return null;
    }

    private class FetchExchangeRateTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String table = params[0];
            String currency = params[1];
            String date = params[2];

            Call<Rate> call = apiService.getExchangeRate(table, currency, date);

            try {
                Response<Rate> response = call.execute();
                if (response.isSuccessful() && response.body() != null) {
                    Rate exchangeRate = response.body();
                    List<Rate> rates = exchangeRate.getRates();
                    if (rates.size() >= 1) {
                        double rate = rates.get(0).getMid();
                        return String.valueOf(rate);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return "Failed";
        }
    }


    private Date getDateFromDatePicker(DatePicker datePicker) {
        int year = datePicker.getYear();
        int month = datePicker.getMonth();
        int dayOfMonth = datePicker.getDayOfMonth();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        return calendar.getTime();
    }

    private String formatDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return dateFormat.format(date);
    }

    private String getSelectedCurrency() {
        Spinner currencySpinner = findViewById(R.id.currencySpinner);
        return currencySpinner.getSelectedItem().toString();
    }

    //    private static Date convertStringToDate(String dateString) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//        try {
//            // Parse the dateString to create a Date object
//            Date date = dateFormat.parse(dateString);
//            return date;
//        } catch (ParseException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
    public static Date convertStringToDate(String dateString) {
        SimpleDateFormat inputDateFormat1 = new SimpleDateFormat("MM/dd/yy");
        SimpleDateFormat inputDateFormat2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Try parsing with the first input date format
            Date date = inputDateFormat1.parse(dateString);
            return date;
        } catch (ParseException e1) {
            try {
                // Try parsing with the second input date format
                Date date = inputDateFormat2.parse(dateString);
                return date;
            } catch (ParseException e2) {
                // Return null if the date format is not recognized by any of the formats
                return null;
            }
        }
    }

}