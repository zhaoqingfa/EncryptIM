package com.encrypt.im.common.widgets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.encrypt.im.R;

/**
 * Created by zqf on 2018/11/27.
 */

public class ImageTextView extends LinearLayout {
    private ImageView mImageView;
    private TextView mTextView;

    public ImageTextView(Context context) {
        super(context);
        init(context);
    }
    public ImageTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.view_image_text, this);
        mImageView = (ImageView) findViewById(R.id.iv);
        mTextView = (TextView) findViewById(R.id.tv);
    }

    public void setImageView(int resId) {
        mImageView.setImageResource(resId);
    }

    public void setText(String text) {
        mTextView.setText(text);
    }

    public void setText(int resId) {
        mTextView.setText(resId);
    }
}
