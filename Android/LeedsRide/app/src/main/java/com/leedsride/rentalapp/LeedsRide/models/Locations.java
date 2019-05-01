package com.leedsride.rentalapp.LeedsRide.models;

//import com.google.gson.annotations.Expose;
//import com.google.gson.annotations.SerializedName;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Locations {

    @SerializedName("RESPONSE")
    @Expose
    final String name;

    public Locations(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
