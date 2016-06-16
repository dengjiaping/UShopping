package com.brand.ushopping.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.SelectDateActivity;

import java.util.Calendar;

/**
 * Created by Administrator on 2016/1/7.
 * 选择日期
 */
public class DateSelectItemView extends LinearLayout {
    private TextView textView;
    private TextView dateView;
    private Calendar calendar;
    private int dayAfter;
    private SelectDateActivity selectDateActivity;

    public DateSelectItemView(final Context context, AttributeSet attrs, int dayAfter) {
        super(context, attrs);
        calendar = Calendar.getInstance();
        this.dayAfter = dayAfter;
        this.selectDateActivity = (SelectDateActivity) context;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.select_date_item, this, true);

        textView = (TextView) view.findViewById(R.id.text);
        dateView = (TextView) view.findViewById(R.id.date);

        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
        calendar.add(Calendar.DAY_OF_MONTH, dayAfter);

        textView.setText((calendar.get(Calendar.MONTH) + 1) + "月" + calendar.get(Calendar.DAY_OF_MONTH) + "日");
        dateView.setText("");

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateActivity.setDate(calendar);
                ((SelectDateActivity) context).dateUnselAll();
                DateSelectItemView.this.setBackgroundColor(context.getResources().getColor(R.color.text_green));
            }
        });
    }

}
