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
    private Integer flag;
    private boolean success;
    private String msg;
    private Boolean model;

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

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Boolean isModel() {
        return model;
    }

    public void setModel(Boolean model) {
        this.model = model;
    }
}
