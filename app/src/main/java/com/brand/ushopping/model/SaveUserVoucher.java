package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/3/2.
 */
public class SaveUserVoucher
{
    private long userId;
    private String sessionid;
    private AppuserId appuserId;
    private AppvoucherId appvoucherId;
    private int endDays;
    private boolean success;
    private String msg;

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

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public AppvoucherId getAppvoucherId() {
        return appvoucherId;
    }

    public void setAppvoucherId(AppvoucherId appvoucherId) {
        this.appvoucherId = appvoucherId;
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

    public int getEndDays() {
        return endDays;
    }

    public void setEndDays(int endDays) {
        this.endDays = endDays;
    }
}
