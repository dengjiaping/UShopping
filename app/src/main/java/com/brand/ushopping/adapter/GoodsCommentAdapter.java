package com.brand.ushopping.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/1/18.
 */
public class GoodsCommentAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public GoodsCommentAdapter(List<Map<String,Object>> list,Activity context){
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
            convertView =inflater.inflate(R.layout.goods_comment_item, null);

            holder.logo=(ImageView)convertView.findViewById(R.id.logo);
            holder.username=(TextView)convertView.findViewById(R.id.username);
            holder.content=(TextView)convertView.findViewById(R.id.content);
            holder.attribute=(TextView)convertView.findViewById(R.id.attribute);
            holder.datetime=(TextView)convertView.findViewById(R.id.datetime);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        /*
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnLoading(R.drawable.logo) // resource or drawable
                .showImageForEmptyUri(R.drawable.logo) // resource or drawable
                .showImageOnFail(R.drawable.logo) // resource or drawable
                .build();
        ImageLoader.getInstance().displayImage(list.get(position).get("logopic").toString(), holder.logo, options);
        */

        holder.username.setText(list.get(position).get("username").toString());
        holder.content.setText(list.get(position).get("content").toString());
        holder.attribute.setText(list.get(position).get("attribute").toString());
        holder.datetime.setText(list.get(position).get("datetime").toString());

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        ImageView logo;
        TextView username;
        TextView content;
        TextView attribute;
        TextView datetime;

    }

}
