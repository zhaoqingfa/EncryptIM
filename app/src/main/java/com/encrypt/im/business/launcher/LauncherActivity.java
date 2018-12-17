package com.encrypt.im.business.launcher;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.encrypt.im.R;
import com.encrypt.im.base.BaseActivity;
import com.encrypt.im.base.interfac.IPresenter;
import com.encrypt.im.business.main.MainActivity;

public class LauncherActivity extends BaseActivity {
    private static final long TIME = 2 * 1000;

    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_launcher;
    }

    @Override
    protected void initData() {
        mHandler = new Handler(Looper.myLooper());
        mRunnable = new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        mHandler.postDelayed(mRunnable, TIME);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
            mHandler = null;
        }
    }

    @Override
    public IPresenter initPresenter() {
        return null;
    }
}
