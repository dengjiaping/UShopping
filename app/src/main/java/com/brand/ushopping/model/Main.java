package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/11/9.
 */
public class Main extends BaseModel
{
    private long userId;
    private String sessionid;
    private ArrayList<Activity> activity;
    private ArrayList<Banner> banner;
    private ArrayList<Category> category;
    private ArrayList<Recommend> recommend;
    private Boolean useCache = true;    //是否使用缓存
    private String area;

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

    public ArrayList<Activity> getActivity() {
        return activity;
    }

    public void setActivity(ArrayList<Activity> activity) {
        this.activity = activity;
    }

    public ArrayList<Banner> getBanner() {
        return banner;
    }

    public void setBanner(ArrayList<Banner> banner) {
        this.banner = banner;
    }

    public ArrayList<Category> getCategory() {
        return category;
    }

    public void setCategory(ArrayList<Category> category) {
        this.category = category;
    }

    public ArrayList<Recommend> getRecommend() {
        return recommend;
    }

    public void setRecommend(ArrayList<Recommend> recommend) {
        this.recommend = recommend;
    }

    public Boolean getUseCache() {
        return useCache;
    }

    public void setUseCache(Boolean useCache) {
        this.useCache = useCache;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
