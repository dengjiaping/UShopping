package com.brand.ushopping.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/7.
 */
public class AppShopcartIdList extends BaseModel
{
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private ArrayList<AppShopcartBrand> appShopcartBrands;
    private long shopcartId;
    private String area;
    private HashMap<String, ArrayList<ShopcartDiscountItem>> shopcartDiscountItems;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<AppShopcartBrand> getAppShopcartBrands() {
        return appShopcartBrands;
    }

    public void setAppShopcartBrands(ArrayList<AppShopcartBrand> appShopcartBrands) {
        this.appShopcartBrands = appShopcartBrands;
    }

    public long getShopcartId() {
        return shopcartId;
    }

    public void setShopcartId(long shopcartId) {
        this.shopcartId = shopcartId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public HashMap<String, ArrayList<ShopcartDiscountItem>> getShopcartDiscountItems() {
        return shopcartDiscountItems;
    }

    public void setShopcartDiscountItems(HashMap<String, ArrayList<ShopcartDiscountItem>> shopcartDiscountItems) {
        this.shopcartDiscountItems = shopcartDiscountItems;
    }
}
