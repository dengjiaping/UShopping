package com.brand.ushopping.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.brand.ushopping.R;
import com.brand.ushopping.action.AddressAction;
import com.brand.ushopping.activity.AddressesActivity;
import com.brand.ushopping.model.Address;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/5.
 */
public class AddressAdapter extends BaseAdapter {
    //填充数据的List
    List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();

    //上下文
    private AddressesActivity context;

    //用来导入布局
    private LayoutInflater inflater =null;

    //构造器
    public AddressAdapter(List<Map<String,Object>> list,AddressesActivity context){
        this.context=context;
        this.list=list;
        inflater=LayoutInflater.from(context);
    }

    private int width;
    private int height;
    private ViewHolder holder;

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
        if (convertView == null){
            holder = new ViewHolder();
            convertView =inflater.inflate(R.layout.address_item, null);

            holder.consignee=(TextView)convertView.findViewById(R.id.consignee);
            holder.mobile=(TextView)convertView.findViewById(R.id.mobile);
            holder.area=(TextView)convertView.findViewById(R.id.area);
            holder.deaddress=(TextView)convertView.findViewById(R.id.deaddress);
            holder.zipcode=(TextView)convertView.findViewById(R.id.zipcode);
            holder.deleteBtn = (Button) convertView.findViewById(R.id.delete);

            //为view设置标签
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }

        holder.consignee.setText(list.get(position).get("consignee").toString());
        holder.mobile.setText(list.get(position).get("mobile").toString());
        holder.area.setText(list.get(position).get("area").toString());
        holder.deaddress.setText(list.get(position).get("deaddress").toString());
        if(list.get(position).get("zipcode") != null)
        {
            holder.zipcode.setText(list.get(position).get("zipcode").toString());
        }

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确定删除此地址?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Address address = new Address();
                        address.setUserId((Long)(list.get(position).get("userId")));
                        address.setSessionid(list.get(position).get("sessionid").toString());
                        address.setAddressId((Long) (list.get(position).get("id")));

                        new AddressDeleteTask().execute(address);
                    }
                });
                builder.setNegativeButton("取消", null);

                builder.create().show();


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
        TextView consignee;
        TextView mobile;
        TextView area;
        TextView deaddress;
        TextView zipcode;
        Button deleteBtn;
    }

    public class AddressDeleteTask extends AsyncTask<Address, Void, Address>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            holder.deleteBtn.setEnabled(false);
        }

        @Override
        protected Address doInBackground(Address... address) {
            return new AddressAction().deleteAddress(address[0]);
        }

        @Override
        protected void onPostExecute(Address result) {
            holder.deleteBtn.setEnabled(true);
            if(result != null)
            {
                if(result.isSuccess())
                {
                    context.reloadList();
                }
                else
                {
                    Toast.makeText(context, result.getMsg(), Toast.LENGTH_SHORT).show();

                }

            }
        }
    }
}
