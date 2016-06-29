package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/11/9.
 */
public class AppgoodsId extends BaseModel
{
    private String barCode;
    private String goodsName;
    private long id;
    private String logopicUrl;
    private double promotionPrice;
    private AppbrandId appbrandId;
    private Long reTime;
    private AppexpressId appexpressId;
    private int saleCount;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogopicUrl() {
        return logopicUrl;
    }

    public void setLogopicUrl(String logopicUrl) {
        this.logopicUrl = logopicUrl;
    }

    public double getPromotionPrice() {
        return promotionPrice;
    }

    public void setPromotionPrice(double promotionPrice) {
        this.promotionPrice = promotionPrice;
    }

    public AppbrandId getAppbrandId() {
        return appbrandId;
    }

    public void setAppbrandId(AppbrandId appbrandId) {
        this.appbrandId = appbrandId;
    }

    public long getReTime() {
        return reTime;
    }

    public void setReTime(long reTime) {
        this.reTime = reTime;
    }

    public AppexpressId getAppexpressId() {
        return appexpressId;
    }

    public void setAppexpressId(AppexpressId appexpressId) {
        this.appexpressId = appexpressId;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }
}
