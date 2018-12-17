package com.encrypt.im.net.http;

import android.content.Context;
import android.net.ParseException;

import com.encrypt.im.MainApplication;
import com.encrypt.im.R;
import com.encrypt.im.common.log.LogUtil;
import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by zqf on 2018/11/23.
 */

public class ApiException {
    public static String handleException(Throwable e) {
        String error;
        Context context = MainApplication.getInstance().getContext();
        if (e instanceof SocketTimeoutException) {
            LogUtil.e(context.getResources().getString(R.string.net_timeout_error));
            error = context.getResources().getString(R.string.net_connect_error);
        } else if (e instanceof ConnectException) {
            LogUtil.e(context.getResources().getString(R.string.net_connect_error));
            error = context.getResources().getString(R.string.net_connect_error);
        } else if (e instanceof UnknownHostException) {
            LogUtil.e(context.getResources().getString(R.string.net_unknown_error));
            error = context.getResources().getString(R.string.net_connect_error);
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            LogUtil.e(context.getResources().getString(R.string.data_json_error));
            error = context.getResources().getString(R.string.data_json_error);
        } else if (e instanceof IllegalArgumentException) {
            LogUtil.e(context.getResources().getString(R.string.parameter_error));
            error = context.getResources().getString(R.string.parameter_error);
        } else if (e instanceof RuntimeException) {
            LogUtil.e(e.getCause().getMessage());
            error = e.getCause().getMessage();
        } else {
            try {
                LogUtil.e(context.getResources().getString(R.string.unknown_error)
                        + e.getMessage());
            } catch (Exception e1) {
                LogUtil.e(context.getResources().getString(R.string.unknown_error));
            }
            error = context.getResources().getString(R.string.unknown_error);
        }
        e.printStackTrace();
        return error;
    }
}
