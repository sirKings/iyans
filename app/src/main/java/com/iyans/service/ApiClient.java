package com.iyans.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.OkHttpClient.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    public static final String BASE_URL = "http://mmdnow.com/DEMO/iyan/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(Level.BODY);
        OkHttpClient client = new Builder().addInterceptor(interceptor).writeTimeout(4, TimeUnit.SECONDS).connectTimeout(4, TimeUnit.MINUTES).readTimeout(4, TimeUnit.MINUTES).build();
        if (retrofit == null) {
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).client(client).build();
        }
        return retrofit;
    }
}
