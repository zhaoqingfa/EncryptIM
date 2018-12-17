package com.encrypt.im.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;

/**
 * Created by zqf on 2018/11/24.
 */

public class SystemUtil {

    /**
     * 检测当前apk包是否为debug
     * @param context
     * @return
     */
    public static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
