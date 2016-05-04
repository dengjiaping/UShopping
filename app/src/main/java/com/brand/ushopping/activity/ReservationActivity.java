package com.brand.ushopping.activity;
//预约订单列表
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.model.OrderAll;
import com.brand.ushopping.model.OrderItem;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.ReservationOrderItemView;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ReservationActivity extends Activity {
    private AppContext appContext;
    private static User user;

    private ImageView backBtn;
    private TextView titleTextView;
    private TextView unpaidTextView;
    private TextView paidTextView;
    private TextView deliveredTextView;
    private ImageView unpaidIdc;
    private ImageView paidIdc;
    private ImageView deliveredIdc;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ViewGroup contentView;
    ArrayList<OrderItem> orderList;
    private int currentOrderType;
    private ViewGroup contentViewGroup;

    private TimeoutbleProgressDialog orderTimeoutDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_reservation);

        contentViewGroup = (ViewGroup) findViewById(R.id.content);

        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);

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

        unpaidTextView = (TextView) findViewById(R.id.unpaid);
        unpaidTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrderType = StaticValues.RESERVATION_ORDER_FLAG_ALL;
                reload();

            }
        });

        paidTextView = (TextView) findViewById(R.id.paid);
        paidTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrderType = StaticValues.RESERVATION_ORDER_FLAG_UNPAID;
                reload();

            }
        });

        deliveredTextView = (TextView) findViewById(R.id.delivered);
        deliveredTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrderType = StaticValues.RESERVATION_ORDER_FLAG_DELIVERED;
                reload();

            }
        });

        contentView = (ViewGroup) findViewById(R.id.content);

        currentOrderType = StaticValues.RESERVATION_ORDER_FLAG_UNPAID;

        unpaidIdc = (ImageView) findViewById(R.id.unpaid_idc);
        paidIdc = (ImageView) findViewById(R.id.paid_idc);
        deliveredIdc = (ImageView) findViewById(R.id.delivered_idc);

        orderTimeoutDialog = TimeoutbleProgressDialog.createProgressDialog(ReservationActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                orderTimeoutDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setMessage("获取订单信息失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                reload();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();

    }

    public void reload()
    {
        selectTab();

        contentView.removeAllViewsInLayout();

        OrderAll orderAll = new OrderAll();
        orderAll.setUserId(user.getUserId());
        orderAll.setSessionid(user.getSessionid());
        if(currentOrderType != StaticValues.RESERVATION_ORDER_FLAG_ALL)
        {
            orderAll.setFlag(currentOrderType);
        }

        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        new GetYyOrderAllActionTask().execute(orderAll);

    }

    //订单列表任务
    public class GetYyOrderAllActionTask extends AsyncTask<OrderAll, Void, OrderAll>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orderTimeoutDialog.show();

        }

        @Override
        protected OrderAll doInBackground(OrderAll... orderAlls) {
            return new OrderAction().getYyOrderAllAction(orderAlls[0]);
        }

        @Override
        protected void onPostExecute(OrderAll result) {
            orderTimeoutDialog.dismiss();
            swipeRefreshLayout.setRefreshing(false);

            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderList = result.getOrderList();

                    if(orderList.size() > 0)
                    {
                        Collections.sort(orderList, new OrderComparator());

                        for(int a=0; a<orderList.size(); a++)
                        {
                            OrderItem orderItem = orderList.get(a);
                            ReservationOrderItemView orderItemView = new ReservationOrderItemView(ReservationActivity.this, null, orderItem, user);
                            contentViewGroup.addView(orderItemView);

                        }
                    }
                    else
                    {
                        warningLayout.setVisibility(View.VISIBLE);
                        warningTextView.setText("暂时没有订单");
                    }

                }
                else
                {
                    warningLayout.setVisibility(View.VISIBLE);
                    warningTextView.setText(result.getMsg());
                }
            }
            else
            {
                warningLayout.setVisibility(View.VISIBLE);
                warningTextView.setText("获取订单列表失败");

            }

        }
    }

    private void selectTab()
    {
        unpaidIdc.setVisibility(View.INVISIBLE);
        paidIdc.setVisibility(View.INVISIBLE);
        deliveredIdc.setVisibility(View.INVISIBLE);

        switch (currentOrderType)
        {
            case StaticValues.RESERVATION_ORDER_FLAG_ALL:
                unpaidIdc.setVisibility(View.VISIBLE);
                break;

            case StaticValues.RESERVATION_ORDER_FLAG_UNPAID:
            case StaticValues.RESERVATION_ORDER_FLAG_PAID:
                paidIdc.setVisibility(View.VISIBLE);
                break;

            case StaticValues.ORDER_FLAG_DELIVERED:
                deliveredIdc.setVisibility(View.VISIBLE);
                break;

        }
    }

    //订单排序
    class OrderComparator implements Comparator<OrderItem>
    {
        @Override
        public int compare(OrderItem orderItem, OrderItem t1) {
            Long id1 = orderItem.getReTime();
            Long id2 = t1.getReTime();

            return id1.compareTo(id2);
        }
    }

}
