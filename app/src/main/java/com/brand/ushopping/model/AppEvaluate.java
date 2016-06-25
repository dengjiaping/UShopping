package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/18.
 */
public class AppEvaluate extends BaseModel {
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private long goodsId;
    private ArrayList<AppEvaluateItem> appEvaluateItems;

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

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public ArrayList<AppEvaluateItem> getAppEvaluateItems() {
        return appEvaluateItems;
    }

    public void setAppEvaluateItems(ArrayList<AppEvaluateItem> appEvaluateItems) {
        this.appEvaluateItems = appEvaluateItems;
    }
}
