package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Locations {

    @SerializedName("name")
    @Expose
    final String name;

    @SerializedName("latitude")
    @Expose
    final float latitude;

    @SerializedName("longitude")
    @Expose
    final float longitude;

    @SerializedName("bikesAvailable")
    @Expose
    final int bikesAvailable;

    public Locations(String name,float latitude, float longitude,int bikesAvailable) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.bikesAvailable = bikesAvailable;
    }

    public String getName() {
        return name;
    }

    public float getLatitude() {
        return latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public int getBikesAvailable() {
        return bikesAvailable;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "name='" + name + '\'' +
                ", latitude=" + latitude + '\'' +
                ", longitude=" + longitude + '\'' +
                ", bikesAvailable=" + bikesAvailable + '\'' +
                '}';
    }
}
