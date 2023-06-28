package com.example.as_flexifuel_firebase_2023;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RefuelingViewHolder extends RecyclerView.ViewHolder {
    private TextView vehicleTextView;
    private TextView dateTextView;
    private TextView mileageTextView;
    private TextView fuelTypeTextView;
    private TextView fuelFPTextView;
    private TextView litersTextView;
    private TextView amountTextView;
    private TextView currencyTextView;
    private TextView notesTextView;

    public RefuelingViewHolder(@NonNull View itemView) {
        super(itemView);
        vehicleTextView = itemView.findViewById(R.id.vehicleTextView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        mileageTextView = itemView.findViewById(R.id.mileageTextView);
        fuelTypeTextView = itemView.findViewById(R.id.fuelTypeTextView);
        fuelFPTextView = itemView.findViewById(R.id.fuelFPTextView);
        litersTextView = itemView.findViewById(R.id.litersTextView);
        amountTextView = itemView.findViewById(R.id.amountTextView);
        currencyTextView = itemView.findViewById(R.id.currencyTextView);
        notesTextView = itemView.findViewById(R.id.notesTextView);


    }

    public void bind(Refueling refueling) {
        vehicleTextView.setText(refueling.getVehicle());
        dateTextView.setText(refueling.getDate());
        mileageTextView.setText(refueling.getMileage());

        fuelTypeTextView.setText(refueling.getFuelType().name());
        fuelFPTextView.setText(refueling.getFuelFP().name());
        litersTextView.setText(refueling.getLiters());
        amountTextView.setText(refueling.getAmount());
        currencyTextView.setText(refueling.getCurrency().name());
        notesTextView.setText(refueling.getNotes());
    }

    public void setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
    }
}


