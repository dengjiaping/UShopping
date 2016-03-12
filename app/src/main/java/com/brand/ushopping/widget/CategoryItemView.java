package com.brand.ushopping.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.CategoryActivity;
import com.brand.ushopping.model.Category;

/**
 * Created by Administrator on 2016/1/26.
 */
public class CategoryItemView extends LinearLayout {
    private TextView nameTextView;

    public CategoryItemView(final Context context, AttributeSet attrs, final Category category) {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);
        final View view = inflater.inflate(R.layout.category_item, this, true);

        nameTextView = (TextView) view.findViewById(R.id.name);
        nameTextView.setText(category.getName());

        nameTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CategoryActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putLong("categoryId", category.getId());
                            bundle.putString("categoryName", category.getName());
                            intent.putExtras(bundle);
                            context.startActivity(intent);
            }
        });

    }


}
