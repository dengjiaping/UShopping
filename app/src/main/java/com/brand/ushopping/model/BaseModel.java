package com.brand.ushopping.model;

import android.content.Context;

import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

/**
 * Created by Administrator on 2016/6/24.
 */
public class BaseModel {
    private String version;
    private Boolean model = StaticValues.MODEL_ANDROID;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Boolean getModel() {
        return model;
    }

    public void setModel(Boolean model) {
        this.model = model;
    }

    public void addVersion(Context context)
    {
        version = CommonUtils.getVersion(context);

    }

}
