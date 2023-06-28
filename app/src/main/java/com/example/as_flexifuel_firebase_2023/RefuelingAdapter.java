package com.example.as_flexifuel_firebase_2023;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RefuelingAdapter extends RecyclerView.Adapter<RefuelingAdapter.RefuelingViewHolder> {

    private List<Refueling> refuelings;
    private OnItemClickListener listener;

    public RefuelingAdapter(List<Refueling> refuelings) {
        this.refuelings = refuelings;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public RefuelingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_refueling, parent, false);
        return new RefuelingViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RefuelingViewHolder holder, int position) {
        Refueling refueling = refuelings.get(position);
        holder.bind(refueling);
    }

    @Override
    public int getItemCount() {
        return refuelings.size();
    }

    public class RefuelingViewHolder extends RecyclerView.ViewHolder {
        private TextView vehicleTextView;
        private TextView dateTextView;
        private TextView fuelTypeTextView;
        private TextView fuelFPTextView;
        private TextView litersTextView;
        private TextView amountTextView;
        private TextView currencyTextView;
        private TextView notesTextView;

        public RefuelingViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            vehicleTextView = itemView.findViewById(R.id.vehicleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            fuelTypeTextView = itemView.findViewById(R.id.fuelTypeTextView);
            fuelFPTextView = itemView.findViewById(R.id.fuelFPTextView);
            litersTextView = itemView.findViewById(R.id.litersTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            currencyTextView = itemView.findViewById(R.id.currencyTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);
                    }
                }
            });
        }

        public void bind(Refueling refueling) {
            if (refueling.getVehicle() != null) {
                vehicleTextView.setText(refueling.getVehicle());
            } else {
                vehicleTextView.setText("");
            }

            if (refueling.getDate() != null) {
                dateTextView.setText(refueling.getDate());
            } else {
                dateTextView.setText("");
            }

            FuelType fuelType = refueling.getFuelType();
            if (fuelType != null) {
                fuelTypeTextView.setText(fuelType.name());
            } else {
                fuelTypeTextView.setText("");
            }

            FuelFP fuelFP = refueling.getFuelFP();
            if (fuelFP != null) {
                fuelFPTextView.setText(fuelFP.name());
            } else {
                fuelFPTextView.setText("");
            }
            if (refueling.getLiters() != null) {
                litersTextView.setText(refueling.getLiters());
            } else {
                litersTextView.setText("");
            }
            if (refueling.getAmount() != null) {
                amountTextView.setText(refueling.getAmount());
            } else {
                amountTextView.setText("");
            }
            Currency currency = refueling.getCurrency();
            if (currency != null) {
                currencyTextView.setText(currency.name());
            } else {
                currencyTextView.setText("");
            }
            if (refueling.getNotes() != null) {
                notesTextView.setText(refueling.getNotes());
            } else {
                notesTextView.setText("");
            }

        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
