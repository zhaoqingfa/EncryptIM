package com.encrypt.im.net.http;

import com.encrypt.im.MainApplication;
import com.encrypt.im.common.log.LogUtil;
import com.encrypt.im.util.Config;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by zqf on 2018/11/25.
 */

public class HttpManager {
    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;
    private long timeout = Config.CONNECT_TIMEOUT;

    private HttpManager() {
        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .build();
        if (MainApplication.getInstance().isDebug()) {
            okHttpClient.newBuilder().addInterceptor(getLogInterceptor());
        }
        retrofit = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setLenient()
                        .create()
                ))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
//                .baseUrl(baseUrl)
                .build();
    }

    private static class LozyHolder {
        private static final HttpManager INSTANCE = new HttpManager();
    }

    public static HttpManager getInstance() {
        return LozyHolder.INSTANCE;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
        okHttpClient.newBuilder().connectTimeout(timeout, TimeUnit.MILLISECONDS);
    }


    /**
     * 日志拦截器
     * 将你访问的接口信息
     * @return 拦截器
     */
    private HttpLogInterceptor getLogInterceptor() {
        //日志显示级别
        HttpLogInterceptor.Level level = HttpLogInterceptor.Level.HEADERS;
        //新建log拦截器
        HttpLogInterceptor logInterceptor = new HttpLogInterceptor(new HttpLogInterceptor.Logger() {
            @Override
            public void log(String message) {
                LogUtil.d("--->" + message);
            }
        });
        logInterceptor.setLevel(level);
        return logInterceptor;
    }
}
