package com.encrypt.im.net.socket;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.encrypt.im.common.log.LogUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by zqf on 18/11/30.
 */

public class SocketClient {
    
    private static final String TAG = "socekt";

    public interface SocketEventListener {
        void onDataReceived(final byte[] data, short capacity);

        void onConnected(short result, Exception e);

        void onDisconnected(short result, Exception e);

        void onDataDelivered(short result, Object e);
    }

    public enum ConnectionState {
        CLOSING,
        CLOSED,
        CONNECTING,
        CONNECTED
    }

    public static final short SUCCESS = 0;
    public static final short ERROR_ALREADY_CONNECTED = 1;
    public static final short ERROR_NOT_CONNECTED = 2;
    public static final short ERROR_QUEUE_IS_FULL = 3;
    public static final short ERROR_OTHER_EXCEPTION = 4;


    private static final short SINGLE_BUFFER_CAPACITY = 512;
    private static final short ASSIST_MSG_FLAG_CONNECT = 1;
    private static final short ASSIST_MSG_FLAG_CLOSE = 2;

    private volatile ConnectionState mConnectionState;
    private final String mAddr;
    private final int mPort;
    private SocketEventListener mSocketEventListener;
    private Thread mReadThread, mWriteThread;
    private Queue<ByteBuffer> mSendBufferQueue;
    private volatile Socket mSocket;
    private final Object mWriteSignal = new Object();

    private Handler mAssistHandler;


    public SocketClient(String addr, int port) {
        LogUtil.d(TAG, addr + port);
        mSendBufferQueue = new LinkedBlockingQueue<ByteBuffer>();
        mAddr = addr;
        mPort = port;
        mConnectionState = ConnectionState.CLOSED;
        HandlerThread ht = new HandlerThread("A-" + mAddr + "-" + mPort);
        ht.start();
        Handler.Callback mAssistCallback = new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case ASSIST_MSG_FLAG_CONNECT:
                        try {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            mSocket = new Socket(mAddr, mPort);
                            LogUtil.d(TAG, "build connect success");
                            if (mSocket.isConnected()) {
                                mConnectionState = ConnectionState.CONNECTED;
                                mReadThread.start();
                                mWriteThread.start();
                                if (mSocketEventListener != null) {
                                    mSocketEventListener.onConnected(SUCCESS, null);
                                }
                            } else {
                                if (mSocketEventListener != null) {
                                    mSocketEventListener.onConnected(ERROR_NOT_CONNECTED, null);
                                }
                            }

                        } catch (IOException e) {
                            LogUtil.d(TAG, "time out");
                            e.printStackTrace();
                            try {
                                mSocket.close();
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            } finally {
                                mConnectionState = ConnectionState.CLOSED;
                                if (mSocketEventListener != null) {
                                    mSocketEventListener.onConnected(ERROR_OTHER_EXCEPTION, e);
                                }
                            }

                        }
                        break;
                    case ASSIST_MSG_FLAG_CLOSE:
                        if (mConnectionState == ConnectionState.CONNECTED) {
                            mConnectionState = ConnectionState.CLOSING;
                            try {
                                mReadThread.interrupt();
                                mWriteThread.interrupt();
                                mSocket.close();
                                mConnectionState = ConnectionState.CLOSED;
                                if (mSocketEventListener != null) {
                                    mSocketEventListener.onDisconnected(SUCCESS, null);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                if (mSocketEventListener != null) {
                                    mSocketEventListener
                                            .onDisconnected(ERROR_OTHER_EXCEPTION, null);
                                }
                            }
                        } else {
                            if (mSocketEventListener != null) {
                                mSocketEventListener.onDisconnected(ERROR_NOT_CONNECTED, null);
                            }
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        };
        mAssistHandler = new Handler(ht.getLooper(), mAssistCallback);
    }

    public void setSocketEventListener(SocketEventListener listener) {
        this.mSocketEventListener = listener;
    }

    public synchronized void connect() {
        if (mConnectionState == ConnectionState.CLOSED) {
            LogUtil.d(TAG, "build connect");
            mReadThread = new Thread(new SocketReader());
            mReadThread.setName("R-" + mAddr + "-" + mPort);
            mWriteThread = new Thread(new SocketWriter());
            mWriteThread.setName("W-" + mAddr + "-" + mPort);
            mConnectionState = ConnectionState.CONNECTING;
            Message msg = new Message();
            msg.what = ASSIST_MSG_FLAG_CONNECT;
            mAssistHandler.sendMessage(msg);
        } else {
            if (mSocketEventListener != null) {
                mSocketEventListener.onConnected(ERROR_ALREADY_CONNECTED, null);
            }
        }
    }

    public synchronized void close() {
        // 防止在链接中关闭导致状态混乱
        if (mConnectionState == ConnectionState.CONNECTING) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    close();
                }
            }, 50);
        }

        Message msg = new Message();
        msg.what = ASSIST_MSG_FLAG_CLOSE;
        mAssistHandler.sendMessage(msg);
    }

    public void send(final ByteBuffer buffer) {
        if (mConnectionState == ConnectionState.CONNECTED) {
            boolean result = false;
            synchronized (mWriteSignal) {
                result = mSendBufferQueue.add(buffer);
                if (result) {
                    mWriteSignal.notify();
                }
            }
            if (!result) {
                if (mSocketEventListener != null) {
                    mSocketEventListener.onDataDelivered(ERROR_QUEUE_IS_FULL, null);
                }
            }

        } else {
            if (mSocketEventListener != null) {
                mSocketEventListener.onDataDelivered(ERROR_NOT_CONNECTED, null);
            }
        }
    }

    public ConnectionState getSocketState() {
        return mConnectionState;
    }


    private class SocketReader implements Runnable {

        @Override
        public void run() {
            BufferedInputStream bis = null;
            while (mConnectionState == ConnectionState.CONNECTED
                    && mSocket.isConnected()) {
                byte[] buffer = new byte[SINGLE_BUFFER_CAPACITY];
                try {
                    bis = new BufferedInputStream(mSocket.getInputStream());
                    while (bis.read(buffer) != -1) {
                        if (mSocketEventListener != null) {
                            LogUtil.d(TAG, "read");
                            mSocketEventListener.onDataReceived(buffer, SINGLE_BUFFER_CAPACITY);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.e(TAG, "read exception:" + e.getMessage());
                }
            }
            try {
                if (bis != null) {
                    bis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class SocketWriter implements Runnable {
        @Override
        public void run() {
            BufferedOutputStream bos = null;
            while (mConnectionState == ConnectionState.CONNECTED
                    && mSocket.isConnected()) {
                try {
                    synchronized (mWriteSignal) {
                        mWriteSignal.wait();
                        bos = new BufferedOutputStream(mSocket.getOutputStream());
                        while (!mSendBufferQueue.isEmpty()) {
                            ByteBuffer buffer = mSendBufferQueue.poll();
                            if (buffer != null) {
                                buffer.position(0);
                                bos.write(buffer.array());
                                bos.flush();
                                if (mSocketEventListener != null) {
                                    LogUtil.d(TAG, "write");
                                    mSocketEventListener.onDataDelivered(SUCCESS, buffer);
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    LogUtil.d(TAG, "write exception:" + e.getMessage());
                    if (mSocketEventListener != null) {
                        mSocketEventListener.onDataDelivered(ERROR_OTHER_EXCEPTION, e);
                    }
                }

            }
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
