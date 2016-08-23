package com.brand.ushopping.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/11/19.
 */
public class GoodsInfo extends BaseModel
{
    private HashMap<String, String[]> attribute;
    private Goods goods;
    private long userId;
    private String sessionid;
    private boolean success;
    private String msg;
    private long goodsId;
    private String area;

    public HashMap<String, String[]> getAttribute() {
        return attribute;
    }

    public void setAttribute(HashMap<String, String[]> attribute) {
        this.attribute = attribute;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
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

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
