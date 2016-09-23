package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/9/22.
 */

public class ShopcartDiscountItem {
    private AppbrandId appbrandId;
    private AppchinaId appchinaId;
    private int count;
    private double money;
    private Long id;

    public AppbrandId getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(AppbrandId appbrandId) {
        this.appbrandId = appbrandId;
    }

    public AppchinaId getAppchinaId() {
        return appchinaId;
    }

    public void setAppchinaId(AppchinaId appchinaId) {
        this.appchinaId = appchinaId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
