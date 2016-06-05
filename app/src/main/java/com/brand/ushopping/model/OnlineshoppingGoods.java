package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/6/5.
 * 主题活动商品
 */
public class OnlineshoppingGoods
{
    private String sessionid;
    private Long userId;
    private Integer max;
    private Integer min;
    private boolean success;
    private String msg;
    private AppOnlineshopping appOnlineshopping;
    private ArrayList<AppgoodsId> appgoodsIds;

    public String getSessionid() {
        return sessionid;
    }

    public void setSessionid(String sessionid) {
        this.sessionid = sessionid;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
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

    public AppOnlineshopping getAppOnlineshopping() {
        return appOnlineshopping;
    }

    public void setAppOnlineshopping(AppOnlineshopping appOnlineshopping) {
        this.appOnlineshopping = appOnlineshopping;
    }

    public ArrayList<AppgoodsId> getAppgoodsIds() {
        return appgoodsIds;
    }

    public void setAppgoodsIds(ArrayList<AppgoodsId> appgoodsIds) {
        this.appgoodsIds = appgoodsIds;
    }
}
