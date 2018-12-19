package com.encrypt.im.common.widgets.xscroll;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by zqf on 2018/12/19.
 */

public class XScrollView extends ScrollView {
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

    }
}
