package com.encrypt.im.common.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.encrypt.im.common.log.LogUtil;
import com.encrypt.im.net.socket.SocketClient;
import com.encrypt.im.util.Config;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * Created by v_zhaoqingfa on 2018/12/17.
 */

public class TCPHeartService extends Service{

//    private static final long HEART_TIME = 5 * 1000;
//
//    private String mIp;
//    private int mPort;
//    private SocketClient mSocketClient;
//
//    private Handler mHandler;
//    private Runnable mRunnable;
//
//    private long sendTime = 0L;
//
//    private IBinder mIBinder = new ITCPHeartAidlInterface.Stub() {
//        @Override
//        public String toActivity(int code, String msg) throws RemoteException {
//            return "";
//        }
//
//        @Override
//        public void toService(String ip, int port) throws RemoteException {
//            mIp = ip;
//            mPort = port;
//        }
//    };
//
//    private SocketClient.SocketEventListener listener = new SocketClient.SocketEventListener() {
//        @Override
//        public void onDataReceived(byte[] data, short capacity) {
//            LogUtil.i("TCPHeartService read success ==>" + Arrays.toString(data));
//        }
//
//        @Override
//        public void onConnected(short result, Exception e) {
//            LogUtil.i("onConnected==>" + result);
//            if (e != null) {
//                LogUtil.i("onConnected=====>>>" + e.toString());
//            }
//            if (SocketClient.SUCCESS == result) {
//                if (mHandler != null && mRunnable != null) {
//                    mHandler.postDelayed(mRunnable, 0);
//                }
//            }
//        }
//
//        @Override
//        public void onDisconnected(short result, Exception e) {
//
//        }
//
//        @Override
//        public void onDataDelivered(short result, Object e) {
//            if (SocketClient.SUCCESS == result) {
//                sendTime = System.currentTimeMillis();
//                LogUtil.i("TCPHeartService write success ==>" + Arrays.toString(((ByteBuffer) e).array()));
//            }
//        }
//    };
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        LogUtil.i("onCreate");
//        ByteBuffer buffer = ByteBuffer.allocate(10000);
//        byte[] bytes = new byte[32];
//        for (int i = 0; i < bytes.length; i++) {
//            bytes[i] = (byte) i;
//        }
//        buffer = ByteBuffer.wrap(bytes);
//        System.out.println(buffer);
//
//        buffer = ByteBuffer.wrap(bytes, 10, 10);
//
//        initSocket();
//        mHandler = new Handler();
//        final ByteBuffer finalBuffer = buffer;
//        mRunnable = new Runnable() {
//            @Override
//            public void run() {
//                LogUtil.i("执行发送");
//                mSocketClient.send(finalBuffer);
//                mHandler.postDelayed(mRunnable, HEART_TIME);
//            }
//        };
//    }
//
//    private void initSocket() {
//        mIp = TextUtils.isEmpty(mIp) ? Config.DEFAULT_IP : mIp;
//        mPort = mPort == 0 ? Config.DEFAULT_PORT : mPort;
//        mSocketClient = new SocketClient(mIp, mPort);
//        mSocketClient.setSocketEventListener(listener);
//        mSocketClient.connect();
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.i("onBind");
//        return mIBinder;
        return null;
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        LogUtil.i("TCPHeartService onDestroy");
//        if (mHandler != null && mRunnable != null) {
//            mHandler.removeCallbacks(mRunnable);
//        }
//        if (mSocketClient != null) {
//            mSocketClient.setSocketEventListener(null);
//        }
//        mSocketClient.close();
//    }
}
