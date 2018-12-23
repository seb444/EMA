package com.example.seb.ema.fragmentpagerefresh;

public class stringMLatLng {
    String uid;
    mLatLng mLatLng;

    public stringMLatLng(){}

    public com.example.seb.ema.fragmentpagerefresh.mLatLng getmLatLng() {
        return mLatLng;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setmLatLng(com.example.seb.ema.fragmentpagerefresh.mLatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return mLatLng.getLongitude()+mLatLng.getLatitude()+uid;
    }
}
