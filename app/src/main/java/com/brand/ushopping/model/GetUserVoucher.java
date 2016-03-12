package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/4.
 */
public class GetUserVoucher
{
    private long userId;
    private String sessionid;
    private ArrayList<UserVoucherItem> userVoucherItems;
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

    public ArrayList<UserVoucherItem> getUserVoucherItems() {
        return userVoucherItems;
    }

    public void setUserVoucherItems(ArrayList<UserVoucherItem> userVoucherItems) {
        this.userVoucherItems = userVoucherItems;
    }
}
