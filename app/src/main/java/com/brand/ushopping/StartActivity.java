package com.brand.ushopping;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.brand.ushopping.activity.SplashActivity;

public class StartActivity extends Activity {
    private AppContext appContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_start);

//        appContext = (AppContext) getApplicationContext();
//
//        //检测是否登录
//        User user = new RefAction().getUser(StartActivity.this);
//        if(user != null)
//        {
//            appContext.setUser(user);
//            appContext.setSessionid(user.getSessionid());
//
//        }
//
//        if(new RefAction().firstOpen(StartActivity.this) == true)
//        {
//            Intent intent = new Intent(StartActivity.this, GuidanceActivity.class);
//            startActivity(intent);
//        }
//        else
//        {
//            Intent intent = new Intent(StartActivity.this, SplashActivity.class);
//            startActivity(intent);
//        }

        Intent intent = new Intent(StartActivity.this, SplashActivity.class);
        startActivity(intent);

        finish();
    }

}
