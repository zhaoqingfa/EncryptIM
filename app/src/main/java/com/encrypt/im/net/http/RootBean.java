package com.encrypt.im.net.http;

/**
 * Created by zqf on 2018/11/23.
 */

public class RootBean<T> {
    private int returnSign;  // 返回码
    private String returnInfo;  // 返回信息
    private T returnObj;  // 返回对象

    public int getReturnSign() {
        return returnSign;
    }

    public void setReturnSign(int returnSign) {
        this.returnSign = returnSign;
    }

    public String getReturnInfo() {
        return returnInfo;
    }

    public void setReturnInfo(String returnInfo) {
        this.returnInfo = returnInfo;
    }

    public T getReturnObj() {
        return returnObj;
    }

    public void setReturnObj(T returnObj) {
        this.returnObj = returnObj;
    }
}
