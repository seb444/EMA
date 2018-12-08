package com.example.seb.ema.fragmentpagerefresh;

public class stringMLatLng {
    public String getUid() {
        return uid;
    }

    public com.example.seb.ema.fragmentpagerefresh.mLatLng getmLatLng() {
        return mLatLng;
    }

    String uid;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setmLatLng(com.example.seb.ema.fragmentpagerefresh.mLatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    mLatLng mLatLng;


    @Override
    public String toString() {
        return mLatLng.getLongitude()+mLatLng.getLatitude()+uid;
    }
    public stringMLatLng(){}
}
