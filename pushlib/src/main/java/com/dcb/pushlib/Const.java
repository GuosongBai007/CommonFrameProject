package com.dcb.pushlib;

import android.text.TextUtils;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 * Description:所有推送的基础信息，比如appId  appSecret
 */
public class Const {

    /**
     * 因为友盟推送需要包名才能够注册 所以抽象出来 以便在注册时候直接注册进去
     */
    private static String umeng_package_name = null;

    private static String miui_app_id = null;
    private static String miui_app_key = null;
    private static String flyme_app_id = null;
    private static String flyme_app_key = null;
    private static String umeng_app_id = null;
    private static String umeng_app_secret = null;
    private static String oppo_app_id = null;
    private static String oppo_app_secret = null;


    private static String miui_app_id_debug = null;
    private static String miui_app_key_debug = null;
    private static String flyme_app_id_debug = null;
    private static String flyme_app_key_debug = null;
    private static String umeng_app_id_debug = null;
    private static String umeng_app_secret_debug = null;
    private static String oppo_app_id_debug = null;
    private static String oppo_app_secret_debug = null;


    private static String miui_app_id_release = null;
    private static String miui_app_key_release = null;
    private static String flyme_app_id_release = null;
    private static String flyme_app_key_release = null;
    private static String umeng_app_id_release = null;
    private static String umeng_app_secret_release = null;

    private static String oppo_app_id_release = null;
    private static String oppo_app_secret_release = null;

    public static String getUmeng_package_name() {
        if (TextUtils.isEmpty(umeng_package_name)){
            throw new NullPointerException("please config umeng_package_name before use it");
        }
        return umeng_package_name;
    }

    public static String getOppo_app_id() {
        oppo_app_id = BuildConfig.DEBUG ? oppo_app_id_debug : oppo_app_id_release;
        if (TextUtils.isEmpty(oppo_app_id)) {
            throw new NullPointerException("please config oppo_app_id before use it");
        }
        return oppo_app_id;
    }

    public static String getOppo_app_secret() {
        oppo_app_secret = BuildConfig.DEBUG ? oppo_app_secret_debug : oppo_app_secret_release;
        if (TextUtils.isEmpty(oppo_app_secret)) {
            throw new NullPointerException("please config oppo_app_secret before use it");
        }
        return oppo_app_secret;
    }


    public static String getMiui_app_id() {
        miui_app_id = BuildConfig.DEBUG ? miui_app_id_debug : miui_app_id_release;
        if (TextUtils.isEmpty(miui_app_id)) {
            throw new NullPointerException("please config miui_app_id before use it");
        }
        return miui_app_id;
    }

    public static String getMiui_app_key() {
        miui_app_key = BuildConfig.DEBUG ? miui_app_key_debug : miui_app_key_release;
        if (TextUtils.isEmpty(miui_app_key)) {
            throw new NullPointerException("please config miui_app_key before use it");
        }
        return miui_app_key;
    }

    public static String getFlyme_app_id() {
        flyme_app_id = BuildConfig.DEBUG ? flyme_app_id_debug : flyme_app_id_release;
        if (TextUtils.isEmpty(flyme_app_id)) {
            throw new NullPointerException("please config flyme_app_id before use it");
        }
        return flyme_app_id;
    }

    public static String getFlyme_app_key() {
        flyme_app_key = BuildConfig.DEBUG ? flyme_app_key_debug : flyme_app_key_release;
        if (TextUtils.isEmpty(flyme_app_key)) {
            throw new NullPointerException("please config flyme_app_key before use it");
        }
        return flyme_app_key;
    }


    public static String getUmeng_app_id() {
        umeng_app_id = BuildConfig.DEBUG ? umeng_app_id_debug : umeng_app_id_release;
        if (TextUtils.isEmpty(umeng_app_id)) {
            throw new NullPointerException("please config umeng_app_id before use it");
        }
        return umeng_app_id;
    }

    public static String getUmeng_app_secret() {
        umeng_app_secret = BuildConfig.DEBUG ? umeng_app_secret_debug : umeng_app_secret_release;
        if (TextUtils.isEmpty(umeng_app_secret)) {
            throw new NullPointerException("please config umeng_app_secret before use it");
        }
        return umeng_app_secret;
    }


    public static void setMiUI_APP(String miui_app_id, String miui_app_key) {
        setMiui_app_id(miui_app_id);
        setMiui_app_key(miui_app_key);
    }

    public static void setMiUI_APP_Debug(String miui_app_id, String miui_app_key) {
        setMiui_app_id_debug(miui_app_id);
        setMiui_app_key_debug(miui_app_key);
    }

    public static void setMiUI_APP_Release(String miui_app_id, String miui_app_key) {
        setMiui_app_id_release(miui_app_id);
        setMiui_app_key_release(miui_app_key);
    }

    public static void setFlyme_APP(String flyme_app_id, String flyme_app_key) {
        setFlyme_app_id(flyme_app_id);
        setFlyme_app_key(flyme_app_key);
    }

    public static void setFlyme_APP_Debug(String flyme_app_id, String flyme_app_key) {
        setFlyme_app_id_debug(flyme_app_id);
        setFlyme_app_key_debug(flyme_app_key);
    }

    public static void setFlyme_APP_Release(String flyme_app_id, String flyme_app_key) {
        setFlyme_app_id_release(flyme_app_id);
        setFlyme_app_key_release(flyme_app_key);
    }

    public static void setUmeng_APP_Debug(String umeng_app_id, String umeng_app_secret) {
        setUmeng_app_id_debug(umeng_app_id);
        setUmeng_app_secret_debug(umeng_app_secret);
    }

    public static void setUmeng_APP_Release(String umeng_app_id, String umeng_app_secret) {
        setUmeng_app_id_release(umeng_app_id);
        setUmeng_app_secret_release(umeng_app_secret);
    }


    private static void setMiui_app_id(String miui_app_id) {
        Const.miui_app_id = miui_app_id;
    }

    private static void setMiui_app_key(String miui_app_key) {
        Const.miui_app_key = miui_app_key;
    }

    private static void setFlyme_app_id(String flyme_app_id) {
        Const.flyme_app_id = flyme_app_id;
    }

    private static void setFlyme_app_key(String flyme_app_key) {
        Const.flyme_app_key = flyme_app_key;
    }

    public static void setUmeng_app_id(String umeng_app_id) {
        Const.umeng_app_id = umeng_app_id;
    }

    public static void setUmeng_app_secret(String umeng_app_secret) {
        Const.umeng_app_secret = umeng_app_secret;
    }

    public static String getMiui_app_id_debug() {
        return miui_app_id_debug;
    }

    public static void setMiui_app_id_debug(String miui_app_id_debug) {
        Const.miui_app_id_debug = miui_app_id_debug;
    }

    public static String getMiui_app_key_debug() {
        return miui_app_key_debug;
    }

    public static void setMiui_app_key_debug(String miui_app_key_debug) {
        Const.miui_app_key_debug = miui_app_key_debug;
    }

    public static String getFlyme_app_id_debug() {
        return flyme_app_id_debug;
    }

    public static void setFlyme_app_id_debug(String flyme_app_id_debug) {
        Const.flyme_app_id_debug = flyme_app_id_debug;
    }

    public static String getFlyme_app_key_debug() {
        return flyme_app_key_debug;
    }

    public static void setFlyme_app_key_debug(String flyme_app_key_debug) {
        Const.flyme_app_key_debug = flyme_app_key_debug;
    }

    public static String getMiui_app_id_release() {
        return miui_app_id_release;
    }

    public static void setMiui_app_id_release(String miui_app_id_release) {
        Const.miui_app_id_release = miui_app_id_release;
    }

    public static String getMiui_app_key_release() {
        return miui_app_key_release;
    }

    public static void setMiui_app_key_release(String miui_app_key_release) {
        Const.miui_app_key_release = miui_app_key_release;
    }

    public static String getFlyme_app_id_release() {
        return flyme_app_id_release;
    }

    public static void setFlyme_app_id_release(String flyme_app_id_release) {
        Const.flyme_app_id_release = flyme_app_id_release;
    }

    public static String getFlyme_app_key_release() {
        return flyme_app_key_release;
    }

    public static void setFlyme_app_key_release(String flyme_app_key_release) {
        Const.flyme_app_key_release = flyme_app_key_release;
    }

    public static String getUmeng_app_id_debug() {
        return umeng_app_id_debug;
    }

    public static void setUmeng_app_id_debug(String umeng_app_id_debug) {
        Const.umeng_app_id_debug = umeng_app_id_debug;
    }

    public static String getUmeng_app_secret_debug() {
        return umeng_app_secret_debug;
    }

    public static void setUmeng_app_secret_debug(String umeng_app_secret_debug) {
        Const.umeng_app_secret_debug = umeng_app_secret_debug;
    }

    public static String getUmeng_app_id_release() {
        return umeng_app_id_release;
    }

    public static void setUmeng_app_id_release(String umeng_app_id_release) {
        Const.umeng_app_id_release = umeng_app_id_release;
    }

    public static String getUmeng_app_secret_release() {
        return umeng_app_secret_release;
    }

    public static void setUmeng_app_secret_release(String umeng_app_secret_release) {
        Const.umeng_app_secret_release = umeng_app_secret_release;
    }

    public static void setOPPO_APP_Debug(String appId, String appSecret) {
        setOppo_app_id_debug(appId);
        setOppo_app_secret_debug(appSecret);
    }

    public static void setOPPO_APP_Release(String appId, String appSecret) {
        setOppo_app_id_release(appId);
        setOppo_app_secret_release(appSecret);
    }


    private static void setOppo_app_id_debug(String oppo_app_id_debug) {
        Const.oppo_app_id_debug = oppo_app_id_debug;
    }

    private static void setOppo_app_secret_debug(String oppo_app_secret_debug) {
        Const.oppo_app_secret_debug = oppo_app_secret_debug;
    }

    private static void setOppo_app_id_release(String oppo_app_id_release) {
        Const.oppo_app_id_release = oppo_app_id_release;
    }

    private static void setOppo_app_secret_release(String oppo_app_secret_release) {
        Const.oppo_app_secret_release = oppo_app_secret_release;
    }

    public static void setUmeng_package_name(String umeng_package_name) {
        Const.umeng_package_name = umeng_package_name;
    }
}
