package com.encrypt.im.common.log.klog;

import android.text.TextUtils;
import android.util.Log;

/**
 * Created by zqf on 2018/11/24.
 */

public class KLogUtil {
    public static boolean isEmpty(String line) {
        return TextUtils.isEmpty(line) || line.equals("\n")
                || line.equals("\t") || TextUtils.isEmpty(line.trim());
    }

    public static void printLine(String tag, boolean isTop) {
        if (isTop) {
            Log.d(tag, "╔══════════════════════════════════════════════════════════════════════════════");
        } else {
            Log.d(tag, "╚══════════════════════════════════════════════════════════════════════════════");
        }
    }
}
