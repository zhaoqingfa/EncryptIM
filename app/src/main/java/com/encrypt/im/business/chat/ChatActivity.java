package com.encrypt.im.business.chat;


import android.view.LayoutInflater;
import android.view.View;

import com.encrypt.im.R;
import com.encrypt.im.base.BaseActivity;
import com.encrypt.im.common.widgets.xscroll.XScrollView;

public class ChatActivity extends BaseActivity<ChatPresenter> implements IChatContact.IV {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void initView() {
        super.initView();
        View inflate = LayoutInflater.from(context).inflate(R.layout.fragment_chat, null);
        XScrollView scrollView = findViewById(R.id.test);
        scrollView.setPullLoadEnable(false);
        scrollView.setPullRefreshEnable(false);
        scrollView.setContentView(inflate);
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter(this);
    }
}
