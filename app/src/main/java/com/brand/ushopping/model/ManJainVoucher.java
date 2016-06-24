package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/4/19.
 */
public class ManJainVoucher extends BaseModel
{
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private ArrayList<ManJianVoucherItem> manJianVoucherItems;

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

    public ArrayList<ManJianVoucherItem> getManJianVoucherItems() {
        return manJianVoucherItems;
    }

    public void setManJianVoucherItems(ArrayList<ManJianVoucherItem> manJianVoucherItems) {
        this.manJianVoucherItems = manJianVoucherItems;
    }
}
