package com.dcb.pushlib.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.PushHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 极光推送 消息接收类
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class JpushReceiver extends BroadcastReceiver {


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

    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return;
        //Log.i("JpushReceiver", "onReceive" + intent.getAction() + ", extras: " + printBundle(bundle));
        int notifyId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String messageId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
        String extraMessage = bundle.getString(JPushInterface.EXTRA_EXTRA);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {//注册回调

            final String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);

            if (sIPushInterface != null) {
                PushHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sIPushInterface.onRegister(context, regId);
                    }
                });
            }

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {//接收到推送下来的自定义message

            if (sIPushInterface != null) {
                final Message message = new Message();
                message.setTitle(bundle.getString(JPushInterface.EXTRA_TITLE));
                message.setMessageID(messageId);
                message.setMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
                message.setExtra(extraMessage);
                message.setTarget(Target.JPUSH);
                PushHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sIPushInterface.onCustomMessage(context, message);
                    }
                });
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {//接收到推送下来的系统message

            if (sIPushInterface != null) {
                final Message message = new Message();
                message.setNotifyID(notifyId);
                message.setMessageID(messageId);
                message.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
                message.setMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
                message.setExtra(extraMessage);
                message.setTarget(Target.JPUSH);
                PushHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sIPushInterface.onMessage(context, message);
                    }
                });
            }

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {//用户点击通知栏消息

            if (sIPushInterface != null) {
                final Message message = new Message();
                message.setNotifyID(notifyId);
                message.setMessageID(messageId);
                message.setTitle(bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
                message.setMessage(bundle.getString(JPushInterface.EXTRA_ALERT));
                message.setExtra(extraMessage);
                message.setTarget(Target.JPUSH);
                PushHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sIPushInterface.onMessageClicked(context, message);
                    }
                });
            }


        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            //连接状态 boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);

        } else {

        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:").append(key).append(", value:").append(bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (bundle.getString(JPushInterface.EXTRA_EXTRA).isEmpty()) {
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:").append(key + ", value: [" +
                                myKey).append(" - ").append(json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                }

            } else {
                sb.append("\nkey:").append(key).append(", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }

}