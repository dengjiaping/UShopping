package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/23.
 */
public class AppStoresList extends BaseModel
{
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private String city;
    private ArrayList<AppStoresListItem> appStoresListItems;

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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<AppStoresListItem> getAppStoresListItems() {
        return appStoresListItems;
    }

    public void setAppStoresListItems(ArrayList<AppStoresListItem> appStoresListItems) {
        this.appStoresListItems = appStoresListItems;
    }
}
