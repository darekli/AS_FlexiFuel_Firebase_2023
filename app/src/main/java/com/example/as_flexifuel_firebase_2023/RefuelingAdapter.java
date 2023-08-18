package com.example.as_flexifuel_firebase_2023;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.as_flexifuel_firebase_2023.enums.Country;
import com.example.as_flexifuel_firebase_2023.enums.Currency;
import com.example.as_flexifuel_firebase_2023.enums.FuelFP;
import com.example.as_flexifuel_firebase_2023.enums.FuelType;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_refuel_md3, parent, false);
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
        private TextView iDTextView;
        private TextView vehicleTextView;
        private TextView dateTextView;
        private TextView mileageTextView;
        private TextView fuelTypeTextView;
        private TextView fuelFPTextView;
        private TextView litersTextView;
        private TextView amountTextView;
        private TextView countryTextView;
        private TextView currencyTextView;
        private TextView currencyRateTextView;

        private TextView timeWornTextView;
        private TextView notesTextView;
        private TextView poiTextView;
        private TextView latTextView;
        private TextView lngTextView;
        public RefuelingViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            iDTextView = itemView.findViewById(R.id.idTextView);
            vehicleTextView = itemView.findViewById(R.id.vehicleTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            mileageTextView = itemView.findViewById(R.id.mileageTextView);
            fuelTypeTextView = itemView.findViewById(R.id.fuelTypeTextView);
            fuelFPTextView = itemView.findViewById(R.id.fuelFPTextView);
            litersTextView = itemView.findViewById(R.id.litersTextView);
            amountTextView = itemView.findViewById(R.id.amountTextView);
            countryTextView = itemView.findViewById(R.id.countryTextView);
            currencyTextView = itemView.findViewById(R.id.currencyTextView);
            currencyRateTextView = itemView.findViewById(R.id.currencyRateTextView);
            timeWornTextView = itemView.findViewById(R.id.timeWornTextView);

            timeWornTextView = itemView.findViewById(R.id.timeWornTextView);
            notesTextView = itemView.findViewById(R.id.notesTextView);
            poiTextView = itemView.findViewById(R.id.poiTextView);
            latTextView = itemView.findViewById(R.id.latTextView);
            lngTextView = itemView.findViewById(R.id.lngTextView);
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
            if (refueling.getId() != null) {
                iDTextView.setText(refueling.getId());
            } else {
                vehicleTextView.setText("");
            }
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
            if (refueling.getMileage() != null) {
                mileageTextView.setText(refueling.getMileage());
            } else {
                mileageTextView.setText("");
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
            Country country = refueling.getCountry();
            if (country != null) {
                countryTextView.setText(country.name());
            } else {
                countryTextView.setText("");
            }
            Currency currency = refueling.getCurrency();
            if (currency != null) {
                currencyTextView.setText(currency.name());
            } else {
                currencyTextView.setText("");
            }
            if (refueling.getCurrencyRate() != null) {
                currencyRateTextView.setText(refueling.getCurrencyRate());
            } else {
                currencyRateTextView.setText("");
            }
            if (refueling.getTimeworn() != null) {
                timeWornTextView.setText(refueling.getTimeworn());
            } else {
                timeWornTextView.setText("");
            }
            if (refueling.getNotes() != null) {
                notesTextView.setText(refueling.getNotes());
            } else {
                notesTextView.setText("");
            }
            if (refueling.getPoi() != null) {
                poiTextView.setText(refueling.getPoi());
            } else {
                poiTextView.setText("");
            }
            if (refueling.getLat() != null) {
                latTextView.setText(refueling.getLat());
            } else {
                latTextView.setText("");
            }
            if (refueling.getLng() != null) {
                lngTextView.setText(refueling.getLng());
            } else {
                lngTextView.setText("");
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
