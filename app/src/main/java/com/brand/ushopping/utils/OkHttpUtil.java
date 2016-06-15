package com.brand.ushopping.utils;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2016/3/21.
 */
public class OkHttpUtil
{
    private static OkHttpClient client = new OkHttpClient();
    private static final String BASE_URL = EnvValues.serverPath;
    public static final MediaType JSONMediaType  = MediaType.parse("application/json; charset=utf-8");

    public static String post(String url, String json) throws Exception{
        RequestBody formBody = new FormBody.Builder()
                .add("param", json)
                .build();

//        RequestBody body = RequestBody.create(JSONMediaType, json);
        Request request = new Request.Builder()
                .url(getAbsoluteUrl(url))
                .post(formBody)
                .build();
        Response response = client.newCall(request).execute();
        return response.body().string();

    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
