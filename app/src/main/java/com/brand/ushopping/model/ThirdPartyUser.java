package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/6/3.
 * 第三方登录
 */
public class ThirdPartyUser {
    private String sinaId;
    private String userName;
    private String gender;
    private Integer flag;
    private Boolean model;
    private String url;

    public String getSinaId() {
        return sinaId;
    }

    public void setSinaId(String sinaId) {
        this.sinaId = sinaId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public Boolean getModel() {
        return model;
    }

    public void setModel(Boolean model) {
        this.model = model;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
