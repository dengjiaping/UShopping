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
import com.brand.ushopping.activity.OrderConfirmActivity;
import com.brand.ushopping.activity.ReservationActivity;
import com.brand.ushopping.activity.SnsShareActivity;
import com.brand.ushopping.adapter.OrderGoodsItemAdapter;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.OrderGoodsItem;
import com.brand.ushopping.model.OrderItem;
import com.brand.ushopping.model.OrderSuccess;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Administrator on 2015/12/31.
 */
public class ReservationOrderItemView extends LinearLayout {
    private Context context;
    private ReservationActivity activity;

    private TextView orderNoTextView;
    private TextView statusTextView;
    private ListView orderGoodsListView;
    private Button payOnlineBtn;
    private Button payOfflineBtn;
    private ListView orderListView;
    private OrderItem orderItem;
    private TextView moneyTextView;
    private TextView quantityTextView;

    public ReservationOrderItemView(final Context context, AttributeSet attrs, final OrderItem orderItem, final User user) {
        super(context, attrs);

        this.context = context;
        this.activity = (ReservationActivity) context;
        this.orderItem = orderItem;

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.reservation_order_item, this, true);

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
            line.put("price",  CommonUtils.df.format(orderGoodsItem.getMoney()));
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

        moneyTextView.setText( CommonUtils.df.format(money));
        quantityTextView.setText(Integer.toString(quantity));

        payOnlineBtn.setVisibility(View.GONE);
        payOfflineBtn.setVisibility(View.GONE);

        if(orderItem.getFlag() == StaticValues.RESERVATION_ORDER_FLAG_UNPAID)
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

                        AppbrandId appbrandId = new AppbrandId();
                        appbrandId.setId(1);
                        appbrandId.setBrandName("");
                        appbrandId.setLogopic("");

                        goods.setAppbrandId(appbrandId);

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
                    bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_RESERVATION);
                    bundle.putInt("operation", StaticValues.ORDER_COMFIRM_PAY);
                    bundle.putString("orderNo", orderItem.getOrderNo());

                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

            payOfflineBtn.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 到店预订订单
                    //线下支付
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("确认线下支付");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            OrderSuccess orderSuccess = new OrderSuccess();
                            orderSuccess.setUserId(user.getUserId());
                            orderSuccess.setSessionid(user.getSessionid());
                            orderSuccess.setOrderNo(orderItem.getOrderNo());
                            orderSuccess.setFlag(1);

                            new YyOrderSuccessActionTask().execute(orderSuccess);

                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();


                }
            });

            payOnlineBtn.setVisibility(View.VISIBLE);
            payOfflineBtn.setVisibility(View.VISIBLE);
        }

    }

    //预约订单支付成功
    public class YyOrderSuccessActionTask extends AsyncTask<OrderSuccess, Void, OrderSuccess>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OrderSuccess doInBackground(OrderSuccess... orderSuccesses) {
            return new OrderAction().yyOrderSuccessAction(orderSuccesses[0]);

        }

        @Override
        protected void onPostExecute(OrderSuccess result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    activity.reload();

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("预约订单支付成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            //进入分享页面
                            Intent intent = new Intent(context, SnsShareActivity.class);
                            context.startActivity(intent);

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(context, "订单支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
