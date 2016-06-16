package com.brand.ushopping.model;

import java.util.ArrayList;

/**
 * Created by Administrator on 2015/12/9.
 */
public class AppShopcartBrand
{
    private long id;
    private String brandName;
    private String logopic;
    private ArrayList<AppShopcart> appShopcarts;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public ArrayList<AppShopcart> getAppShopcarts() {
        return appShopcarts;
    }

    public void setAppShopcarts(ArrayList<AppShopcart> appShopcarts) {
        this.appShopcarts = appShopcarts;
    }

    public String getLogopic() {
        return logopic;
    }

    public void setLogopic(String logopic) {
        this.logopic = logopic;
    }
}
