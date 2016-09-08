package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;

public class WebViewActivity extends Activity {
    private WebView webView;
    private ImageView backBtn;
    private TextView titleTextView;
    private AppContext appContext;
    private String url;
    private ImageView shareBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.web_view);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
//        titleTextView.setText(this.getTitle().toString());

        Bundle bundle = null;
        try{
            bundle = getIntent().getExtras();
        }catch (Exception e)
        {
            e.printStackTrace();
            bundle = appContext.getBundleObj();
        }
        if(bundle != null)
        {
            url = bundle.getString("url");
        }
        else
        {
            finish();
        }

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// false 显示frameset, true 不显示Frameset
            }
        });
        webView.loadUrl(url);

        shareBtn = (ImageView) findViewById(R.id.share);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入分享页面
                Intent intent = new Intent(WebViewActivity.this, SnsShareActivity.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("title", "U购分享");
                bundle1.putString("url", url);
                intent.putExtras(bundle1);
                startActivity(intent);

            }
        });

    }

}
