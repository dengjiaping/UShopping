package com.brand.ushopping.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Viator42 on 2016/6/18.
 * 数据缓存
 *
 */
public class DataCache {
    private static SharedPreferences ref;
    private static long defaultAliveTime = 30000;

    public static void putData(Context context, String key, String value)
    {
        doPutData(context, key, value, 0);
    }

    public static void putData(Context context, String key, String value, long page)
    {
        doPutData(context, key, value, page);
    }

    //缓存写入
    public static boolean doPutData(Context context, String key, String value, long page)
    {
        try {
            ref = context.getSharedPreferences(key+'_'+page,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = ref.edit();
            editor.putString("value", value);
            editor.putLong("timestamp", System.currentTimeMillis());
            editor.putLong("aliveTime", defaultAliveTime);
            editor.putLong("page", page);

            editor.commit();
            return true;
        } catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    public static String getData(Context context, String key)
    {
        return doGetData(context, key, 0);
    }

    public static String getData(Context context, String key, long page)
    {
        return doGetData(context, key, page);
    }

    //缓存读取
    public static String doGetData(Context context, String key, long page)
    {
        String result = null;
        try {
            ref = context.getSharedPreferences(key+'_'+page,Context.MODE_PRIVATE);
            if(ref != null)
            {
                long aliveTime = ref.getLong("aliveTime", 0);
                long timestamp = ref.getLong("timestamp", 0);
                long currentTime = System.currentTimeMillis();
                long timeInterval = currentTime - timestamp;

                //过期时间
                if(timeInterval > aliveTime)
                {
                    return null;
                }
                else
                {
                    result = ref.getString("value", null);

                }

            }
        } catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

        return result;
    }

}
