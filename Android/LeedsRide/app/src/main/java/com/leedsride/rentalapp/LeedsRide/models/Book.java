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
    private int bikeNumber;
    @SerializedName("rentalLength")
    @Expose
    private int rentalLength;
    @SerializedName("rentalDate")
    @Expose
    private String rentalDate;
    @SerializedName("rentalTime")
    @Expose
    private String rentalTime;
    @SerializedName("cost")
    @Expose
    private int cost;
    @SerializedName("bookingStatus")
    @Expose
    private String bookingStatus;

    public void setRequestType(String requestType) {
        this.requestType = requestType;
    }

    public void setBikeNumber(int bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public void setRentalLength(int rentalLength) {
        this.rentalLength = rentalLength;
    }

    public void setRentalDate(String rentalDate) {
        this.rentalDate = rentalDate;
    }

    public void setRentalTime(String rentalTime) {
        this.rentalTime = rentalTime;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    @Override
    public String toString() {
        return "Book{" +
                "requestType='" + requestType + '\'' +
                ", bikeNumber=" + bikeNumber +
                ", rentalLength=" + rentalLength +
                ", rentalDate='" + rentalDate + '\'' +
                ", rentalTime='" + rentalTime + '\'' +
                ", bookingStatus='" + bookingStatus + '\'' +
                '}';
    }
}
