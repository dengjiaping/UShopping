package com.brand.ushopping.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/16.
 */
public class TextValueAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater = null;

    //构造器
    public TextValueAdapter(List<Map<String,Object>> list,Activity context){
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
//        return (Long)(list.get(position).get("id"));
        return 0;
    }

    public String getText(int position) {
        return (String) list.get(position).get("text");
    }

    public String getValue(int position) {
        return (String) list.get(position).get("value");
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null){
            holder = new ViewHolder();
            convertView =inflater.inflate(R.layout.text_value_item, null);

            holder.text=(TextView)convertView.findViewById(R.id.text);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.text.setText(list.get(position).get("text").toString());

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        return convertView;
    }
    static class ViewHolder {
        TextView text;

    }

}
