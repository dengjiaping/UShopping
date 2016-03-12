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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.adapter.GoodsVoucherAdapter;
import com.brand.ushopping.adapter.OrderGoodsItemAdapter;
import com.brand.ushopping.model.AppaddressId;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.AppvoucherId;
import com.brand.ushopping.model.Charge;
import com.brand.ushopping.model.ClientCharge;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.OrderSave;
import com.brand.ushopping.model.OrderSaveList;
import com.brand.ushopping.model.OrderSuccess;
import com.brand.ushopping.model.SmOrderSave;
import com.brand.ushopping.model.SmOrderSaveList;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.YyOrderSave;
import com.brand.ushopping.model.YyOrderSaveList;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.MyListView;
import com.brand.ushopping.widget.OrderSubmitPopup;
import com.pingplusplus.android.PaymentActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.brand.ushopping.R.id.root_view;

public class OrderConfirmActivity extends Activity {
    private AppContext appContext;
    private User user;
    private Goods goods;
    private AppbrandId appbrandId;
    private ArrayList<Goods> goodsList;
    private View rootView;
    private Charge chargeObj;
    private int paymentMode;
    private int operation = StaticValues.ORDER_COMFIRM_GEN_ORDER;

    private TextView consigneeTextView;
    private TextView mobileTextView;
    private TextView deaddressTextView;
    private ListView goodsListView;
    private TextView storeNameTextView;
    private TextView totalCountTextView;
    private TextView summaryTextView;
    private ImageView backBtn;
    private TextView titleTextView;
    private Button orderSubmitBtn;
    private TextView addressSelectBtn;

    private OrderSubmitPopup orderSubmitPopup;

    private TextView voucherBtn;
    private MyListView voucherListView;
    private ArrayList<AppvoucherId> appvoucherIds;
    private GoodsVoucherAdapter goodsVoucherAdapter;

    private int totalCount = 0;
    private double summary = 0;

    private long reservationDate;
    private int boughtType;
    private String orderNo;

    private Date timeShop;  //到店预约时间

    private Long addressId;
    private String deaddress;

    public OrderConfirmActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_order_confirm);

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
        addressSelectBtn = (TextView) findViewById(R.id.address_select);

        Bundle bundle = getIntent().getExtras();
        goodsList = bundle.getParcelableArrayList("goods");
        reservationDate = bundle.getLong("reservationDate");
        boughtType = bundle.getInt("boughtType");
        operation = bundle.getInt("operation", StaticValues.ORDER_COMFIRM_GEN_ORDER);
        orderNo = bundle.getString("orderNo", null);
        appbrandId = goodsList.get(0).getAppbrandId();

        consigneeTextView.setText(user.getUserName());
        mobileTextView.setText(user.getMobile());

        addressId = appContext.getDefaultAddressId();
        deaddress = appContext.getDefaultAddress();

        deaddressTextView.setText(deaddress);

        List listData = new ArrayList<Map<String,Object>>();
        for(Goods goods: goodsList)
        {
            Map line = new HashMap();

            int count = goods.getCount();
            double price = goods.getPromotionPrice();

            line.put("id", goods.getId());
            line.put("img", goods.getLogopicUrl());
            line.put("goodsName", goods.getGoodsName());
            line.put("attribute", goods.getAttribute());
            line.put("price", price);
            line.put("count", count);
            line.put("customerFlag", StaticValues.CUSTOMER_FLAG_NONE);

            listData.add(line);

        }
        OrderGoodsItemAdapter adapter = new OrderGoodsItemAdapter(listData, OrderConfirmActivity.this);
        goodsListView.setAdapter(adapter);

        calculateSummary();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

//        int totalHeight = 0;
//        for (int i = 0; i < adapter.getCount(); i++) {
//            View listItem = adapter.getView(i, null, goodsListView);
//            listItem.measure(0, 0);
//            totalHeight += listItem.getMeasuredHeight();
//        }

//        ViewGroup.LayoutParams params = goodsListView.getLayoutParams();
//        params.height = totalHeight + (goodsListView.getDividerHeight() * (adapter.getCount() - 1));
//        ((ViewGroup.MarginLayoutParams)params).setMargins(10, 10, 10, 10);
//        goodsListView.setLayoutParams(params);

        if(appbrandId != null)
        {
            storeNameTextView.setText(appbrandId.getBrandName());
        }

        orderSubmitBtn = (Button) findViewById(R.id.order_submit);
        orderSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addressId == 0) {
                    Toast.makeText(OrderConfirmActivity.this, "没有设置默认地址,请到地址列表选择默认地址", Toast.LENGTH_SHORT).show();
                    return;
                }

                switch (operation) {
                    case StaticValues.ORDER_COMFIRM_GEN_ORDER:
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

                                orderSubmitPopup = new OrderSubmitPopup(OrderConfirmActivity.this, clientCharge);
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
                                    appaddressId.setId(addressId);
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
                                    appaddressId.setId(addressId);
                                    smOrderSave.setAppaddressId(appaddressId);

                                    //商品信息
                                    AppgoodsId appgoodsId = new AppgoodsId();
                                    appgoodsId.setId(goods.getId());
                                    smOrderSave.setAppgoodsId(appgoodsId);

                                    smOrderSave.setAttribute(goods.getAttribute());
                                    smOrderSave.setQuantity(goods.getCount());

                                    smOrderSave.setMoney(goods.getPromotionPrice() * goods.getCount());

                                    smOrderSave.setTimeShop(new Date(reservationDate));

                                    smOrderSaveArrayList.add(smOrderSave);
                                }

                                smOrderSaveList.setSmorder(smOrderSaveArrayList);

                                new SmOrderSaveTask().execute(smOrderSaveList);
                                break;
                        }
                        break;

                    case StaticValues.ORDER_COMFIRM_PAY:
                        //订单支付
//                        Toast.makeText(OrderConfirmActivity.this, "订单支付", Toast.LENGTH_SHORT).show();
                        switch (boughtType) {
                            case StaticValues.BOUTHT_TYPE_NORMAL:
                                //普通
                                Toast.makeText(OrderConfirmActivity.this, "普通订单支付", Toast.LENGTH_SHORT).show();

                                break;

                            case StaticValues.BOUTHT_TYPE_TRYIT:
                            case StaticValues.BOUTHT_TYPE_RESERVATION:
                                //预约 //上门
                                ClientCharge clientCharge = new ClientCharge();
                                clientCharge.setUserId(user.getUserId());
                                clientCharge.setSessionid(user.getSessionid());
                                clientCharge.setSummary(summary);
                                int sumConverted = (int) (summary * 100);
                                clientCharge.setAmountVal(sumConverted);

                                orderSubmitPopup = new OrderSubmitPopup(OrderConfirmActivity.this, clientCharge);
                                orderSubmitPopup.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                                break;

                                /*
                                YyOrderSaveList yyOrderSaveList = new YyOrderSaveList();

                                yyOrderSaveList.setUserId(user.getUserId());
                                yyOrderSaveList.setSessionid(user.getSessionid());

                                ArrayList<YyOrderSave> yyOrderSaveArrayList = new ArrayList<YyOrderSave>();
                                for (Goods goods: goodsList)
                                {
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
                                */

                                /*
                                SmOrderSaveList smOrderSaveList = new SmOrderSaveList();

                                smOrderSaveList.setUserId(user.getUserId());
                                smOrderSaveList.setSessionid(user.getSessionid());

                                ArrayList<SmOrderSave> smOrderSaveArrayList = new ArrayList<SmOrderSave>();
                                for (Goods goods: goodsList)
                                {
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
                                */
                        }


                        break;
                }

            }
        });

        addressSelectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmActivity.this, AddressesActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putInt("enterMode", StaticValues.ADDRESSES_ENTER_MODE_PICK);
                intent.putExtras(bundle1);

                startActivityForResult(intent, StaticValues.CODE_ADDRESSES_PICK);

            }
        });

        voucherBtn = (TextView) findViewById(R.id.voucher);
        voucherBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmActivity.this, VoucherActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("enterType", StaticValues.VOUCHER_ENTER_PICK);
                intent.putExtras(bundle);

                startActivityForResult(intent, StaticValues.CODE_VOUCHER_PICK);

            }
        });

        voucherListView = (MyListView) findViewById(R.id.voucher_list);
        appvoucherIds = new ArrayList<AppvoucherId>();

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
                    OrderSuccess orderSuccess;
                    switch (boughtType)
                    {
                        case StaticValues.BOUTHT_TYPE_NORMAL:
                            // 生成普通订单
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
                                appaddressId.setId(addressId);
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

                            //优惠券
                            ArrayList<Long> userVoucherId = new ArrayList<Long>();
                            for(AppvoucherId appvoucherId: appvoucherIds)
                            {
                                userVoucherId.add(appvoucherId.getId());

                            }
                            orderSaveList.setUserVoucherId(userVoucherId);

                            new OrderSaveTask().execute(orderSaveList);

                            break;

                        case StaticValues.BOUTHT_TYPE_RESERVATION:
                            // 到店预订订单
                            orderSuccess = new OrderSuccess();
                            orderSuccess.setUserId(user.getUserId());
                            orderSuccess.setSessionid(user.getSessionid());
                            orderSuccess.setOrderNo(orderNo);
                            orderSuccess.setFlag(1);

                            new YyOrderSuccessActionTask().execute(orderSuccess);
                            break;
                        case StaticValues.BOUTHT_TYPE_TRYIT:
                            // 上门试衣订单
                            orderSuccess = new OrderSuccess();
                            orderSuccess.setUserId(user.getUserId());
                            orderSuccess.setSessionid(user.getSessionid());
                            orderSuccess.setOrderNo(orderNo);
                            orderSuccess.setFlag(1);

                            new SmOrderSuccessActionTask().execute(orderSuccess);

                            break;

                    }

                }
                if(result.equals(StaticValues.PAYMENT_RESULT_FAIL))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("支付失败");
                    builder.setTitle("提示");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();

                }
                if(result.equals(StaticValues.PAYMENT_RESULT_CANCEL))
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("支付取消");
                    builder.setTitle("提示");
                    builder.setPositiveButton("OK", null);
                    builder.create().show();
                }

            }
        }

        if (requestCode == StaticValues.CODE_ADDRESSES_PICK)
        {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                addressId = bundle.getLong("addressId");
                deaddress = bundle.getString("deaddress");

                deaddressTextView.setText(deaddress);

            }
        }

        if (requestCode == StaticValues.CODE_VOUCHER_PICK)
        {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                AppvoucherId appvoucherId = bundle.getParcelable("voucher");

                //判断是否允许添加
                AppbrandId appbrandId = appvoucherId.getAppbrandId();
                //全品牌
                for(AppvoucherId appvoucherId1: appvoucherIds)
                {
                    if(appvoucherId.getId() == appvoucherId1.getId())
                    {
                        Toast.makeText(OrderConfirmActivity.this, "您已添加此优惠券,不能重复添加", Toast.LENGTH_SHORT).show();
                        return;

                    }

                }

//                if(appbrandId != null)
//                {
//                    //品牌
//                    for(Goods goods: goodsList)
//                    {
//                        AppbrandId goodsAppbrandId = goods.getAppbrandId();
//                        if(goodsAppbrandId != null)
//                        {
//                            if(goodsAppbrandId.getId() == appbrandId.getId())
//                            {
//                                Toast.makeText(OrderConfirmActivity.this, "您已添加此品牌的优惠券,不能重复添加", Toast.LENGTH_SHORT).show();
//                                return;
//                            }
//
//                        }
//
//                    }
//
//                }
//                else
//                {
//
//
//                }

                appvoucherIds.add(appvoucherId);

                voucherReload();
                calculateSummary();

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
            return new OrderAction().orderSaveAction(orderSaveLists[0]);
        }

        @Override
        protected void onPostExecute(OrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderSubmitPopup.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("订单生成成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderConfirmActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderConfirmActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderConfirmActivity.this, "订单生成失败", Toast.LENGTH_SHORT).show();
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
            return new OrderAction().yyOrderSaveAction(yyOrderSaveLists[0]);
        }

        @Override
        protected void onPostExecute(YyOrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("预约订单生成成功,请到到店预订订单页面支付");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderConfirmActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderConfirmActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderConfirmActivity.this, "订单生成失败", Toast.LENGTH_SHORT).show();
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
            return  new OrderAction().smOrderSaveAction(smOrderSaveLists[0]);

        }

        @Override
        protected void onPostExecute(SmOrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("上门试衣订单生成成功,请到上门试衣订单页面支付");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderConfirmActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderConfirmActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderConfirmActivity.this, "订单生成失败", Toast.LENGTH_SHORT).show();
            }
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
                    orderSubmitPopup.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("预约订单支付成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderConfirmActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderConfirmActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderConfirmActivity.this, "订单支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //上门订单支付成功
    public class SmOrderSuccessActionTask extends AsyncTask<OrderSuccess, Void, OrderSuccess>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected OrderSuccess doInBackground(OrderSuccess... orderSuccesses) {
            return new OrderAction().smOrderSuccessAction(orderSuccesses[0]);

        }

        @Override
        protected void onPostExecute(OrderSuccess result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderSubmitPopup.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("上门订单支付成功");
                    builder.setTitle("提示");
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            OrderConfirmActivity.this.finish();

                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(OrderConfirmActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(OrderConfirmActivity.this, "上门订单支付失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void voucherReload()
    {
        voucherListView.removeAllViewsInLayout();

        List listData = new ArrayList<Map<String,Object>>();
        for(AppvoucherId appvoucherId: appvoucherIds)
        {
            Map line = new HashMap();

            line.put("id", appvoucherId.getId());
            line.put("money01", appvoucherId.getMoney01());
            line.put("money02", appvoucherId.getMoney02());
            line.put("name", appvoucherId.getName());

            listData.add(line);

        }
        goodsVoucherAdapter = new GoodsVoucherAdapter(listData, OrderConfirmActivity.this);
        voucherListView.setAdapter(goodsVoucherAdapter);

    }

    public int getBoughtType() {
        return boughtType;
    }

    public void setBoughtType(int boughtType) {
        this.boughtType = boughtType;
    }

    public int getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(int paymentMode) {
        this.paymentMode = paymentMode;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if(orderSubmitPopup != null)
        {
            orderSubmitPopup.dismiss();

        }

    }

    public void removeVoucher(long id)
    {
        for(int a=0; a<appvoucherIds.size(); a++)
        {
            AppvoucherId appvoucherId = appvoucherIds.get(a);
            if(appvoucherId.getId() == id)
            {
                appvoucherIds.remove(a);
                voucherReload();
                calculateSummary();

            }
        }

    }

    //计算总金额
    public void calculateSummary()
    {
        totalCount = 0;
        summary = 0;

        for(Goods goods: goodsList)
        {
            Map line = new HashMap();

            int count = goods.getCount();
            double price = goods.getPromotionPrice();

            totalCount += count;
            summary += price * count;
        }

        //优惠券
        if(appvoucherIds != null)
        {
            for(AppvoucherId appvoucherId: appvoucherIds)
            {
                summary -= appvoucherId.getMoney01();
            }

        }

        totalCountTextView.setText(Integer.toString(totalCount));
        summaryTextView.setText(CommonUtils.df.format(summary));
    }

}
