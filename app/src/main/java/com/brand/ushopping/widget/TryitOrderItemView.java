package com.brand.ushopping.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.OrderConfirmActivity;
import com.brand.ushopping.adapter.OrderGoodsItemAdapter;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.OrderGoodsItem;
import com.brand.ushopping.model.OrderItem;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/31.
 */
public class TryitOrderItemView extends LinearLayout
{
    private Context context;

    private TextView orderNoTextView;
    private TextView statusTextView;
    private ListView orderGoodsListView;
    private Button payOnlineBtn;
    private Button payOfflineBtn;
    private ListView orderListView;
    private OrderItem orderItem;
    private TextView moneyTextView;
    private TextView quantityTextView;


    public TryitOrderItemView(final Context context, AttributeSet attrs, final OrderItem orderItem) {
        super(context, attrs);

        this.context = context;
        this.orderItem = orderItem;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tryit_order_item, this, true);

        orderNoTextView = (TextView) view.findViewById(R.id.order_no);
        statusTextView = (TextView) view.findViewById(R.id.status);
        orderGoodsListView = (ListView) view.findViewById(R.id.goods_list);
        payOnlineBtn = (Button) view.findViewById(R.id.pay_online);
        payOfflineBtn = (Button) view.findViewById(R.id.pay_offline);
        orderListView = (ListView) view.findViewById(R.id.goods_list);
        moneyTextView = (TextView) view.findViewById(R.id.money);
        quantityTextView = (TextView) view.findViewById(R.id.quantity);

        orderNoTextView.setText(orderItem.getOrderNo());

        int quantity = 0;
        double money =0;

        List listData = new ArrayList<Map<String,Object>>();
        for(OrderGoodsItem orderGoodsItem: orderItem.getOrderGoodsItems())
        {
            Map line = new HashMap();

            line.put("id", orderGoodsItem.getId());

            AppgoodsId appgoodsId = orderGoodsItem.getAppgoodsId();
            line.put("img", appgoodsId.getLogopicUrl());
            line.put("goodsId", appgoodsId.getId());
            line.put("goodsName", appgoodsId.getGoodsName());
            line.put("attribute", orderGoodsItem.getAttribute());
            line.put("price", orderGoodsItem.getMoney());
            line.put("count", orderGoodsItem.getQuantity());
            line.put("customerFlag", StaticValues.CUSTOMER_FLAG_NONE);

            listData.add(line);

            quantity += orderGoodsItem.getQuantity();
            money += (orderGoodsItem.getMoney() * orderGoodsItem.getQuantity());
        }

        OrderGoodsItemAdapter adapter = new OrderGoodsItemAdapter(listData, context);
        orderListView.setAdapter(adapter);

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

        moneyTextView.setText(Double.toString(money));
        quantityTextView.setText(Integer.toString(quantity));

        payOnlineBtn.setVisibility(View.GONE);
        payOfflineBtn.setVisibility(View.GONE);

        if(orderItem.getFlag() == StaticValues.TRYOUT_ORDER_FLAG_UNPAID)
        {
            payOnlineBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //在线支付
                    Intent intent = new Intent(context, OrderConfirmActivity.class);
                    Bundle bundle = new Bundle();
                    ArrayList<OrderGoodsItem> orderGoodsItems = orderItem.getOrderGoodsItems();
                    ArrayList<Goods> goodsList = new ArrayList<Goods>();
                    for(int a=0; a<orderGoodsItems.size(); a++)
                    {
                        OrderGoodsItem orderGoodsItem = orderGoodsItems.get(a);
                        Goods goods = new Goods();

                        goods.setId(orderGoodsItem.getId());
                        AppgoodsId appgoodsId = orderGoodsItem.getAppgoodsId();
                        goods.setGoodsName(appgoodsId.getGoodsName());
                        goods.setLogopicUrl(appgoodsId.getLogopicUrl());
                        goods.setAttribute(orderGoodsItem.getAttribute());
                        goods.setPromotionPrice(orderGoodsItem.getMoney());
                        goods.setCount(orderGoodsItem.getQuantity());

                        goodsList.add(goods);
                    }

                    bundle.putParcelableArrayList("goods", goodsList);
                    bundle.putLong("reservationDate", 0);
                    bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_TRYIT);
                    bundle.putInt("operation", StaticValues.ORDER_COMFIRM_PAY);
                    bundle.putString("orderNo", orderItem.getOrderNo());

                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });


            payOfflineBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            payOnlineBtn.setVisibility(View.VISIBLE);
//            payOfflineBtn.setVisibility(View.VISIBLE);
        }

    }

}
