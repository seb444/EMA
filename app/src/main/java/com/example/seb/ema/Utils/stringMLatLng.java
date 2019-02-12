package com.example.seb.ema.Utils;

public class stringMLatLng {
    private String uid;
    private mLatLng mLatLng;

    //Important for recivieng data from firebase

    public stringMLatLng(){}

    public com.example.seb.ema.Utils.mLatLng getmLatLng() {
        return mLatLng;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public void setmLatLng(com.example.seb.ema.Utils.mLatLng mLatLng) {
        this.mLatLng = mLatLng;
    }


    @Override
    public String toString() {
        return mLatLng.getLongitude()+mLatLng.getLatitude()+uid;
    }
}
