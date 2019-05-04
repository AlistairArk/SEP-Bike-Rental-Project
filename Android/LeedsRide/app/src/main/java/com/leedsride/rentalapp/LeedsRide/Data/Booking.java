package com.leedsride.rentalapp.LeedsRide.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Booking implements Parcelable {
    private String bookingLocation;
    private String bookingID;
    private String userID;
    private int bikeQuantity;
    private float orderPrice;
    private Boolean isPaid;
    private String paymentId;

    public Booking() {
    }

    protected Booking(Parcel in) {
        bookingLocation = in.readString();
        bookingID = in.readString();
        userID = in.readString();
        bikeQuantity = in.readInt();
        orderPrice = in.readFloat();
        byte tmpIsPaid = in.readByte();
        isPaid = tmpIsPaid == 0 ? null : tmpIsPaid == 1;
        paymentId = in.readString();
    }

    public static final Creator<Booking> CREATOR = new Creator<Booking>() {
        @Override
        public Booking createFromParcel(Parcel in) {
            return new Booking(in);
        }

        @Override
        public Booking[] newArray(int size) {
            return new Booking[size];
        }
    };

    public String getBookingLocation() {
        return bookingLocation;
    }

    public void setBookingLocation(String bookingLocation) {
        this.bookingLocation = bookingLocation;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getBikeQuantity() {
        return bikeQuantity;
    }

    public void setBikeQuantity(int bikeQuantity) {
        this.bikeQuantity = bikeQuantity;
    }

    public float getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(float orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bookingLocation);
        dest.writeString(bookingID);
        dest.writeString(userID);
        dest.writeInt(bikeQuantity);
        dest.writeFloat(orderPrice);
        dest.writeByte((byte) (isPaid == null ? 0 : isPaid ? 1 : 2));
        dest.writeString(paymentId);
    }
}
