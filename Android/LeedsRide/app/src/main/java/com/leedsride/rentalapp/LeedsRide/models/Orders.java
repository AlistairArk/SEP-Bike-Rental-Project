package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Orders {

    @SerializedName("id")
    @Expose
    final String id;

    @SerializedName("bikeNumber")
    @Expose
    final int bikeNumber;

    @SerializedName("location")
    @Expose
    final String location;

    @SerializedName("StartDate")
    @Expose
    final String startDate;

    @SerializedName("endDate")
    @Expose
    final String endDate;

    @SerializedName("collected")
    @Expose
    final Boolean collected;

    @SerializedName("returned")
    @Expose
    final Boolean returned;

    public Orders(String id, int bikeNumber, String location, String startDate, String endDate, Boolean collected, Boolean returned) {
        this.id = id;
        this.bikeNumber = bikeNumber;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.collected = collected;
        this.returned = returned;
    }

    public String getId() {
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

    public Boolean getCollected() {
        return collected;
    }

    public Boolean getReturned() {
        return returned;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", bikeNumber=" + bikeNumber +
                ", location=" + location +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", collected=" + collected +
                ", returned=" + returned +
                '}';
    }
}
