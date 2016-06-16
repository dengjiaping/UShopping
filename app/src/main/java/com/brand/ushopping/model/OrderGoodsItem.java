package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/29.
 */
public class OrderGoodsItem
{
    private AppaddressId appaddressId;
    private AppgoodsId appgoodsId;
    private AppuserId appuserId;
    private AppushopId appushopId;
    private String attribute = "";
    private int flag;
    private long id;
    private double money;
    private String orderNo;
    private int quantity;

    private int customerFlag = 0;
    private String customerContent = "";
    private long customerStartTime = 0;
    private long customerEndTime = 0;

    public AppaddressId getAppaddressId() {
        return appaddressId;
    }

    public void setAppaddressId(AppaddressId appaddressId) {
        this.appaddressId = appaddressId;
    }

    public AppgoodsId getAppgoodsId() {
        return appgoodsId;
    }

    public void setAppgoodsId(AppgoodsId appgoodsId) {
        this.appgoodsId = appgoodsId;
    }

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public AppushopId getAppushopId() {
        return appushopId;
    }

    public void setAppushopId(AppushopId appushopId) {
        this.appushopId = appushopId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getCustomerFlag() {
        return customerFlag;
    }

    public void setCustomerFlag(int customerFlag) {
        this.customerFlag = customerFlag;
    }

    public String getCustomerContent() {
        return customerContent;
    }

    public void setCustomerContent(String customerContent) {
        this.customerContent = customerContent;
    }

    public long getCustomerStartTime() {
        return customerStartTime;
    }

    public void setCustomerStartTime(long customerStartTime) {
        this.customerStartTime = customerStartTime;
    }

    public long getCustomerEndTime() {
        return customerEndTime;
    }

    public void setCustomerEndTime(long customerEndTime) {
        this.customerEndTime = customerEndTime;
    }
}
