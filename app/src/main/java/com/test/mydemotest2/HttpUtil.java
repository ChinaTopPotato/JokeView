package com.test.mydemotest2;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 利用外部库OkHttp进行网络数据的传输
 */

public class HttpUtil {
    public static void sendOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
