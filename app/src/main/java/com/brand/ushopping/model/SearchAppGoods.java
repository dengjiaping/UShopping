package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/16.
 */
public class SearchAppGoods
{
    private long userId;
    private String sessionid;
    private String goodsName;
    private String barCode;
    private int min;
    private int max;
    private boolean success;
    private String msg;
    private ArrayList<Goods> goodses;

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

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
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

    public ArrayList<Goods> getGoodses() {
        return goodses;
    }

    public void setGoodses(ArrayList<Goods> goodses) {
        this.goodses = goodses;
    }

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }
}
