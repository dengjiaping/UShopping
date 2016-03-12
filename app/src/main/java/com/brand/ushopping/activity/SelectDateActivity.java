package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.DateSelectItemView;
import com.brand.ushopping.widget.TimeSelectItemView;
import com.google.android.gms.common.api.GoogleApiClient;

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

//    private TextView today;
//    private TextView datePlus1;
//    private TextView datePlus2;
//    private TextView datePlus3;
//    private TextView datePlus4;

    private ArrayList<TimeSelectItemView> timeSelectItemViews;

//    private TextView text0800;
//    private TextView text0830;
//    private TextView text0900;
//    private TextView text0930;
//    private TextView text1000;
//    private TextView text1030;
//    private TextView text1100;
//    private TextView text1130;
//    private TextView text1200;
//    private TextView text1230;
//    private TextView text1300;
//    private TextView text1330;
//    private TextView text1400;
//    private TextView text1430;
//    private TextView text1500;
//    private TextView text1530;
//    private TextView text1600;
//    private TextView text1630;
//    private TextView text1700;
//    private TextView text1730;
//    private TextView text1800;

    private Calendar todayCalendar;
    private Calendar reservationCalendar;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");

    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    private TextView selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_select_date);

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

        Bundle bundle = getIntent().getExtras();
        goodsList = bundle.getParcelableArrayList("goods");
        boughtType = bundle.getInt("boughtType");

        reservationCalendar = Calendar.getInstance();
        todayCalendar = Calendar.getInstance();
        updateDate();

        dateSelectItemViews = new ArrayList<DateSelectItemView>();
        for(int day=1; day<=5; day++)
        {
            DateSelectItemView dateSelectItemView = new DateSelectItemView(SelectDateActivity.this, null, day);
            dateSelectItemViews.add(dateSelectItemView);
            datePickLayout.addView(dateSelectItemView);
        }

        timeSelectItemViews = new ArrayList<TimeSelectItemView>();

        for(int minuteCount=0; minuteCount<20; minuteCount++)
        {
            TimeSelectItemView timeSelectItemView = new TimeSelectItemView(SelectDateActivity.this, null, StaticValues.SELECT_DATE_TIME_INTERVAL * minuteCount);
            timeSelectItemViews.add(timeSelectItemView);
            timePickLayout.addView(timeSelectItemView);

        }

        /*
        datePlus1 = (TextView) findViewById(R.id.plus1);
        reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
        reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
        reservationCalendar.add(Calendar.DAY_OF_MONTH, 1);
        datePlus1.setText(dateFormat.format(reservationCalendar.getTime()));
        datePlus1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
                reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
                reservationCalendar.add(Calendar.DAY_OF_MONTH, 1);
                updateDate();

            }
        });
        datePlus2 = (TextView) findViewById(R.id.plus2);
        reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
        reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
        reservationCalendar.add(Calendar.DAY_OF_MONTH, 2);
        datePlus2.setText(dateFormat.format(reservationCalendar.getTime()));
        datePlus2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
                reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
                reservationCalendar.add(Calendar.DAY_OF_MONTH, 2);
                updateDate();

            }
        });
        datePlus3 = (TextView) findViewById(R.id.plus3);
        reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
        reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
        reservationCalendar.add(Calendar.DAY_OF_MONTH, 3);
        datePlus3.setText(dateFormat.format(reservationCalendar.getTime()));
        datePlus3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
                reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
                reservationCalendar.add(Calendar.DAY_OF_MONTH, 3);
                updateDate();

            }
        });
        datePlus4 = (TextView) findViewById(R.id.plus4);
        reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
        reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
        reservationCalendar.add(Calendar.DAY_OF_MONTH, 4);
        datePlus4.setText(dateFormat.format(reservationCalendar.getTime()));
        datePlus4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.MONTH, todayCalendar.get(Calendar.MONTH));
                reservationCalendar.set(Calendar.DAY_OF_MONTH, todayCalendar.get(Calendar.DAY_OF_MONTH));
                reservationCalendar.add(Calendar.DAY_OF_MONTH, 4);
                updateDate();

            }
        });
        */
        /*
        text0800 = (TextView) findViewById(R.id.time0800);
        text0800.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 8);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text0830 = (TextView) findViewById(R.id.time0830);
        text0830.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 8);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text0900 = (TextView) findViewById(R.id.time0900);
        text0900.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 9);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text0930 = (TextView) findViewById(R.id.time0930);
        text0930.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 9);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1000 = (TextView) findViewById(R.id.time1000);
        text1000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 10);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1030 = (TextView) findViewById(R.id.time1030);
        text1030.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 10);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1100 = (TextView) findViewById(R.id.time1100);
        text1100.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 11);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1130 = (TextView) findViewById(R.id.time1130);
        text1130.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 11);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1200 = (TextView) findViewById(R.id.time1200);
        text1200.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 12);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1230 = (TextView) findViewById(R.id.time1230);
        text1230.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 12);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1300 = (TextView) findViewById(R.id.time1300);
        text1300.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 13);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1330 = (TextView) findViewById(R.id.time1330);
        text1330.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 13);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1400 = (TextView) findViewById(R.id.time1400);
        text1400.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 14);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1430 = (TextView) findViewById(R.id.time1430);
        text1430.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 14);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1500 = (TextView) findViewById(R.id.time1500);
        text1500.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 15);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1530 = (TextView) findViewById(R.id.time1530);
        text1530.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 15);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1600 = (TextView) findViewById(R.id.time1600);
        text1600.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 16);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1630 = (TextView) findViewById(R.id.time1630);
        text1630.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 16);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1700 = (TextView) findViewById(R.id.time1700);
        text1700.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 17);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        text1730 = (TextView) findViewById(R.id.time1730);
        text1730.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 17);
                reservationCalendar.set(Calendar.MINUTE, 30);
                updateDate();
            }
        });
        text1800 = (TextView) findViewById(R.id.time1800);
        text1800.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reservationCalendar.set(Calendar.HOUR, 18);
                reservationCalendar.set(Calendar.MINUTE, 0);
                updateDate();
            }
        });
        */

        confirmBtn = (Button) findViewById(R.id.confirm);
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(dateSelectedFlg && timeSelectedFlg)
                {
                    Long reservationDate = reservationCalendar.getTimeInMillis();

                    Intent intent = new Intent(SelectDateActivity.this, OrderConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("boughtType", boughtType);
                    bundle.putLong("reservationDate", reservationDate);
                    bundle.putParcelableArrayList("goods", goodsList);

                    intent.putExtras(bundle);
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
