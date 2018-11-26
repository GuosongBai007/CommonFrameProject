package com.dcb.pushlib.utils;

import android.content.Context;
import android.content.Intent;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class PushUtil {

    public static void startLaunchActivity(Context context) {
        if (context == null) {
            return;
        }
        try {
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
