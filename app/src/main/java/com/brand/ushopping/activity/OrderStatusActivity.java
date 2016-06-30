package com.brand.ushopping.activity;
/**
 * -- 查询上门订单状态
 */

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.adapter.OrderStatusAdapter;
import com.brand.ushopping.model.GetSmorderStatusList;
import com.brand.ushopping.model.OrderStatusListItem;
import com.brand.ushopping.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderStatusActivity extends AppCompatActivity {
    private AppContext appContext;
    private ListView orderStatusListView;
    private String orderNo;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();
        orderStatusListView = (ListView) findViewById(R.id.status_list);

        Bundle bundle = getIntent().getExtras();
        orderNo = bundle.getString("orderNo");

    }

    @Override
    protected void onStart() {
        super.onStart();
        reload();
    }

    private void reload()
    {
        GetSmorderStatusList getSmorderStatusList = new GetSmorderStatusList();
        if(user != null)
        {
            getSmorderStatusList.setUserId(user.getUserId());
            getSmorderStatusList.setSessionid(user.getSessionid());
        }
        getSmorderStatusList.setOrder_no(orderNo);

        new GetSmorderStatusListTask().execute(getSmorderStatusList);
    }

    //检查版本
    public class GetSmorderStatusListTask extends AsyncTask<GetSmorderStatusList, Void, GetSmorderStatusList>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected GetSmorderStatusList doInBackground(GetSmorderStatusList... getSmorderStatusLists) {
            return new OrderAction(OrderStatusActivity.this).getSmorderStatusListAction(getSmorderStatusLists[0]);
        }

        @Override
        protected void onPostExecute(final GetSmorderStatusList result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    //UI更新
                    List listData = new ArrayList<Map<String,Object>>();
                    ArrayList<OrderStatusListItem> orderStatusListItems = result.getOrderStatusListItems();
                    if(!orderStatusListItems.isEmpty())
                    {
                        for(OrderStatusListItem orderStatusListItem: orderStatusListItems)
                        {
                            Map line = new HashMap();
                            line.put("id", orderStatusListItem.getId());
                            line.put("title", orderStatusListItem.getTitle());
                            line.put("time", orderStatusListItem.getTime());
                            line.put("body", orderStatusListItem.getBody());

                            listData.add(line);
                        }

                    }

                    OrderStatusAdapter orderStatusAdapter = new OrderStatusAdapter(listData, OrderStatusActivity.this);
                    orderStatusListView.setAdapter(orderStatusAdapter);

                }
                else
                {
                    Toast.makeText(OrderStatusActivity.this, result.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(OrderStatusActivity.this, "获取订单状态失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

}
