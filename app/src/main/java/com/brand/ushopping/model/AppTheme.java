package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/20.
 */
public class AppTheme {
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private ArrayList<AppThemeItem> appThemeItems;

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

    public ArrayList<AppThemeItem> getAppThemeItems() {
        return appThemeItems;
    }

    public void setAppThemeItems(ArrayList<AppThemeItem> appThemeItems) {
        this.appThemeItems = appThemeItems;
    }
}
