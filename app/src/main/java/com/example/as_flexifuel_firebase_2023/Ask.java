//package com.example.as_flexifuel_firebase_2023;
//
//import android.app.DatePickerDialog;
//import android.content.Intent;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.DatePicker;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.Query;
//import com.google.firebase.database.ValueEventListener;
//
//public class Ask extends AppCompatActivity {
//    private DatePicker askDateDatePicker;
//    private TextView tv_answer_01;
//    private Button buttonAsk;
//    private DatabaseReference databaseRef;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.ask_data);
//        //find the lower mileage then tdate equal...
//
//
//        tv_answer_01 = findViewById(R.id.tv_answer_01);
//        askDateDatePicker = findViewById(R.id.askDateDatePicker);
//
//        buttonAsk = findViewById(R.id.button_ask);
//
//
//        databaseRef = FirebaseDatabase.getInstance().getReference("refuelings");
//
//        buttonAsk.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Wywołaj metodę, która ma zwrócić odpowiedź
////                getLowestMileageByDate("29/06/2023"); // Przykładowa docelowa data
////                Query query = databaseRef.orderByChild("date").equalTo("29/06/2023").limitToFirst(1);
////                //tv_answer_01.setText(String.valueOf(query) + " lower mileage");
//                tv_answer_01.setText("hghjgjgj");
//
//            }
//        });
//    }
//
//    private void getLowestMileageByDate(String targetDate) {
//        Query query = databaseRef.orderByChild("date").equalTo(targetDate).limitToFirst(1);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    // Otrzymano element dla określonej daty
//                    DataSnapshot firstSnapshot = dataSnapshot.getChildren().iterator().next();
//                    Refueling refueling = firstSnapshot.getValue(Refueling.class);
//
//                    // Pobierz najniższy przebieg dla określonej daty
//                    String lowestMileage = refueling.getMileage();
//                    //tv_answer_01.setText(lowestMileage + " lower mileage");
//                    //  tv_answer_01.setText("hjhgjhghjgjgh");
//                    // Wykonaj działania na najniższym przebiegu
//                    // ...
//                } else {
//                    // Brak danych w bazie danych dla określonej daty
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Obsłuż błąd
//            }
//        });
//    }
////    public void initialize() {
////       // setContentView(R.layout.ask_data);
////        tv_answer_01 = findViewById(R.id.tv_answer_01);
////        tv_answer_01.setText("hghjgjgj");
////
////        // Add any additional Ask initialization code here
////    }
//
//}
