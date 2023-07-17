package com.example.as_flexifuel_firebase_2023;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PatternMatcher;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.Log;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.BuildConfig;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public EditText vehicleEditText;
    public DatePicker dateDatePicker;
    public EditText mileageEditText;

    public Spinner fuelTypeSpinner;
    public Spinner fuelFPSpinner;
    public EditText litersEditText;
    public EditText amountEditText;
    public Spinner countrySpinner;
    public Spinner currencySpinner;
    public TextView timeWornTextView;
    public EditText notesEditText;
    public EditText poiEditText;

    public EditText latEditText;
    public EditText lngEditText;


    public Button addButton;

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

    private Handler handler;
    private Runnable versionCheckRunnable = new Runnable() {
        @Override
        public void run() {
            checkForVersionUpdate();
            handler.postDelayed(this, 1000); // Check for update every 1 second
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        // timeWornTextView.setText(totalMinutes);
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
        addButton = findViewById(R.id.addButton);

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
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRefueling();
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
        String timeworn = timeWornTextView.getText().toString().trim();
        String notes = notesEditText.getText().toString().trim();
        String poi = poiEditText.getText().toString().trim();
        String lat = latEditText.getText().toString().trim();
        String lng = lngEditText.getText().toString().trim();

        String refuelingId = refuelingsRef.push().getKey();
        if (refuelingId != null) {
            Refueling refueling = new Refueling(refuelingId, vehicle, date, mileage, fuelType, fuelFP, liters, amount, country, currency, timeworn, notes, poi, lat, lng);
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

    private void openEditDialog(final Refueling refueling) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Refueling");

        // Inflate the layout for the dialog
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_refueling, null);
        builder.setView(dialogView);

        final EditText vehicleEditText = dialogView.findViewById(R.id.dialogVehicleEditText);
        final DatePicker dateDatePicker = dialogView.findViewById(R.id.dialogDateDatePicker);
        final EditText mileageEditText = dialogView.findViewById(R.id.dialogMileageEditText);

        final Spinner fuelTypeSpinner = dialogView.findViewById(R.id.dialogFuelTypeSpinner);
        final Spinner fuelFPSpinner = dialogView.findViewById(R.id.dialogFuelFPSpinner);
        final EditText litersEditText = dialogView.findViewById(R.id.dialogLitersEditText);
        final EditText amountEditText = dialogView.findViewById(R.id.dialogAmountEditText);
        final Spinner countrySpinner = dialogView.findViewById(R.id.dialogCountrySpinner);
        final Spinner currencySpinner = dialogView.findViewById(R.id.dialogCurrencySpinner);
        final EditText notesEditText = dialogView.findViewById(R.id.dialogNotesEditText);
        final EditText poiEditText = dialogView.findViewById(R.id.dialogPoiEditText);
        final EditText latEditText = dialogView.findViewById(R.id.dialogLatEditText);
        final EditText lngEditText = dialogView.findViewById(R.id.dialogLngEditText);


        // Set initial values for the dialog fields
        vehicleEditText.setText(refueling.getVehicle());
        String[] dateParts = refueling.getDate().split("/");
        int day = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]) - 1; // Months are zero-based
        int year = Integer.parseInt(dateParts[2]);
        dateDatePicker.updateDate(year, month, day);
        mileageEditText.setText(refueling.getMileage());

        // Create the fuel type adapter and set the selection
        List<FuelType> fuelTypeList = Arrays.asList(FuelType.values());
        ArrayAdapter<FuelType> fuelTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fuelTypeList);
        fuelTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fuelTypeSpinner.setAdapter(fuelTypeAdapter);
        FuelType refuelingFuelType = refueling.getFuelType();
        if (refuelingFuelType != null) {
            int fuelTypeSelection = fuelTypeAdapter.getPosition(refuelingFuelType);
            if (fuelTypeSelection != Spinner.INVALID_POSITION) {
                fuelTypeSpinner.setSelection(fuelTypeSelection);
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
        fuelFPSpinner.setAdapter(fuelFPAdapter);
        FuelFP refuelingFuelFP = refueling.getFuelFP();
        if (refuelingFuelFP != null) {
            int fuelFPSelection = fuelFPAdapter.getPosition(refuelingFuelFP);
            if (fuelFPSelection != Spinner.INVALID_POSITION) {
                fuelFPSpinner.setSelection(fuelFPSelection);
            } else {
                // Handle the case where the fuel FP is not found in the adapter
            }
        } else {
            // Handle the case where the fuel FP is null
            // Set a default selection or perform any other desired action
        }
        litersEditText.setText(refueling.getLiters());
        amountEditText.setText(refueling.getAmount());

        List<Country> countryList = Arrays.asList(Country.values());
        ArrayAdapter<Country> countryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, countryList);
        countryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        countrySpinner.setAdapter(countryAdapter);
        Country refuelingCountry = refueling.getCountry();
        if (refuelingCountry != null) {
            int countrySelection = countryAdapter.getPosition(refuelingCountry);
            if (countrySelection != Spinner.INVALID_POSITION) {
                countrySpinner.setSelection(countrySelection);
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
        currencySpinner.setAdapter(currencyAdapter);
        Currency refuelingCurrency = refueling.getCurrency();
        if (refuelingCurrency != null) {
            int currencySelection = currencyAdapter.getPosition(refuelingCurrency);
            if (currencySelection != Spinner.INVALID_POSITION) {
                currencySpinner.setSelection(currencySelection);
            } else {
                // Handle the case where the fuel FP is not found in the adapter
            }
        } else {
            // Handle the case where the fuel FP is null
            // Set a default selection or perform any other desired action
        }
        notesEditText.setText(refueling.getNotes());
        poiEditText.setText(refueling.getPoi());


        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the updated values from the dialog fields
                String updatedVehicle = vehicleEditText.getText().toString().trim();
//                int updatedDay = dateDatePicker.getDayOfMonth();
//                int updatedMonth = dateDatePicker.getMonth() + 1; // Months are zero-based
//                int updatedYear = dateDatePicker.getYear();
                String updatedDate = userDatePickerFormatDate();//updatedDay + "/" + updatedMonth + "/" + updatedYear;
                String updatedMileage = mileageEditText.getText().toString().trim();

                FuelType updatedFuelType = (FuelType) fuelTypeSpinner.getSelectedItem();
                FuelFP updatedFuelFP = (FuelFP) fuelFPSpinner.getSelectedItem();
                String updatedLiters = litersEditText.getText().toString().trim();
                String updatedAmount = amountEditText.getText().toString().trim();
                Country updateCountry = (Country) countrySpinner.getSelectedItem();
                Currency updatedCurrency = (Currency) currencySpinner.getSelectedItem();
                String updatedNotes = notesEditText.getText().toString().trim();
                String updatePoi = poiEditText.getText().toString().trim();
                String updateLat = latEditText.getText().toString().trim();
                String updateLng = lngEditText.getText().toString().trim();
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
        String updatedDate = updatedDay + "/" + updatedMonth + "/" + updatedYear;
        return updatedDate;

    }


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

}