package com.dcb.pushlib.flyme;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.handler.MzPushMessage;
import com.meizu.cloud.pushsdk.notification.PushNotificationBuilder;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.dcb.pushlib.Const;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.PushHandler;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 魅族推送 消息接收类
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class FlymeReceiver extends MzPushMessageReceiver {

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
    public void onRegister(final Context context, final String pushid) {
        if (sIPushInterface != null) {
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onRegister(context, pushid);
                }
            });
        }
    }

    /**
     * 若该回调有数据，就证明是透传消息，否则全部都是通知消息
     *
     * @param context
     * @param s
     */
    @Override
    public void onMessage(final Context context, final String s) {
        if (sIPushInterface != null) {
            final Message message = new Message();
            message.setMessageID("");
            message.setTarget(Target.FLYME);
            message.setExtra(s);
            PushHandler.handler().post(() -> sIPushInterface.onCustomMessage(context, message));
        }
    }

    /**
     * 这个是自定义intent的，然而并没有什么..
     *
     * @param context
     * @param intent
     */
    @Override
    public void onMessage(Context context, Intent intent) {
        super.onMessage(context, intent);
    }

    /**
     * 调用PushManager.unRegister(context）方法后，会在此回调反注册状态
     *
     * @param context
     * @param b
     */
    @Override
    public void onUnRegister(final Context context, boolean b) {
        if (b == true) {
            if (sIPushInterface != null) {
                PushHandler.handler().post(new Runnable() {
                    @Override
                    public void run() {
                        sIPushInterface.onUnRegister(context);
                    }
                });
            }
        }
    }


    /**
     * 设置通知栏小图标
     *
     * @param pushNotificationBuilder
     */
    @Override
    public void onUpdateNotificationBuilder(PushNotificationBuilder pushNotificationBuilder) {
        //pushNotificationBuilder.setmStatusbarIcon(R.drawable.mz_stat_share_weibo);
    }

    /**
     * 检查通知栏和透传消息开关状态回调
     *
     * @param context
     * @param pushSwitchStatus
     */
    @Override
    public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
    }

    /**
     * 新版订阅回调
     *
     * @param context
     * @param registerStatus
     */
    @Override
    public void onRegisterStatus(final Context context, final RegisterStatus registerStatus) {
        PushManager.checkPush(context, Const.getFlyme_app_id(), Const.getFlyme_app_key(), PushManager.getPushId(context));
        //新版订阅回调
        if (sIPushInterface != null) {
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onRegister(context, registerStatus.getPushId());
                }
            });
        }
    }

    /**
     * 反注册回调
     *
     * @param context
     * @param unRegisterStatus
     */
    @Override
    public void onUnRegisterStatus(final Context context, UnRegisterStatus unRegisterStatus) {
        if (sIPushInterface != null) {
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onUnRegister(context);
                }
            });
        }
    }

    /**
     * 标签回调
     *
     * @param context
     * @param subTagsStatus
     */
    @Override
    public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {

    }

    /**
     * 别名回调
     *
     * @param context
     * @param subAliasStatus
     */
    @Override
    public void onSubAliasStatus(final Context context, final SubAliasStatus subAliasStatus) {
        if (sIPushInterface != null) {
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onAlias(context, subAliasStatus.getAlias());
                }
            });
        }
    }

    @Override
    public void onNotificationClicked(final Context context, MzPushMessage mzPushMessage) {
        super.onNotificationClicked(context, mzPushMessage);
        if (mzPushMessage == null)
            return;
        int msgType = -1;
        String userId = "";
        String selfDefineContentString = mzPushMessage.getSelfDefineContentString();
        if (!TextUtils.isEmpty(selfDefineContentString)) {
            try {
                JSONObject jsonObject = new JSONObject(selfDefineContentString);
                msgType = jsonObject.optInt("msgType", -1);
                userId = jsonObject.optString("userid");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        if (sIPushInterface != null) {
            final Message message = new Message();
            message.setMessageID(String.valueOf(mzPushMessage.getNotifyId()));
            message.setTarget(Target.FLYME);
            message.setTitle(mzPushMessage.getTitle());
            message.setMessage(mzPushMessage.getContent());
            message.setMsgType(msgType);
            message.setUserId(userId);
            message.setExtra(mzPushMessage.getContent());
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onMessageClicked(context, message);
                }
            });
        }
    }
}
