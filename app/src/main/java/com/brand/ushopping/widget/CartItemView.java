package com.brand.ushopping.widget;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.adapter.CartGoodsAdapter;
import com.brand.ushopping.fragment.CartFragment;
import com.brand.ushopping.model.AppShopcart;
import com.brand.ushopping.model.AppShopcartBrand;
import com.brand.ushopping.model.AppgoodsId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/7.
 */
public class CartItemView extends LinearLayout {
    private CheckBox storeCheckBox;
    private TextView storeNameTextView;
    private ListView goodsListView;
    private ArrayList<AppShopcart> appShopcarts;
    private Activity activity;
    private CartGoodsAdapter adapter;
    private List listData;
    private CartFragment cartFragment;

    public CartItemView(Context context, final CartFragment cartFragment, AttributeSet attrs, AppShopcartBrand appShopcartBrand) {
        super(context, attrs);
        this.activity = (Activity) context;
        this.cartFragment = cartFragment;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cart_item, this, true);

        storeCheckBox = (CheckBox) view.findViewById(R.id.store_check);
        storeCheckBox.setChecked(true);
        storeNameTextView = (TextView) view.findViewById(R.id.store_name);
        goodsListView = (ListView) view.findViewById(R.id.goods_list);

        storeNameTextView.setText(appShopcartBrand.getBrandName());

        appShopcarts = appShopcartBrand.getAppShopcarts();

        listData = new ArrayList<Map<String,Object>>();
        for(int a=0; a<appShopcarts.size(); a++)
        {
            AppShopcart appShopcart = appShopcarts.get(a);
            AppgoodsId appgoodsId = appShopcart.getAppgoodsId();

            Map line = new HashMap();

            line.put("checked", true);
            line.put("id", appShopcart.getId());
            line.put("name", appgoodsId.getGoodsName());
            line.put("goodsId", appgoodsId.getId());
            line.put("logopic", appgoodsId.getLogopicUrl());
            line.put("attribute", appShopcart.getAttribute());
            line.put("price", appgoodsId.getPromotionPrice());
            line.put("originalPrice", appgoodsId.getPromotionPrice());
            line.put("count", appShopcart.getQuantity());
            line.put("obj", appShopcart);

            listData.add(line);

            this.cartFragment.addCartItem(appShopcart);

        }
        adapter = new CartGoodsAdapter(listData, activity, cartFragment);
        goodsListView.setAdapter(adapter);

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, goodsListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = goodsListView.getLayoutParams();
        params.height = totalHeight + (goodsListView.getDividerHeight() * (adapter.getCount() - 1));
        ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        goodsListView.setLayoutParams(params);

        storeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                for(int a=0; a<listData.size(); a++)
                {
                    HashMap line = (HashMap) listData.get(a);
                    line.put("checked", isChecked);

                    if(isChecked)
                    {
                        cartFragment.addCartItem((AppShopcart) line.get("obj"));

                    }
                    else
                    {
                        cartFragment.removeCartItem((Long) line.get("id"));

                    }

                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    public void setChecked(boolean checked)
    {
        storeCheckBox.setChecked(checked);

    }

}
