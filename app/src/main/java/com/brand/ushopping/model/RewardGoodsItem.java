package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/22.
 */
public class RewardGoodsItem
{
    private long id;
    private int flag;
    private String name;
    private String image;
    private int integral;
    private int amount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
