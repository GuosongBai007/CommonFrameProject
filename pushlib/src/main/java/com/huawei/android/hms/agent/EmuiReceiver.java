package com.huawei.android.hms.agent;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.huawei.hms.support.api.push.PushReceiver;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.JsonUtils;
import com.dcb.pushlib.utils.PushHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * 华为推送 消息接收类
 * Created by: GuosngBai
 * 2018/5/2.
 */
public class EmuiReceiver extends PushReceiver {

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
    public void onToken(final Context context, final String token, Bundle extras) {
        if (sIPushInterface != null) {
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onRegister(context, token);
                }
            });
        }
    }


    /**
     * 方法已过时，使用onPushMsg(final Context context, byte[] msg, Bundle bundle)接收消息
     *
     * @param context
     * @param bytes
     * @param s
     */
    @Override
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
    }


    /**
     * 接收华为推送过来的消息
     *
     * @param context
     * @param msg
     * @param bundle
     * @return
     */
    @Override
    public boolean onPushMsg(final Context context, byte[] msg, Bundle bundle) {
        //这里是透传消息， msg是透传消息的字节数组 bundle字段没用
        try {
            String content = new String(msg, "UTF-8");
            if (sIPushInterface != null) {
                final Message message = new Message();
                message.setMessage(content);
                //华为的sdk在透传的时候无法实现extra字段，这里要注意
                message.setExtra("{}");
                message.setTarget(Target.EMUI);
                PushHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sIPushInterface.onCustomMessage(context, message);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 通知栏的事件:
     * NOTIFICATION_OPENED, //通知栏中的通知被点击打开
     * NOTIFICATION_CLICK_BTN, //通知栏中通知上的按钮被点击
     *
     * @param context
     * @param event
     * @param extras
     */
    public void onEvent(final Context context, Event event, Bundle extras) {
        if (Event.NOTIFICATION_OPENED.equals(event) || Event.NOTIFICATION_CLICK_BTN.equals(event)) {
            int notifyId = extras.getInt(BOUND_KEY.pushNotifyId, 0);
            String content = extras.getString(BOUND_KEY.pushMsgKey);
            int msgType = -1;
            String userId = "";
            if (!TextUtils.isEmpty(content)) {
                try {
                    JSONArray jsonArray = new JSONArray(content);
                    if (jsonArray.length() > 0) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                        msgType = jsonObject.optInt("msgType");
                        userId = jsonObject.optString("userid");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (0 != notifyId) {
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager != null) {
                    manager.cancel(notifyId);
                }
            }
            try {
                if (sIPushInterface != null) {
                    final Message message = new Message();
                    message.setNotifyID(notifyId);
                    message.setMessage(content);
                    message.setExtra(JsonUtils.getJson(extras));
                    message.setTarget(Target.EMUI);
                    message.setMsgType(msgType);
                    message.setUserId(userId);
                    PushHandler.handler().post(new Runnable() {
                        @Override
                        public void run() {
                            sIPushInterface.onMessageClicked(context, message);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
