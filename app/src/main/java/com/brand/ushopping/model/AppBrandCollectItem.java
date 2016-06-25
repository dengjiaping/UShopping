package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/1/17.
 */
public class AppBrandCollectItem extends BaseModel
{
    private Brand appbrandId;
    private long reTime;

    public Brand getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(Brand appbrandId) {
        this.appbrandId = appbrandId;
    }

    public long getReTime() {
        return reTime;
    }

    public void setReTime(long reTime) {
        this.reTime = reTime;
    }
}
