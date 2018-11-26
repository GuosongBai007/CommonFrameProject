package com.dcb.pushlib.utils;

import android.os.Handler;
import android.os.Looper;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class PushHandler {
    private static Handler mHandler = null;

    public static Handler handler() {
        if (mHandler == null) {
            synchronized (PushHandler.class) {
                mHandler = new Handler(Looper.getMainLooper());
            }
        }
        return mHandler;
    }

}
