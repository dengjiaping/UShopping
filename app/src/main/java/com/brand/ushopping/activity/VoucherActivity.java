package com.brand.ushopping.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.brand.ushopping.model.BatchSaveUserVoucher;
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
    private ImageView pickAllBtn;
    private RelativeLayout voucherRuleContainer;
    private TextView voucherRuleTextView;
    private int pickType = StaticValues.VOUCHER_PICK_SINGLE;
    private int enterType = StaticValues.VOUCHER_ENTER_LIST;
    private long currentTime = System.currentTimeMillis();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
        ////一键领取大礼包
        pickAllBtn = (ImageView) findViewById(R.id.pick_all);
        pickAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user != null)
                {
                    BatchSaveUserVoucher batchSaveUserVoucher = new BatchSaveUserVoucher();

                    batchSaveUserVoucher.setUserId(user.getUserId());
                    batchSaveUserVoucher.setSessionid(user.getSessionid());

                    ArrayList<UserVoucherItem> userVoucherItems = new ArrayList<UserVoucherItem>();
                    if(voucherItems != null)
                    {
                        for(VoucherItem voucherItem: voucherItems)
                        {
                            UserVoucherItem userVoucherItem = new UserVoucherItem();
                            AppuserId appuserId = new AppuserId();
                            appuserId.setUserId(user.getUserId());
                            userVoucherItem.setAppuserId(appuserId);
                            AppvoucherId appvoucherId = new AppvoucherId();
                            appvoucherId.setId(voucherItem.getId());
                            userVoucherItem.setAppvoucherId(appvoucherId);

                            userVoucherItem.setDays(voucherItem.getDays());

                            userVoucherItems.add(userVoucherItem);
                        }

                    }

                    batchSaveUserVoucher.setUserVoucher(userVoucherItems);

                    new BatchSaveUserVoucherActionTask().execute(batchSaveUserVoucher);
                }
                else
                {
                    Toast.makeText(VoucherActivity.this, "请登录或注册", Toast.LENGTH_SHORT).show();

                }

            }
        });

        voucherRuleContainer = (RelativeLayout) findViewById(R.id.voucher_rule);
        voucherRuleContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(voucherRuleTextView.getVisibility() == View.VISIBLE)
                {
                    voucherRuleTextView.setVisibility(View.GONE);
                }
                else
                {
                    voucherRuleTextView.setVisibility(View.VISIBLE);
                }

            }
        });

        voucherRuleTextView = (TextView) findViewById(R.id.voucher_rule_text);
        voucherRuleTextView.setText("1.\t优惠券请查看文字表述，只能在限定条件内使用。\n" +
                "2.\t优惠券过期后将无法使用。\n" +
                "3.\t使用优惠券发生退货关系，优惠券将不再返还。\n" +
                "4.\t活动最终解释权归U购所有。\n");

        Bundle bundle = getIntent().getExtras();
        enterType = bundle.getInt("enterType");

        switch (enterType)
        {
            case StaticValues.VOUCHER_ENTER_LIST:
                voucherRuleContainer.setVisibility(View.VISIBLE);
//                pickAllBtn.setVisibility(View.VISIBLE);
                voucherListView.setBackgroundColor(ContextCompat.getColor(VoucherActivity.this, R.color.voucher_bg));
                break;

            case StaticValues.VOUCHER_ENTER_PICK:
            case StaticValues.VOUCHER_ENTER_MINE:
                voucherRuleContainer.setVisibility(View.GONE);
                pickAllBtn.setVisibility(View.GONE);

                break;
        }

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
                        // 领取单张优惠券
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
                                        if(voucherItem.getFlag() == StaticValues.VOUCHER_ITEM_STATUS_GOT)
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
//                        pickAllBtn.setVisibility(View.VISIBLE);
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
//                appVoucherSelect.setFlag(pickType);
//                appVoucherSelect.setModel(StaticValues.MODEL_ANDROID);
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
//                getUserVoucher.setFlag(pickType);
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
                            line.put("brand", "夏装优惠券");
                        }
                        line.put("come", voucherItem.getCome());

                        line.put("days", CommonUtils.timestampToDate(voucherItem.getDays() / 1000));
                        line.put("validity", CommonUtils.timestampToDate(voucherItem.getValidity() / 1000));
                        line.put("enterType", enterType);
                        line.put("flag", voucherItem.getFlag());
                        line.put("pickType", pickType);

                        //过期时间限制
                        if(currentTime > voucherItem.getValidity() && currentTime < voucherItem.getDays())
                        {
                            listData.add(line);

                        }

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
                        line.put("flag", userVoucherItem.getFlag());

                        //过期时间限制
                        if(currentTime > appvoucherId.getValidity() && currentTime < appvoucherId.getDays())
                        {
                            listData.add(line);
                        }

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

    //一键领取大礼包
    public class BatchSaveUserVoucherActionTask extends AsyncTask<BatchSaveUserVoucher, Void, BatchSaveUserVoucher>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected BatchSaveUserVoucher doInBackground(BatchSaveUserVoucher... batchSaveUserVouchers) {
            return new VoucherAction().batchSaveUserVoucherAction(batchSaveUserVouchers[0]);

        }

        @Override
        protected void onPostExecute(BatchSaveUserVoucher result) {
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

    // 取消领取优惠券
    public void voucherSaveDisable()
    {
        pickAllBtn.setVisibility(View.GONE);

    }


}
