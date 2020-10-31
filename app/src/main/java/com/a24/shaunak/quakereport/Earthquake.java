package com.a24.shaunak.quakereport;

/**
 * Created by Shaunak on 28-05-2018.
 */

public class Earthquake {

    private double mMagnitude;

    private String mLocation;

    private Long mTimeInMilliseconds;

    private String mUrl;

    public Earthquake(double Magnitude, String Location, Long timeInMilliseconds , String url) {
        this.mMagnitude = Magnitude;
        this.mLocation = Location;
        this.mTimeInMilliseconds = timeInMilliseconds;
        this.mUrl = url;
    }

    public double getMagnitude() {
        return  mMagnitude;
    }

    public String getLocation() {
        return  mLocation;
    }
    public Long getTimeInMilliseconds() {
        return  mTimeInMilliseconds;
    }
    public String getmUrl() {
        return  mUrl;
    }

}
