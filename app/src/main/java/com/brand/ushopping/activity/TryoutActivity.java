package com.brand.ushopping.activity;
//上门试衣

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.brand.ushopping.widget.TryitOrderItemView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TryoutActivity extends Activity {
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
    ArrayList<OrderItem> orderList;
    private int currentOrderType;
    private ViewGroup contentViewGroup;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tryout);
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

        contentViewGroup = (ViewGroup) findViewById(R.id.content);

        unpaidTextView = (TextView) findViewById(R.id.unpaid);
        unpaidTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrderType = StaticValues.TRYOUT_ORDER_FLAG_UNPAID;
                reload();
            }
        });
        paidTextView = (TextView) findViewById(R.id.paid);
        paidTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrderType = StaticValues.TRYOUT_ORDER_FLAG_PAID;
                reload();
            }
        });
        deliveredTextView = (TextView) findViewById(R.id.delivered);
        deliveredTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentOrderType = StaticValues.TRYOUT_ORDER_FLAG_DELIVERED;
                reload();
            }
        });

        currentOrderType = StaticValues.TRYOUT_ORDER_FLAG_UNPAID;

        unpaidIdc = (ImageView) findViewById(R.id.unpaid_idc);
        paidIdc = (ImageView) findViewById(R.id.paid_idc);
        deliveredIdc = (ImageView) findViewById(R.id.delivered_idc);

    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();

    }

    public void reload()
    {
        selectTab();

        contentViewGroup.removeAllViewsInLayout();

        OrderAll orderAll = new OrderAll();
        orderAll.setUserId(user.getUserId());
        orderAll.setSessionid(user.getSessionid());
        orderAll.setFlag(currentOrderType);

        //没有数据时的提示
        warningLayout.setVisibility(View.GONE);

        new GetSmOrderAllActionTask().execute(orderAll);
    }

    //订单列表任务
    public class GetSmOrderAllActionTask extends AsyncTask<OrderAll, Void, OrderAll>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OrderAll doInBackground(OrderAll... orderAlls) {
            return new OrderAction().getSmOrderAllAction(orderAlls[0]);
        }

        @Override
        protected void onPostExecute(OrderAll result) {
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
                            TryitOrderItemView orderItemView = new TryitOrderItemView(TryoutActivity.this, null, orderItem);
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
            case StaticValues.ORDER_FLAG_UNPAY:
                unpaidIdc.setVisibility(View.VISIBLE);
                break;

            case StaticValues.ORDER_FLAG_PAID:
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
            Long id1 = orderItem.getId();
            Long id2 = t1.getId();

            return id1.compareTo(id2);
        }
    }

}
