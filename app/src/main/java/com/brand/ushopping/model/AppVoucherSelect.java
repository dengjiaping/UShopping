package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/3/1.
 */
public class AppVoucherSelect
{
    private long userId;
    private String sessionid;
    private ArrayList<VoucherItem> voucherItems;
    private int flag;
    private boolean success;
    private String msg;
    private boolean model;

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

    public ArrayList<VoucherItem> getVoucherItems() {
        return voucherItems;
    }

    public void setVoucherItems(ArrayList<VoucherItem> voucherItems) {
        this.voucherItems = voucherItems;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public boolean isModel() {
        return model;
    }

    public void setModel(boolean model) {
        this.model = model;
    }
}
