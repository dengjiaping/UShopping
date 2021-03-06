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
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.RewardsAction;
import com.brand.ushopping.adapter.RewardItemAdapter;
import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.AppaddressId;
import com.brand.ushopping.model.AppuserId;
import com.brand.ushopping.model.AppushopId;
import com.brand.ushopping.model.RewardGoodsItem;
import com.brand.ushopping.model.User;
import com.brand.ushopping.model.UserReward;
import com.brand.ushopping.utils.StaticValues;
import com.brand.ushopping.widget.RewardConfirmDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRewardActivity extends Activity {
    private AppContext appContext;
    private User user;
    private TextView titleTextView;
    private TextView closeBtn;
    private TextView rewardsTextView;
    private GridView goodsListGridView;
    private UserReward userReward;

    private Address address;
//    private long addressId = 0;
//    private String deaddress;
    private RewardConfirmDialog rewardConfirmDialog;

    private RewardGoodsItem rewardGoodsItemSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_reward);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        titleTextView = (TextView) findViewById(R.id.title);
        closeBtn = (TextView) findViewById(R.id.close);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        titleTextView.setText(this.getTitle().toString());

        goodsListGridView = (GridView) findViewById(R.id.goods_list);
        goodsListGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, final long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(UserRewardActivity.this);
                builder.setMessage("是否兑换此商品?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        RewardGoodsItem rewardGoodsItem;
                        ArrayList<RewardGoodsItem> rewardGoodsItems = userReward.getRewardGoodsItems();
                        for(int a=0; a<rewardGoodsItems.size(); a++)
                        {
                            rewardGoodsItem = rewardGoodsItems.get(a);
                            if(rewardGoodsItem.getId() == id)
                            {
                                rewardGoodsItemSelected = rewardGoodsItem;

                                rewardConfirmDialog = new RewardConfirmDialog(UserRewardActivity.this);

                                dialogInterface.dismiss();

                            }
                        }

                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();

            }
        });

        rewardsTextView = (TextView) findViewById(R.id.reward);

        reload();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void reload()
    {
        try
        {
            UserReward userReward = new UserReward();
            userReward.setUserId(user.getUserId());
            userReward.setSessionid(user.getSessionid());
            new GetAppUserRewardsTask().execute(userReward);
            new GetAppUshopAll().execute(userReward);
        }catch (Exception e)
        {
            e.printStackTrace();
            finish();
        }

    }

    public class GetAppUserRewardsTask extends AsyncTask<UserReward, Void, UserReward>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected UserReward doInBackground(UserReward... userRewards) {
            return new RewardsAction(UserRewardActivity.this).getAppUserRewards(userRewards[0]);
        }

        @Override
        protected void onPostExecute(UserReward result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    rewardsTextView.setText(Integer.toString(result.getRewards()));

                }
                else
                {
                    Toast.makeText(UserRewardActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(UserRewardActivity.this, "获取U币信息失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public class GetAppUshopAll extends AsyncTask<UserReward, Void, UserReward>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected UserReward doInBackground(UserReward... userRewards) {
            return new RewardsAction(UserRewardActivity.this).getAppUshopAll(userRewards[0]);
        }

        @Override
        protected void onPostExecute(UserReward result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    userReward = result;

                    List listData = new ArrayList<Map<String,Object>>();
                    for(RewardGoodsItem rewardGoodsItem: result.getRewardGoodsItems())
                    {
                        Map line = new HashMap();

                        line.put("id", rewardGoodsItem.getId());
                        line.put("name", rewardGoodsItem.getName());
                        line.put("image", rewardGoodsItem.getImage());
                        line.put("integral", rewardGoodsItem.getIntegral());

                        listData.add(line);
                    }

                    RewardItemAdapter adapter = new RewardItemAdapter(listData, UserRewardActivity.this);
                    goodsListGridView.setAdapter(adapter);

                }
                else
                {
                    Toast.makeText(UserRewardActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(UserRewardActivity.this, "获取商品列表失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public class AppUshopBuyTask extends AsyncTask<UserReward, Void, UserReward>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected UserReward doInBackground(UserReward... userRewards) {
            return new RewardsAction(UserRewardActivity.this).appUshopBuy(userRewards[0]);
        }

        @Override
        protected void onPostExecute(UserReward result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    rewardConfirmDialog.dismiss();

                    AlertDialog.Builder builder = new AlertDialog.Builder(UserRewardActivity.this);
                    builder.setMessage("商品已兑换");
                    builder.setTitle("提示");
                    builder.setPositiveButton("查看订单", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //查看订单
                            Intent intent = new Intent(UserRewardActivity.this, OrderActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("enterType", StaticValues.ORDER_FLAG_PAID);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            appContext.setBundleObj(bundle);
                            UserRewardActivity.this.finish();
                        }
                    });
                    builder.setNegativeButton("关闭", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            UserRewardActivity.this.finish();
                        }
                    });
                    builder.create().show();

                }
                else
                {
                    Toast.makeText(UserRewardActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(UserRewardActivity.this, "商品兑换失败", Toast.LENGTH_SHORT).show();

            }

        }
    }

    public void pickAddress()
    {
        Intent intent = new Intent(UserRewardActivity.this, AddressesActivity.class);
        Bundle bundle1 = new Bundle();
        bundle1.putInt("enterMode", StaticValues.ADDRESSES_ENTER_MODE_PICK);
        bundle1.putSerializable("enterActivityClass", UserRewardActivity.class);
        intent.putExtras(bundle1);
        appContext.setBundleObj(bundle1);
        startActivityForResult(intent, StaticValues.CODE_ADDRESSES_PICK);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == StaticValues.CODE_ADDRESSES_PICK)
        {
            if (resultCode == Activity.RESULT_OK) {
                Bundle bundle = data.getExtras();
                address = bundle.getParcelable("obj");
                if(address != null)
                {
                    rewardConfirmDialog.setAddressTextView(address.getDeaddress());
                }
            }
        }

    }

    //完成兑换
    public void rewardAction()
    {
        if(address != null)
        {
            UserReward userReward = new UserReward();

            userReward.setUserId(user.getUserId());
            userReward.setSessionid(user.getSessionid());

            AppuserId appuserId = new AppuserId();
            appuserId.setUserId(user.getUserId());
            userReward.setAppuserId(appuserId);

            AppushopId appushopId = new AppushopId();
            appushopId.setId(rewardGoodsItemSelected.getId());
            appushopId.setAttribute(rewardGoodsItemSelected.getName());
            userReward.setAppushopId(appushopId);

            AppaddressId appaddressId = new AppaddressId();
            appaddressId.setId(address.getId());
            userReward.setAppaddressId(appaddressId);

            userReward.setUshopId(rewardGoodsItemSelected.getId());
            new AppUshopBuyTask().execute(userReward);

            rewardConfirmDialog.dismiss();
        }
        else
        {
            Toast.makeText(UserRewardActivity.this, "请选择送货地址", Toast.LENGTH_SHORT).show();

        }

    }

}
