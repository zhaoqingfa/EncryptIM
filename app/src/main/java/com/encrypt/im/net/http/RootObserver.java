package com.encrypt.im.net.http;

import com.encrypt.im.util.Config;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by zqf on 2018/11/23.
 */

public abstract class RootObserver<T> implements Observer<RootBean<T>> {
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull RootBean<T> rootBean) {
        if (onSuccess(rootBean)) {
            return;
        }
        T t;
        if (rootBean != null){
            t = rootBean.getReturnObj();
            if (rootBean.getReturnSign() == Config.CODE_OK && t != null){
                onSuccessAll(t);
            }else {
                //TODO 在此处可处理各种返回码
            }
        }else {

        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        String error = ApiException.handleException(e);
        onFail(e, error);
    }

    @Override
    public void onComplete() {

    }

    /**
     * 失败时回调
     * @param e
     * @param error
     */
    public abstract void onFail(Throwable e, String error);

    /**
     * onNext时回调
     *       在这个方法中处理最初结果 （如隐藏加载框）
     * @param rootBean
     * @return
     */
    public abstract boolean onSuccess(RootBean<T> rootBean);

    /**
     * 成功时回调
     * @param t
     */
    public abstract void onSuccessAll(T t);
}
