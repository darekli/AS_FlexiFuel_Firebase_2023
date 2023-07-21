//package com.example.as_flexifuel_firebase_2023.nbp;
//
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.as_flexifuel_firebase_2023.MainActivity;
//import com.example.as_flexifuel_firebase_2023.R;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import java.util.Locale;
//import java.util.concurrent.ExecutionException;
//
//import retrofit2.Call;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class NbpPage extends AppCompatActivity {
//
//    private static final String BASE_URL = "https://api.nbp.pl/api/";
//
//    private NbpApiService apiService;
//    private DatePicker datePicker;
//    private TextView exchangeRateTv;
//    public Button button_home_page, fetchCurrencyButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.nbp_page);
//        button_home_page = findViewById(R.id.button_home_page);
//        button_home_page.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//            }
//        });
//        datePicker = findViewById(R.id.datePicker);
//        exchangeRateTv = findViewById(R.id.exchange_rate_tv);
//
//        Gson gson = new GsonBuilder().create();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        apiService = retrofit.create(NbpApiService.class);
//
//
//        fetchCurrencyButton = findViewById(R.id.fetchCurrencyButton);
////        fetchCurrencyButton.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                fetchExchangeRate();
////            }
////        });
//    }
//
//    public void fetchExchangeRate() {
//        String table = "a";
//        String currency = getSelectedCurrency();
//
//        Date selectedDate = getDateFromDatePicker(datePicker);
//        String selectedDateString = formatDate(selectedDate);
//        System.out.println("User date: " + selectedDateString);
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(selectedDate);
//
//        ArrayList<String> rateStrings = new ArrayList<>();
//        boolean rateFound = false;
//
//        for (int i = 0; i < 5; i++) {
//            Date currentDate = calendar.getTime();
//            String currentDateString = formatDate(currentDate);
//
//            FetchExchangeRateTask task = new FetchExchangeRateTask();
//            task.execute(table, currency, currentDateString);
//
//            try {
//                String result = task.get(); // Wait for the task to complete and get the result
//                if (result != null && !result.isEmpty()) {
//                    if (result.equals("Failed")) {
//                        System.out.println("Failed to fetch exchange rate data for " + currentDateString);
//                    } else {
//                        double rate = Double.parseDouble(result);
//                        if (rate > 0) {
//                            rateStrings.add(String.valueOf(rate));
//                            rateFound = true;
//                            break; // Stop fetching rates if a valid rate is found
//                        }
//                    }
//                }
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//
//            // Move to the previous date
//            calendar.add(Calendar.DAY_OF_MONTH, -1);
//        }
//        Double rate = null;
//
//        try {
//            Date currentDate = calendar.getTime();
//            String currentDateString = formatDate(currentDate);
//            String rateString = rateStrings.get(0);
//            rate = Double.parseDouble(rateString);
//
//            if (rateFound) {
//                System.out.println("Rates: " + rateStrings + " for " + currentDateString);
//                // Get the first (and only) string element
//                exchangeRateTv.setText("date: " + currentDateString + ", rate: " + String.valueOf(rate) + " " + currency);
//                // Display or process the rates as needed
//            } else {
//                System.out.println("No valid rates found.");
//                rate = 0.0;
//                exchangeRateTv.setText("invalid rate. " + rate + " " + currency);
//            }
//        } catch (IndexOutOfBoundsException e) {
//            rate = null;
//            exchangeRateTv.setText("invalid rate. " + rate + " " + currency);
//
//        }
//    }
//
//    private class FetchExchangeRateTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... params) {
//            String table = params[0];
//            String currency = params[1];
//            String date = params[2];
//
//            Call<Rate> call = apiService.getExchangeRate(table, currency, date);
//
//            try {
//                Response<Rate> response = call.execute();
//                if (response.isSuccessful() && response.body() != null) {
//                    Rate exchangeRate = response.body();
//                    List<Rate> rates = exchangeRate.getRates();
//                    if (rates.size() >= 1) {
//                        double rate = rates.get(0).getMid();
//                        return String.valueOf(rate);
//                    }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            return "Failed";
//        }
//    }
//
//
//    private Date getDateFromDatePicker(DatePicker datePicker) {
//        int year = datePicker.getYear();
//        int month = datePicker.getMonth();
//        int dayOfMonth = datePicker.getDayOfMonth();
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, dayOfMonth);
//        return calendar.getTime();
//    }
//
//    private String formatDate(Date date) {
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
//        return dateFormat.format(date);
//    }
//
//    private String getSelectedCurrency() {
//        Spinner currencySpinner = findViewById(R.id.currencySpinner);
//        return currencySpinner.getSelectedItem().toString();
//    }
//}
