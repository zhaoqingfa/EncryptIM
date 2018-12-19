package com.encrypt.im.business.chat;


import com.encrypt.im.R;
import com.encrypt.im.base.BaseActivity;

public class ChatActivity extends BaseActivity<ChatPresenter> implements IChatContact.IV {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    public ChatPresenter initPresenter() {
        return new ChatPresenter(this);
    }
}
