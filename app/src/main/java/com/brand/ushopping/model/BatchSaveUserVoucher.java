package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/21.
 */
public class BatchSaveUserVoucher extends BaseModel
{
    private long userId;
    private String sessionid;
    private ArrayList<UserVoucherItem> userVoucher;
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

    public ArrayList<UserVoucherItem> getUserVoucher() {
        return userVoucher;
    }

    public void setUserVoucher(ArrayList<UserVoucherItem> userVoucher) {
        this.userVoucher = userVoucher;
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
