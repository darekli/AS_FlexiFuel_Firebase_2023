package com.example.as_flexifuel_firebase_2023.modelFuelConsStats;

public class ModelFuelConsStats {
    int image;
    String value;
    String desc;


    public ModelFuelConsStats() {
    }

    public ModelFuelConsStats(int image, String value, String desc) {
        this.image = image;
        this.value = value;
        this.desc = desc;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
