package com.brand.ushopping.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/9/22.
 */

public class ShopcartDiscount {
    private HashMap<String, ArrayList<ShopcartDiscountItem>> shopcartDiscountItems;

    public HashMap<String, ArrayList<ShopcartDiscountItem>> getShopcartDiscountItems() {
        return shopcartDiscountItems;
    }

    public void setShopcartDiscountItems(HashMap<String, ArrayList<ShopcartDiscountItem>> shopcartDiscountItems) {
        this.shopcartDiscountItems = shopcartDiscountItems;
    }
}
