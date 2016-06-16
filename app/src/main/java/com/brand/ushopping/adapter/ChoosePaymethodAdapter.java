package com.brand.ushopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.AddressesActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/30.
 */
public class ChoosePaymethodAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private AddressesActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public ChoosePaymethodAdapter(List<Map<String,Object>> list,AddressesActivity context){
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

    public String getValue(int position)
    {
        return (String)(list.get(position).get("value"));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView =inflater.inflate(R.layout.choose_paymethod_item, null);

            holder.name=(TextView)convertView.findViewById(R.id.name);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }


        return convertView;
    }

    static class ViewHolder {
        TextView name;

    }
}
