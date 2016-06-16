package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/23.
 */
public class BrandGoodsList
{
    private long userId;
    private String sessionid;
    private int min;
    private int max;
    private String appbrandId;
    private boolean success;
    private String msg;
    private ArrayList<AppgoodsId> appgoodsIds;

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

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(String appbrandId) {
        this.appbrandId = appbrandId;
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

    public ArrayList<AppgoodsId> getAppgoodsIds() {
        return appgoodsIds;
    }

    public void setAppgoodsIds(ArrayList<AppgoodsId> appgoodsIds) {
        this.appgoodsIds = appgoodsIds;
    }
}
