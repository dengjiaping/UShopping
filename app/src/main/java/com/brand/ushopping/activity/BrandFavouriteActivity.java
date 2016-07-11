package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.BrandAction;
import com.brand.ushopping.adapter.BrandFavouriteItemAdapter;
import com.brand.ushopping.model.AppBrandCollect;
import com.brand.ushopping.model.AppBrandCollectItem;
import com.brand.ushopping.model.Brand;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.TimeoutbleProgressDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrandFavouriteActivity extends Activity {
    private ImageView backBtn;
    private TextView titleTextView;
    private AppContext appContext;
    private User user;
    private ListView brandListView;
    private ArrayList<AppBrandCollectItem> appBrandCollectItems;

    private TimeoutbleProgressDialog brandFavouriteDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_brand_favourite);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());

        brandListView = (ListView) findViewById(R.id.brand_list);
        brandListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for(AppBrandCollectItem appBrandCollectItem: appBrandCollectItems)
                {
                    Brand brand = appBrandCollectItem.getAppbrandId();
                    if(brand.getId() == id)
                    {
                        Intent intent = new Intent(BrandFavouriteActivity.this, BrandActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("brand", brand);
                        bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                        intent.putExtras(bundle);
                        appContext.setBundleObj(bundle);
                        startActivity(intent);
                        break;
                    }

                }

            }
        });

        brandFavouriteDialog = TimeoutbleProgressDialog.createProgressDialog(BrandFavouriteActivity.this, StaticValues.CONNECTION_TIMEOUT, new TimeoutbleProgressDialog.OnTimeOutListener() {
            @Override
            public void onTimeOut(TimeoutbleProgressDialog dialog) {
                brandFavouriteDialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(BrandFavouriteActivity.this);
                builder.setMessage("登录失败");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.create().show();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(user != null)
        {
            AppBrandCollect appBrandCollect = new AppBrandCollect();
            appBrandCollect.setUserId(user.getUserId());
            appBrandCollect.setSessionid(user.getSessionid());

            new GetListAppBrandCollectUserIdTask().execute(appBrandCollect);
        }
        else
        {
            Toast.makeText(BrandFavouriteActivity.this, "登录注册之后启用收藏功能", Toast.LENGTH_SHORT).show();

        }

    }

    //店铺收藏列表
    public class GetListAppBrandCollectUserIdTask extends AsyncTask<AppBrandCollect, Void, AppBrandCollect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            brandFavouriteDialog.show();
        }

        @Override
        protected AppBrandCollect doInBackground(AppBrandCollect... appBrandCollects) {
            return new BrandAction(BrandFavouriteActivity.this).getListAppBrandCollectUserIdAction(appBrandCollects[0]);

        }

        @Override
        protected void onPostExecute(AppBrandCollect result) {
            brandFavouriteDialog.dismiss();
            if(result != null)
            {
                if(result.isSuccess())
                {
                    ArrayList<Map<String,Object>> brandListData = new ArrayList<Map<String,Object>>();
                    appBrandCollectItems = result.getAppBrandCollectItems();
                    for(AppBrandCollectItem appBrandCollectItem: appBrandCollectItems)
                    {
                        Brand brand = appBrandCollectItem.getAppbrandId();
                        Map line = new HashMap();

                        line.put("id", brand.getId());
                        line.put("img", brand.getLogopic());
                        line.put("name", brand.getBrandName());
                        line.put("detail", brand.getIntro());

                        brandListData.add(line);
                    }
                    BrandFavouriteItemAdapter adapter = new BrandFavouriteItemAdapter(brandListData, BrandFavouriteActivity.this);
                    brandListView.setAdapter(adapter);

                }
                else
                {
                    Toast.makeText(BrandFavouriteActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(BrandFavouriteActivity.this, "获取收藏列表失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
