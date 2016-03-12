package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/7.
 */
public class AppShopcartIdList
{
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private ArrayList<AppShopcartBrand> appShopcartBrands;
    private long shopcartId;

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
}
