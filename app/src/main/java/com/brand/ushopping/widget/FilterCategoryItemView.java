package com.brand.ushopping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.GoodsFilterActivity;
import com.brand.ushopping.model.Category;

/**
 * Created by Administrator on 2016/1/26.
 */
public class FilterCategoryItemView extends LinearLayout {
    private TextView nameTextView;
    private GoodsFilterActivity goodsFilterActivity;

    public FilterCategoryItemView(final Context context, AttributeSet attrs, final Category category) {
        super(context, attrs);
        goodsFilterActivity = (GoodsFilterActivity) context;
        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.filter_category_item, this, true);

        nameTextView = (TextView) view.findViewById(R.id.name);
        nameTextView.setText(category.getName());

        nameTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                goodsFilterActivity.categoryClearSelected();
                goodsFilterActivity.setAppCategoryIdSelected(category.getId());
                nameTextView.setBackgroundColor(goodsFilterActivity.getResources().getColor(R.color.attribute_btn_selected));
            }
        });

    }

    public void unselect()
    {
        nameTextView.setBackgroundColor(goodsFilterActivity.getResources().getColor(R.color.bg_white));
    }
}
