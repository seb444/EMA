package com.example.seb.ema.Utils;


public class mWeightProgress {
    private Double weight;
    private String date;
    private String note;

    public mWeightProgress(Double weight, String date, String note) {
        this.weight = weight;
        this.date = date;
        this.note = note;
    }
    //Important for Recivieng data from firebase
    public  mWeightProgress(){}

    public Double getWeight() {
        return weight;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getNote() {
        return note;
    }

}