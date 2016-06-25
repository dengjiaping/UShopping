package com.brand.ushopping.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.brand.ushopping.R;
import com.brand.ushopping.action.AppAction;
import com.brand.ushopping.utils.StaticValues;

public class SplashAdActivity extends AppCompatActivity {
    private ImageView splashAdImageView;
    private long SplashTime = 3000;
    private long currentTimeMil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_ad);

        splashAdImageView = (ImageView) findViewById(R.id.splash_ad);

        Bitmap bitmap = new AppAction(SplashAdActivity.this).loadSplash(SplashAdActivity.this);
        if(bitmap != null)
        {


        }

        currentTimeMil = System.currentTimeMillis();

        new LoadTask().start();
    }

    public class LoadTask extends Thread
    {
        public LoadTask() {
        }

        @Override
        public void run() {

            long timeInterval = System.currentTimeMillis() - currentTimeMil;
            if(timeInterval < SplashTime)
            {
                try {
                    sleep(SplashTime - timeInterval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            Intent intent = new Intent(SplashAdActivity.this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("initTab", StaticValues.MAIN_ACTIVITY_TAB_MAINPAGE);
            intent.putExtras(bundle);
            startActivity(intent);

            finish();

        }
    }

}
