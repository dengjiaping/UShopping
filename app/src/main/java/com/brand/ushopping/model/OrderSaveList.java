package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/19.
 */
public class OrderSaveList
{
    private ArrayList<OrderSave> order;
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;

    public ArrayList<OrderSave> getOrder() {
        return order;
    }

    public void setOrder(ArrayList<OrderSave> order) {
        this.order = order;
    }

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

}
