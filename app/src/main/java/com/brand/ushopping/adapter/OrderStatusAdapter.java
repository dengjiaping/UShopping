package com.brand.ushopping.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.utils.CommonUtils;

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
            convertView =inflater.inflate(R.layout.order_status_item, null);

            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.body = (TextView) convertView.findViewById(R.id.body);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.title.setText(list.get(position).get("title").toString());
        holder.time.setText(CommonUtils.timestampToDatetime((Long) list.get(position).get("time")));
        holder.body.setText(list.get(position).get("body").toString());

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

//        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        TextView title;
        TextView time;
        TextView body;

    }
}
