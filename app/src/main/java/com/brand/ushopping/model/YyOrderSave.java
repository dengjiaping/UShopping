package com.brand.ushopping.model;

import com.brand.ushopping.utils.StaticValues;

import java.util.Date;

/**
 * Created by Administrator on 2015/12/25.
 */
public class YyOrderSave extends BaseModel
{
    private double money;
    private int paymentMode;
    private int flag;
    private AppuserId appuserId;
    private AppaddressId appaddressId;
    private String remark;
    private Date timeShop;
    private AppgoodsId appgoodsId;
    private String attribute;
    private int quantity;
    private String userVoucherId;
    private Long appVoucherId;
    private Integer client = StaticValues.ORDER_SAVE_PLATFORM;
    private Long firstAppsmorderId;
    private AppStoresId appStoresId;

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public AppaddressId getAppaddressId() {
        return appaddressId;
    }

    public void setAppaddressId(AppaddressId appaddressId) {
        this.appaddressId = appaddressId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public AppgoodsId getAppgoodsId() {
        return appgoodsId;
    }

    public void setAppgoodsId(AppgoodsId appgoodsId) {
        this.appgoodsId = appgoodsId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Date getTimeShop() {
        return timeShop;
    }

    public void setTimeShop(Date timeShop) {
        this.timeShop = timeShop;
    }

    public String getUserVoucherId() {
        return userVoucherId;
    }

    public void setUserVoucherId(String userVoucherId) {
        this.userVoucherId = userVoucherId;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Long getAppVoucherId() {
        return appVoucherId;
    }

    public void setAppVoucherId(Long appVoucherId) {
        this.appVoucherId = appVoucherId;
    }

    public Long getFirstAppsmorderId() {
        return firstAppsmorderId;
    }

    public void setFirstAppsmorderId(Long firstAppsmorderId) {
        this.firstAppsmorderId = firstAppsmorderId;
    }

    public AppStoresId getAppStoresId() {
        return appStoresId;
    }

    public void setAppStoresId(AppStoresId appStoresId) {
        this.appStoresId = appStoresId;
    }
}
