package com.encrypt.im.common.widgets.xscroll;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.encrypt.im.R;

/**
 * Created by v_zhaoqingfa on 2018/12/20.
 */

public class XHeaderView extends LinearLayout {
    public final static int STATE_NORMAL = 0;
    public final static int STATE_READY = 1;
    public final static int STATE_REFRESHING = 2;

    private final int ROTATE_ANIM_DURATION = 180;

    private LinearLayout mLayout;

    private RelativeLayout mHeaderContent;

    private ImageView mArrowImageView;

    private ProgressBar mProgressBar;

    private TextView mHintTextView;

    private TextView mHintTextTime;

    private int mState = STATE_NORMAL;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;

    private boolean mIsFirst;

    public XHeaderView(Context context) {
        super(context);
        init(context);
    }

    public XHeaderView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public XHeaderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        // Initial set header view height 0
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, 0);
        mLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.view_xheader, null);
        addView(mLayout, lp);
        setGravity(Gravity.BOTTOM);

        mHeaderContent = (RelativeLayout) findViewById(R.id.mrl_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.miv_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.mtv_header_hint_text);
        mHintTextTime = (TextView) findViewById(R.id.mtv_header_hint_time);
        mProgressBar = (ProgressBar) findViewById(R.id.mpb_header_progressbar);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    public void setState(int state) {
        if (state == mState && mIsFirst) {
            mIsFirst = true;
            return;
        }

        if (state == STATE_REFRESHING) {
            // show progress
            mArrowImageView.clearAnimation();
            mArrowImageView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            // show arrow image
            mArrowImageView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        switch (state) {
            case STATE_NORMAL:
                if (mState == STATE_READY) {
                    mArrowImageView.startAnimation(mRotateDownAnim);
                }

                if (mState == STATE_REFRESHING) {
                    mArrowImageView.clearAnimation();
                }

                mHintTextView.setText(R.string.x_header_hint_refresh_normal);
                break;

            case STATE_READY:
                if (mState != STATE_READY) {
                    mArrowImageView.clearAnimation();
                    mArrowImageView.startAnimation(mRotateUpAnim);
                    mHintTextView.setText(R.string.x_header_hint_refresh_ready);
                }
                break;

            case STATE_REFRESHING:
                mHintTextView.setText(R.string.x_header_hint_refresh_loading);
                break;

            default:
                break;
        }

        mState = state;
    }

    /**
     * Set the header view visible height.
     *
     * @param height
     */
    public void setVisibleHeight(int height) {
        if (height < 0) height = 0;
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mLayout.getLayoutParams();
        lp.height = height;
        mLayout.setLayoutParams(lp);
    }

    /**
     * Get the header view visible height.
     *
     * @return
     */
    public int getVisibleHeight() {
        return mLayout.getLayoutParams().height;
    }

    /**
     * Get the header view visible height.
     *
     * @return
     */
    public int getHeaderContentHeight() {
        return mHeaderContent.getHeight();
    }

    /**
     * Set last refresh time
     *
     * @param time
     */
    public void setRefreshTime(String time) {
        mHintTextTime.setText(time);
    }

    public void headerContentVisibility(int visibility) {
        mHeaderContent.setVisibility(visibility);
    }
}
