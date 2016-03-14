package com.brand.ushopping.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.OrderConfirmActivity;

/**
 * Created by Administrator on 2016/3/14.
 */
public class SharePopup extends PopupWindow {
    private Context context;
    private OrderConfirmActivity activity;

    private ImageView wxBtn;
    private ImageView qqBtn;
    private ImageView weiboBtn;

    public SharePopup(final Context context)
    {
        super(context);

        this.activity = (OrderConfirmActivity) context;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.goods_attribute, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);

        wxBtn = (ImageView) view.findViewById(R.id.wx);
        wxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        qqBtn = (ImageView) view.findViewById(R.id.qq);
        qqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        weiboBtn = (ImageView) view.findViewById(R.id.weibo);
        weiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void share(int type)
    {
        switch (type)
        {
            


        }

    }


}
