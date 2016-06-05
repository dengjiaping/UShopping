package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/6/5.
 */
public class AppOnlineshopping
{
    private Long id;
    private String brannerColor;
    private String brannerFont;
    private String images;
    private Long reTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrannerColor() {
        return brannerColor;
    }

    public void setBrannerColor(String brannerColor) {
        this.brannerColor = brannerColor;
    }

    public String getBrannerFont() {
        return brannerFont;
    }

    public void setBrannerFont(String brannerFont) {
        this.brannerFont = brannerFont;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public Long getReTime() {
        return reTime;
    }

    public void setReTime(Long reTime) {
        this.reTime = reTime;
    }
}
