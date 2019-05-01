package com.leedsride.rentalapp.LeedsRide;

public class MyOrdersRecycler {
    private String itemHeader;
    private String itemInfo;
    private String itemOrderType;
    private Boolean itemIsHeader;

    public MyOrdersRecycler(String header, String info, String orderType , Boolean isHeader) {
        itemHeader = header;
        itemInfo = info;
        itemOrderType = orderType;
        itemIsHeader = isHeader;

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

}
