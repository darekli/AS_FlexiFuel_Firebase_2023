package com.example.as_flexifuel_firebase_2023.modelFuelConsStats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.as_flexifuel_firebase_2023.R;

import java.util.List;

public class AdapterGaugeFuel extends RecyclerView.Adapter<AdapterGaugeFuel.ImageViewHolder> {


    private List<ModelGaugeFuel> modelGaugeFuels;
    //private ArrayList<ModelESE> modelESEArrayList;
    private ViewPager2 viewPager2;


    public AdapterGaugeFuel(List<ModelGaugeFuel> modelGaugeFuels, ViewPager2 viewPager2) {
        this.modelGaugeFuels = modelGaugeFuels;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gauge_fuel_l, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.tvFuelValue.setText(modelGaugeFuels.get(position).getValue());

        if (position == modelGaugeFuels.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return modelGaugeFuels.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        TextView tvFuelValue;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFuelValue = (TextView) itemView.findViewById(R.id.gauge_fuel_r);
        }
    }
    //responsible for sliding images in loop

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            modelGaugeFuels.addAll(modelGaugeFuels);
            notifyDataSetChanged();
        }
    };
}
