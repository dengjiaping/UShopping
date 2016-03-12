package com.brand.ushopping.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.fragment.CartFragment;
import com.brand.ushopping.model.Activity;
import com.brand.ushopping.utils.StaticValues;

/**
 * Created by Administrator on 2015/12/23.
 */
public class CartTypePopup extends PopupWindow {
    private Context context;
    private Activity activity;
    private CartFragment fragment;

    private TextView cartTextView;
    private TextView tryitCartTextView;
    private TextView reservationCartTextView;

    public CartTypePopup(Context context, final CartFragment fragment)
    {
        super(context);
        this.context = context;
        this.fragment = fragment;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.cart_type, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        cartTextView = (TextView) view.findViewById(R.id.cart);
        cartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.setCartType(StaticValues.CART_TYPE_NORMAL);
                fragment.setCartTypeText("购物车");

                CartTypePopup.this.dismiss();
            }
        });

        tryitCartTextView = (TextView) view.findViewById(R.id.tryit_cart);
        tryitCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.setCartType(StaticValues.CART_TYPE_TRYIT);
                fragment.setCartTypeText("上门购物车");

                CartTypePopup.this.dismiss();
            }
        });

        reservationCartTextView = (TextView) view.findViewById(R.id.reservation_cart);
        reservationCartTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.setCartType(StaticValues.CART_TYPE_RESERVATION);
                fragment.setCartTypeText("预订购物车");

                CartTypePopup.this.dismiss();
            }
        });

        setContentView(view);
    }



}
