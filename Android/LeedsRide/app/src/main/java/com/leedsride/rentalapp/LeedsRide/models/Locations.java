package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Locations {

    @SerializedName("name")
    @Expose
    final String name;

    @SerializedName("location")
    @Expose
    final String location;

    @SerializedName("bikesAvailable")
    @Expose
    final String bikesAvailable;

    public Locations(String name,String location,String bikesAvailable) {
        this.name = name;
        this.location = location;
        this.bikesAvailable = bikesAvailable;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
        return location;
    }

    public String getBikesAvailable() {
        return bikesAvailable;
    }

    @Override
    public String toString() {
        return "Locations{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", bikesAvailable='" + bikesAvailable + '\'' +
                '}';
    }
}
