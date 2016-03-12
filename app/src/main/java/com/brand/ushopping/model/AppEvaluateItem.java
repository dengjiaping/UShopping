package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/1/18.
 */
public class AppEvaluateItem
{
    private long id;
    private AppgoodsId appgoodsId;
    private AppuserId appuserId;
    private String attribute;
    private String content;
    private long reTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AppgoodsId getAppgoodsId() {
        return appgoodsId;
    }

    public void setAppgoodsId(AppgoodsId appgoodsId) {
        this.appgoodsId = appgoodsId;
    }

    public AppuserId getAppuserId() {
        return appuserId;
    }

    public void setAppuserId(AppuserId appuserId) {
        this.appuserId = appuserId;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getReTime() {
        return reTime;
    }

    public void setReTime(long reTime) {
        this.reTime = reTime;
    }
}
