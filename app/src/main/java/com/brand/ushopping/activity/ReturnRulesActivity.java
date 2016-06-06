package com.brand.ushopping.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;

public class ReturnRulesActivity extends Activity {
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

        String content = "七天退换货规则\n" +
                "U购商品支持“七天退换货”，如需换货先退货，再由买家重新下单进行购买。退换货要求如下：\n" +
                "1.\t商品支持7天退换货。\n" +
                "2.\t因质量问题产生的退货，所有邮费由卖家承担，7天退货质量问题的界定为货品破损或残缺。\n" +
                "3. 退货要求具备商品收到的完整包装（包含配饰）、水洗标、logo、吊牌、小票等；购买物品个人破坏不予退换。\n" +
                "4. 商品沾染油渍、异物、异味等不予退换。\n" +
                "5. 非商品质量问题的退换货，由买家承担运费。\n" +
                "6. 为避免由于商品在途中造成经济损失或产生其他纠纷，所有退货商品，买家需电话通知品牌发货方、并及时返回至指定地址。\n" +
                "7. 退款于卖家收到商品后的1-7个工作日内退款，退款最终到账时间以银行办理时间为准。\n" +
                "8. 售后电话：400-966-7876（早9:00-晚17:00）\n";

        contentTextView.setText(content);

    }

}
