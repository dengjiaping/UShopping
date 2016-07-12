package com.brand.ushopping.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.action.OrderAction;
import com.brand.ushopping.activity.AfterSaleServiceActivity;
import com.brand.ushopping.activity.GoodsActivity;
import com.brand.ushopping.activity.GoodsEvaluateActivity;
import com.brand.ushopping.activity.TryoutActivity;
import com.brand.ushopping.model.DeleteAppSmOder;
import com.brand.ushopping.model.User;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/25.
 */
public class OrderGoodsItemAdapter extends BaseAdapter{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //用来导入布局
    private LayoutInflater inflater =null;
    private Context context;
    private AppContext appContext;
    private User user;

    //构造器
    public OrderGoodsItemAdapter(List<Map<String,Object>> list, Context context){
        this.list=list;
        this.context = context;
        this.appContext = (AppContext) context.getApplicationContext();
        inflater=LayoutInflater.from(context);
    }

    private int width;
    private int height;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (Long)(list.get(position).get("id"));
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView =inflater.inflate(R.layout.order_goods_item, null);

            holder.imageView = (ImageView)convertView.findViewById(R.id.img);
            holder.goodsName = (TextView) convertView.findViewById(R.id.goods_name);
            holder.attribute = (TextView) convertView.findViewById(R.id.attribute);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.decrease = (ImageView) convertView.findViewById(R.id.decrease);
            holder.increase = (ImageView) convertView.findViewById(R.id.increase);
            holder.afterSaleService = (Button) convertView.findViewById(R.id.after_sale_service);
            holder.evaluate = (Button) convertView.findViewById(R.id.evaluate);
            holder.deleteItemBtn = (TextView) convertView.findViewById(R.id.delete_item);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        user = (User) list.get(position).get("user");
        ImageLoader.getInstance().displayImage(list.get(position).get("img").toString(), holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GoodsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLong("goodsId", (Long) list.get(position).get("goodsId"));
                bundle.putInt("boughtType", StaticValues.BOUTHT_TYPE_NORMAL);
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                context.startActivity(intent);

            }
        });

        holder.goodsName.setText(list.get(position).get("goodsName").toString());
        holder.attribute.setText(list.get(position).get("attribute").toString());
        holder.price.setText(list.get(position).get("price").toString());
        holder.count.setText(list.get(position).get("count").toString());

        holder.afterSaleService.setVisibility(View.GONE);
        holder.evaluate.setVisibility(View.GONE);

        final int customerFlag = (Integer) list.get(position).get("customerFlag");
        if(customerFlag != StaticValues.CUSTOMER_FLAG_NONE)
        {
            switch ((Integer) list.get(position).get("flag"))
            {
                case StaticValues.ORDER_FLAG_DELIVERED:
                    //已发货
                    holder.afterSaleService.setVisibility(View.VISIBLE);

                    switch (customerFlag)
                    {
                        case StaticValues.CUSTOMER_FLAG_APPLY:
                            holder.afterSaleService.setText("已申请");
                            break;
                        case StaticValues.CUSTOMER_FLAG_SUBMITED:
                            holder.afterSaleService.setText("待处理");
                            break;
                        case StaticValues.CUSTOMER_FLAG_DONE:
                            holder.afterSaleService.setText("已完成");
                            break;
                        default:
                            holder.afterSaleService.setText("申请售后");
                            break;

                    }

                    holder.afterSaleService.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, AfterSaleServiceActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("img", list.get(position).get("img").toString());
                            bundle.putString("goodsName", list.get(position).get("goodsName").toString());
                            bundle.putString("attribute", list.get(position).get("attribute").toString());
                            bundle.putDouble("price", (Double) list.get(position).get("price"));
                            bundle.putInt("count", (Integer) list.get(position).get("count"));

                            bundle.putInt("customerFlag", customerFlag);
                            bundle.putString("customerContent", list.get(position).get("customerContent").toString());
                            bundle.putLong("startTime", (Long) list.get(position).get("startTime"));
                            bundle.putLong("endTime", (Long) list.get(position).get("endTime"));
                            bundle.putLong("orderId", (Long) list.get(position).get("orderId"));
                            bundle.putString("orderNo", (String) list.get(position).get("orderNo"));
                            bundle.putDouble("money", (Double) list.get(position).get("money"));

                            intent.putExtras(bundle);
                            appContext.setBundleObj(bundle);
                            context.startActivity(intent);
                        }
                    });

                    break;

                case StaticValues.ORDER_FLAG_CONFIRMED:
                    //已发货
                    holder.evaluate.setVisibility(View.VISIBLE);
                    holder.evaluate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(context, GoodsEvaluateActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("img", list.get(position).get("img").toString());
                            bundle.putString("goodsName", list.get(position).get("goodsName").toString());
                            bundle.putString("attribute", list.get(position).get("attribute").toString());
                            bundle.putDouble("price", (Double) list.get(position).get("price"));
                            bundle.putInt("count", (Integer) list.get(position).get("count"));

                            bundle.putLong("goodsId", (Long) list.get(position).get("goodsId"));

                            intent.putExtras(bundle);
                            appContext.setBundleObj(bundle);
                            context.startActivity(intent);
                        }
                    });

                    break;
            }

        }

        holder.deleteItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确认移除订单商品?");
                builder.setTitle("提示");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();

                        DeleteAppSmOder deleteAppSmOder = new DeleteAppSmOder();
                        if(user != null)
                        {
                            deleteAppSmOder.setUserId(user.getUserId());
                            deleteAppSmOder.setSessionid(user.getSessionid());
                        }
                        deleteAppSmOder.setId((Long) list.get(position).get("id"));

                        new DeleteAppSmOderActionTask().execute(deleteAppSmOder);
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create().show();
            }
        });
        holder.deleteItemBtn.setVisibility(View.INVISIBLE);

        switch ((int) list.get(position).get("boughtType"))
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:
                break;
            case StaticValues.BOUTHT_TYPE_TRYIT:
                holder.deleteItemBtn.setVisibility(View.VISIBLE);
                break;
            case StaticValues.BOUTHT_TYPE_RESERVATION:
                break;

        }

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }
    static class ViewHolder {
        ImageView imageView;
        TextView goodsName;
        TextView attribute;
        TextView price;
        TextView count;
        ImageView decrease;
        ImageView increase;
        Button afterSaleService;
        Button evaluate;
        TextView deleteItemBtn;
    }

    //上门订单商品删除
    public class DeleteAppSmOderActionTask extends AsyncTask<DeleteAppSmOder, Void, DeleteAppSmOder>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected DeleteAppSmOder doInBackground(DeleteAppSmOder... deleteAppSmOders) {
            return new OrderAction(context).deleteAppSmOderAction(deleteAppSmOders[0]);

        }

        @Override
        protected void onPostExecute(DeleteAppSmOder result) {
            if(result != null)
            {
                if(result.isSuccess())
                {
                    Toast.makeText(context, "商品已删除", Toast.LENGTH_SHORT).show();
                    TryoutActivity activity = (TryoutActivity)context;
                    activity.reload();
                }
                else
                {
                    Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();
                }

            }
            else
            {
                Toast.makeText(context, "商品删除失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
