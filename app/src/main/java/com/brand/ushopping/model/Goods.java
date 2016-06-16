package com.brand.ushopping.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2015/11/19.
 */
public class Goods implements Parcelable
{

    private String goodsName;
    private double goodsPrice;
    private long id;
    private double originalPrice;
    private double promotionPrice;
    private String logopicUrl;
//    private String color;
//    private String size;
    private String attribute;
    private int count;
    private AppbrandId appbrandId;
    private AppexpressId appexpressId;
    private String goodsDetail;
    private String goodsNewDetail;
    private String goodsIntro;
    private String images;
    private String barCode;
    private int flag;
    private int saleCount;

    public AppexpressId getAppexpressId() {
        return appexpressId;
    }

    public void setAppexpressId(AppexpressId appexpressId) {
        this.appexpressId = appexpressId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
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

    public String getLogopicUrl() {
        return logopicUrl;
    }

    public void setLogopicUrl(String logopicUrl) {
        this.logopicUrl = logopicUrl;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(String goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public String getGoodsNewDetail() {
        return goodsNewDetail;
    }

    public void setGoodsNewDetail(String goodsNewDetail) {
        this.goodsNewDetail = goodsNewDetail;
    }

    public String getGoodsIntro() {
        return goodsIntro;
    }

    public void setGoodsIntro(String goodsIntro) {
        this.goodsIntro = goodsIntro;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public int getSaleCount() {
        return saleCount;
    }

    public void setSaleCount(int saleCount) {
        this.saleCount = saleCount;
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Goods createFromParcel(Parcel in) {
            return new Goods(in);
        }

        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };

    public Goods(Parcel in){
        readFromParcel(in);
    }

    public Goods()
    {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(goodsName);
        dest.writeDouble(goodsPrice);
        dest.writeLong(id);
        dest.writeDouble(originalPrice);
        dest.writeDouble(promotionPrice);
        dest.writeString(logopicUrl);
        dest.writeString(attribute);
        dest.writeInt(count);
        dest.writeParcelable(appbrandId, flags);
        dest.writeString(goodsDetail);
        dest.writeString(goodsNewDetail);
        dest.writeString(goodsIntro);
        dest.writeString(images);
        dest.writeString(barCode);
        dest.writeInt(flag);
        dest.writeInt(saleCount);

    }

    public void readFromParcel(Parcel in)
    {
        goodsName = in.readString();
        goodsPrice = in.readDouble();
        id = in.readLong();
        originalPrice = in.readDouble();
        promotionPrice = in.readDouble();
        logopicUrl = in.readString();
        attribute = in.readString();
        count = in.readInt();
        appbrandId = in.readParcelable(AppbrandId.class.getClassLoader());
        goodsDetail = in.readString();
        goodsNewDetail = in.readString();
        goodsIntro = in.readString();
        images = in.readString();
        barCode = in.readString();
        flag = in.readInt();
        saleCount = in.readInt();

    }
}
