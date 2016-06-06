package com.brand.ushopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;

public class KuaidiActivity extends AppCompatActivity {
    private WebView webView;
    private ImageView backBtn;
    private TextView titleTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_kuaidi);

        Bundle bundle = getIntent().getExtras();
        String type = bundle.getString("type");
        String postid = bundle.getString("postid");

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

//        String url = bundle.getString("url");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;// false 显示frameset, true 不显示Frameset
            }
        });
        //1901463879501 yunda
        String url = "http://m.kuaidi100.com/index_all.html?type="+type+"&postid="+postid;

        webView.loadUrl(url);

    }
}
