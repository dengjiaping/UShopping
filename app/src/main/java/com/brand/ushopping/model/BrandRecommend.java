package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/16.
 * 主页 品牌推荐
 */
public class BrandRecommend
{
    private long userId;
    private String sessionid;
    private ArrayList<Brand> appBrandAll;
    private ArrayList<Brand> recommend;
    private boolean success;
    private String msg;

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

    public ArrayList<Brand> getAppBrandAll() {
        return appBrandAll;
    }

    public void setAppBrandAll(ArrayList<Brand> appBrandAll) {
        this.appBrandAll = appBrandAll;
    }

    public ArrayList<Brand> getRecommend() {
        return recommend;
    }

    public void setRecommend(ArrayList<Brand> recommend) {
        this.recommend = recommend;
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
