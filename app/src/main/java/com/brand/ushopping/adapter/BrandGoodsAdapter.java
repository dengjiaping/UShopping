package com.brand.ushopping.adapter;

import android.app.Activity;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/12/24.
 */
public class BrandGoodsAdapter extends BaseAdapter{
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public BrandGoodsAdapter(List<Map<String,Object>> list,Activity context){
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
            convertView =inflater.inflate(R.layout.brand_goods_item, null);

            holder.img=(SimpleDraweeView)convertView.findViewById(R.id.img);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.promotionPrice=(TextView)convertView.findViewById(R.id.promotion_price);
            holder.originalPrice=(TextView)convertView.findViewById(R.id.original_price);
            holder.salesCount = (TextView) convertView.findViewById(R.id.sales_count);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        Object img = list.get(position).get("logopic");
        if(img != null)
        {
            holder.img.setImageURI(Uri.parse(img.toString()));

        }

        holder.name.setText(list.get(position).get("name").toString());
        holder.promotionPrice.setText(list.get(position).get("promotionPrice").toString());
        holder.originalPrice.setText(list.get(position).get("originalPrice").toString());
        holder.salesCount.setText(list.get(position).get("salesCount").toString());

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        SimpleDraweeView img;
        TextView name;
        TextView promotionPrice;
        TextView originalPrice;
        TextView salesCount;

    }

}
