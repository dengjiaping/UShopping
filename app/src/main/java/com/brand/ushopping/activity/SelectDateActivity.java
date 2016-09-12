package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.DateSelectItemView;
import com.brand.ushopping.widget.TimeSelectItemView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SelectDateActivity extends Activity implements View.OnClickListener {
    private AppContext appContext;
    private User user;
    private int boughtType;
    private Button confirmBtn;
    private ImageView backBtn;
    private TextView titleTextView;
    private LinearLayout datePickLayout;
    private GridLayout timePickLayout;
    private ArrayList<DateSelectItemView> dateSelectItemViews;
    private boolean dateSelectedFlg = false;
    private boolean timeSelectedFlg = false;

    private ArrayList<TimeSelectItemView> timeSelectItemViews;

    private Calendar todayCalendar;
    private Calendar reservationCalendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private TextView selectedDate;
    private long currentTime;
//    private long storeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_date);
        currentTime = System.currentTimeMillis();
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        datePickLayout = (LinearLayout) findViewById(R.id.date_pick);
        timePickLayout = (GridLayout) findViewById(R.id.time_pick);
        selectedDate = (TextView) findViewById(R.id.selected_date);

        Bundle bundle = null;
        try
        {
            bundle = getIntent().getExtras();
        }catch (Exception e)
        {
            e.printStackTrace();
            bundle = appContext.getBundleObj();
        }
        if(bundle != null)
        {
            goodsList = bundle.getParcelableArrayList("goods");
            boughtType = bundle.getInt("boughtType");
//            storeId = bundle.getLong("storeId", 0);
        }
        else
        {
            finish();
        }
        
        reservationCalendar = Calendar.getInstance();
        todayCalendar = Calendar.getInstance();
        updateDate();

        dateSelectItemViews = new ArrayList<DateSelectItemView>();
        for(int day=0; day<=4; day++)
        {
            DateSelectItemView dateSelectItemView = new DateSelectItemView(SelectDateActivity.this, null, day);
            dateSelectItemViews.add(dateSelectItemView);
            datePickLayout.addView(dateSelectItemView);
        }

        updateTimeCotainer();

        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelectedFlg && timeSelectedFlg)
                {
                    Long reservationDate = reservationCalendar.getTimeInMillis();

                    Log.v("reservation time", reservationDate +"        " + reservationCalendar.toString());

                    Intent intent = new Intent(SelectDateActivity.this, OrderConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("boughtType", boughtType);
                    bundle.putLong("reservationDate", reservationDate);
                    bundle.putParcelableArrayList("goods", goodsList);
//                    bundle.putLong("storeId", storeId);
                    intent.putExtras(bundle);
                    appContext.setBundleObj(bundle);
                    startActivity(intent);
                    SelectDateActivity.this.finish();
                }
                else
                {
                    Toast.makeText(SelectDateActivity.this, "请选择上门日期和时间", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    public void updateTimeCotainer()
    {
        timePickLayout.removeAllViewsInLayout();

        timeSelectItemViews = new ArrayList<TimeSelectItemView>();

        for(int minuteCount=0; minuteCount<20; minuteCount++)
        {
            boolean avaliable = true;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);

            int minuteAfter = StaticValues.SELECT_DATE_TIME_INTERVAL * minuteCount;
            calendar.add(Calendar.MINUTE, minuteAfter);

            if(CommonUtils.isSameDay(currentTime, reservationCalendar.getTimeInMillis()))
            {
                if((calendar.getTimeInMillis() - currentTime) < StaticValues.DELIVER_AFTER)
                {
                    avaliable = false;
                }
            }

            TimeSelectItemView timeSelectItemView = new TimeSelectItemView(SelectDateActivity.this, null, calendar, avaliable);
            timeSelectItemViews.add(timeSelectItemView);
            timePickLayout.addView(timeSelectItemView);

        }

    }


    public void updateDate()
    {
        selectedDate.setText(dateFormat.format(reservationCalendar.getTime()));

    }

    public void dateUnselAll()
    {
        for(int a=0; a<dateSelectItemViews.size(); a++)
        {
            dateSelectItemViews.get(a).setBackgroundColor(SelectDateActivity.this.getResources().getColor(R.color.bg_grey));

        }

    }

    public void timeUnselAll()
    {
        for(int a=0; a<timeSelectItemViews.size(); a++)
        {
            timeSelectItemViews.get(a).setBackgroundColor(SelectDateActivity.this.getResources().getColor(R.color.bg_grey));

        }

    }

    public void setDate(Calendar calendar)
    {
        dateSelectedFlg = true;

        reservationCalendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        reservationCalendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));

        updateDate();

    }

    public void setTime(Calendar calendar)
    {
        timeSelectedFlg = true;

        reservationCalendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY));
        reservationCalendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE));

        updateDate();

    }

    @Override
    public void onClick(View v) {


    }
}
