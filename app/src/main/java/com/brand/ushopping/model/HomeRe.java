package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/9.
 */
public class HomeRe
{
    private long userId;
    private String msg;
    private String sessionid;
    private int min;
    private int max;
    private boolean success;
    private ArrayList<AppgoodsId> appgoodsId;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public ArrayList<AppgoodsId> getAppgoodsId() {
        return appgoodsId;
    }

    public void setAppgoodsId(ArrayList<AppgoodsId> appgoodsId) {
        this.appgoodsId = appgoodsId;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
