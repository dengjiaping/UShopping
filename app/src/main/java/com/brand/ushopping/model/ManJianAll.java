package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/7/1.
 */
public class ManJianAll extends BaseModel
{
    private long userId;
    private String sessionid;
    private int appOrderType;
    private boolean success;
    private String msg;
    private double money;

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

    public int getAppOrderType() {
        return appOrderType;
    }

    public void setAppOrderType(int appOrderType) {
        this.appOrderType = appOrderType;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }
}
