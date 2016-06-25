package com.brand.ushopping.model;

/**
 * Created by Administrator on 2015/11/9.
 */
public class Category extends BaseModel
{
    long id;
    String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
