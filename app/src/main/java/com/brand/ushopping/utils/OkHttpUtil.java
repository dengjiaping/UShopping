package com.brand.ushopping.utils;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/3/21.
 */
public class OkHttpUtil
{
    private static OkHttpClient client = new OkHttpClient();

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
