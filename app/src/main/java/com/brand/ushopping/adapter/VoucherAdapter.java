package com.brand.ushopping.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brand.ushopping.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/1.
 */
public class VoucherAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public VoucherAdapter(List<Map<String,Object>> list,Activity context){
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView =inflater.inflate(R.layout.voucher_item, null);

            holder.money02=(TextView)convertView.findViewById(R.id.money02);
            holder.money01=(TextView)convertView.findViewById(R.id.money01);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.validity=(TextView)convertView.findViewById(R.id.validity);
            holder.come=(TextView)convertView.findViewById(R.id.come);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.money02.setText(list.get(position).get("money02").toString());
        holder.money01.setText(list.get(position).get("money01").toString());
        holder.name.setText(list.get(position).get("name").toString());
        holder.validity.setText(list.get(position).get("validity").toString());
        holder.come.setText(list.get(position).get("come").toString());

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        TextView money02;   //金额
        TextView money01;   //满减金额
        TextView name;
        TextView validity;
        TextView come;      //有效天数

    }
}
