package com.brand.ushopping.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AppAction;
import com.brand.ushopping.action.RefAction;
import com.brand.ushopping.model.Location;
import com.brand.ushopping.model.User;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

public class SplashActivity extends Activity {
    private AppContext appContext;
    private User user;
    private long SplashTime = 2000;
    private long currentTimeMil;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        appContext = (AppContext)getApplicationContext();
        user = appContext.getUser();

//        CommonUtils.setPreferredNetwork(SplashActivity.this, StaticValues.NETWORK_TYPE_WIFI);

        //获取屏幕分辨率
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        appContext.setScreenWidth(metrics.widthPixels);
        appContext.setScreenHeight(metrics.heightPixels);

        currentTimeMil = System.currentTimeMillis();

        //获取手机的信息
        try {
            TelephonyManager tm = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
            appContext.setImie(tm.getDeviceId());
        }catch (Exception e)
        {
            e.printStackTrace();
        }

        Location location = new RefAction(SplashActivity.this).getLocation(SplashActivity.this);
        if(location != null)
        {
            appContext.setCity(location.getCity());
            appContext.setArea(location.getArea());
        }

        //检测是否登录
        User user = new RefAction(SplashActivity.this).getUser(SplashActivity.this);
        if(user != null)
        {
            appContext.setUser(user);
            appContext.setSessionid(user.getSessionid());

        }

        if(new RefAction(SplashActivity.this).firstOpen(SplashActivity.this) == true)
        {
            Intent intent = new Intent(SplashActivity.this, GuidanceActivity.class);
            startActivity(intent);
        }
        else
        {
            new LoadTask(user).start();

        }

        // 开启logcat输出，方便debug，发布时请关闭
//         XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushManager.registerPush(getApplicationContext());
        appContext.setXgPushToken(XGPushConfig.getToken(getApplicationContext()));

    }

    public class LoadTask extends Thread
    {
        private User user;

        public LoadTask(User user) {
            this.user = user;

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

            boolean networkEnabled = isOpenNetwork();
            appContext.setNeetworkEnable(networkEnabled);
            if(networkEnabled)
            {
                new AppAction(SplashActivity.this).appDataInit(user);

            }

            intent = new Intent(SplashActivity.this, SplashAdActivity.class);
            startActivity(intent);

//            intent = new Intent(SplashActivity.this, MainActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putInt("initTab", StaticValues.MAIN_ACTIVITY_TAB_MAINPAGE);
//            intent.putExtras(bundle);
//            appContext.setBundleObj(bundle);
//            startActivity(intent);

            finish();

        }
    }

    //检测网络是否可用
    private boolean isOpenNetwork() {
        boolean result = false;

        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connManager.getActiveNetworkInfo() != null) {

            NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            if(NetworkInfo.State.CONNECTED == state)
            {
                Log.v("ushopping network", "WIfi");
                connManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);

            }
            else
            {
                Log.v("ushopping network", "Mobile");
                connManager.setNetworkPreference(ConnectivityManager.TYPE_MOBILE);

            }

            result = connManager.getActiveNetworkInfo().isAvailable();
        }

        return result;
    }

}
