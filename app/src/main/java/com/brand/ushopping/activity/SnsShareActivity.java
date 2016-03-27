package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.R;
import com.brand.ushopping.utils.EnvValues;
import com.brand.ushopping.utils.StaticValues;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

public class SnsShareActivity extends Activity {
    private ViewGroup wxBtn;
    private ViewGroup wxCircleBtn;
    private ViewGroup qqBtn;
    private ViewGroup weiboBtn;
    private ImageView backBtn;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sns_share);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        wxBtn = (ViewGroup) findViewById(R.id.wx);
        wxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UMImage image = new UMImage(SnsShareActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.sns_share));

                ShareAction shareAction = new ShareAction(SnsShareActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("分享链接,领取优惠券");
                shareAction.withTitle("U购优惠券");
                shareAction.withTargetUrl(EnvValues.serverPath + StaticValues.voucherAddress);
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();
            }
        });

        wxCircleBtn = (ViewGroup) findViewById(R.id.wx_circle);
        wxCircleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.sns_share));

                ShareAction shareAction = new ShareAction(SnsShareActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("分享链接,领取优惠券");
                shareAction.withTitle("U购优惠券");
                shareAction.withTargetUrl(EnvValues.serverPath + StaticValues.voucherAddress);
                shareAction.share();
            }
        });

        qqBtn = (ViewGroup) findViewById(R.id.qq);
        qqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.sns_share));

                ShareAction shareAction = new ShareAction(SnsShareActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("分享链接,领取优惠券");
                shareAction.withTitle("U购优惠券");
                shareAction.withTargetUrl(EnvValues.serverPath + StaticValues.voucherAddress);
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();

            }
        });

        weiboBtn = (ViewGroup) findViewById(R.id.weibo);
        weiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.sns_share));
                ShareAction shareAction = new ShareAction(SnsShareActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.SINA);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("分享链接,领取优惠券");
                shareAction.withTitle("U购优惠券");
                shareAction.withTargetUrl(EnvValues.serverPath + StaticValues.voucherAddress);
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();
            }
        });

    }

    public UMShareListener testmulListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(SnsShareActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(SnsShareActivity.this,platform + " 分享失败", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(SnsShareActivity.this, platform + " 分享取消", Toast.LENGTH_SHORT).show();
        }
    };


    //分享返回处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

}
