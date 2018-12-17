package com.encrypt.im.business.main;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.encrypt.im.R;
import com.encrypt.im.base.BaseActivity;
import com.encrypt.im.base.interfac.IPresenter;
import com.encrypt.im.common.log.LogUtil;
import com.encrypt.im.common.service.TCPHeartService;
import com.encrypt.im.common.widgets.ViewPagerAdapter;
import com.encrypt.im.util.Config;

public class MainActivity extends BaseActivity<IPresenter> {
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private ViewPagerAdapter adapter;

    private ITCPHeartAidlInterface aidlInterface;
    private ServiceConnection serviceConnection;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mViewPager = findViewById(R.id.vp);
        mTabLayout = findViewById(R.id.tl);
    }

    @Override
    protected void initData() {
        super.initData();
        bindService();
//        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, null, null);
//        mViewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void initListener() {
        super.initListener();
        mTabLayout.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        break;
                }
//                tab.getCustomView().findViewById()
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    private void bindService() {
        Intent intent = new Intent(this, TCPHeartService.class);
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LogUtil.i("onServiceConnected");
                aidlInterface = ITCPHeartAidlInterface.Stub.asInterface(service);
                try {
                    aidlInterface.toService(Config.DEFAULT_IP, Config.DEFAULT_PORT);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                aidlInterface = null;
            }
        };
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void unBindService() {
        if (serviceConnection != null) {
            unbindService(serviceConnection);
        }
    }

    @Override
    public IPresenter initPresenter() {
        return null;
    }
}
