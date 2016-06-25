package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/30.
 */
public class SmOrderSaveList extends BaseModel
{
    private ArrayList<SmOrderSave> smorder;
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;

    public ArrayList<SmOrderSave> getSmorder() {
        return smorder;
    }

    public void setSmorder(ArrayList<SmOrderSave> smorder) {
        this.smorder = smorder;
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
