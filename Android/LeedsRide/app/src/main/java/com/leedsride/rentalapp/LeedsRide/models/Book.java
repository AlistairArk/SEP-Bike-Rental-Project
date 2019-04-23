package com.leedsride.rentalapp.LeedsRide.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Book {
    /*********************
            * Declared fields are only used internally,
     * what needs to match the API key names are the Serialized names
     */
    @SerializedName("requestType")
    @Expose
    private String requestType;
    @SerializedName("bikeNumber")
    @Expose
    private String bikeNumber;
    @SerializedName("rentalLength")
    @Expose
    private String rentalLength;
    @SerializedName("rentalDate")
    @Expose
    private String rentalDate;
    @SerializedName("rentalTime")
    @Expose
    private String rentalTime;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;
}
