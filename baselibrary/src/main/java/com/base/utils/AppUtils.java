package com.base.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import com.base.thread.Dispatcher;

import java.io.File;

/**
 * Author:GuosongBai
 * Date:2018/11/7 11:17
 * Description:
 */
public class AppUtils {
    private static Context sContext;

    private static CharSequence sLastToastMsg;
    private static Toast sLastToast;

    public static void init(Context context) {
        sContext = context;
    }

    public static Context getContext() {
        return sContext;
    }

    public static Drawable getDrawable(int resId) {
        if (sContext == null) {
            return null;
        }
        return sContext.getResources().getDrawable(resId);
    }

    public static void showToast(int resId) {
        showToast(sContext.getString(resId));
    }

    public static void showToast(final CharSequence text) {
        Dispatcher.runOnUiThread(() -> {
            if (sLastToastMsg != null && text.equals(sLastToastMsg)) {
                sLastToast.cancel();
            }
            sLastToastMsg = text;
            sLastToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
            sLastToast.show();
        });
    }

    public static void showToastInCenter(final CharSequence text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        if (sLastToastMsg != null && text.equals(sLastToastMsg)) {
            sLastToast.cancel();
        }
        sLastToastMsg = text;
        sLastToast = Toast.makeText(sContext, text, Toast.LENGTH_SHORT);
        sLastToast.setGravity(Gravity.CENTER, 0, 0);
        sLastToast.show();
    }

    public static void showToastInCenter(int resId) {
        showToastInCenter(sContext.getString(resId));
    }

    public static String getAppDir(){
        File file = sContext.getExternalFilesDir(null);
        if (file != null){
            return file.getAbsolutePath();
        }
        return sContext.getFilesDir().getAbsolutePath();
    }

}
