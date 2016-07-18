package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.utils.BitmapTools;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class DevActivity extends Activity {
    private Button weiboShare;
    private Button wxShare;
    private Button qqShare;
    private Button kuaidiBtn;
    private Spinner kuaidiTypeSpinner;
    private EditText kuaidiPostidEditText;
    private Button animationBtn;
    private Button themeActivityBtn;
    private TextView imieTextView;
    private AppContext appContext;
//    private RecyclerView recyclerViewTest;
//    private LinearLayoutManager linearLayoutManager;
//    private RecyclerViewAdapter recyclerViewAdapter;
    private String kuaidiType = null;
    private String kuaidiPostid = null;
    private TextView handleTextView;
    private Button weiboApiBtn;
    private UMShareAPI mShareAPI;
    private Button resolverBtn;
    private Button chooseImageBtn;
    private Button cameraBtn;
    private Bitmap img = null;
    private ImageView imgView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev);
        appContext = (AppContext) getApplicationContext();

        weiboShare = (Button) findViewById(R.id.weibo_share);
        weiboShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(DevActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");

                ShareAction shareAction = new ShareAction(DevActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.SINA);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("微博分享测试");
                shareAction.withTargetUrl("https://www.baidu.com");
//                shareAction.withTitle("微博分享测试内容");
//                        .withTargetUrl("https://www.baidu.com/")
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();
            }
        });

        wxShare = (Button) findViewById(R.id.wx_share);
        wxShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(DevActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");

                ShareAction shareAction = new ShareAction(DevActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("微信分享测试");
                shareAction.withTitle("微信分享测试内容");
//                        .withTargetUrl("https://www.baidu.com/")
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();
            }
        });

        qqShare = (Button) findViewById(R.id.qq_share);
        qqShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UMImage image = new UMImage(DevActivity.this, "http://www.umeng.com/images/pic/social/integrated_3.png");

                ShareAction shareAction = new ShareAction(DevActivity.this);
                shareAction.setPlatform(SHARE_MEDIA.QZONE);
                shareAction.setCallback(testmulListener);

                shareAction.withMedia(image);
                shareAction.withText("QQ分享测试");
                shareAction.withTitle("QQ分享测试内容");
//                        .withTargetUrl("https://www.baidu.com/")
                //.withMedia(new UMEmoji(ShareActivity.this,"http://img.newyx.net/news_img/201306/20/1371714170_1812223777.gif"))
                shareAction.share();
            }
        });
        kuaidiTypeSpinner = (Spinner) findViewById(R.id.kuaidi_type);

        ArrayList<String> kuaidiTypeList = new ArrayList<String>();
        kuaidiTypeList.add("aae全球专递");
        kuaidiTypeList.add("安捷快递");
        kuaidiTypeList.add("安信达快递");
        kuaidiTypeList.add("彪记快递");
        kuaidiTypeList.add("韵达快运");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kuaidiTypeList);
        kuaidiTypeSpinner.setAdapter(arrayAdapter);
        kuaidiTypeSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                kuaidiType = arrayAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kuaidiType = null;
            }
        });

        kuaidiPostidEditText = (EditText) findViewById(R.id.kuaidi_postid);

        kuaidiBtn = (Button) findViewById(R.id.kuaidi);
        kuaidiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kuaidiPostid = kuaidiPostidEditText.getText().toString();
                if(CommonUtils.isValueEmpty(kuaidiPostid))
                {
                    Toast.makeText(DevActivity.this, "请填写单号", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(CommonUtils.isValueEmpty(kuaidiType))
                {
                    Toast.makeText(DevActivity.this, "选择快递公司", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(DevActivity.this, KuaidiActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", kuaidiType);
                bundle.putString("postid", kuaidiPostid);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                startActivity(intent);

            }
        });

        animationBtn = (Button) findViewById(R.id.animation);
        animationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevActivity.this, AnimationActivity.class);
                startActivity(intent);

            }
        });

        themeActivityBtn = (Button) findViewById(R.id.theme_activity);
        themeActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DevActivity.this, ThemeActivity.class);
                startActivity(intent);

            }
        });

//        recyclerViewTest = (RecyclerView) findViewById(R.id.recycler_view_test);
//        linearLayoutManager = new LinearLayoutManager(this);
//        recyclerViewTest.setLayoutManager();

        imieTextView = (TextView) findViewById(R.id.imie);
        imieTextView.setText(appContext.getImie());

//        TelephonyManager tm = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);

        handleTextView = (TextView) findViewById(R.id.handle_info);
        handleTextView.setText(getHandSetInfo());

        weiboApiBtn = (Button) findViewById(R.id.weibo_api);
        weiboApiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //微博登录
                SHARE_MEDIA platform = SHARE_MEDIA.SINA;
                mShareAPI.doOauthVerify(DevActivity.this, platform, new UMAuthListener() {
                    @Override
                    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
                        Log.v("weibo login", map.toString());

                        Intent intent = new Intent();
                        intent.setClass(DevActivity.this, WeiboActivity.class);
                        startActivity(intent);

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
                        Toast.makeText( getApplicationContext(), "微博登录失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media, int i) {
                        Toast.makeText( getApplicationContext(), "微博登录取消", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        mShareAPI = UMShareAPI.get(this);
        resolverBtn = (Button) findViewById(R.id.resolver);
        resolverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                getContentResolver().query()

            }
        });

        chooseImageBtn = (Button) findViewById(R.id.choose_image);
        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开相册
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent.putExtra("return-data", true);

                startActivityForResult(intent, StaticValues.REQUEST_CODE_IMAGE_UPLOAD);

            }
        });
        cameraBtn = (Button) findViewById(R.id.camera);
        cameraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //拍照
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, StaticValues.REQUEST_CODE_IMAGE_CAMERA);

            }
        });
        imgView = (ImageView) findViewById(R.id.img);

    }

    private UMShareListener testmulListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(DevActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(DevActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(DevActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** attention to this below ,must add this**/
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == StaticValues.REQUEST_CODE_IMAGE_UPLOAD && resultCode == RESULT_OK) {
            if(data != null)
            {
                ContentResolver resolver = getContentResolver();
                Uri originalUri = data.getData();
                try {
                    img = BitmapTools.zoomImg(MediaStore.Images.Media.getBitmap(resolver, originalUri));

                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (img != null)
                {
                    //获取Bitmap
                    imgView.setImageBitmap(img);

                }

            }
            else
            {
                Toast.makeText(DevActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == StaticValues.REQUEST_CODE_IMAGE_CAMERA && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            img = (Bitmap)bundle.get("data");
            if (img != null)
            {
                //获取Bitmap
                imgView.setImageBitmap(img);

            }
            else
            {
                Toast.makeText(DevActivity.this, "图片读取失败", Toast.LENGTH_SHORT).show();
            }

        }

    }

    private String getHandSetInfo(){
        try{
            String handSetInfo=
                    "手机型号:" + android.os.Build.MODEL +
                            ",SDK版本:" + android.os.Build.VERSION.SDK +
                            ",系统版本:" + android.os.Build.VERSION.RELEASE+
                            ",软件版本:"+getAppVersionName(DevActivity.this);
            return handSetInfo;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }

    //获取当前版本号
    private  String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo("cn.testgethandsetinfo", 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }


//    public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
//    {
//
//
//        @Override
//        public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            return null;
//        }
//
//        @Override
//        public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return 0;
//        }
//
//        //自定义的ViewHolder，持有每个Item的的所有界面元素
//        public class ViewHolder extends RecyclerView.ViewHolder {
//            public TextView mTextView;
//            public ViewHolder(View view){
//                super(view);
//                mTextView = (TextView) view.findViewById(R.id.text);
//            }
//        }
//
//    }

}
