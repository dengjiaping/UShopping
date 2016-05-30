package com.brand.ushopping.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.activity.KuaidiActivity;
import com.brand.ushopping.activity.OrderActivity;
import com.brand.ushopping.adapter.OrderGoodsItemAdapter;
import com.brand.ushopping.model.AppexpressId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.ConfirmOrder;
import com.brand.ushopping.model.OrderGoodsItem;
import com.brand.ushopping.model.OrderItem;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/26.
 */
public class OrderItemView extends LinearLayout {
    private Context context;
    private OrderActivity activity;
    private User user;
    private TextView orderNoTextView;
    private TextView statusTextView;
    private ListView orderGoodsListView;
    private ListView orderListView;
    private OrderItem orderItem;
    private TextView moneyTextView;
    private TextView quantityTextView;
    private Button kuaidiBtn;
    private Button confirmBtn;
    private String expressCompany = "";
    private String expressNo = "";


    public OrderItemView(final Context context, final OrderActivity activity, AttributeSet attrs, final OrderItem orderItem, final User user) {
        super(context, attrs);

        this.context = context;
        this.activity = activity;
        this.orderItem = orderItem;
        this.user = user;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.order_item, this, true);

        orderNoTextView = (TextView) view.findViewById(R.id.order_no);
        statusTextView = (TextView) view.findViewById(R.id.status);
        orderGoodsListView = (ListView) view.findViewById(R.id.goods_list);
        orderListView = (ListView) view.findViewById(R.id.goods_list);
        moneyTextView = (TextView) view.findViewById(R.id.money);
        quantityTextView = (TextView) view.findViewById(R.id.quantity);
        confirmBtn = (Button) view.findViewById(R.id.confirm);

        orderNoTextView.setText(orderItem.getOrderNo());
        confirmBtn.setVisibility(View.GONE);

        kuaidiBtn = (Button) view.findViewById(R.id.kuaidi);
        kuaidiBtn.setVisibility(View.GONE);

        int quantity = 0;
        double money =0;

        List listData = new ArrayList<Map<String,Object>>();
        for(OrderGoodsItem orderGoodsItem: orderItem.getOrderGoodsItems())
        {
            Map line = new HashMap();

            line.put("id", orderGoodsItem.getId());

            AppgoodsId appgoodsId = orderGoodsItem.getAppgoodsId();
            if(appgoodsId != null)
            {
                line.put("img", appgoodsId.getLogopicUrl());
                line.put("goodsId", appgoodsId.getId());
                line.put("goodsName", appgoodsId.getGoodsName());
                line.put("attribute", orderGoodsItem.getAttribute());
                line.put("price", orderGoodsItem.getMoney());
                line.put("count", orderGoodsItem.getQuantity());

                line.put("customerFlag", orderGoodsItem.getCustomerFlag());
                line.put("customerContent", orderGoodsItem.getCustomerContent());
                line.put("startTime", orderGoodsItem.getCustomerStartTime());
                line.put("endTime", orderGoodsItem.getCustomerEndTime());
                line.put("orderId", orderGoodsItem.getId());
                line.put("orderNo", orderItem.getOrderNo());
                line.put("money", CommonUtils.df.format(orderGoodsItem.getMoney()));
                line.put("flag", orderItem.getFlag());

                listData.add(line);

                quantity += orderGoodsItem.getQuantity();
                money += (orderGoodsItem.getMoney() * orderGoodsItem.getQuantity());

            }

            AppexpressId appexpressId = appgoodsId.getAppexpressId();
            if(appexpressId != null)
            {
                expressCompany = appexpressId.getCompany();
                expressNo = appexpressId.getExpressNo();

            }

        }

        OrderGoodsItemAdapter adapter = new OrderGoodsItemAdapter(listData, activity);
        orderListView.setAdapter(adapter);

        switch (orderItem.getFlag())
        {
            case StaticValues.ORDER_FLAG_DELIVERED:
                kuaidiBtn.setVisibility(View.VISIBLE);
                confirmBtn.setVisibility(View.VISIBLE);

                kuaidiBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, KuaidiActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type", expressCompany);
                        bundle.putString("postid", expressNo);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                });

                confirmBtn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        builder.setMessage("是否确认收货?");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                ConfirmOrder confirmOrder = new ConfirmOrder();
                                confirmOrder.setUserId(user.getUserId());
                                confirmOrder.setSessionid(user.getSessionid());
                                confirmOrder.setFlag(StaticValues.ORDER_FLAG_CONFIRMED);
                                confirmOrder.setOrderNo(orderItem.getOrderNo());

                                new ConfirmOrderActionTask().execute(confirmOrder);

                            }
                        });
                        builder.setNegativeButton("取消", null);

                        builder.create().show();

                    }
                });

                break;


        }

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, orderListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = orderListView.getLayoutParams();
        params.height = totalHeight + (orderListView.getDividerHeight() * (adapter.getCount() - 1));
        ((MarginLayoutParams)params).setMargins(10, 10, 10, 10);
        orderListView.setLayoutParams(params);

        moneyTextView.setText(CommonUtils.df.format(money));
        quantityTextView.setText(Integer.toString(quantity));

    }

    //订单确认
    public class ConfirmOrderActionTask extends AsyncTask<ConfirmOrder, Void, ConfirmOrder>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ConfirmOrder doInBackground(ConfirmOrder... confirmOrders) {
            return new OrderAction().confirmOrderAction(confirmOrders[0]);
        }

        @Override
        protected void onPostExecute(ConfirmOrder result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    activity.reload();

                }
                else
                {
                    Toast.makeText(activity, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(activity, "订单确认失败,请稍后再试", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
