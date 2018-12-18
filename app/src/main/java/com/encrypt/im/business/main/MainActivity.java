package com.encrypt.im.business.main;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.encrypt.im.R;
import com.encrypt.im.base.BaseActivity;
import com.encrypt.im.base.interfac.IPresenter;
import com.encrypt.im.common.widgets.ControllableViewPager;
import com.encrypt.im.common.widgets.ImageTextView;
import com.encrypt.im.common.widgets.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<IPresenter> {
    private ControllableViewPager mViewPager;
    private TabLayout mTabLayout;

    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;
    private List<String> titles;
    private List<Integer> imagIds;

//    private ITCPHeartAidlInterface aidlInterface;
//    private ServiceConnection serviceConnection;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        super.initView();
        mViewPager = findViewById(R.id.vp);
        mViewPager.setCanScroll(false);
        mTabLayout = findViewById(R.id.tl);
    }

    @Override
    protected void initData() {
        super.initData();
//        bindService();
        initTabData();
        setTabFragment();

    }

    private void initTabData() {
        titles = new ArrayList<>();
        titles.add(getResources().getString(R.string.tab_chat));
        titles.add(getResources().getString(R.string.tab_contacts));
        titles.add(getResources().getString(R.string.tab_found));
        titles.add(getResources().getString(R.string.tab_me));

        imagIds = new ArrayList<>();
        imagIds.add(R.drawable.tab_chat_image_selector);
        imagIds.add(R.drawable.tab_contacts_image_selector);
        imagIds.add(R.drawable.tab_found_image_selector);
        imagIds.add(R.drawable.tab_me_image_selector);

        fragments = new ArrayList<>();
        fragments.add(ChatFragment.newInstance());
        fragments.add(ContactsFragment.newInstance());
        fragments.add(FoundFragment.newInstance());
        fragments.add(MeFragment.newInstance());
    }
    
    private void setTabFragment() {
        for (int i = 0; i < titles.size(); i++) {
            ImageTextView imageTextView = new ImageTextView(this);
            imageTextView.setText(titles.get(i));
            imageTextView.setImageView(imagIds.get(i));
            TabLayout.Tab tab = mTabLayout.newTab();
            if (tab != null) {
                tab.setCustomView(imageTextView);
            }
            if (i == 0) {
                mTabLayout.addTab(tab, true);
            } else {
                mTabLayout.addTab(tab, false);
            }
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), this, fragments, titles);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unBindService();
    }

    private void bindService() {
//        Intent intent = new Intent(this, TCPHeartService.class);
//        serviceConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName name, IBinder service) {
//                LogUtil.i("onServiceConnected");
//                aidlInterface = ITCPHeartAidlInterface.Stub.asInterface(service);
//                try {
//                    aidlInterface.toService(Config.DEFAULT_IP, Config.DEFAULT_PORT);
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName name) {
//                aidlInterface = null;
//            }
//        };
//        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private void unBindService() {
//        if (serviceConnection != null) {
//            unbindService(serviceConnection);
//        }
    }

    @Override
    public IPresenter initPresenter() {
        return null;
    }
}
