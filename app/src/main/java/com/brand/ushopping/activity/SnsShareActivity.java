package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private ImageView wxBtn;
    private ImageView qqBtn;
    private ImageView weiboBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns_share);

        wxBtn = (ImageView)
                findViewById(R.id.wx);
        wxBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                UMImage image = new UMImage(SnsShareActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.logo));

                ShareAction shareAction = new ShareAction(SnsShareActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("分享链接,领取优惠券");
                shareAction.withTitle("U购优惠券");
                shareAction.withTargetUrl(EnvValues.serverPath + StaticValues.voucherAddress);
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();
            }
        });

        qqBtn = (ImageView) findViewById(R.id.qq);
        qqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.logo));

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

        weiboBtn = (ImageView) findViewById(R.id.weibo);
        weiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(SnsShareActivity.this, BitmapFactory.decodeResource(getResources(), R.drawable.logo));
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
            Toast.makeText(SnsShareActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
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
