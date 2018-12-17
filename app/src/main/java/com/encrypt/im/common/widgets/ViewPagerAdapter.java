package com.encrypt.im.common.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by zqf on 2018/11/25.
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Fragment> fragments;
    private List<String> titleNames;
    public ViewPagerAdapter(FragmentManager fm, Context context,
                            List<Fragment> fragments, List<String> titleNames) {
        super(fm);
        this.context = context;
        this.fragments = fragments;
        this.titleNames = titleNames;
    }

    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return titleNames.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleNames.get(position);
    }
}
