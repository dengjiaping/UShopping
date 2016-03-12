package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/3/8.
 */
public class UserVoucherItem {
    private AppvoucherId appvoucherId;
    private long days;
    private int flag;

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
}
