package com.leedsride.rentalapp.LeedsRide;

public class MyOrdersRecycler {
    private String itemHeader;
    private String itemInfo;
    private String itemOrderType;
    private Boolean itemIsHeader;
    private int itemBikeNumber;
    private int itemBookingID;
    private String itemEndDate;

    public MyOrdersRecycler(String header, String info, String orderType , Boolean isHeader, int bikeNumber, int bookingID, String endDate) {
        itemHeader = header;
        itemInfo = info;
        itemOrderType = orderType;
        itemIsHeader = isHeader;
        itemBikeNumber = bikeNumber;
        itemBookingID = bookingID;
        itemEndDate = endDate;

    }

    public String getItemHeader(){
        return itemHeader;
    }

    public String getItemInfo() {
        return itemInfo;
    }

    public String getItemOrderType(){
        return itemOrderType;
    }

    public  Boolean getItemIsHeader() { return itemIsHeader; }

    public int getItemBikeNumber() {
        return itemBikeNumber;
    }

    public int getItemBookingID() {
        return itemBookingID;
    }

    public String getItemEndDate() {
        return itemEndDate;
    }
}
