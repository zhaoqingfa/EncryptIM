package com.encrypt.im.common.widgets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 手动控制viewpager是否可以左右滑动
 * Created by v_zhaoqingfa on 2018/12/18.
 */

public class ControllableViewPager extends ViewPager {
    private boolean isCanScroll = true;

    public ControllableViewPager(@NonNull Context context) {
        super(context);
    }

    public ControllableViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return isCanScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        return isCanScroll && super.onInterceptTouchEvent(arg0);
    }

    @Override
    public void setCurrentItem(int item) {
        if (isCanScroll) {
            super.setCurrentItem(item);
        } else {
            // false 去除滚动效果
            super.setCurrentItem(item, false);
        }
    }

    public boolean isCanScroll() {
        return isCanScroll;
    }

    public void setCanScroll(boolean canScroll) {
        isCanScroll = canScroll;
    }
}
