package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.VoucherAction;
import com.brand.ushopping.adapter.VoucherAdapter;
import com.brand.ushopping.model.AppVoucherSelect;
import com.brand.ushopping.model.AppbrandId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.AppvoucherId;
import com.brand.ushopping.model.GetUserVoucher;
import com.brand.ushopping.model.SaveUserVoucher;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.UserVoucherItem;
import com.brand.ushopping.model.VoucherItem;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherActivity extends Activity {
    private User user;
    private AppContext appContext;
    private ListView voucherListView;
    private ImageView backBtn;
    private TextView titleTextView;
    private ArrayList<VoucherItem> voucherItems;
    private ArrayList<UserVoucherItem> userVoucherItems;
    private VoucherAdapter voucherAdapter;
    private Button pickAllBtn;

    private int enterType = StaticValues.VOUCHER_ENTER_LIST;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_voucher);

        voucherListView = (ListView) findViewById(R.id.voucher_list);
        backBtn = (ImageView) findViewById(R.id.back);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView = (TextView) findViewById(R.id.title);
        titleTextView.setText(this.getTitle().toString());
        pickAllBtn = (Button) findViewById(R.id.pick_all);

        Bundle bundle = getIntent().getExtras();
        enterType = bundle.getInt("enterType");

    }

    @Override
    protected void onStart() {
        super.onStart();
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        voucherListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                switch (enterType)
                {
                    case StaticValues.VOUCHER_ENTER_LIST:
                        AlertDialog.Builder builder = new AlertDialog.Builder(VoucherActivity.this);
                        builder.setMessage("是否领取这张优惠券?");
                        builder.setTitle("提示");
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();

                                for(int a=0; a<voucherItems.size(); a++)
                                {
                                    VoucherItem voucherItem = voucherItems.get(a);
                                    if(voucherItem.getId() == id)
                                    {
                                        if(voucherItem.getFlag() == 1)
                                        {
                                            Toast.makeText(VoucherActivity.this, "您已领取该优惠券,不能重复领取", Toast.LENGTH_SHORT).show();

                                        }
                                        else
                                        {
                                            if(user != null)
                                            {
                                                SaveUserVoucher saveUserVoucher = new SaveUserVoucher();
                                                saveUserVoucher.setUserId(user.getUserId());
                                                saveUserVoucher.setSessionid(user.getSessionid());

                                                AppuserId appuserId = new AppuserId();
                                                appuserId.setUserId(user.getUserId());
                                                saveUserVoucher.setAppuserId(appuserId);

                                                AppvoucherId appvoucherId = new AppvoucherId();
                                                appvoucherId.setId(id);
                                                saveUserVoucher.setAppvoucherId(appvoucherId);

                                                saveUserVoucher.setEndDays(voucherItem.getCome());

                                                new SaveUserVoucherActionTask().execute(saveUserVoucher);

                                            }
                                            else
                                            {
                                                Toast.makeText(VoucherActivity.this, "请先注册或登录后领取优惠券", Toast.LENGTH_SHORT).show();

                                            }

                                        }

                                    }

                                }

                            }
                        });
                        builder.setNegativeButton("取消", null);

                        builder.create().show();

                        break;

                    case StaticValues.VOUCHER_ENTER_MINE:
                        break;

                    case StaticValues.VOUCHER_ENTER_PICK:
                        for(int a=0; a<userVoucherItems.size(); a++)
                        {
                            UserVoucherItem userVoucherItem = userVoucherItems.get(a);
                            AppvoucherId appvoucherId = userVoucherItem.getAppvoucherId();
                            if(userVoucherItem.getId() == id)
                            {
                                Intent intent = new Intent(VoucherActivity.this, GoodsActivity.class);

                                Bundle bundle = new Bundle();
                                bundle.putParcelable("voucher", userVoucherItem);

                                intent.putExtras(bundle);

                                setResult(Activity.RESULT_OK, intent);
                                finish();

                            }
                        }

                        break;

                    case StaticValues.VOUCHER_ENTER_BUNDLE:
                        pickAllBtn.setVisibility(View.VISIBLE);
                        pickAllBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });

                        break;
                }

            }
        });

        reload();

    }

    public void reload()
    {
        voucherListView.removeAllViewsInLayout();

        switch (enterType)
        {
            case StaticValues.VOUCHER_ENTER_LIST:
                AppVoucherSelect appVoucherSelect = new AppVoucherSelect();
                if(user != null)
                {
                    appVoucherSelect.setUserId(user.getUserId());
                    appVoucherSelect.setSessionid(user.getSessionid());

                }
                new AppVoucherSelectGeneralActionTask().execute(appVoucherSelect);

                break;

            case StaticValues.VOUCHER_ENTER_PICK:
            case StaticValues.VOUCHER_ENTER_MINE:
                GetUserVoucher getUserVoucher = new GetUserVoucher();
                if(user != null)
                {
                    getUserVoucher.setUserId(user.getUserId());
                    getUserVoucher.setSessionid(user.getSessionid());
                }
                new GetUserVoucherIdActionTask().execute(getUserVoucher);

                break;
        }

    }

    //优惠券列表
    public class AppVoucherSelectGeneralActionTask extends AsyncTask<AppVoucherSelect, Void, AppVoucherSelect>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected AppVoucherSelect doInBackground(AppVoucherSelect... appVoucherSelects) {
            return new VoucherAction().appVoucherSelectGeneralAction(appVoucherSelects[0]);

        }

        @Override
        protected void onPostExecute(AppVoucherSelect result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    voucherItems = result.getVoucherItems();

                    List listData = new ArrayList<Map<String,Object>>();
                    for (VoucherItem voucherItem : voucherItems)
                    {
                        Map line = new HashMap();

                        line.put("id", voucherItem.getId());
                        line.put("money02", voucherItem.getMoney02());
                        line.put("money01", voucherItem.getMoney01());
                        line.put("name", voucherItem.getName());
                        AppbrandId appbrandId = voucherItem.getAppbrandId();
                        if(appbrandId != null)
                        {
                            line.put("brand", appbrandId.getBrandName());
                        }
                        else
                        {
                            line.put("brand", "全品牌");
                        }
                        line.put("come", voucherItem.getCome());

                        line.put("days", CommonUtils.timestampToDate(voucherItem.getDays() / 1000));
                        line.put("validity", CommonUtils.timestampToDate(voucherItem.getValidity() / 1000));
                        line.put("enterType", enterType);
                        line.put("flag", voucherItem.getFlag());

                        listData.add(line);

                    }
                    voucherAdapter = new VoucherAdapter(listData, VoucherActivity.this);
                    voucherListView.setAdapter(voucherAdapter);


//                    Toast.makeText(VoucherActivity.this, "收藏添加成功", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(VoucherActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
//                Toast.makeText(VoucherActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    // 我的优惠券
    public class GetUserVoucherIdActionTask extends AsyncTask<GetUserVoucher, Void, GetUserVoucher>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetUserVoucher doInBackground(GetUserVoucher... getUserVouchers) {
            return new VoucherAction().getUserVoucherIdAction(getUserVouchers[0]);

        }

        @Override
        protected void onPostExecute(GetUserVoucher result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    userVoucherItems = result.getUserVoucherItems();

                    List listData = new ArrayList<Map<String,Object>>();
                    for(UserVoucherItem userVoucherItem: userVoucherItems)
                    {
                        AppvoucherId appvoucherId = userVoucherItem.getAppvoucherId();
                        Map line = new HashMap();

                        line.put("id", userVoucherItem.getId());
                        line.put("money02", appvoucherId.getMoney02());
                        line.put("money01", appvoucherId.getMoney01());
                        AppbrandId appbrandId = appvoucherId.getAppbrandId();
                        if(appbrandId != null)
                        {
                            line.put("name", appbrandId.getBrandName());
                        }
                        else
                        {
                            line.put("name", "全品牌");
                        }

                        line.put("come", 12);

                        line.put("days", CommonUtils.timestampToDate(appvoucherId.getDays() / 1000));
                        line.put("validity", CommonUtils.timestampToDate(appvoucherId.getValidity() / 1000));
                        line.put("enterType", enterType);

                        listData.add(line);

                    }
                    voucherAdapter = new VoucherAdapter(listData, VoucherActivity.this);
                    voucherListView.setAdapter(voucherAdapter);

                }
                else
                {
                    Toast.makeText(VoucherActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
//                Toast.makeText(VoucherActivity.this, "添加收藏失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    //领取优惠券
    public class SaveUserVoucherActionTask extends AsyncTask<SaveUserVoucher, Void, SaveUserVoucher>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected SaveUserVoucher doInBackground(SaveUserVoucher... saveUserVouchers) {
            return new VoucherAction().saveUserVoucherAction(saveUserVouchers[0]);

        }

        @Override
        protected void onPostExecute(SaveUserVoucher result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    reload();

                    Toast.makeText(VoucherActivity.this, "优惠券领取成功", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(VoucherActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(VoucherActivity.this, "优惠券领取失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
