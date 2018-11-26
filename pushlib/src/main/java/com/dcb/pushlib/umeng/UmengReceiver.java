package com.dcb.pushlib.umeng;

import android.app.Notification;
import android.content.Context;

import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.JsonUtils;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class UmengReceiver extends UmengMessageHandler {

    private static IPushInterface sIPushInterface;

    public static void registerInterface(IPushInterface pushInterface) {
        sIPushInterface = pushInterface;
    }

    public static IPushInterface getPushInterface() {
        return sIPushInterface;
    }

    public static void clearPushInterface() {
        sIPushInterface = null;
    }


    /**
     * 调用super，会展示通知，不调用super，则不展示通知。
     *
     * @param context
     * @param msg
     */
    @Override
    public void dealWithNotificationMessage(Context context, UMessage msg) {
        super.dealWithNotificationMessage(context, msg);
        Message result = new Message();
        result.setMessageID(msg.msg_id);
        result.setTitle(msg.title);
        result.setMessage(msg.text);
        result.setTarget(Target.UMENG);
        sIPushInterface.onMessage(context, result);
    }

    /**
     * 自定义消息的回调方法
     */
    @Override
    public void dealWithCustomMessage(final Context context, final UMessage msg) {
        Message result = new Message();
        result.setMessageID(msg.msg_id);
        result.setTitle(msg.title);
        result.setMessage(msg.text);
        try {
            result.setExtra(JsonUtils.setJson(msg.extra).toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setTarget(Target.UMENG);
        sIPushInterface.onCustomMessage(context, result);
    }

    @Override
    public Notification getNotification(Context context, UMessage uMessage) {
        return super.getNotification(context, uMessage);
    }
}
