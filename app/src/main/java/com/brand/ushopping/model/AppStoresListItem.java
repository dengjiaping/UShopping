package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/23.
 */
public class AppStoresListItem {
    private long id;
    private String shopName;
    private String shopAddr;
    private String logopicUrl;
    private String shopTele;
    private double latitude;
    private double longitude;
    private int flag;   //0 支持预订 1 不支持
    private int door;    // 支持上门 0 支持 1 不支持
    private AppbrandId appbrandId;
    private String businessHours;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAddr() {
        return shopAddr;
    }

    public void setShopAddr(String shopAddr) {
        this.shopAddr = shopAddr;
    }

    public String getLogopicUrl() {
        return logopicUrl;
    }

    public void setLogopicUrl(String logopicUrl) {
        this.logopicUrl = logopicUrl;
    }

    public String getShopTele() {
        return shopTele;
    }

    public void setShopTele(String shopTele) {
        this.shopTele = shopTele;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getDoor() {
        return door;
    }

    public void setDoor(int door) {
        this.door = door;
    }

    public AppbrandId getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(AppbrandId appbrandId) {
        this.appbrandId = appbrandId;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }
}
