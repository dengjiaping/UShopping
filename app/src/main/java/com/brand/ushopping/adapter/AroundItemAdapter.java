package com.brand.ushopping.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.brand.ushopping.AppContext;
import com.brand.ushopping.R;
import com.brand.ushopping.activity.BrandActivity;
import com.brand.ushopping.model.Brand;
import com.brand.ushopping.utils.CommonUtils;
import com.brand.ushopping.utils.StaticValues;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/30.
 */
public class AroundItemAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private Activity context;
    private AppContext appContext;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public AroundItemAdapter(List<Map<String,Object>> list,Activity context){
        this.context=context;
        this.appContext = (AppContext) context.getApplicationContext();
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
            convertView =inflater.inflate(R.layout.around_item, null);

            holder.img=(ImageView)convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.address=(TextView)convertView.findViewById(R.id.address);
            holder.telephone=(TextView)convertView.findViewById(R.id.telephone);
            holder.openTime=(TextView)convertView.findViewById(R.id.open_time);
            holder.reservation=(Button)convertView.findViewById(R.id.reservation);
            holder.distance = (TextView) convertView.findViewById(R.id.distance);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        ImageLoader.getInstance().displayImage(list.get(position).get("logopicUrl").toString(), holder.img);

        holder.name.setText(list.get(position).get("shopName").toString());
        holder.address.setText(list.get(position).get("shopAddr").toString());
        holder.telephone.setText(list.get(position).get("shopTele").toString());
        holder.openTime.setText(list.get(position).get("businessHours").toString());

        holder.reservation.setEnabled(false);
        switch ((Integer) list.get(position).get("boughtType"))
        {
            case StaticValues.BOUTHT_TYPE_NORMAL:
                break;

            case StaticValues.BOUTHT_TYPE_RESERVATION:
                if((Integer) list.get(position).get("flag") == 0)
                {
                    holder.reservation.setText("我要预订");
                    holder.reservation.setEnabled(true);
                }
                else
                {
                    holder.reservation.setText("暂不支持");
                    holder.reservation.setEnabled(false);
                }

                break;

            case StaticValues.BOUTHT_TYPE_TRYIT:
                if((Integer) list.get(position).get("door") == 0)
                {
                    holder.reservation.setText("上门试衣");
                    holder.reservation.setEnabled(true);
                }
                else
                {
                    holder.reservation.setText("暂不支持");
                    holder.reservation.setEnabled(false);
                }

                break;

        }

        holder.reservation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BrandActivity.class);
                Brand brand = new Brand();
                brand.setId((Long) list.get(position).get("id"));
                brand.setLogopic((String) list.get(position).get("logopicUrl"));
                brand.setBrandName((String) list.get(position).get("shopName"));
                brand.setShowpic((String) list.get(position).get("showpic"));

                Bundle bundle = new Bundle();
                bundle.putParcelable("brand", brand);
                bundle.putInt("boughtType", (Integer) list.get(position).get("boughtType"));
                bundle.putInt("enterType", StaticValues.BRAND_ENTER_TYPE_AROUND);
                bundle.putLong("storeId", (Long) list.get(position).get("shopId"));
                intent.putExtras(bundle);
                appContext.setBundleObj(bundle);
                context.startActivity(intent);
            }
        });

        holder.distance.setText("距离: "+CommonUtils.distanceFormat((Double) list.get(position).get("distance")));

        return convertView;
    }

    static class ViewHolder {
        ImageView img;
        TextView name;
        TextView address;
        TextView telephone;
        TextView openTime;
        Button reservation;
        TextView distance;

    }
}
