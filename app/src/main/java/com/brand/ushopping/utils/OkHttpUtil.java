package com.brand.ushopping.utils;

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
    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public static String post(String url, String json) {
        try
        {
            RequestBody body = RequestBody.create(JSONMediaType, json);
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            Response response = client.newCall(request).execute();
            return response.body().string();

        }catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;

    }

//    public static String post(String url, String param)
//    {
//        try {
//            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://ajax.googleapis.com/ajax/services/search/images").newBuilder();
//            urlBuilder.addQueryParameter("data",param);
//
//            RequestBody formBody = new FormEncodingBuilder()
//                    .add("platform", "android")
//                    .add("name", "bug")
//                    .add("subject", "XXXXXXXXXXXXXXX")
//                    .build();
//
//            Request request = new Request.Builder()
//                    .url(url)
//                    .post(formBody)
//                    .build();
//
//            Response response = client.newCall(request).execute();;
//
//            if (response.isSuccessful()) {
//                return response.body().string();
//            }
//            else
//            {
//                return null;
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//
//        }
//
//    }

}
