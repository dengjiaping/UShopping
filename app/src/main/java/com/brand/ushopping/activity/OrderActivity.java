package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import com.brand.ushopping.widget.OrderItemView;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class OrderActivity extends Activity {
    private AppContext appContext;
    private static User user;
    private Integer currentType;
    private ImageView backBtn;
    private TextView titleTextView;

    private TextView unpaidBtn;
    private TextView paidBtn;
    private TextView deliveredBtn;
    private TextView confirmedBtn;
    private ImageView unpaidIdc;
    private ImageView paidIdc;
    private ImageView deliveredIdc;
    private ImageView confirmedIdc;
    private FrameLayout warningLayout;
    private TextView warningTextView;
    private ViewGroup contentViewGroup;

    ArrayList<OrderItem> orderList;

    private TimeoutbleProgressDialog orderTimeoutDialog;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        warningLayout = (FrameLayout) findViewById(R.id.warning_layout);
        warningTextView = (TextView) findViewById(R.id.warning_text);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        unpaidBtn = (TextView) findViewById(R.id.unpaid);
        unpaidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = StaticValues.ORDER_FLAG_UNPAY;
                reload();
            }
        });

        paidBtn = (TextView) findViewById(R.id.paid);
        paidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = StaticValues.ORDER_FLAG_PAID;
                reload();
            }
        });

        deliveredBtn = (TextView) findViewById(R.id.delivered);
        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = StaticValues.ORDER_FLAG_DELIVERED;
                reload();
            }
        });

        confirmedBtn = (TextView) findViewById(R.id.confirmed);
        confirmedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentType = StaticValues.ORDER_FLAG_CONFIRMED;
                reload();

            }
        });

        unpaidIdc = (ImageView) findViewById(R.id.unpaid_idc);
        paidIdc = (ImageView) findViewById(R.id.paid_idc);
        deliveredIdc = (ImageView) findViewById(R.id.delivered_idc);
        confirmedIdc = (ImageView) findViewById(R.id.confirmed_idc);

        contentViewGroup = (ViewGroup) findViewById(R.id.content);

        orderTimeoutDialog = TimeoutbleProgressDialog.createProgressDialog(OrderActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                orderTimeoutDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
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

        currentType = getIntent().getExtras().getInt("enterType", StaticValues.ORDER_FLAG_UNPAY);

        selectTab();

        reload();

    }

    public void reload()
    {
        selectTab();

        contentViewGroup.removeAllViewsInLayout();

        OrderAll orderAll = new OrderAll();
        orderAll.setUserId(user.getUserId());
        orderAll.setSessionid(user.getSessionid());

        if(currentType != StaticValues.ORDER_FLAG_UNPAY)
        {
            orderAll.setFlag(currentType);
        }


        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        new GetOrderActionTask().execute(orderAll);

    }

    //订单列表任务
    public class GetOrderActionTask extends AsyncTask<OrderAll, Void, OrderAll>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orderTimeoutDialog.show();
        }

        @Override
        protected OrderAll doInBackground(OrderAll... orderAlls) {
            return new OrderAction(OrderActivity.this).getOrderAction(orderAlls[0]);
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

                    if(orderList.isEmpty())
                    {
                        //列表为空
                        warningLayout.setVisibility(View.VISIBLE);
                        warningTextView.setText("暂时没有订单");
                        return;

                    }
                    Collections.sort(orderList, new OrderComparator());

                    for(int a=0; a<orderList.size(); a++)
                    {
                        OrderItem orderItem = orderList.get(a);
                        OrderItemView orderItemView = new OrderItemView(OrderActivity.this, OrderActivity.this, null, orderItem, user);
                        contentViewGroup.addView(orderItemView);

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
        confirmedIdc.setVisibility(View.INVISIBLE);

        switch (currentType)
        {
            case StaticValues.ORDER_FLAG_UNPAY:
                unpaidIdc.setVisibility(View.VISIBLE);
                break;

            case StaticValues.ORDER_FLAG_PAID:
                paidIdc.setVisibility(View.VISIBLE);
                break;

            case StaticValues.ORDER_FLAG_DELIVERED:
                deliveredIdc.setVisibility(View.VISIBLE);
                break;

            case StaticValues.ORDER_FLAG_CONFIRMED:
                confirmedIdc.setVisibility(View.VISIBLE);
                break;

        }
    }

    //订单排序
    class OrderComparator implements Comparator<OrderItem>
    {
        @Override
        public int compare(OrderItem orderItem, OrderItem t1) {
            Long reTime1 = orderItem.getReTime();
            Long reTime2 = orderItem.getReTime();

            return reTime1.compareTo(reTime2);
        }
    }

}
