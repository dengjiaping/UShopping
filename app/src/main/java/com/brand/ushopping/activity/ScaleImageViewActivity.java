package com.brand.ushopping.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import com.brand.ushopping.widget.ScaleImageView;

import com.brand.ushopping.R;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ScaleImageViewActivity extends AppCompatActivity {
    private ScaleImageView imageview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scale_image_view);

        Bundle bundle = getIntent().getExtras();
        String url = bundle.getString("url");

        imageview = (ScaleImageView) findViewById(R.id.imgview);
        ImageLoader.getInstance().displayImage(url, imageview);


    }
}
