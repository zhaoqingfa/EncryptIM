package com.encrypt.im.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by v_zhaoqingfa on 2018/12/17.
 */

public class TCPServiceClient extends Service{
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
