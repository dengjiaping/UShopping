package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/1/12.
 */
public class AppGoodsTypeId extends BaseModel
{
    private long userId;
    private String sessionid;
    private Long appcategoryId;
    private int min;
    private int max;
    private boolean success;
    private String msg;
    private ArrayList<Goods> goods;
    private Boolean useCache = true;

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

    public Long getAppcategoryId() {
        return appcategoryId;
    }

    public void setAppcategoryId(Long appcategoryId) {
        this.appcategoryId = appcategoryId;
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

    public ArrayList<Goods> getGoods() {
        return goods;
    }

    public void setGoods(ArrayList<Goods> goods) {
        this.goods = goods;
    }

    public Boolean getUseCache() {
        return useCache;
    }

    public void setUseCache(Boolean useCache) {
        this.useCache = useCache;
    }
}
