package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.GoodsAction;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.action.VoucherAction;
import com.brand.ushopping.adapter.GoodsVoucherAdapter;
import com.brand.ushopping.adapter.ManjianVoucherAdapter;
import com.brand.ushopping.adapter.OrderGoodsItemAdapter;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.AppStoresId;
import com.brand.ushopping.model.AppaddressId;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppgoodsId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.AppvoucherId;
import com.brand.ushopping.model.Charge;
import com.brand.ushopping.model.ClientCharge;
import com.brand.ushopping.model.Goods;
import com.brand.ushopping.model.ManJainVoucher;
import com.brand.ushopping.model.ManJianAll;
import com.brand.ushopping.model.ManJianVoucherItem;
import com.brand.ushopping.model.OrderSave;
import com.brand.ushopping.model.OrderSaveList;
import com.brand.ushopping.model.OrderSuccess;
import com.brand.ushopping.model.SelectChargeId;
import com.brand.ushopping.model.SmOrderSave;
import com.brand.ushopping.model.SmOrderSaveList;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.UserVoucherItem;
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

import static com.brand.ushopping.R.id.manjian_first_money;
import static com.brand.ushopping.R.id.root_view;

public class OrderConfirmActivity extends Activity {
    private AppContext appContext;
    private User user;
    private Goods goods;
    private AppbrandId appbrandId;
    private ArrayList<Goods> goodsList;
    private View rootView;
    private Charge chargeObj = null;
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
    private ViewGroup manjianFirstViewGroup;
    private TextView manjianFirstMoneyTextView;
    private ManJianAll manJianAll;
//    private double manjianFirstMoney = 0;

    private OrderSubmitPopup orderSubmitPopup;

    private TextView voucherBtn;
    private MyListView voucherListView;
    private ArrayList<UserVoucherItem> userVoucherItems;
    private GoodsVoucherAdapter goodsVoucherAdapter;

    private ArrayList<ManJianVoucherItem> manJianVoucherItems = new ArrayList<ManJianVoucherItem>();
    private ArrayList<ManJianVoucherItem> manJianVoucherItemResults;

    private ManjianVoucherAdapter manjianVoucherAdapter;
    private MyListView manjianVoucherListView;

    private int totalCount = 0;
    private double summary = 0;

    private long reservationDate;
    private int boughtType;
    private String orderNo;
    private long storeId;
    private Date timeShop;  //到店预约时间

    private Long addressId;
    private String deaddress;

    private TextView shareBtn;

    private long currentTime = System.currentTimeMillis();
    private List voucherListData = new ArrayList<Map<String,Object>>();
    private List manjianListDatalistData = new ArrayList<Map<String,Object>>();

    private EditText commentEditText;
    private String payMethod = null;
    public boolean chargedCheck = false;
    private ClientCharge clientCharge = null;
    private String charge;
    public OrderConfirmActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        manjianFirstViewGroup = (ViewGroup) findViewById(R.id.manjian_first);
        manjianFirstMoneyTextView = (TextView) findViewById(manjian_first_money);

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
            operation = bundle.getInt("operation", StaticValues.ORDER_COMFIRM_GEN_ORDER);
            orderNo = bundle.getString("orderNo", null);
            storeId = bundle.getLong("storeId", 0);
        }
        else
        {
            finish();
        }

        appbrandId = goodsList.get(0).getAppbrandId();

        if(!CommonUtils.isValueEmpty(user.getUserName()))
        {
            consigneeTextView.setText(user.getUserName());
        }
        mobileTextView.setText(user.getMobile());

        commentEditText = (EditText) findViewById(R.id.comment);

        List listData = new ArrayList<Map<String,Object>>();
        for(Goods goods: goodsList)
        {
            Map line = new HashMap();

            int count = goods.getCount();
            double price = goods.getPromotionPrice();

            line.put("id", goods.getId());
            line.put("img", goods.getLogopicUrl());
            line.put("goodsId", goods.getId());
            line.put("goodsName", goods.getGoodsName());
            line.put("attribute", goods.getAttribute());
            line.put("price", price);
            line.put("count", count);
            line.put("customerFlag", StaticValues.CUSTOMER_FLAG_NONE);
            line.put("context", OrderConfirmActivity.this);
            line.put("boughtType", boughtType);
            line.put("user", user);
            line.put("orderFlag", StaticValues.ORDER_FLAG_ALL);

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

        shareBtn = (TextView) findViewById(R.id.share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderConfirmActivity.this, SnsShareActivity.class);
                startActivity(intent);
                OrderConfirmActivity.this.finish();

            }
        });

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
                        StringBuffer userVoucherId;
                        switch (boughtType) {
                            case StaticValues.BOUTHT_TYPE_NORMAL:
                                //普通订单
                                ClientCharge clientCharge = new ClientCharge();
                                clientCharge.setUserId(user.getUserId());
                                clientCharge.setSessionid(user.getSessionid());
                                clientCharge.setSummary(summary);
                                int sumConverted = (int) (summary * 100);
                                clientCharge.setAmountVal(sumConverted);
                                String comment = commentEditText.getText().toString();
                                if(!CommonUtils.isValueEmpty(comment))
                                {
                                    clientCharge.setSubjectVal(comment);
                                    clientCharge.setBodyVal(comment);

                                }
                                else
                                {
                                    clientCharge.setSubjectVal("暂无备注");
                                    clientCharge.setBodyVal("暂无备注");
                                }

                                orderSubmitPopup = new OrderSubmitPopup(OrderConfirmActivity.this, clientCharge);
                                orderSubmitPopup.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                                break;

                            case StaticValues.BOUTHT_TYPE_RESERVATION:
                                generateYyOrder();
                                break;

                            case StaticValues.BOUTHT_TYPE_TRYIT:
                                generateSmOrder();
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
                                clientCharge.addVersion(OrderConfirmActivity.this);

                                clientCharge.setUserId(user.getUserId());
                                clientCharge.setSessionid(user.getSessionid());
                                clientCharge.setSummary(summary);
                                int sumConverted = (int) (summary * 100);
                                clientCharge.setAmountVal(sumConverted);
                                String comment = commentEditText.getText().toString();
                                if(!CommonUtils.isValueEmpty(comment))
                                {
                                    clientCharge.setSubjectVal(comment);
                                    clientCharge.setBodyVal(comment);

                                }
                                else
                                {
                                    clientCharge.setSubjectVal("暂无备注");
                                    clientCharge.setBodyVal("暂无备注");
                                }
                                clientCharge.setAppOrderType(boughtType);
                                orderSubmitPopup = new OrderSubmitPopup(OrderConfirmActivity.this, clientCharge);
                                orderSubmitPopup.showAtLocation(rootView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                                break;

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
                appContext.setBundleObj(bundle1);
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
                appContext.setBundleObj(bundle);
                startActivityForResult(intent, StaticValues.CODE_VOUCHER_PICK);

            }
        });

        voucherListView = (MyListView) findViewById(R.id.voucher_list);
        userVoucherItems = new ArrayList<UserVoucherItem>();

        manjianVoucherListView = (MyListView) findViewById(R.id.manjian_voucher_list);

        //满减
        ManJainVoucher manJainVoucher = new ManJainVoucher();
        manJainVoucher.setUserId(user.getUserId());
        manJainVoucher.setSessionid(user.getSessionid());
        manJainVoucher.setAppOrderType(this.boughtType);

        new ManJainAllActionTask().execute(manJainVoucher);

        //首单满减
        ManJianAll manJianAll = new ManJianAll();
        manJianAll.setUserId(user.getUserId());
        manJianAll.setSessionid(user.getSessionid());
        manJianAll.setAppOrderType(this.boughtType);
        new ManJainAllFirstActionTask().execute(manJianAll);

        switch (operation)
        {
            case StaticValues.ORDER_COMFIRM_GEN_ORDER:
                //订单提交选择默认地址
                addressId = appContext.getDefaultAddressId();
                deaddress = appContext.getDefaultAddress();
                deaddressTextView.setText(deaddress);

                break;
            case StaticValues.ORDER_COMFIRM_PAY:
                //订单付款只显示地址
                AppaddressId address = goodsList.get(0).getAppaddressId();
                if(address != null)
                {
                    addressId = address.getId();
                    deaddress = appContext.getDeaddressFromId(address.getId());
                    if(!CommonUtils.isValueEmpty(deaddress))
                    {
                        deaddressTextView.setText(deaddress);
                        addressSelectBtn.setVisibility(View.GONE);
                    }
                    else
                    {
                        deaddressTextView.setText(deaddress);
                    }

                }
                else
                {
                    addressId = appContext.getDefaultAddressId();
                    deaddress = appContext.getDefaultAddress();
                    deaddressTextView.setText(deaddress);
                }

                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if(chargedCheck)
        {
            Log.v("charge", "charge check");

        }

    }

    //开始支付,先生成订单再支付
    public void startPay(String charge)
    {
        this.charge = charge;
        this.chargeObj = JSON.parseObject(charge, Charge.class);

        switch (this.boughtType)
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:
                generateOrder();
                break;
            case StaticValues.BOUTHT_TYPE_RESERVATION:
                if(operation == StaticValues.ORDER_COMFIRM_PAY)
                {
//                    this.chargeObj.setOrderNo(this.chargeObj.getOrderNo() + "yy");
//                    this.charge = JSON.toJSONString(this.chargeObj);
                }
                Log.v("charge", this.charge);
                callClientCharge();

                break;
            case StaticValues.BOUTHT_TYPE_TRYIT:
                if(operation == StaticValues.ORDER_COMFIRM_PAY)
                {
//                    this.chargeObj.setOrderNo(this.chargeObj.getOrderNo() + "yy");
//                    this.charge = JSON.toJSONString(this.chargeObj);
                }
                Log.v("charge", this.charge);
                callClientCharge();

                break;
        }

    }

    //唤起支付对象
    public void callClientCharge()
    {
        Intent intent = new Intent();
        String packageName = getPackageName();
        ComponentName componentName = new ComponentName(packageName, packageName + ".wxapi.WXPayEntryActivity");
        intent.setComponent(componentName);
        intent.putExtra(PaymentActivity.EXTRA_CHARGE, this.charge);
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
                 *
                 * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
                 */
                if(!CommonUtils.isValueEmpty(result))
                {
                    if(result.equals(StaticValues.PAYMENT_RESULT_SUCCESS))
                    {
                        //支付成功校验
                        if(!CommonUtils.isValueEmpty(chargeObj.getId()))
                        {
                            SelectChargeId selectChargeId = new SelectChargeId();
                            selectChargeId.setUserId(user.getUserId());
                            selectChargeId.setSessionid(user.getSessionid());
                            selectChargeId.setCharge_id(chargeObj.getId());

                            new SelectChargeIdPingActionTask().execute(selectChargeId);
                        }

                    }
                    if(result.equals(StaticValues.PAYMENT_RESULT_FAIL))
                    {
                        String errorMsg = data.getExtras().getString("error_msg"); // 错误信息
                        String extraMsg = data.getExtras().getString("extra_msg"); // 错误信息

                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                        builder.setMessage("支付失败 error_msg: " + errorMsg + " extra_msg:" + extraMsg);
                        builder.setTitle("提示");
                        builder.setPositiveButton("OK", null);
                        builder.setCancelable(false);
                        builder.create().show();

                    }
                    if(result.equals(StaticValues.PAYMENT_RESULT_CANCEL))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                        builder.setMessage("支付取消");
                        builder.setTitle("提示");
                        builder.setPositiveButton("OK", null);
                        builder.setCancelable(false);
                        builder.create().show();
                    }
                    if(result.equals(StaticValues.PAYMENT_RESULT_INVALID))
                    {
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                        builder.setMessage("您尚未安装银联安全支付控件");
                        builder.setTitle("提示");
                        builder.setPositiveButton("OK", null);
                        builder.setCancelable(false);
                        builder.create().show();
                    }

                }

            }
        }

        // 添加地址
        if (requestCode == StaticValues.CODE_ADDRESSES_PICK)
        {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                Address address = bundle.getParcelable("obj");
                addressId = address.getAddressId();
                deaddress = address.getDeaddress();

                deaddressTextView.setText(deaddress);

            }
        }

        // 添加优惠券
        if (requestCode == StaticValues.CODE_VOUCHER_PICK)
        {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                UserVoucherItem userVoucherItem = bundle.getParcelable("voucher");
                AppvoucherId appvoucherId = userVoucherItem.getAppvoucherId();

                // 判定是否允许添加优惠券
                AppbrandId appbrandId = appvoucherId.getAppbrandId();

                //已有判断
                for(UserVoucherItem userVoucherItem1: userVoucherItems)
                {
                    if(userVoucherItem.getId() == userVoucherItem1.getId())
                    {
                        Toast.makeText(OrderConfirmActivity.this, "您已添加此优惠券,不能重复添加", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                //商品判断
                boolean voucherGoodsJudge = false;
                for(Goods goods: goodsList)
                {
                    //已有该品牌的优惠券
                    AppbrandId appbrandId1 = goods.getAppbrandId();
                    if(appbrandId.getId() == 0 || appbrandId.getId() == appbrandId1.getId())
                    {
                        voucherGoodsJudge = true;
                        break;
                    }
                }
                if(!voucherGoodsJudge)
                {
                    Toast.makeText(OrderConfirmActivity.this, "只能添加购买商品品牌所属的优惠券", Toast.LENGTH_SHORT).show();
                    return;
                }


                //金额限制
                if(summary < appvoucherId.getMoney02())
                {
                    Toast.makeText(OrderConfirmActivity.this, "未达到优惠券满减金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                //过期时间限制
                if(currentTime < appvoucherId.getValidity() || currentTime > appvoucherId.getDays())
                {
                    Toast.makeText(OrderConfirmActivity.this, "优惠券已过期,无法添加", Toast.LENGTH_SHORT).show();
                    return;
                }

                userVoucherItems.add(userVoucherItem);

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
            return new OrderAction(OrderConfirmActivity.this).orderSaveAction(orderSaveLists[0]);
        }

        @Override
        protected void onPostExecute(OrderSaveList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderSubmitPopup.dismiss();

                    //发起付款请求
//                    new ReturnClientChargeTask().execute(clientCharge);
                    callClientCharge();

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
            return new OrderAction(OrderConfirmActivity.this).yyOrderSaveAction(yyOrderSaveLists[0]);
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

                            Intent intent = new Intent(OrderConfirmActivity.this, ReservationActivity.class);
                            startActivity(intent);

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
            return  new OrderAction(OrderConfirmActivity.this).smOrderSaveAction(smOrderSaveLists[0]);

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

                            Intent intent = new Intent(OrderConfirmActivity.this, TryoutActivity.class);
                            startActivity(intent);

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
            return new OrderAction(OrderConfirmActivity.this).yyOrderSuccessAction(orderSuccesses[0]);

        }

        @Override
        protected void onPostExecute(OrderSuccess result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderSubmitPopup.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("预约订单支付成功,分享链接领取优惠券");
                    builder.setTitle("提示");
                    builder.setPositiveButton("分享链接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            //弹出分享优惠券
                            Intent intent = new Intent(OrderConfirmActivity.this, SnsShareActivity.class);
                            startActivity(intent);
                            OrderConfirmActivity.this.finish();

                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OrderConfirmActivity.this.finish();

                        }
                    });

                    builder.setCancelable(false);
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
            return new OrderAction(OrderConfirmActivity.this).smOrderSuccessAction(orderSuccesses[0]);

        }

        @Override
        protected void onPostExecute(OrderSuccess result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    orderSubmitPopup.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("上门订单支付成功,分享链接领取优惠券");
                    builder.setTitle("提示");
                    builder.setPositiveButton("分享链接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            //弹出分享优惠券
                            Intent intent = new Intent(OrderConfirmActivity.this, SnsShareActivity.class);
                            startActivity(intent);
                            OrderConfirmActivity.this.finish();

                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            OrderConfirmActivity.this.finish();

                        }
                    });

                    builder.setCancelable(false);
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

    //满减券列表
    public class ManJainAllActionTask extends AsyncTask<ManJainVoucher, Void, ManJainVoucher>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ManJainVoucher doInBackground(ManJainVoucher... manJainVouchers) {
            return new VoucherAction(OrderConfirmActivity.this).manJainAllAction(manJainVouchers[0]);

        }

        @Override
        protected void onPostExecute(ManJainVoucher result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    manJianVoucherItemResults = result.getManJianVoucherItems();

                    voucherReload();
                    calculateSummary();

                }
                else
                {
                    Toast.makeText(OrderConfirmActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
//            else
//            {
//                Toast.makeText(OrderConfirmActivity.this, "满减券列表获取失败", Toast.LENGTH_SHORT).show();
//            }
        }
    }

    // 满减券是否有效
    private boolean ifManjianVoucherAvalible(ManJianVoucherItem manJianVoucherItem)
    {
        if(summary < manJianVoucherItem.getMoney02())
        {
            return false;
        }

        //过期时间限制
        if(currentTime < manJianVoucherItem.getValidity() || currentTime > manJianVoucherItem.getDays())
        {
            return false;
        }

        return true;
    }

    private void voucherReload()
    {
        voucherListView.removeAllViewsInLayout();
        voucherListData.clear();

        for(UserVoucherItem userVoucherItem: userVoucherItems)
        {
            AppvoucherId appvoucherId = userVoucherItem.getAppvoucherId();
            Map line = new HashMap();

            line.put("id", userVoucherItem.getId());
            line.put("money01", appvoucherId.getMoney01());
            line.put("money02", appvoucherId.getMoney02());
            line.put("name", appvoucherId.getAppbrandId().getBrandName());

            voucherListData.add(line);

        }
        if(goodsVoucherAdapter == null)
        {
            goodsVoucherAdapter = new GoodsVoucherAdapter(voucherListData, OrderConfirmActivity.this);
            voucherListView.setAdapter(goodsVoucherAdapter);

        }
        else
        {
            goodsVoucherAdapter.notifyDataSetChanged();
        }

        // 满减券
        manjianVoucherListView.removeAllViewsInLayout();
        manjianListDatalistData.clear();
        manJianVoucherItems.clear();
        for(int a=0; a<manJianVoucherItemResults.size(); a++)
        {
            ManJianVoucherItem manJianVoucherItem = manJianVoucherItemResults.get(a);
            //判断是否符合满减条件
            if(ifManjianVoucherAvalible(manJianVoucherItem))
            {
                Map line = new HashMap();

                line.put("id", manJianVoucherItem.getId());
                line.put("money", manJianVoucherItem.getMoney01());
                line.put("name", manJianVoucherItem.getName());
                line.put("regulation", manJianVoucherItem.getRegulation());

                manjianListDatalistData.add(line);
                this.manJianVoucherItems.add(manJianVoucherItem);
                break;
            }
        }

        if(manjianVoucherAdapter == null)
        {
            manjianVoucherAdapter = new ManjianVoucherAdapter(manjianListDatalistData, OrderConfirmActivity.this);
            manjianVoucherListView.setAdapter(manjianVoucherAdapter);

        }
        else
        {
            manjianVoucherAdapter.notifyDataSetChanged();
        }
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
        for(int a=0; a<userVoucherItems.size(); a++)
        {
            UserVoucherItem userVoucherItem = userVoucherItems.get(a);
            if(userVoucherItem.getId() == id)
            {
                userVoucherItems.remove(a);
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
        if(userVoucherItems != null)
        {
            for(UserVoucherItem userVoucherItem: userVoucherItems)
            {
                AppvoucherId appvoucherId = userVoucherItem.getAppvoucherId();
                summary -= appvoucherId.getMoney01();

            }

        }

        //满减
        if(this.manJianVoucherItems != null)
        {
            for(ManJianVoucherItem manJianVoucherItem: this.manJianVoucherItems)
            {
                summary -= manJianVoucherItem.getMoney01();

            }

        }

        //首单满减
        if(manJianAll != null)
        {
            summary -= manJianAll.getMoney();
        }

        summary = Double.valueOf(CommonUtils.df.format(summary));

        totalCountTextView.setText(Integer.toString(totalCount));
        summaryTextView.setText(CommonUtils.df.format(summary));
    }

    private int putPaymentMode(String channel )
    {
        int result = 0;
        if(channel.equals(StaticValues.PAY_METHOD_ALIPAY))
        {
            result = StaticValues.ORDER_PAY_ALIPAY;
        }
        if(channel.equals(StaticValues.PAY_METHOD_UPACP))
        {
            result = StaticValues.ORDER_PAY_UNACP;
        }
        if(channel.equals(StaticValues.PAY_METHOD_WX))
        {
            result = StaticValues.ORDER_PAY_WX;
        }
        if(channel.equals(StaticValues.PAY_METHOD_CASH))
        {
            result = StaticValues.ORDER_PAY_CASH;
        }

        return result;
    }

    //Charge对象查询
    public class SelectChargeIdPingActionTask extends AsyncTask<SelectChargeId, Void, SelectChargeId>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SelectChargeId doInBackground(SelectChargeId... selectChargeId) {
            return new GoodsAction(OrderConfirmActivity.this).selectChargeIdPingAction(selectChargeId[0]);

        }

        @Override
        protected void onPostExecute(SelectChargeId result) {
            if(result != null)
            {
                paidSuccess();

            }
            else
            {
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                builder.setMessage("您是否已成功支付?");
                builder.setTitle("提示");
                builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        paidSuccess();

                    }
                });

                builder.setNegativeButton("否", null);

                builder.setCancelable(false);
                builder.create().show();

            }

        }
    }

    //支付成功
    private void paidSuccess()
    {
        OrderSuccess orderSuccess;
        switch (boughtType)
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:
                    AlertDialog.Builder builder = new AlertDialog.Builder(OrderConfirmActivity.this);
                    builder.setMessage("订单生成成功,分享链接领取优惠券");
                    builder.setTitle("提示");
                    builder.setPositiveButton("分享链接", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();

                            //弹出分享优惠券
                            Intent intent = new Intent(OrderConfirmActivity.this, SnsShareActivity.class);
                            startActivity(intent);
                            OrderConfirmActivity.this.finish();

                        }
                    });

                    builder.setNegativeButton("查看订单", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Intent intent = new Intent(OrderConfirmActivity.this, OrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("enterType", StaticValues.ORDER_FLAG_PAID);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            appContext.setBundleObj(bundle);
                            OrderConfirmActivity.this.finish();

                        }
                    });

                    builder.setCancelable(false);
                    builder.create().show();

                break;

            case StaticValues.BOUTHT_TYPE_RESERVATION:
                // 到店预订订单
                yyOrderSuccess();
                break;
            case StaticValues.BOUTHT_TYPE_TRYIT:
                // 上门试衣订单
                smOrderSuccess();

                break;

        }

    }

    //生成普通订单
    private void generateOrder()
    {
        ArrayList<OrderSave> orderSaveArrayList = new ArrayList<OrderSave>();
//                for (Goods goods: goodsList)
        for(int a=0; a<goodsList.size(); a++)
        {
            Goods goods = goodsList.get(a);
            OrderSave orderSave = new OrderSave();

            orderSave.setOrderNo(this.chargeObj.getOrderNo());
            orderSave.setMoney(goods.getPromotionPrice());

            if(a == 0)
            {
                //优惠券扣除到第一个商品中
                double money = orderSave.getMoney();
                for(UserVoucherItem userVoucherItem: userVoucherItems)
                {
                    money -= userVoucherItem.getAppvoucherId().getMoney01();

                }
                //首单满减
                if(manJianAll != null)
                {
                    money -= manJianAll.getMoney();
                }

                orderSave.setMoney(money);

            }

            orderSave.setPaymentMode(StaticValues.ORDER_PAY_CASH);
            String channel = chargeObj.getChannel();

            orderSave.setPaymentMode(putPaymentMode(channel));

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

        //添加优惠券
        orderSaveArrayList.get(0).setUserVoucherId(addVouchers(userVoucherItems));
        //添加满减券
//        orderSaveArrayList.get(0).setAppVoucherId(manJianVoucherItems.get(0).getId());
        //添加首单优惠
        if(manJianAll != null)
        {
            orderSaveArrayList.get(0).setFirstAppsmorderId(manJianAll.getId());
        }

        OrderSaveList orderSaveList = new OrderSaveList();
        orderSaveList.setUserId(user.getUserId());
        orderSaveList.setSessionid(user.getSessionid());
        orderSaveList.setOrder(orderSaveArrayList);

        new OrderSaveTask().execute(orderSaveList);
    }

    public void generateSmOrder()
    {
        //生成上门订单
        SmOrderSaveList smOrderSaveList = new SmOrderSaveList();

        smOrderSaveList.setUserId(user.getUserId());
        smOrderSaveList.setSessionid(user.getSessionid());

        ArrayList<SmOrderSave> smOrderSaveArrayList = new ArrayList<SmOrderSave>();
        for(int a=0; a<goodsList.size(); a++)
        {
            Goods goods = goodsList.get(a);
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

            //品牌信息
            appgoodsId.setAppbrandId(goods.getAppbrandId());

            smOrderSave.setAppgoodsId(appgoodsId);

            smOrderSave.setAttribute(goods.getAttribute());
            smOrderSave.setQuantity(goods.getCount());

            smOrderSave.setMoney(goods.getPromotionPrice());
            //优惠券扣除到第一个商品中
            if(a == 0)
            {
                double money = smOrderSave.getMoney();
                for(UserVoucherItem userVoucherItem: userVoucherItems)
                {
                    money -= userVoucherItem.getAppvoucherId().getMoney01();
                }
                //首单满减
                if(manJianAll != null)
                {
                    money -= manJianAll.getMoney();
                }

                smOrderSave.setMoney(money);

            }

            smOrderSave.setTimeShop(new Date(reservationDate));

            //店铺信息
            AppStoresId appStoresId = new AppStoresId();
            appStoresId.setId(storeId);
            smOrderSave.setAppStoresId(appStoresId);

            smOrderSaveArrayList.add(smOrderSave);
        }

        //添加优惠券
        smOrderSaveArrayList.get(0).setUserVoucherId(addVouchers(userVoucherItems));
        //添加满减券
//                                smOrderSaveArrayList.get(0).setAppVoucherId(manJianVoucherItems.get(0).getId());
        //添加首单优惠
        if(manJianAll != null)
        {
            smOrderSaveArrayList.get(0).setFirstAppsmorderId(manJianAll.getId());
        }

        smOrderSaveList.setSmorder(smOrderSaveArrayList);

        new SmOrderSaveTask().execute(smOrderSaveList);
    }

    //生成预约订单
    public void generateYyOrder()
    {
        YyOrderSaveList yyOrderSaveList = new YyOrderSaveList();

        yyOrderSaveList.setUserId(user.getUserId());
        yyOrderSaveList.setSessionid(user.getSessionid());

        ArrayList<YyOrderSave> yyOrderSaveArrayList = new ArrayList<YyOrderSave>();
        for(int a=0; a<goodsList.size(); a++)
        {
            Goods goods = goodsList.get(a);
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

            yyOrderSave.setMoney(goods.getPromotionPrice());
            //优惠券扣除到第一个商品中
            if(a == 0)
            {
                double money = yyOrderSave.getMoney();
                for(UserVoucherItem userVoucherItem: userVoucherItems)
                {
                    money -= userVoucherItem.getAppvoucherId().getMoney01();
                }
                //首单满减
                if(manJianAll != null)
                {
                    money -= manJianAll.getMoney();
                }

                yyOrderSave.setMoney(money);

            }

            yyOrderSave.setTimeShop(new Date(reservationDate));

            //店铺信息
            AppStoresId appStoresId = new AppStoresId();
            appStoresId.setId(storeId);
            yyOrderSave.setAppStoresId(appStoresId);

            yyOrderSaveArrayList.add(yyOrderSave);
        }

        //添加优惠券
        yyOrderSaveArrayList.get(0).setUserVoucherId(addVouchers(userVoucherItems));

        //添加满减券
//                                yyOrderSaveArrayList.get(0).setAppVoucherId(manJianVoucherItems.get(0).getId());
        //添加首单优惠
        if(manJianAll != null)
        {
            yyOrderSaveArrayList.get(0).setFirstAppsmorderId(manJianAll.getId());
        }

        yyOrderSaveList.setYyorder(yyOrderSaveArrayList);

        new YyOrderSaveTask().execute(yyOrderSaveList);
    }


    public void yyOrderSuccess()
    {
        OrderSuccess orderSuccess = new OrderSuccess();
        orderSuccess.setUserId(user.getUserId());
        orderSuccess.setSessionid(user.getSessionid());
        orderSuccess.setOrderNo(orderNo);
        orderSuccess.setFlag(1);

        String channel = chargeObj.getChannel();
        orderSuccess.setPaymentMode(putPaymentMode(channel));

        new YyOrderSuccessActionTask().execute(orderSuccess);
    }

    public void smOrderSuccess()
    {
        OrderSuccess orderSuccess = new OrderSuccess();
        orderSuccess.setUserId(user.getUserId());
        orderSuccess.setSessionid(user.getSessionid());
        orderSuccess.setOrderNo(orderNo);
        orderSuccess.setFlag(1);

        String channel1 = chargeObj.getChannel();
        orderSuccess.setPaymentMode(putPaymentMode(channel1));

        new SmOrderSuccessActionTask().execute(orderSuccess);
    }


    //添加优惠券
    private String addVouchers(ArrayList<UserVoucherItem> userVoucherItems)
    {
        if(userVoucherItems != null && !userVoucherItems.isEmpty())
        {
            StringBuffer userVoucherId = new StringBuffer();

            for(UserVoucherItem userVoucherItem: userVoucherItems)
            {
                userVoucherId.append(userVoucherItem.getId());
                userVoucherId.append(',');

            }
            return userVoucherId.toString();
        }
        else
        {
            return null;
        }
    }

    //首单满减
    public class ManJainAllFirstActionTask extends AsyncTask<ManJianAll, Void, ManJianAll>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ManJianAll doInBackground(ManJianAll... manJianAlls) {
            return new OrderAction(OrderConfirmActivity.this).manJainAllAction(manJianAlls[0]);

        }

        @Override
        protected void onPostExecute(ManJianAll result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    manjianFirstViewGroup.setVisibility(View.VISIBLE);
                    manJianAll = result;
                    manjianFirstMoneyTextView.setText(Double.toString(result.getMoney()));
                    calculateSummary();
                }

            }

        }
    }

}
