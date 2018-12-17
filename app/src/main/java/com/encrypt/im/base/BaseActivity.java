package com.encrypt.im.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.encrypt.im.R;
import com.encrypt.im.base.interfac.IPresenter;
import com.encrypt.im.base.interfac.IView;
import com.encrypt.im.manager.ActivityManager;
import com.encrypt.im.util.StatusBarUtil;

/**
 * Created by zqf on 2018/11/23.
 */

public abstract class BaseActivity<P extends IPresenter> extends AppCompatActivity implements IView {
    protected P presenter;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 将当前activity添加进入管理栈
        ActivityManager.getInstance().addActivity(this);
        context = this;
        setContentView(getLayoutId());
        presenter = initPresenter();
        setStatusBar();
        initView();
        initData();
        initListener();
    }

    protected void initView() {}

    protected void initData() {}

    protected void initListener() {}

    @SuppressLint("ResourceAsColor")
    protected void setStatusBar() {
        StatusBarUtil.setColor(this, R.color.colorAccent);
    }

    protected abstract int getLayoutId();

    /**
     * 在子类中初始化对应的presenter
     * @return 相应的presenter
     */
    public abstract P initPresenter();

    @Override
    protected void onDestroy() {
        // 将当前activity移除管理栈
        ActivityManager.getInstance().removeActivity(this);
        if (presenter != null) {
            // 在presenter中解绑释放view
            presenter.destroy();
            presenter = null;
        }
        super.onDestroy();
    }

    @Override
    public void showLoadingDialog(String msg) {

    }

    @Override
    public void dismissLoadingDialog() {

    }
}
