package com.dcb.pushlib;

import android.content.Context;

import com.dcb.pushlib.model.Message;

/**
 * 在flyme的推送中,不会回调onMessage()和onMessageClicked()方法
 * 在华为推送中，不会回调onMessage(),onMessageClicked()和onAlias()方法
 * 如上两个方法不会回调是因为设计的时候，通知栏的点击事件由后台负责控制,手机端只负责处理透传消息
 *
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public interface IPushInterface {

    /**
     * 注册成功之后回调
     *
     * @param context
     * @param pushToken
     */
    void onRegister(Context context, String pushToken);

    /**
     * 注册失败之后的回调
     * @param osType 注册平台
     * @param msg 失败信息 或者errorCode
     */
    void onRegisterFailed(String osType,String msg);

    /**
     * 取消注册成功
     *
     * @param context
     */
    void onUnRegister(Context context);

    /**
     * 暂停推送
     *
     * @param context
     */
    void onPaused(Context context);

    /**
     * 开启推送
     *
     * @param context
     */
    void onResume(Context context);

    /**
     * 通知下来之后
     *
     * @param context
     * @param message
     */
    void onMessage(Context context, Message message);

    /**
     * 通知栏被点击之后
     *
     * @param context
     * @param message
     */
    void onMessageClicked(Context context, Message message);

    /**
     * 透传消息
     *
     * @param context
     * @param message
     */
    void onCustomMessage(Context context, Message message);


    /**
     * 别名设置成功的回调
     *
     * @param context
     * @param alias
     */
    void onAlias(Context context, String alias);
}
