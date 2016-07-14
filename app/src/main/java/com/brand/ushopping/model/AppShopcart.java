package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/7.
 */
public class AppShopcart extends BaseModel
{
    private AppStoresId appStoresId;
    private AppgoodsId appgoodsId;
    private String attribute;
    private long id;
    private int quantity;

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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AppStoresId getAppStoresId() {
        return appStoresId;
    }

    public void setAppStoresId(AppStoresId appStoresId) {
        this.appStoresId = appStoresId;
    }
}
