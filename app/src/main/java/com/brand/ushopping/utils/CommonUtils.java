package com.brand.ushopping.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by viator42 on 15/5/5.
 *
 * 一般工具类
 */
public class CommonUtils {

    /// region 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
    /// <summary>
    /// 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
    /// </summary>
    /// <param name="lng1">经度1</param>
    /// <param name="lat1">纬度1</param>
    /// <param name="lng2">经度2</param>
    /// <param name="lat2">纬度2</param>
    /// <returns>返回距离（米）</returns>
    public static double getDistance(double lng1, double lat1, double lng2, double lat2)
    {
        double radLat1 = lat1 * StaticValues.RAD;  // // RAD=π/180
        double radLat2 = lat2 * StaticValues.RAD;
        double a = radLat1 - radLat2;
        double b = (lng1 - lng2) * StaticValues.RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * StaticValues.EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static int getSpinnerPosition(String code, ArrayList<Map<String, Object>> list)
    {
        int position = 0;

        for(int a=0; a<list.size(); a++)
        {
            Map item = list.get(a);
            if(code.equals(item.get("id")))
            {
                position = a;
            }
        }

        return position;
    }

    public static String getProvinceCode(String code)
    {
        return code.substring(0, 2)+"0000";

    }

    public static String getCityCode(String code)
    {
        return code.substring(0, 4)+"00";

    }

    //位置信息是否可用
    public static boolean isPositionAvalible(double longitude, double latitude)
    {
        return !(longitude == 0 || latitude == 0);
    }

    public static String distanceFormat(double distance) {
        /*
         * %.2f % 表示 小数点前任意位数 2 表示两位小数 格式后的结果为 f 表示浮点型
         */
        String value;
        if(distance > 1000)
        {
            value = new Formatter().format("%.2f", distance / 1000).toString() + "公里";

        }
        else
        {
            value = new Formatter().format("%.1f", distance).toString() + "米";
        }

        return value;
    }

    // 时间戳转换为日期字符串
    public static String timestampToDate(long timestamp)
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(timestamp * 1000L);

    }

    // 时间戳转换为日期+时间字符串
    public static String timestampToDatetime(long timestamp)
    {
        return new SimpleDateFormat("yyyy-MM-dd \n HH:mm").format(timestamp * 1000L);

    }

    //字符串是否为空值
    public static boolean isValueEmpty(String str)
    {
        return str == null || str.isEmpty() || str.equals("null");
    }

    //获取资源路径
    public static String getImgFullpath(String path)
    {
        return EnvValues.serverPath + path;

    }

    /**
     * 获取版本
     * @return 当前应用的版本
     * */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     * */
    public static int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    //
    public static boolean isNumber(String str)
    {
        return str.matches("[0-9]+");
    }

    //图片圆角化 第一个参数是Bitmap,第二个参数是圆角角度
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap,int roundPixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(0xFFFFFFFF);
        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }

    //日期是否是同一天
    public static boolean isSameDay(long timestampA, long timestampB)
    {
        Date dateA = new Date(timestampA);
        Date dateB = new Date(timestampB);

        Calendar calDateA = Calendar.getInstance();
        calDateA.setTime(dateA);

        Calendar calDateB = Calendar.getInstance();
        calDateB.setTime(dateB);

        return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
                && calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
                &&  calDateA.get(Calendar.DAY_OF_MONTH) == calDateB.get(Calendar.DAY_OF_MONTH);

    }

    public static java.text.DecimalFormat df = new java.text.DecimalFormat("#.0");

    public static String getAbsoluteUrl(String relativeUrl) {
        return EnvValues.serverPath + relativeUrl;
    }

    public static Map<String, Object> generateParams(String jsonString)
    {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("param", jsonString);

        return map;
    }

    //设置优先使用的网络
//    public static void setPreferredNetwork(Context context, int networkType) {
//        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        connMgr.
//
//        connMgr.setNetworkPreference(networkType);
//
//        WifiManager wifiMgr = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
//        wifiMgr.disconnect();
//    }
}
