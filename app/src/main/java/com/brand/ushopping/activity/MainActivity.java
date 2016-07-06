package com.brand.ushopping.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.AppAction;
import com.brand.ushopping.fragment.BrandFragment;
import com.brand.ushopping.fragment.CartFragment;
import com.brand.ushopping.fragment.MainpageFragment;
import com.brand.ushopping.fragment.MineFragment;
import com.brand.ushopping.fragment.ThemeFragment;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.Version;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.EnvValues;
import com.brand.ushopping.utils.StaticValues;

public class MainActivity extends UActivity
        implements BrandFragment.OnFragmentInteractionListener,
        CartFragment.OnFragmentInteractionListener,
        MainpageFragment.OnFragmentInteractionListener,
        MineFragment.OnFragmentInteractionListener,
        ThemeFragment.OnFragmentInteractionListener

{
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private AppContext appContext;
    private User user;
    private boolean isMainpageSelected = true;

    private Fragment mainpageFragment = null;
    private Fragment brandFragment = null;
    private Fragment themeFragment = null;
    private Fragment mineFragment = null;
    private Fragment cartFragment = null;

    private LinearLayout mainpageBtn;
    private LinearLayout brandBtn;
    private LinearLayout themeBtn;
    private LinearLayout mineBtn;
    private LinearLayout cartBtn;

    private ImageView mainpageIcon;
    private ImageView brandIcon;
    private ImageView themeIcon;
    private ImageView mineIcon;

    private RelativeLayout contentLayout;

    private int initTab = StaticValues.MAIN_ACTIVITY_TAB_MAINPAGE;

    private boolean buttomBarEnable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);

        appContext = (AppContext) getApplicationContext();

        contentLayout = (RelativeLayout) findViewById(R.id.content);

        mainpageIcon = (ImageView) findViewById(R.id.mainpage_icon);
        brandIcon = (ImageView) findViewById(R.id.brand_icon);
        themeIcon = (ImageView) findViewById(R.id.theme_icon);
        mineIcon = (ImageView) findViewById(R.id.mine_icon);

        mainpageBtn = (LinearLayout) findViewById(R.id.mainpage);
        mainpageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttomBarEnable)
                {
                    if(mainpageFragment == null)
                    {
                        mainpageFragment = new MainpageFragment();
                        Bundle bundle = new Bundle();
                        mainpageFragment.setArguments(bundle);
                    }

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, mainpageFragment);
                    transaction.commit();

                    iconUnselect();
                    mainpageIcon.setBackgroundResource(R.drawable.tab_home_s);
                }

            }
        });

        brandBtn = (LinearLayout) findViewById(R.id.brand);
        brandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttomBarEnable)
                {
                    if(brandFragment == null)
                    {
                        brandFragment = new BrandFragment();
                        Bundle bundle = new Bundle();
                        brandFragment.setArguments(bundle);
                    }

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, brandFragment);
                    transaction.commit();

                    iconUnselect();
                    brandIcon.setBackgroundResource(R.drawable.tab_brand_s);
                }

            }
        });

        themeBtn = (LinearLayout) findViewById(R.id.theme);
        themeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttomBarEnable)
                {
                    if(themeFragment == null)
                    {
                        themeFragment = new ThemeFragment();
                        Bundle bundle = new Bundle();
                        themeFragment.setArguments(bundle);
                    }

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, themeFragment);
                    transaction.commit();

                    iconUnselect();
                    themeIcon.setBackgroundResource(R.drawable.tab_theme_s);
                }

            }
        });

        mineBtn = (LinearLayout) findViewById(R.id.mine);
        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttomBarEnable)
                {
                    if(mineFragment == null)
                    {
                        mineFragment = new MineFragment();
                        Bundle bundle = new Bundle();
                        mineFragment.setArguments(bundle);
                    }

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, mineFragment);
                    transaction.commit();

                    iconUnselect();
                    mineIcon.setBackgroundResource(R.drawable.tab_mine_s);
                }

            }
        });

        cartBtn = (LinearLayout) findViewById(R.id.cart);
        cartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(buttomBarEnable)
                {
                    iconUnselect();
                    if(cartFragment == null)
                    {
                        cartFragment = new CartFragment();
                        Bundle bundle = new Bundle();
                        cartFragment.setArguments(bundle);
                    }

                    manager = getFragmentManager();
                    transaction = manager.beginTransaction();
                    transaction.replace(R.id.content, cartFragment);
                    transaction.commit();
                }

            }
        });

        Bundle bundle = getIntent().getExtras();
        initTab = bundle.getInt("initTab", StaticValues.MAIN_ACTIVITY_TAB_MAINPAGE);

        iconUnselect();
        mainpageFragment = new MainpageFragment();

        Bundle fragmentBundle = new Bundle();
        mainpageFragment.setArguments(fragmentBundle);
        manager = getFragmentManager();
        transaction = manager.beginTransaction();

        switch (initTab)
        {
            case StaticValues.MAIN_ACTIVITY_TAB_MAINPAGE:
                transaction.replace(R.id.content, mainpageFragment);
                mainpageIcon.setBackgroundResource(R.drawable.tab_home_s);
                break;

            case StaticValues.MAIN_ACTIVITY_TAB_BRAND:
                transaction.replace(R.id.content, brandFragment);
                brandIcon.setBackgroundResource(R.drawable.tab_brand_s);
                break;

            case StaticValues.MAIN_ACTIVITY_TAB_THEME:
                transaction.replace(R.id.content, themeFragment);
                themeIcon.setBackgroundResource(R.drawable.tab_theme_s);
                break;

            case StaticValues.MAIN_ACTIVITY_TAB_MINE:
                transaction.replace(R.id.content, mineFragment);
                mineIcon.setBackgroundResource(R.drawable.tab_mine_s);
                break;

            case StaticValues.MAIN_ACTIVITY_TAB_CART:
                transaction.replace(R.id.content, cartFragment);

                break;

        }

        transaction.commit();

        //检查更新
        Version version = new Version();
        if(user != null)
        {
            version.setUserId(user.getUserId());
            version.setSessionid(user.getSessionid());
        }
        version.setSystemtype(StaticValues.SYSTEM_TYPE_ANDROID);
        version.setVersionNumber(CommonUtils.getVersionCode(MainActivity.this));
        new GetMaxVersionTask().execute(version);

    }

    @Override
    protected void onStart() {
        super.onStart();
        user = appContext.getUser();

    }

    private void iconUnselect()
    {
        mainpageIcon.setBackgroundResource(R.drawable.tab_home);
        brandIcon.setBackgroundResource(R.drawable.tab_brand);
        themeIcon.setBackgroundResource(R.drawable.tab_theme);
        mineIcon.setBackgroundResource(R.drawable.tab_mine);

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onBackPressed() {

//        if(isMainpageSelected)
//        {
//            //主页面点击返回不退出
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//
//        }
//        else
//        {
//            actionBar.selectTab(homeTab);
//
//        }

        //退出确认
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("是否退出U购商城？");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                System.exit(0);

            }
        });
        builder.setNegativeButton("取消", null);
        builder.create().show();

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //重新获取user信息
        //检测是否登录
//        User user = new RefAction().getUser(MainActivity.this);
//        if(user != null)
//        {
//            appContext.setUser(user);
//            appContext.setSessionid(user.getSessionid());
//
//        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.mainpage_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.item_1:
                Toast.makeText(MainActivity.this, "Menu item 1", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_2:
                Toast.makeText(MainActivity.this, "Menu item 2", Toast.LENGTH_SHORT).show();
                break;

            case R.id.item_3:
                Toast.makeText(MainActivity.this, "Menu item 3", Toast.LENGTH_SHORT).show();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    public boolean isButtomBarEnable() {
        return buttomBarEnable;
    }

    public void setButtomBarEnable(boolean buttomBarEnable) {
        this.buttomBarEnable = buttomBarEnable;
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
            return new AppAction(MainActivity.this).getMaxVersionAction(versions[0]);
        }

        @Override
        protected void onPostExecute(final Version result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    //退出确认
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(result.getExplains());
                    builder.setTitle("检测到新版本,是否升级");
                    builder.setPositiveButton("升级", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            appContext.downloadApp(EnvValues.serverPath + result.getDownloadUrl() + result.getAppName());
                            Toast.makeText(MainActivity.this, "下载开始", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("取消", null);
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(MainActivity.this, "当前已是最新版,无需升级", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

}
