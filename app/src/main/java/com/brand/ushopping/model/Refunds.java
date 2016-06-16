package com.brand.ushopping.model;

import java.util.HashMap;

/**
 * Created by Administrator on 2015/12/14.
 */
public class Refunds
{
    private Object data;
    private boolean hasMore;
    private String object;
    private String uRL;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public boolean isHasMore() {
        return hasMore;
    }

    public void setHasMore(boolean hasMore) {
        this.hasMore = hasMore;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getuRL() {
        return uRL;
    }

    public void setuRL(String uRL) {
        this.uRL = uRL;
    }
}
