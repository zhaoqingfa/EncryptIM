package com.encrypt.im.common.widgets.xscroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.encrypt.im.R;

/**
 * Created by zqf on 2018/12/19.
 */

public class XScrollView extends ScrollView implements AbsListView.OnScrollListener{

    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;

    private LinearLayout mHeaderLayout;
    private LinearLayout mContentLayout;
    private LinearLayout mFooterLayout;

    private XHeaderView mHeaderView;
    private XFooterView mFooterView;

    private int mHeaderContentHeight;

    private boolean mIsShowHeaderView;
    private boolean mIsShowFooterView;
    private boolean mIsEnablePullRefresh;
    private boolean mIsEnablePullLoad;

    private float mLastY = -1;

    public XScrollView(Context context) {
        super(context);
        init(context);
    }

    public XScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_xscroll, this);
        mContentLayout = (LinearLayout) findViewById(R.id.mll_content_layout);
        mHeaderLayout = (LinearLayout) findViewById(R.id.mll_header_layout);
        mFooterLayout = (LinearLayout) findViewById(R.id.mll_footer_layout);

//        this.setOnScrollChangeListener(this);

        // init header view
        mHeaderView = new XHeaderView(context);
        mHeaderLayout.addView(mHeaderView);

        // init footer view
        mFooterView = new XFooterView(context);
        mFooterLayout.addView(mFooterView);

        // init header height
        ViewTreeObserver observer = mHeaderView.getViewTreeObserver();
        if (null != observer) {
            observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @SuppressWarnings("deprecation")
                @Override
                public void onGlobalLayout() {
                    mHeaderContentHeight = mHeaderView.getHeaderContentHeight();
                    ViewTreeObserver observer = getViewTreeObserver();
                    if (null != observer) {
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            observer.removeGlobalOnLayoutListener(this);
                        } else {
                            observer.removeOnGlobalLayoutListener(this);
                        }
                    }
                }
            });
        }
    }

    /**
     * Set the content ViewGroup for XScrollView.
     *
     * @param content
     */
    public void setContentView(View content) {
        if (mContentLayout.getChildCount() > 0) {
            mContentLayout.removeAllViews();
        }
        mContentLayout.addView(content);
    }

    /**
     * Enable or disable pull down refresh feature.
     *
     * @param enable
     */
    public void setPullRefreshEnable(boolean enable) {
        setPullRefreshEnable(enable, true);
    }

    /**
     * Enable or disable pull down refresh feature.
     * @param enable
     * @param isShowHeader
     */
    public void setPullRefreshEnable(boolean enable, boolean isShowHeader) {
        mIsEnablePullRefresh = enable;
        mIsShowHeaderView = isShowHeader;
        mHeaderView.setVisibility(enable && isShowHeader ? View.VISIBLE : View.INVISIBLE);
    }

//    /**
//     * Enable or disable pull up load more feature.
//     *
//     * @param enable
//     */
//    public void setPullLoadEnable(boolean enable) {
//        mIsEnablePullRefresh = enable;
//
//        if (!mEnablePullLoad) {
//            mFooterView.setBottomMargin(0);
//            mFooterView.hide();
//            mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
//            mFooterView.setOnClickListener(null);
//
//        } else {
//            mPullLoading = false;
//            mFooterView.setPadding(0, 0, 0, 0);
//            mFooterView.show();
//            mFooterView.setState(XFooterView.STATE_NORMAL);
//            // both "pull up" and "click" will invoke load more.
//            mFooterView.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startLoadMore();
//                }
//            });
//        }
//    }

    /**
     * Stop refresh, reset header view.
     */
    public void stopRefresh() {
//        if (mPullRefreshing) {
//            mPullRefreshing = false;
//            resetHeaderHeight();
//        }
    }

    /**
     * Stop load more, reset footer view.
     */
    public void stopLoadMore() {
//        if (mPullLoading) {
//            mPullLoading = false;
//            mFooterView.setState(XFooterView.STATE_NORMAL);
//        }
    }
    /**
     * Set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderView.setRefreshTime(time);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;

            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();

                if (isTop() && (mHeaderView.getVisibleHeight() > 0 || deltaY > 0)) {
                    // the first item is showing, header has shown or pull down.
                    updateHeaderHeight(deltaY / OFFSET_RADIO);
//                    invokeOnScrolling();

//                } else if (isBottom() && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
//                    // last item, already pulled up or want to pull up.
//                    updateFooterHeight(-deltaY / OFFSET_RADIO);

                }
                break;

            default:
                // reset
                mLastY = -1;

//                resetHeaderOrBottom();
                break;
        }

        return super.onTouchEvent(ev);
    }

    private boolean isTop() {
        return getScrollY() <= 0 || mHeaderView.getVisibleHeight() > mHeaderContentHeight
                || mContentLayout.getTop() > 0;
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());

//        if (mEnablePullRefresh && !mPullRefreshing) {
//            // update the arrow image unrefreshing
//            if (mHeader.getVisibleHeight() > mHeaderHeight) {
//                mHeader.setState(XHeaderView.STATE_READY);
//            } else {
//                mHeader.setState(XHeaderView.STATE_NORMAL);
//            }
//        }

        // scroll to top each time
        post(new Runnable() {
            @Override
            public void run() {
                XScrollView.this.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

//    private boolean isBottom() {
//        return Math.abs(getScrollY() + getHeight() - computeVerticalScrollRange()) <= 5 ||
//                (getScrollY() > 0 && null != mFooterView && mFooterView.getBottomMargin() > 0);
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }


}
