package com.example.as_flexifuel_firebase_2023.modelFuelConsStats;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;


import com.example.as_flexifuel_firebase_2023.R;

import java.util.List;

public class AdapterFuelConsStats extends RecyclerView.Adapter<AdapterFuelConsStats.ImageViewHolder> {


    private List<ModelFuelConsStats>modelListESE;
    //private ArrayList<ModelESE> modelESEArrayList;
    private ViewPager2 viewPager2;


    public AdapterFuelConsStats(List<ModelFuelConsStats> modelListESE, ViewPager2 viewPager2) {
        this.modelListESE = modelListESE;
        this.viewPager2 = viewPager2;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fcs_design, parent, false);
        return new ImageViewHolder(view);
    }

//    ImageView imageTv = view.findViewById(R.id.image_ese_id);
//    TextView descTv = view.findViewById(R.id.tv_ese_descr);
//    ImageView imageTv2 = view.findViewById(R.id.imageTv2);
//
//    //get
//    ModelESE modelESE = modelESEArrayList.get(position);
//    int image = modelESE.getImage();
//    String description = modelESE.getDesc();
//    int image2 = modelESE.getImage2();
//
//
//    //set
//        imageTv.setImageResource(image);
//        descTv.setText(description);
//        imageTv2.setImageResource(image2);
@Override
public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
    holder.imageFcsId.setImageResource(modelListESE.get(position).getImage());
    holder.tvFcsValue.setText(modelListESE.get(position).getValue());
    holder.tvFcsDescr.setText(modelListESE.get(position).getDesc());

    // int image2 = modelList.get(position).getImage();

    if (position == modelListESE.size() - 2) {
        viewPager2.post(runnable);
    }
}

    @Override
    public int getItemCount() {
        return modelListESE.size();
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        // RoundedImageView imageView;
        ImageView imageFcsId;
        TextView tvFcsValue;
        TextView tvFcsDescr;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageFcsId = (ImageView) itemView.findViewById(R.id.image_fcs_id);
            tvFcsValue = (TextView) itemView.findViewById(R.id.tv_fcs_value);
            tvFcsDescr = (TextView) itemView.findViewById(R.id.tv_fcs_descr);


        }
    }
    //responsible for sliding images in loop

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {

            modelListESE.addAll(modelListESE);
            notifyDataSetChanged();
        }
    };
}
