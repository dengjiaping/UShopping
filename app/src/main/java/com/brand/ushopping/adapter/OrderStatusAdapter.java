package com.brand.ushopping.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.ThemeActivity;
import com.brand.ushopping.activity.VoucherActivity;
import com.brand.ushopping.activity.WebViewActivity;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/6/27.
 */
public class OrderStatusAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public OrderStatusAdapter(List<Map<String,Object>> list,Activity context){
        this.context=context;
        this.list=list;
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
            convertView =inflater.inflate(R.layout.theme_item, null);

            holder.img=(ImageView)convertView.findViewById(R.id.img);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(list.get(position).get("img").toString(), holder.img);

        /*
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WebViewActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).get("url").toString());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
        */
        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int flag =(Integer) list.get(position).get("flag");
                Intent intent = new Intent();
                Bundle bundle = new Bundle();

                switch (flag)
                {
                    case StaticValues.ACTIVITY_FLAG_WEBPAGE:
                        intent.setClass(context, WebViewActivity.class);
                        bundle.putString("url", list.get(position).get("url").toString());
                        intent.putExtras(bundle);

                        context.startActivity(intent);

                        break;
                    case StaticValues.ACTIVITY_FLAG_VOUCHER:
                        intent.setClass(context, VoucherActivity.class);
                        bundle.putInt("enterType", StaticValues.VOUCHER_ENTER_LIST);
                        intent.putExtras(bundle);

                        context.startActivity(intent);

                        break;
                    case StaticValues.ACTIVITY_FLAG_THEME_ACTIVITY:
                        intent.setClass(context, ThemeActivity.class);
                        context.startActivity(intent);

                }

            }
        });

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

//        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        ImageView img;

    }
}
