package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.utils.CommonUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;

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
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
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
