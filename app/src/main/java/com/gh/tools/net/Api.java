package com.gh.tools.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * author: gh
 * time: 2016/10/5.
 * description: 创建retrofit对象
 */

public class Api {

    private static ApiService SERVICE;

    public static ApiService getDefault() {
        if (SERVICE == null) {
            //日志输出
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(logging).build();
            SERVICE = new Retrofit.Builder()
                    .baseUrl("http://gank.io")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build()
                    .create(ApiService.class);
        }
        return SERVICE;
    }

}
