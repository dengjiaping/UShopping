package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AppAction;
import com.brand.ushopping.action.RefAction;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.Version;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.EnvValues;
import com.brand.ushopping.utils.StaticValues;

import simplecache.ACache;

public class SettingsActivity extends Activity {
    private AppContext appContext;
    private User user;

    private ViewGroup aboutUsBtn;
    private ViewGroup addressUsBtn;
    private ViewGroup cleanCacheBtn;
    private ViewGroup checkUpdateBtn;
    private ViewGroup upayBtn;
    private ImageView backBtn;
    private TextView titleTextView;
    private ViewGroup logoutBtn;
    private ACache aCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_settings);

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        aboutUsBtn = (ViewGroup) findViewById(R.id.about_us);
        aboutUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });

        addressUsBtn = (ViewGroup) findViewById(R.id.address);
        addressUsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user == null)
                {
                    Toast.makeText(SettingsActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent intent = new Intent(SettingsActivity.this, AddressesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("enterMode", StaticValues.ADDRESSES_ENTER_MODE_EDIT);
                    intent.putExtras(bundle);

                    startActivity(intent);
                }

            }
        });

        cleanCacheBtn = (ViewGroup) findViewById(R.id.clean_cache);
        cleanCacheBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ImageLoader.getInstance().clearMemoryCache();
//                ImageLoader.getInstance().clearDiskCache();
//                aCache = ACache.get(SettingsActivity.this);
//                aCache.clear();

                Toast.makeText(SettingsActivity.this, "图片缓存已清理", Toast.LENGTH_SHORT).show();

            }
        });

        checkUpdateBtn = (ViewGroup) findViewById(R.id.check_update);
        checkUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Version version = new Version();
                if(user != null)
                {
                    version.setUserId(user.getUserId());
                    version.setSessionid(user.getSessionid());
                }
                version.setSystemtype(StaticValues.SYSTEM_TYPE_ANDROID);
                version.setVersionNumber(CommonUtils.getVersionCode(SettingsActivity.this));
                new GetMaxVersionTask().execute(version);
            }
        });

        logoutBtn = (ViewGroup) findViewById(R.id.logout);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setMessage("确定要退出登录?");
                builder.setTitle("登录");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        new RefAction(SettingsActivity.this).removeUser(SettingsActivity.this);
                        appContext.setUser(null);

                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
                        startActivity(intent);

                        SettingsActivity.this.finish();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        upayBtn = (ViewGroup) findViewById(R.id.upay);
        upayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

//        mapBtn = (Button) findViewById(R.id.map);
//        mapBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SettingsActivity.this, MapLocateActivity.class);
//                startActivity(intent);
//            }
//        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        if(user == null)
        {
            logoutBtn.setVisibility(View.GONE);
        }

    }

    public boolean onKeyDown(int keyCode,KeyEvent event) {
        // back键回退至主界面
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent intent = new Intent(CartActivity.this, MainActivity.class);
//            startActivity(intent);
            this.finish();

        }
        return true;
    }

    //检查版本
    public class GetMaxVersionTask extends AsyncTask<Version, Void, Version>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Version doInBackground(Version... versions) {
            return new AppAction(SettingsActivity.this).getMaxVersionAction(versions[0]);
        }

        @Override
        protected void onPostExecute(final Version result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                    builder.setMessage(result.getExplains());
                    builder.setTitle("检测到新版本,是否升级");
                    builder.setPositiveButton("直接升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            try
                            {
                                appContext.downloadApp(EnvValues.serverPath + result.getDownloadUrl() + result.getAppName());
                                Toast.makeText(SettingsActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
                            }catch (Exception e)
                            {
                                //自动下载出错,使用浏览器下载
                                appContext.openUrlinBrowser(SettingsActivity.this, StaticValues.WEBSITE);
                            }

                        }
                    });
                    builder.setNegativeButton("官网下载", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //使用浏览器下载
                            appContext.openUrlinBrowser(SettingsActivity.this, StaticValues.WEBSITE);
                        }
                    });
                    builder.setNeutralButton("取消", null);
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(SettingsActivity.this, "当前已是最新版,无需升级", Toast.LENGTH_SHORT).show();

                }
            }
            else
            {
                Toast.makeText(SettingsActivity.this, "当前已是最新版,无需升级", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
