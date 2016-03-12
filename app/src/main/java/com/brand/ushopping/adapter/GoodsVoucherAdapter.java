package com.brand.ushopping.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.brand.ushopping.R;
import com.brand.ushopping.activity.OrderConfirmActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/3/8.
 */
public class GoodsVoucherAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private OrderConfirmActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public GoodsVoucherAdapter(List<Map<String,Object>> list,OrderConfirmActivity context){
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
            convertView =inflater.inflate(R.layout.goods_voucher_item, null);

            holder.money=(TextView)convertView.findViewById(R.id.money);
            holder.name=(TextView)convertView.findViewById(R.id.name);
            holder.removeBtn = (Button) convertView.findViewById(R.id.remove);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.money.setText(list.get(position).get("money01").toString());
        holder.name.setText(list.get(position).get("name").toString());

        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.removeVoucher((Long) list.get(position).get("id"));
            }
        });

        width =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);
        height =View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);

        convertView.measure(width,height);

        height=convertView.getMeasuredHeight();
        width=convertView.getMeasuredWidth();

        return convertView;
    }

    static class ViewHolder {
        TextView money;
        TextView name;
        Button removeBtn;

    }
}
