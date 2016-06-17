package com.brand.ushopping.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/3/15.
 *
 */
public class URLConnectionUtil
{
    private static final String BASE_URL = EnvValues.serverPath;

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static String post(String path, String paramsString) throws Exception
    {
        URL url = new URL(getAbsoluteUrl(path));

        PrintWriter printWriter = null;
        BufferedReader bufferedReader = null;
        StringBuffer responseResult = null;
        StringBuffer params = new StringBuffer();
        params.append("param=" + URLEncoder.encode(paramsString, "UTF-8"));

//        if (params.length() > 0) {
//            params.deleteCharAt(params.length() - 1);
//        }

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        // 设置通用的请求属性
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", String.valueOf(params.length()));
        conn.setUseCaches(false);
        conn.setInstanceFollowRedirects(true);

        // 获取URLConnection对象对应的输出流
        printWriter = new PrintWriter(conn.getOutputStream());
        // 发送请求参数
        printWriter.write(params.toString());
        // flush输出流的缓冲
        printWriter.flush();

        int responseCode = conn.getResponseCode();
        if (responseCode == 200)
        {
            // 定义BufferedReader输入流来读取URL的ResponseData
            bufferedReader = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));
            String line;
            responseResult = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                responseResult.append(line);
            }

        }

        if (printWriter != null) {
            printWriter.close();
        }
        if (bufferedReader != null) {
            bufferedReader.close();
        }
        conn.disconnect();

        if(responseResult != null)
        {
            return responseResult.toString();
        }
        else
        {
            return null;
        }

    }

//    //获取httpclient
//    private static HttpClient getHttpClient() {
//        HttpClient httpClient = HttpClients.createDefault();
//
//        return httpClient;
//    }
//
//    public static String get(String url, String params)
//    {
//        String result = "";
//        BufferedReader bufferedReader = null;
//        String urlName = url + "?" + params;
//        try {
//            URL realUrl = new URL(urlName);
//            //打开和URL之间的连接
//            try {
//                URLConnection urlConnection = realUrl.openConnection();
//                        /*设置通用请求属性*/
//                //告诉WEB服务器自己接受什么介质类型，*/* 表示任何类型，type/* 表示该类型下的所有子类型，type/sub-type。
//                urlConnection.setRequestProperty("accept", "*/*");
//                urlConnection.setRequestProperty("connection", "Keep-Alive");
//                //浏览器表明自己的身份（是哪种浏览器）
//                urlConnection.setRequestProperty("user-agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.8.1.14)");
//                //建立实际连接
//                urlConnection.connect();
//                Log.e("contentType", ""+urlConnection.getContentType());
//                Log.e("contentLength", ""+urlConnection.getContentLength());
//                Log.e("contentEncoding", ""+urlConnection.getContentEncoding());
//                Log.e("contentDate", ""+urlConnection.getDate());
//                //获取所有相应头字段
//                Map<String, List<String>> map = urlConnection.getHeaderFields();
//                //遍历所有响应头字段
//                for (String key:map.keySet()){
//                    Log.i("GET方式请求", ""+map.get(key));
//                }
//                //定义BufferReader输入流来读取URL的响应
//                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String line;
//                for (;(line = bufferedReader.readLine()) != null;){
//                    result += "\n" + line;
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                Log.e("GET方式请求", "发送GET请求异常" + e);
//                e.printStackTrace();
//            }
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            if (null != bufferedReader){
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//        }
//        return result;
//    }
//
//    public static String post(String url, String params)
//    {
//        String result = "";
//        PrintWriter printWriter = null;
//        BufferedReader bufferedReader = null;
//        try {
//            URL realUrl = new URL(getAbsoluteUrl(url));
//            //打开和URL之间的连接
//            try {
//                URLConnection urlConnection = realUrl.openConnection();
//                //设置通用请求属性
//                urlConnection.setRequestProperty("accept", "*/*");
//                urlConnection.setRequestProperty("connection", "Keep-Alive");
//                urlConnection.setRequestProperty("user-agent", "Mozilla/4.0(compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//
//                //发送POST请求必须设置如下两行
//                urlConnection.setDoOutput(true);
//                urlConnection.setDoInput(true);
//                //获取所有相应头字段
//                Map<String, List<String>> map = urlConnection.getHeaderFields();
//                //遍历所有响应头字段
//                for (String key:map.keySet()){
//                    Log.i("POST方式请求", ""+map.get(key));
//                }
//                //获取URLConnection对象对应的输出流
//                printWriter = new PrintWriter(urlConnection.getOutputStream());
//                //发送请求参数
//                printWriter.print(params);
//                //flush输出流缓冲
//                printWriter.flush();
//                //定义BufferReader输入流来读取URL的响应
//                bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
//                String line;
//                for (;(line = bufferedReader.readLine()) != null;){
//                    result += "\n" + line;
//                }
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                Log.e("GET方式请求", "发送GET请求异常"+e);
//                e.printStackTrace();
//            }
//        } catch (MalformedURLException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            if (null != bufferedReader){
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            if (null != printWriter){
//                printWriter.close();
//            }
//        }
//        return result;
//    }

}
