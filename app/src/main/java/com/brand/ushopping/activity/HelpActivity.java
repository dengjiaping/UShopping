package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;

public class HelpActivity extends Activity {
    private ImageView backBtn;
    private TextView titleTextView;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_help);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        contentTextView = (TextView) findViewById(R.id.content);

        String content = "U购APP模块使用帮助\n" +
                "一.\t首页\n" +
                "1.\t搜索。可搜索衣服名称、品牌等\n" +
                "2.\t类目。例如点开上衣，可显示全部上衣，可根据销量价格等排序\n" +
                "3.\t广告位。可查看广告，点开广告位后返回web格式活动页面\n" +
                "4.\t签到。点开后可进行签到，并可查看签到天数\n" +
                "5.\t上门试衣。选择附近门店-选择品牌-选择喜欢的商品-选择上门时间-生成订单-店铺收到订单通知，并与买确定订单是否还需修改商品和时间-支付（买家可线上支付，也可送货上门后选择下线支付）-完成-可评价，可申请售后。\n" +
                "6.\t到店预订。选择附近门店-选择品牌-选择喜欢的商品-选择到店时间--生成订单-店铺收到订单通知，并与买家确定订单是否还需修改商品和时间-支付（买家可线上支付，也可到店后选择下线支付）-完成-可评价，可申请售后。\n" +
                "7.\tU币。登录U币商城，可用U币兑换礼品。每消费1元获得1U币\n" +
                "8.\t潮流前线。展示单品，并可进行购买支付等\n" +
                "9.\t最新活动。点开后返回web格式活动页面，可查看活动内容\n" +
                "10.\t推荐商品。可推荐品牌滞销款等，下拉显示20条。\n" +
                "二． 品牌\n" +
                "点击品牌后进入品牌列表，进入某品牌后显示该品牌商品、活动内容等，商品可进行新品、价格、销量排序及筛选\n" +
                "三． 主题\n" +
                "点开后返回web格式活动页面，可查看活动内容\n" +
                "四． 我的\n" +
                "1. 可进行登录。注册\n" +
                "2. 可设置收货地址管理、清除缓存、关于我们、检查更新\n" +
                "3. 可查看收藏商品、品牌、历史足迹\n" +
                "4. 可查看待付款、待收货、待评论、退款/售后，并可进行操作\n" +
                "5. 查看所有订单。包含上门试衣、到店预订、普通订单\n" +
                "6. 我的U币。点进入后查看U币数量并同时进入U币商城\n" +
                "7. 我的卡券，将领取的所有卡券，例如优惠券、包邮券等呈现\n" +
                "8. 使用帮助。图片版显示特色功能\n" +
                "9. 反馈信息。提交反馈建议\n";

        contentTextView.setText(content);

    }

}
