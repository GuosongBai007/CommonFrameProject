package com.base.app;

import android.support.multidex.MultiDexApplication;

import com.base.thread.Dispatcher;
import com.base.utils.AppLogger;
import com.base.utils.AppUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.frame.baselibrary.BuildConfig;

/**
 * Author:GuosongBai
 * Date:2018/11/8 10:44
 * Description:
 */
public class BaseApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.init(this);
        AppLogger.init(BuildConfig.DEBUG, "");
        Fresco.initialize(this);
        Dispatcher.init(Thread.currentThread());

    }
}
