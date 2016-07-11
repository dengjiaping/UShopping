package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.adapter.OrderGoodsItemAdapter;
import com.brand.ushopping.model.AppaddressId;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.Charge;
import com.brand.ushopping.model.ClientCharge;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.OrderSave;
import com.brand.ushopping.model.OrderSaveList;
import com.brand.ushopping.model.SmOrderSave;
import com.brand.ushopping.model.SmOrderSaveList;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.YyOrderSave;
import com.brand.ushopping.model.YyOrderSaveList;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.OrderSubmitPopup;
import com.pingplusplus.android.PaymentActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brand.ushopping.R.id.root_view;

public class OrderPayActivity extends Activity {
    private AppContext appContext;
    private User user;
    private Goods goods;
    private AppbrandId appbrandId;
    private ArrayList<Goods> goodsList;
    private View rootView;
    private Charge chargeObj;
    private int paymentMode;

    private TextView consigneeTextView;
    private TextView mobileTextView;
    private TextView deaddressTextView;
    private ListView goodsListView;
    private TextView storeNameTextView;
    private TextView totalCountTextView;
    private TextView summaryTextView;

    private Button orderSubmitBtn;

    private OrderSubmitPopup orderSubmitPopup;

    private int totalCount = 0;
    private double summary = 0;

    private long reservationDate;
    private int boughtType;

    private Date timeShop;  //到店预约时间

    public OrderPayActivity()
    {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_pay);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        rootView = findViewById(root_view);
        consigneeTextView = (TextView) findViewById(R.id.consignee);
        mobileTextView = (TextView) findViewById(R.id.mobile);
        deaddressTextView = (TextView) findViewById(R.id.deaddress);
        goodsListView = (ListView) findViewById(R.id.goods_list);
        storeNameTextView = (TextView) findViewById(R.id.store_name);

        totalCountTextView = (TextView) findViewById(R.id.total_count);
        summaryTextView = (TextView) findViewById(R.id.summary);

        Bundle bundle = null;
        try {
            bundle = getIntent().getExtras();
        }catch (Exception e)
        {
            e.printStackTrace();
            bundle = appContext.getBundleObj();
        }
        if(bundle != null)
        {
            goodsList = bundle.getParcelableArrayList("goods");
            reservationDate = bundle.getLong("reservationDate");
            boughtType = bundle.getInt("boughtType");
        }
        else
        {
            finish();
        }

        appbrandId = goodsList.get(0).getAppbrandId();

        consigneeTextView.setText(user.getUserName());
        mobileTextView.setText(user.getMobile());
        deaddressTextView.setText(appContext.getDefaultAddress());

        totalCount = 0;
        summary = 0;
        List listData = new ArrayList<Map<String, Object>>();
        for (Goods goods : goodsList) {
            Map line = new HashMap();

            int count = goods.getCount();
            double price = goods.getGoodsPrice();

            line.put("id", goods.getId());
            line.put("img", goods.getLogopicUrl());
            line.put("goodsName", goods.getGoodsName());
            line.put("attribute", goods.getAttribute());
            line.put("price", price);
            line.put("count", count);

            listData.add(line);

            totalCount += count;
            summary += price * count;
        }
        OrderGoodsItemAdapter adapter = new OrderGoodsItemAdapter(listData, OrderPayActivity.this);
        goodsListView.setAdapter(adapter);

        int totalHeight = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, goodsListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = goodsListView.getLayoutParams();
        params.height = totalHeight + (goodsListView.getDividerHeight() * (adapter.getCount() - 1));
        ((ViewGroup.MarginLayoutParams) params).setMargins(10, 10, 10, 10);
        goodsListView.setLayoutParams(params);

        storeNameTextView.setText(appbrandId.getBrandName());

        totalCountTextView.setText(Integer.toString(totalCount));
        summaryTextView.setText(Double.toString(summary));

        orderSubmitBtn = (Button) findViewById(R.id.order_submit);
        orderSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //订单提交
                switch (boughtType) {
                    case StaticValues.BOUTHT_TYPE_NORMAL:
                        //普通
                        ClientCharge clientCharge = new ClientCharge();
                        clientCharge.setUserId(user.getUserId());
                        clientCharge.setSessionid(user.getSessionid());
                        clientCharge.setSummary(summary);
                        int sumConverted = (int) (summary * 100);
                        clientCharge.setAmountVal(sumConverted);

                        orderSubmitPopup = new OrderSubmitPopup(OrderPayActivity.this, clientCharge);
                        orderSubmitPopup.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                        break;

                    case StaticValues.BOUTHT_TYPE_RESERVATION:
                        //预约
                        YyOrderSaveList yyOrderSaveList = new YyOrderSaveList();

                        yyOrderSaveList.setUserId(user.getUserId());
                        yyOrderSaveList.setSessionid(user.getSessionid());

                        ArrayList<YyOrderSave> yyOrderSaveArrayList = new ArrayList<YyOrderSave>();
                        for (Goods goods : goodsList) {
                            YyOrderSave yyOrderSave = new YyOrderSave();

                            yyOrderSave.setFlag(StaticValues.ORDER_FLAG_UNPAY);

                            AppuserId appuserId = new AppuserId();
                            appuserId.setUserId(user.getUserId());
                            yyOrderSave.setAppuserId(appuserId);

                            AppaddressId appaddressId = new AppaddressId();
                            appaddressId.setId(appContext.getDefaultAddressId());
                            yyOrderSave.setAppaddressId(appaddressId);

                            //商品信息
                            AppgoodsId appgoodsId = new AppgoodsId();
                            appgoodsId.setId(goods.getId());
                            yyOrderSave.setAppgoodsId(appgoodsId);

                            yyOrderSave.setAttribute(goods.getAttribute());
                            yyOrderSave.setQuantity(goods.getCount());

                            yyOrderSave.setMoney(goods.getGoodsPrice() * goods.getCount());

                            yyOrderSave.setTimeShop(new Date(reservationDate));

                            yyOrderSaveArrayList.add(yyOrderSave);
                        }

                        yyOrderSaveList.setYyorder(yyOrderSaveArrayList);

                        new YyOrderSaveTask().execute(yyOrderSaveList);
                        break;

                    case StaticValues.BOUTHT_TYPE_TRYIT:
                        //上门
                        SmOrderSaveList smOrderSaveList = new SmOrderSaveList();

                        smOrderSaveList.setUserId(user.getUserId());
                        smOrderSaveList.setSessionid(user.getSessionid());

                        ArrayList<SmOrderSave> smOrderSaveArrayList = new ArrayList<SmOrderSave>();
                        for (Goods goods : goodsList) {
                            SmOrderSave smOrderSave = new SmOrderSave();

                            smOrderSave.setFlag(StaticValues.ORDER_FLAG_UNPAY);

                            AppuserId appuserId = new AppuserId();
                            appuserId.setUserId(user.getUserId());
                            smOrderSave.setAppuserId(appuserId);

                            AppaddressId appaddressId = new AppaddressId();
                            appaddressId.setId(appContext.getDefaultAddressId());
                            smOrderSave.setAppaddressId(appaddressId);

                            //商品信息
                            AppgoodsId appgoodsId = new AppgoodsId();
                            appgoodsId.setId(goods.getId());
                            smOrderSave.setAppgoodsId(appgoodsId);

                            smOrderSave.setAttribute(goods.getAttribute());
                            smOrderSave.setQuantity(goods.getCount());

                            smOrderSave.setMoney(goods.getGoodsPrice() * goods.getCount());

                            smOrderSave.setTimeShop(new Date(reservationDate));

                            smOrderSaveArrayList.add(smOrderSave);
                        }

                        smOrderSaveList.setSmorder(smOrderSaveArrayList);

                        new SmOrderSaveTask().execute(smOrderSaveList);
                        break;
                }

            }
        });

    }

    //唤起支付对象
    public void startPay(String charge)
    {
        this.chargeObj = JSON.parseObject(charge, Charge.class);;

        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, charge);
        startActivityForResult(intent, StaticValues.REQUEST_CODE_PAYMENT);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //支付页面返回处理
        if (requestCode == StaticValues.REQUEST_CODE_PAYMENT) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getExtras().getString("pay_result");
            /* 处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
                if(result.equals(StaticValues.PAYMENT_RESULT_SUCCESS))
                {
                    //支付成功

                    ArrayList<OrderSave> orderSaveArrayList = new ArrayList<OrderSave>();
                    for (Goods goods: goodsList)
                    {
                        OrderSave orderSave = new OrderSave();

                        orderSave.setOrderNo(chargeObj.getOrderNo());
                        orderSave.setMoney(summary);

                        orderSave.setPaymentMode(StaticValues.ORDER_PAY_CASH);
                        String channel = chargeObj.getChannel();
                        if(channel.equals(StaticValues.PAY_METHOD_ALIPAY))
                        {
                            orderSave.setPaymentMode(StaticValues.ORDER_PAY_ALIPAY);
                        }
                        if(channel.equals(StaticValues.PAY_METHOD_UPACP))
                        {
                            orderSave.setPaymentMode(StaticValues.ORDER_PAY_UNACP);
                        }
                        if(channel.equals(StaticValues.PAY_METHOD_WX))
                        {
                            orderSave.setPaymentMode(StaticValues.ORDER_PAY_WX);
                        }
                        if(channel.equals(StaticValues.PAY_METHOD_CASH))
                        {
                            orderSave.setPaymentMode(StaticValues.ORDER_PAY_CASH);
                        }

                        orderSave.setFlag(StaticValues.ORDER_FLAG_PAID);

                        AppuserId appuserId = new AppuserId();
                        appuserId.setUserId(user.getUserId());
                        orderSave.setAppuserId(appuserId);

                        AppaddressId appaddressId = new AppaddressId();
                        appaddressId.setId(appContext.getDefaultAddressId());
                        orderSave.setAppaddressId(appaddressId);

                        orderSave.setRemark(chargeObj.getBody());

                        //商品信息
                        AppgoodsId appgoodsId = new AppgoodsId();
                        appgoodsId.setId(goods.getId());
                        orderSave.setAppgoodsId(appgoodsId);

                        orderSave.setAttribute(goods.getAttribute());
                        orderSave.setQuantity(goods.getCount());

                        orderSaveArrayList.add(orderSave);
                    }

                    OrderSaveList orderSaveList = new OrderSaveList();
                    orderSaveList.setUserId(user.getUserId());
                    orderSaveList.setSessionid(user.getSessionid());
                    orderSaveList.setOrder(orderSaveArrayList);

                    new OrderSaveTask().execute(orderSaveList);

                }
                else
                {
                    String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                    String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息
                    showMsg(result, errorMsg, extraMsg);
                }
            }
        }
    }

    //生成订单
    public class OrderSaveTask extends AsyncTask<OrderSaveList, Void, OrderSaveList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OrderSaveList doInBackground(OrderSaveList... orderSaveLists) {
            return new OrderAction(OrderPayActivity.this).orderSaveAction(orderSaveLists[0]);
        }

        @Override
        protected void onPostExecute(OrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderSubmitPopup.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity.this);
                    builder.setMessage("订单生成成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderPayActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderPayActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderPayActivity.this, "订单生成失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //预订生成订单
    public class YyOrderSaveTask extends AsyncTask<YyOrderSaveList, Void, YyOrderSaveList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected YyOrderSaveList doInBackground(YyOrderSaveList... yyOrderSaveLists) {
            return new OrderAction(OrderPayActivity.this).yyOrderSaveAction(yyOrderSaveLists[0]);
        }

        @Override
        protected void onPostExecute(YyOrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity.this);
                    builder.setMessage("预约订单生成成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderPayActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderPayActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderPayActivity.this, "订单生成失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //上门试衣生成订单
    public class SmOrderSaveTask extends AsyncTask<SmOrderSaveList, Void, SmOrderSaveList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SmOrderSaveList doInBackground(SmOrderSaveList... smOrderSaveLists) {
            return  new OrderAction(OrderPayActivity.this).smOrderSaveAction(smOrderSaveLists[0]);

        }

        @Override
        protected void onPostExecute(SmOrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity.this);
                    builder.setMessage("上门试衣订单生成成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderPayActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderPayActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderPayActivity.this, "订单生成失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void showMsg(String title, String msg1, String msg2) {
        String str = title;
        if (null !=msg1 && msg1.length() != 0) {
            str += "\n" + msg1;
        }
        if (null !=msg2 && msg2.length() != 0) {
            str += "\n" + msg2;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(OrderPayActivity.this);
        builder.setMessage(str);
        builder.setTitle("提示");
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

}
