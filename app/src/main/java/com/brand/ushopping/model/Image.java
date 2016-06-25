package com.brand.ushopping.model;

/**
 * Created by Administrator on 2016/5/10.
 */
public class Image extends BaseModel
{
    private String url;
    private String msg;
    private boolean uploadSuccess;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isUploadSuccess() {
        return uploadSuccess;
    }

    public void setUploadSuccess(boolean uploadSuccess) {
        this.uploadSuccess = uploadSuccess;
    }
}
