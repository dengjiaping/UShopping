package com.brand.ushopping.model;

import android.content.Context;

import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

/**
 * Created by Administrator on 2016/6/24.
 */
public class BaseModel {
    private String android_version;
    private Boolean model = StaticValues.MODEL_ANDROID;

    public String getAndroid_version() {
        return android_version;
    }

    public void setAndroid_version(String android_version) {
        this.android_version = android_version;
    }

    public Boolean getModel() {
        return model;
    }

    public void setModel(Boolean model) {
        this.model = model;
    }

    public void addVersion(Context context)
    {
        android_version = "" + CommonUtils.getVersion(context);
    }

}
