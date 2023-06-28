package com.example.as_flexifuel_firebase_2023;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RefuelingViewHolder extends RecyclerView.ViewHolder {
    private TextView vehicleTextView;
    private TextView dateTextView;
    private TextView fuelTypeTextView;
    private TextView fuelFPTextView;
    private TextView litersTextView;
    private TextView amountTextView;
    private TextView currencyTextView;
    public RefuelingViewHolder(@NonNull View itemView) {
        super(itemView);
        vehicleTextView = itemView.findViewById(R.id.vehicleTextView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        fuelTypeTextView = itemView.findViewById(R.id.fuelTypeTextView);
        fuelFPTextView = itemView.findViewById(R.id.fuelFPTextView);
        litersTextView = itemView.findViewById(R.id.litersTextView);
        amountTextView = itemView.findViewById(R.id.amountTextView);
        currencyTextView = itemView.findViewById(R.id.currencyTextView);


    }

    public void bind(Refueling refueling) {
        vehicleTextView.setText(refueling.getVehicle());
        dateTextView.setText(refueling.getDate());
        fuelTypeTextView.setText(refueling.getFuelType().name());
        fuelFPTextView.setText(refueling.getFuelFP().name());
        litersTextView.setText(refueling.getLiters());
        amountTextView.setText(refueling.getAmount());
        currencyTextView.setText(refueling.getCurrency().name());

    }

    public void setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }
}


