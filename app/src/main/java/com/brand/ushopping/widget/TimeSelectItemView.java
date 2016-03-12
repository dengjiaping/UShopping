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
 * Created by Administrator on 2016/1/8.
 */
public class TimeSelectItemView extends LinearLayout {
    private TextView textView;

    private Calendar calendar;
    private int minuteAfter;
    private SelectDateActivity selectDateActivity;

    public TimeSelectItemView(final Context context, AttributeSet attrs, int minuteAfter) {
        super(context, attrs);

        calendar = Calendar.getInstance();
        this.minuteAfter = minuteAfter;
        this.selectDateActivity = (SelectDateActivity) context;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.select_time_item, this, true);

        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        calendar.add(Calendar.MINUTE, minuteAfter);
        textView = (TextView) view.findViewById(R.id.text);
        String minute = "";
        if(calendar.get(Calendar.MINUTE) == 0)
        {
            minute = "00";
        }
        else
        {
            minute = Integer.toString(calendar.get(Calendar.MINUTE));
        }
        textView.setText(calendar.get(Calendar.HOUR_OF_DAY)+":"+minute);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDateActivity.setTime(calendar);
                ((SelectDateActivity) context).timeUnselAll();
                TimeSelectItemView.this.setBackgroundColor(context.getResources().getColor(R.color.text_green));

            }
        });
    }
}
