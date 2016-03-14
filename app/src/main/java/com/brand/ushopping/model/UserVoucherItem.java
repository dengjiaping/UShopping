package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/3/8.
 */
public class UserVoucherItem {
    private AppvoucherId appvoucherId;
    private long days;
    private int flag;
    private long id;
    private AppuserId appuserId;

    public AppvoucherId getAppvoucherId() {
        return appvoucherId;
    }

    public void setAppvoucherId(AppvoucherId appvoucherId) {
        this.appvoucherId = appvoucherId;
    }

    public long getDays() {
        return days;
    }

    public void setDays(long days) {
        this.days = days;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }
}
