package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/21.
 */
public class AppReturngoodsSave extends BaseModel {
    private long userId;
    private String sessionid;
    private String msg;
    private boolean success;
    private ApporderId apporderId;
    private AppsmorderId appsmorderId;
    private AppyyorderId appyyorderId;
    private Integer problem;
    private ArrayList<String> images;
    private ArrayList<String> lastFileName;
    private String explain;

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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ApporderId getApporderId() {
        return apporderId;
    }

    public void setApporderId(ApporderId apporderId) {
        this.apporderId = apporderId;
    }

    public AppsmorderId getAppsmorderId() {
        return appsmorderId;
    }

    public void setAppsmorderId(AppsmorderId appsmorderId) {
        this.appsmorderId = appsmorderId;
    }

    public AppyyorderId getAppyyorderId() {
        return appyyorderId;
    }

    public void setAppyyorderId(AppyyorderId appyyorderId) {
        this.appyyorderId = appyyorderId;
    }

    public Integer getProblem() {
        return problem;
    }

    public void setProblem(Integer problem) {
        this.problem = problem;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getExplain() {
        return explain;
    }

    public void setExplain(String explain) {
        this.explain = explain;
    }

    public ArrayList<String> getLastFileName() {
        return lastFileName;
    }

    public void setLastFileName(ArrayList<String> lastFileName) {
        this.lastFileName = lastFileName;
    }
}
