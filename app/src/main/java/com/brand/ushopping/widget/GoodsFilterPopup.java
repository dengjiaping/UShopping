package com.brand.ushopping.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.BrandMoreGoodsActivity;

/**
 * Created by Administrator on 2016/5/31.
 */
public class GoodsFilterPopup extends PopupWindow {
    private Context context;
    private BrandMoreGoodsActivity brandMoreGoodsActivity;
    private Button resetBtn;
    private Button confirmBtn;

    public GoodsFilterPopup(Context context) {
        super(context);
        this.context = context;

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.goods_filter_popup, null);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        resetBtn = (Button) view.findViewById(R.id.reset);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsFilterPopup.this.dismiss();

            }
        });

        confirmBtn = (Button) view.findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodsFilterPopup.this.dismiss();
            }
        });

        this.setContentView(view);
    }
}
