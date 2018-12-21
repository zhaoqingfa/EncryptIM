package com.encrypt.im.common.widgets.xscroll;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.encrypt.im.R;

/**
 * Created by zqf on 2018/12/19.
 */

public class XScrollView extends ScrollView implements AbsListView.OnScrollListener{

    private final static int SCROLL_BACK_HEADER = 0;
    private final static int SCROLL_BACK_FOOTER = 1;

    // support iOS like pull
    private final static float OFFSET_RADIO = 1.8f;
    // scroll time
    private final static int SCROLL_DURATION = 400;
    // when pull up >= 50px
    private final static int PULL_LOAD_MORE_DELTA = 50;

    private LinearLayout mHeaderLayout;
    private LinearLayout mContentLayout;
    private LinearLayout mFooterLayout;

    private XHeaderView mHeaderView;
    private XFooterView mFooterView;

    private int mHeaderContentHeight;

    private boolean mIsEnablePullRefresh = true;
    private boolean mIsPullRefreshing = false;
    private boolean mIsAutoLoad = false;
    private boolean mIsEnablePullLoad = true;
    private boolean mIsPullLoading = false;
    // for mScroller, scroll back from header or footer.
    private int mScrollBack;

    private OnXScrollViewListener mListener;
    // used for scroll back
    private Scroller mScroller;


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

        mScroller = new Scroller(context, new DecelerateInterpolator());

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
        mIsEnablePullRefresh = enable;
        // disable, hide the content
        mHeaderView.headerContentVisibility(enable ? View.VISIBLE : View.INVISIBLE);
    }

    /**
     * Enable or disable pull up load more feature.
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mIsEnablePullLoad = enable;

        if (!mIsEnablePullLoad) {
            mFooterView.setBottomMargin(0);
            mFooterView.hide();
            mFooterView.setPadding(0, 0, 0, mFooterView.getHeight() * (-1));
            mFooterView.setOnClickListener(null);

        } else {
            mIsPullLoading = false;
            mFooterView.setPadding(0, 0, 0, 0);
            mFooterView.show();
            mFooterView.setState(XFooterView.STATE_NORMAL);
            // both "pull up" and "click" will invoke load more.
            mFooterView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /**
     * Stop refresh, reset header view.
     */
    public void stopRefresh() {
        if (mIsPullRefreshing) {
            mIsPullRefreshing = false;
            resetHeaderHeight();
        }

    }

    /**
     * Stop load more, reset footer view.
     */
    public void stopLoadMore() {
        if (mIsPullLoading) {
            mIsPullLoading = false;
            mFooterView.setState(XFooterView.STATE_NORMAL);
        }
    }

    /**
     * Set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHeaderView.setRefreshTime(time);
    }

    /**
     * Set listener.
     *
     * @param listener
     */
    public void setOnXScrollViewListener(OnXScrollViewListener listener) {
        mListener = listener;
    }

    /**
     * Enable or disable auto load more feature when scroll to bottom.
     *
     * @param enable
     */
    public void setAutoLoad(boolean enable) {
        mIsAutoLoad = enable;
    }

    /**
     * Auto call back refresh.
     */
    public void setAutoRefresh() {
        mHeaderView.setVisibleHeight(mHeaderContentHeight);
        mHeaderView.headerContentVisibility(View.VISIBLE);
        if (mIsEnablePullRefresh && !mIsPullRefreshing) {
            // update the arrow image not refreshing
            if (mHeaderView.getVisibleHeight() > mHeaderContentHeight) {
                mHeaderView.setState(XHeaderView.STATE_READY);
            } else {
                mHeaderView.setState(XHeaderView.STATE_NORMAL);
            }
        }

        mIsPullRefreshing = true;
        mHeaderView.setState(XHeaderView.STATE_REFRESHING);
        refresh();
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
                    invokeOnScrolling();

                } else if (isBottom() && (mFooterView.getBottomMargin() > 0 || deltaY < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-deltaY / OFFSET_RADIO);

                }
                break;

            default:
                // reset
                mLastY = -1;
                resetHeaderOrBottom();
                break;
        }

        return super.onTouchEvent(ev);
    }

    private boolean isTop() {
        return getScrollY() <= 0 || mHeaderView.getVisibleHeight() > mHeaderContentHeight
                || mContentLayout.getTop() > 0;
    }


    private boolean isBottom() {
        return Math.abs(getScrollY() + getHeight() - computeVerticalScrollRange()) <= 5 ||
                (getScrollY() > 0 && null != mFooterView && mFooterView.getBottomMargin() > 0);
    }

    private void resetHeaderOrBottom() {
        if (isTop()) {
            // invoke refresh
            if (mIsEnablePullRefresh && mHeaderView.getVisibleHeight() > mHeaderContentHeight) {
                mIsPullRefreshing = true;
                mHeaderView.setState(XHeaderView.STATE_REFRESHING);
                refresh();
            }
            resetHeaderHeight();

        } else if (isBottom()) {
            // invoke load more.
            if (mIsEnablePullLoad && mFooterView.getBottomMargin() > PULL_LOAD_MORE_DELTA) {
                startLoadMore();
            }
            resetFooterHeight();
        }
    }

    private void updateHeaderHeight(float delta) {
        mHeaderView.setVisibleHeight((int) delta + mHeaderView.getVisibleHeight());

        if (mIsEnablePullRefresh && !mIsPullRefreshing) {
            // update the arrow image unrefreshing
            if (mHeaderView.getVisibleHeight() > mHeaderContentHeight) {
                mHeaderView.setState(XHeaderView.STATE_READY);
            } else {
                mHeaderView.setState(XHeaderView.STATE_NORMAL);
            }
        }

        // scroll to top each time
        post(new Runnable() {
            @Override
            public void run() {
                XScrollView.this.fullScroll(ScrollView.FOCUS_UP);
            }
        });
    }

    private void resetHeaderHeight() {
        int height = mHeaderView.getVisibleHeight();
        if (height == 0) return;

        // refreshing and header isn't shown fully. do nothing.
        if (mIsPullRefreshing && height <= mHeaderContentHeight) return;

        // default: scroll back to dismiss header.
        int finalHeight = 0;
        // is refreshing, just scroll back to show all the header.
        if (mIsPullRefreshing && height > mHeaderContentHeight) {
            finalHeight = mHeaderContentHeight;
        }

        mScrollBack = SCROLL_BACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, SCROLL_DURATION);

        // trigger computeScroll
        invalidate();
    }

    private void updateFooterHeight(float delta) {
        int height = mFooterView.getBottomMargin() + (int) delta;

        if (mIsEnablePullLoad && !mIsPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) {
                // height enough to invoke load  more.
                mFooterView.setState(XFooterView.STATE_READY);
            } else {
                mFooterView.setState(XFooterView.STATE_NORMAL);
            }
        }

        mFooterView.setBottomMargin(height);

        // scroll to bottom
        post(new Runnable() {
            @Override
            public void run() {
                XScrollView.this.fullScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    private void resetFooterHeight() {
        int bottomMargin = mFooterView.getBottomMargin();

        if (bottomMargin > 0) {
            mScrollBack = SCROLL_BACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin, SCROLL_DURATION);
            invalidate();
        }
    }

    private void startLoadMore() {
        if (!mIsPullLoading) {
            mIsPullLoading = true;
            mFooterView.setState(XFooterView.STATE_LOADING);
            loadMore();
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLL_BACK_HEADER) {
                mHeaderView.setVisibleHeight(mScroller.getCurrY());
            } else {
                mFooterView.setBottomMargin(mScroller.getCurrY());
            }

            postInvalidate();
            invokeOnScrolling();
        }
        super.computeScroll();
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        // Grab the last child placed in the ScrollView, we need it to determinate the bottom position.
        View view = getChildAt(getChildCount() - 1);

        if (null != view) {
            // Calculate the scroll diff
            int diff = (view.getBottom() - (view.getHeight() + view.getScrollY()));

            // if diff is zero, then the bottom has been reached
            if (diff == 0 && mIsAutoLoad) {
                // notify that we have reached the bottom
                startLoadMore();
            }
        }

        super.onScrollChanged(l, t, oldl, oldt);
    }

    private void invokeOnScrolling() {
//        if (mScrollListener instanceof OnXScrollListener) {
//            OnXScrollListener l = (OnXScrollListener) mScrollListener;
//            l.onXScrolling(this);
//        }
    }

//    public void setOnScrollListener(OnScrollListener l) {
//        mScrollListener = l;
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//        if (mScrollListener != null) {
//            mScrollListener.onScrollStateChanged(view, scrollState);
//        }
    }
//
    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {
//        // send to user's listener
//        if (mScrollListener != null) {
//            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
//        }
    }

    //    /**
//     * You can listen ListView.OnScrollListener or this one. it will invoke
//     * onXScrolling when header/footer scroll back.
//     */
//    public interface OnXScrollListener extends OnScrollListener {
//        public void onXScrolling(View view);
//    }

    private void refresh() {
        if (mIsEnablePullRefresh && null != mListener) {
            mListener.onRefresh();
        }
    }

    private void loadMore() {
        if (mIsEnablePullLoad && null != mListener) {
            mListener.onLoadMore();
        }
    }

    /**
     * Implements this interface to get refresh/load more event.
     */
    public interface OnXScrollViewListener {
        public void onRefresh();

        public void onLoadMore();
    }


}
