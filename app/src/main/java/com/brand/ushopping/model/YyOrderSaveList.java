package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/30.
 */
public class YyOrderSaveList extends BaseModel
{
    private ArrayList<YyOrderSave> yyorder;
    private long userId;
    private String sessionid;

    private boolean success;
    private String msg;

    public YyOrderSaveList() {
    }

    public ArrayList<YyOrderSave> getYyorder() {
        return yyorder;
    }

    public void setYyorder(ArrayList<YyOrderSave> yyorder) {
        this.yyorder = yyorder;
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
