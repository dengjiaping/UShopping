package com.brand.ushopping.action;

import android.content.Context;
import android.content.SharedPreferences;

import com.brand.ushopping.model.Address;
import com.brand.ushopping.model.Location;
import com.brand.ushopping.model.Sign;
import com.brand.ushopping.model.User;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2015/11/4.
 */
public class RefAction extends BaseAction
{
    public RefAction(Context context) {
        super(context);
    }

    public void setUser(User user)
    {
        //用户信息写入ref
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putLong("userId", user.getUserId());
        editor.putString("userName", user.getUserName());
        editor.putString("mobile", user.getMobile());
        editor.putString("sessionid", user.getSessionid());
        editor.putString("headImg", user.getHeadImg());

        editor.commit();

    }

    public User getUser(Context context)
    {
        User user = null;
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        if(ref != null)
        {
            long userId = ref.getLong("userId", 0);
            if(userId != 0)
            {
                user = new User();
                user.setUserId(userId);
                user.setUserName(ref.getString("userName", null));
                user.setMobile(ref.getString("mobile", null));
                user.setSessionid(ref.getString("sessionid", null));
                user.setHeadImg(ref.getString("headImg", null));
            }
        }

        return user;
    }

    public void removeUser(Context context)
    {
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.clear();

        editor.commit();
    }

    //是否是首次开启
    public boolean firstOpen(Context context)
    {
        boolean result = true;

        User user = null;
        SharedPreferences ref = context.getSharedPreferences("first_open", Context.MODE_PRIVATE);

        if(ref != null)
        {
            result = ref.getBoolean("first_open", true);

            if(result == true)
            {
                SharedPreferences.Editor editor = ref.edit();
                editor.putBoolean("first_open", false);

                editor.commit();
            }
        }

        return result;
    }

    //保持签到信息
    public void setSign(Context context, Sign sign)
    {
        //用户信息写入ref
        SharedPreferences ref = context.getSharedPreferences("sign", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();

        editor.putInt("fate", sign.getFate());
        editor.putLong("reTime", sign.getReTime());

        editor.commit();
    }

    public Sign getSign(Context context)
    {
        Sign sign = null;
        SharedPreferences ref = context.getSharedPreferences("sign", Context.MODE_PRIVATE);

        if(ref != null)
        {
            sign = new Sign();

            sign.setFate(ref.getInt("fate", 0));
            sign.setReTime(ref.getLong("reTime", 0));

        }

        return sign;
    }

    //最近搜索关键词
    public HashSet<String> getRecentSearch(Context context)
    {
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        if(ref != null)
        {
            return (HashSet<String>) ref.getStringSet("search", null);

        }
        else
        {
            return null;
        }
    }

    public void setRecentSearch(Context context, String keyword)
    {
        //信息写入ref
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();

        Set<String> search = ref.getStringSet("search", null);
        if(search != null)
        {
            search.add(keyword);

        }
        else
        {
            search = new HashSet<String>();

        }
        editor.putStringSet("search", search);
        editor.commit();

    }

    public void clearRecentSearch(Context context)
    {
        //信息写入ref
        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();

        editor.putStringSet("search", new HashSet<String>());
        editor.commit();

    }

    //获取默认地址
    public Address getDefaultAddress(Context context)
    {
        Address address = null;

        SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        if(ref != null)
        {
            address = new Address();
            address.setAddressId(ref.getLong("uDefaultAddressId", 0));
            address.setDeaddress(ref.getString("uDefaultDeaddress", null));
        }

        return address;
    }

    //保存默认地址
    public boolean setDefaultAddress(Context context, Address address)
    {
        try
        {
            //信息写入ref
            SharedPreferences ref = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = ref.edit();

            editor.putLong("uDefaultAddressId", address.getAddressId());
            editor.putString("uDefaultDeaddress", address.getDeaddress());

            editor.commit();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    //位置信息
    public boolean setLocation(Context context, Location location)
    {
        try
        {
            //信息写入ref
            SharedPreferences ref = context.getSharedPreferences("location", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = ref.edit();

            editor.putString("city", location.getCity());
            editor.putString("longitude", location.getLongitude());
            editor.putString("latitude", location.getLatitude());
            editor.putLong("time", System.currentTimeMillis());

            editor.commit();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
    }

    public Location getLocation(Context context)
    {
        Location location = null;
        try
        {
            SharedPreferences ref = context.getSharedPreferences("location", Context.MODE_PRIVATE);

            if(ref != null)
            {
                location = new Location();
                location.setCity(ref.getString("city", null));
                location.setLongitude(ref.getString("longitude", null));
                location.setLatitude(ref.getString("latitude", null));
                location.setTime(ref.getLong("time", 0));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return location;
    }

}
