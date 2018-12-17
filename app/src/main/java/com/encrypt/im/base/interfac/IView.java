package com.encrypt.im.base.interfac;

/**
 * Created by zqf on 2018/11/23.
 */

public interface IView {
    /**
     * 展示加载弹窗
     * @param msg
     */
    void showLoadingDialog(String msg);

    /**
     * 取消加载弹窗
     */
    void dismissLoadingDialog();
}
