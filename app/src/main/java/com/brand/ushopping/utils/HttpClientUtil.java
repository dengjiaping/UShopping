package com.brand.ushopping.utils;

/**
 * Created by Administrator on 2015/11/2.
 */

import android.graphics.Bitmap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class HttpClientUtil
{
    private static final String BASE_URL = EnvValues.serverPath;

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    //获取httpclient
    private static HttpClient getHttpClient() {
        HttpClient httpClient = HttpClients.createDefault();

        return httpClient;
    }

    public static String post(String url, List<NameValuePair> params) throws Exception {
        // 创建HttpClient实例
        HttpClient client = getHttpClient();
        // 根据URL创建HttpPost实例
        HttpPost post = new HttpPost(getAbsoluteUrl(url));
        // 编码格式转换
        post.setHeader("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
        HttpEntity entity = new UrlEncodedFormEntity(params, HTTP.UTF_8);
        // 传入请求体
        post.setEntity(entity);
        // 发送请求，得到响应体
        HttpResponse response = client.execute(post);
        // 判断是否正常返回
        if (response.getStatusLine().getStatusCode() == 200) {
            // 解析数据
            InputStream is = response.getEntity().getContent();
            String s = convertInputStreamToString(is);

            return s;
        }
        else
        {
            return null;
        }
    }

    public static String get(String url, String paramsString) throws Exception {
        // 创建HttpClient实例
        HttpClient client = getHttpClient();

        HttpGet get = new HttpGet(url + "/" + paramsString);

        // 发送请求，得到响应体
        HttpResponse response = client.execute(get);
        // 判断是否正常返回
        if (response.getStatusLine().getStatusCode() == 200) {
            // 解析数据
            InputStream is = response.getEntity().getContent();
            String s = convertInputStreamToString(is);

            return s;
        }
        else
        {
            return null;
        }

    }

    public static byte[] getImageFromWeb(String url) throws Exception
    {
        byte[] result = null;

        // 创建HttpClient实例
        HttpClient client = getHttpClient();
        // 根据URL创建HttpPost实例
        HttpGet get = new HttpGet(url);

        // 发送请求，得到响应体
        HttpResponse response = client.execute(get);
        // 判断是否正常返回
        if (response.getStatusLine().getStatusCode() == 200) {
            // 解析数据
            InputStream is = response.getEntity().getContent();

            byte[] bytes = new byte[1024];
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            int count = 0;
            while ((count = is.read(bytes)) != -1) {
                bos.write(bytes, 0, count);
            }
            return bos.toByteArray();

        }

        return result;

    }



    private static String convertInputStreamToString(InputStream inputStream)
            throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    //图片上传
    public static String uploadImage(Bitmap bitmap) throws Exception {
        HttpClient client = getHttpClient();
        HttpPost post = new HttpPost(getAbsoluteUrl("image/upload"));

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        byte[] data = bos.toByteArray();

        MultipartEntity entity = new MultipartEntity();
        entity.addPart("file", new ByteArrayBody(data, "upload.jpg"));

        post.setEntity(entity);

        // 发送请求，得到响应体
        HttpResponse response = client.execute(post);
        // 判断是否正常返回
        if (response.getStatusLine().getStatusCode() == 200) {
            // 解析数据
            InputStream is = response.getEntity().getContent();
            String s = convertInputStreamToString(is);

            return s;
        } else {
            return null;
        }
    }
}
