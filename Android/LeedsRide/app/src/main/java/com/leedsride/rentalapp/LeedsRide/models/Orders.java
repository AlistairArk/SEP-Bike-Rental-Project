package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Orders {

    @SerializedName("id")
    @Expose
    final int id;

    @SerializedName("cost")
    @Expose
    final Float cost;

    @SerializedName("bikeNumber")
    @Expose
    final int bikeNumber;

    @SerializedName("location")
    @Expose
    final String location;

    @SerializedName("startDate")
    @Expose
    final String startDate;

    @SerializedName("endDate")
    @Expose
    final String endDate;

    @SerializedName("bikesInUse")
    @Expose
    final Boolean bikesInUse;

    @SerializedName("username")
    @Expose
    final String username;

    @SerializedName("password")
    @Expose
    final String password;

    public Orders(int id, float cost, int bikeNumber, String location, String startDate, String endDate, Boolean bikesInUse, String username, String password) {
        this.id = id;
        this.cost = cost;
        this.bikeNumber = bikeNumber;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.bikesInUse = bikesInUse;
        this.username = username;
        this.password = password;


    }

    public int getId() {
        return id;
    }

    public int getBikeNumber() {
        return bikeNumber;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getStartDate() {
        return startDate;
    }

    public Boolean getBikesInUse() {
        return bikesInUse;
    }

    public float getCost() {
        return cost;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", cost=" + cost +
                ", bikeNumber=" + bikeNumber +
                ", location=" + location +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", bikesInUse=" + bikesInUse +
                ", username=" + username +
                ", password=" + password +
                '}';
    }
}
