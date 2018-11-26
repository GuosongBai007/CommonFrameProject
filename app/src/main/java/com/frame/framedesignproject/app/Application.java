package com.frame.framedesignproject.app;

import android.content.Context;
import android.util.Log;

import com.base.app.BaseApplication;
import com.base.utils.AppLogger;
import com.dcb.pushlib.Const;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.Push;
import com.dcb.pushlib.model.Message;
import com.frame.framedesignproject.BuildConfig;

/**
 * Author:GuosongBai
 * Date:2018/11/8 10:40
 * Description:
 */
public class Application extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        initPush();
    }

    /**
     *
     *
     */
    private void initPush() {
        Const.setFlyme_APP_Debug("123456", "3544fasfasf");
        Const.setFlyme_APP_Release("123456", "3544fasfasf");

        Const.setMiUI_APP_Debug("123456", "3544fasfasf");
        Const.setMiUI_APP_Release("123456", "3544fasfasf");

        String pageName = BuildConfig.DEBUG ? "com.frame.framedesignproject.debug" : "com.frame.framedesignproject";
        Const.setUmeng_package_name(pageName);
        Const.setUmeng_APP_Debug("5bed3684f1f556cab7000210", "9b7654e61c69d13def30c1d1ce5b0ee5");
        Const.setUmeng_APP_Release("5bed3684f1f556cab7000210", "9b7654e61c69d13def30c1d1ce5b0ee5");

        Const.setOPPO_APP_Debug("123456","123456");
        Const.setOPPO_APP_Release("123456","123456");

        Push.register(this, BuildConfig.DEBUG, new IPushInterface() {
            @Override
            public void onRegister(Context context, String pushToken) {
                AppLogger.i("GuosongBai Token", pushToken);
                Push.sPushToken = pushToken;
            }

            @Override
            public void onRegisterFailed(String osType, String msg) {
                AppLogger.i("GuosongBai onRegisterFailed", osType + "==" + msg);
            }

            @Override
            public void onUnRegister(Context context) {

            }

            @Override
            public void onPaused(Context context) {

            }

            @Override
            public void onResume(Context context) {

            }

            @Override
            public void onMessage(Context context, Message message) {

            }

            @Override
            public void onMessageClicked(Context context, Message message) {

            }

            @Override
            public void onCustomMessage(Context context, Message message) {

            }

            @Override
            public void onAlias(Context context, String alias) {

            }
        });
    }

}
