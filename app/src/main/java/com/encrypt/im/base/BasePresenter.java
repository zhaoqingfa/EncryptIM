package com.encrypt.im.base;

import android.annotation.SuppressLint;
import android.support.v4.util.Preconditions;

import com.encrypt.im.base.interfac.IModel;
import com.encrypt.im.base.interfac.IPresenter;
import com.encrypt.im.base.interfac.IView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by zqf on 2018/11/23.
 */

public abstract class BasePresenter<V extends IView, M extends IModel> implements IPresenter {
    protected V view;
    protected M model;
    // 将所有正在处理的Subscription都添加到CompositeSubscription中
    protected CompositeDisposable mCompositeDisposable;

    /**
     * 如果当前页面同时需要 Model 层和 View 层,则使用此构造函数(默认)
     * @param model
     * @param view
     */
    @SuppressLint("RestrictedApi")
    public BasePresenter(V view, M model) {
        Preconditions.checkNotNull(model, IModel.class.getName() + "%s cannot be null");
        Preconditions.checkNotNull(view, IView.class.getName() + "%s cannot be null");
        this.model = model;
        this.view = view;
        init();
    }

    /**
     * 如果当前页面不需要操作数据,只需要 View 层,则使用此构造函数
     * @param view
     */
    @SuppressLint("RestrictedApi")
    public BasePresenter(V view) {
        Preconditions.checkNotNull(view, IView.class.getName() + "%s cannot be null");
        this.view = view;
        init();
    }

    public BasePresenter() {
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public void destroy() {
        unDisposable();
        view = null;
        if (model != null) {
            model.destroy();
            model = null;
        }
        mCompositeDisposable = null;
    }

    /**
     * 将Disposable添加
     * @param subscription
     */
    public void addDisposable(Disposable subscription) {
        // csb 如果解绑了的话添加 sb 需要新的实例否则绑定时无效的
        if (mCompositeDisposable == null || mCompositeDisposable.isDisposed()) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    /**
     * 在界面退出等需要解绑观察者的情况下调用此方法统一解绑，防止Rx造成的内存泄漏
     */
    public void unDisposable() {
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
    }
}
