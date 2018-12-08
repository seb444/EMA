package com.example.seb.ema.fragmentpagerefresh;

public class mLatLng {
    private Double latitude;

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private Double longitude;
    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }


    @Override
    public String toString() {
        return latitude+"\n"+longitude+"\n";
    }
}
