package com.base.utils;

import android.util.Log;

/**
 * Author:GuosongBai
 * Date:2018/11/7 14:40
 * Description:
 */
public class AppLogger {

    /**
     * Debug 模式下允许日志输出 release不允许日志输出
     */
    private static boolean sIsDebug;

    /**
     * 默认输出
     */
    public static String sDefaultMsg = "";

    /**
     * 控制日志等级
     */
    public static final int V = 1;
    public static final int D = 2;
    public static final int I = 3;
    public static final int W = 4;
    public static final int E = 5;


    /**
     * 初始化方法  可以放在Application 中进行初始化
     */
    public static void init(boolean isDebug, String defaultMsg) {
        sIsDebug = isDebug;
        sDefaultMsg = defaultMsg;
    }

    public static void v() {
        _log(V, null, sDefaultMsg);
    }

    public static void v(Object obj) {
        _log(V, null, obj);
    }

    public static void v(String tag, Object obj) {
        _log(V, tag, obj);
    }

    public static void d() {
        _log(D, null, sDefaultMsg);
    }

    public static void d(Object obj) {
        _log(D, null, obj);
    }

    public static void d(String tag, Object obj) {
        _log(D, tag, obj);
    }

    public static void i() {
        _log(I, null, sDefaultMsg);
    }

    public static void i(Object obj) {
        _log(I, null, obj);
    }

    public static void i(String tag, String obj) {
        _log(I, tag, obj);
    }

    public static void w() {
        _log(W, null, sDefaultMsg);
    }

    public static void w(Object obj) {
        _log(W, null, obj);
    }

    public static void w(String tag, Object obj) {
        _log(W, tag, obj);
    }

    public static void e() {
        _log(E, null, sDefaultMsg);
    }

    public static void e(Object obj) {
        _log(E, null, obj);
    }

    public static void e(String tag, Object obj) {
        _log(E, tag, obj);
    }

    /**
     * 执行打印方法
     * @param type
     * @param tagStr
     * @param obj
     */
    private static void _log(int type, String tagStr, Object obj) {
        String msg;
        if (!sIsDebug) {
            return;
        }

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

        int index = 4;
        String className = stackTrace[index].getFileName();
        String methodName = stackTrace[index].getMethodName();
        int lineNumber = stackTrace[index].getLineNumber();

        String tag = (tagStr == null ? className : tagStr);
        methodName = methodName.substring(0, 1).toUpperCase() + methodName.substring(1);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[ (").append(className).append(":").append(lineNumber).append(")#").append(methodName).append(" ] ");

        if (obj == null) {
            msg = "Log with null Object";
        } else {
            msg = obj.toString();
        }
        if (msg != null) {
            stringBuilder.append(msg);
        }

        String logStr = stringBuilder.toString();

        switch (type) {
            case V:
                Log.v(tag, logStr);
                break;
            case D:
                Log.d(tag, logStr);
                break;
            case I:
                Log.i(tag, logStr);
                break;
            case W:
                Log.w(tag, logStr);
                break;
            case E:
                Log.e(tag, logStr);
                break;
        }
    }

}
