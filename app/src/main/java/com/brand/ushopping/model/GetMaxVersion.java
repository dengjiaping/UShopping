package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/12/30.
 */
public class GetMaxVersion
{
    private int systemtype = 1; //1 android, 2 ios
    private int versionNumber;

    public int getSystemtype() {
        return systemtype;
    }

    public void setSystemtype(int systemtype) {
        this.systemtype = systemtype;
    }

    public int getVersionNumber() {
        return versionNumber;
    }

    public void setVersionNumber(int versionNumber) {
        this.versionNumber = versionNumber;
    }
}
