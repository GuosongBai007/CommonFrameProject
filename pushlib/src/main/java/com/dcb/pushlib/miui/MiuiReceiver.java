package com.dcb.pushlib.miui;

import android.content.Context;
import android.text.TextUtils;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.JsonUtils;
import com.dcb.pushlib.utils.PushHandler;
import com.dcb.pushlib.utils.PushUtil;

import java.util.List;
import java.util.Map;

/**
 * 小米推送，消息接收类，具体可参照小米开放平台api
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class MiuiReceiver extends PushMessageReceiver {

    private String mRegId;
    private String mReason;
    private String mCommand;
    private String mMessage;
    private String mTopic;
    private String mAlias;
    private String mUserAccount;
    private String mStartTime;
    private String mEndTime;

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
     * 自定义消息
     *
     * @param context
     * @param message
     */
    @Override
    public void onReceivePassThroughMessage(final Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        if (sIPushInterface != null) {
            final Message result = new Message();
            result.setMessageID(message.getMessageId());
            result.setTitle(message.getTitle());
            result.setMessage(message.getContent());
            result.setExtra(mMessage);
            result.setTarget(Target.MIUI);
            try {
                result.setExtra(message.getContent());
            } catch (Exception e) {
                result.setExtra("{}");
            }
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onCustomMessage(context, result);
                }
            });
        }
    }

    /**
     * 消息被点击
     *
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageClicked(final Context context, MiPushMessage message) {
        PushUtil.startLaunchActivity(context);
        if (message == null)
            return;
        int msgType = -1;
        String userId = "";
        mMessage = message.getContent();
        Map<String, String> extraMap = message.getExtra();
        if (extraMap != null) {
            if (extraMap.containsKey("msgType")) {
                msgType = Integer.parseInt(extraMap.get("msgType"));
            }
            if (extraMap.containsKey("userid")) {
                userId = extraMap.get("userid");
            }
        }
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        if (sIPushInterface != null) {
            final Message result = new Message();
            result.setNotifyID(message.getNotifyId());
            result.setMessageID(message.getMessageId());
            result.setTitle(mTopic);
            result.setMessage(mMessage);
            result.setTarget(Target.MIUI);
            result.setMsgType(msgType);
            result.setUserId(userId);
            try {
                result.setExtra(JsonUtils.setJson(message.getExtra()).toString());
            } catch (Exception e) {
                result.setExtra("{}");
            }
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onMessageClicked(context, result);
                }
            });
        }
    }

    /**
     * 默认消息
     *
     * @param context
     * @param message
     */
    @Override
    public void onNotificationMessageArrived(final Context context, MiPushMessage message) {
        mMessage = message.getContent();
        if (!TextUtils.isEmpty(message.getTopic())) {
            mTopic = message.getTopic();
        } else if (!TextUtils.isEmpty(message.getAlias())) {
            mAlias = message.getAlias();
        } else if (!TextUtils.isEmpty(message.getUserAccount())) {
            mUserAccount = message.getUserAccount();
        }
        if (sIPushInterface != null) {
            final Message result = new Message();
            result.setTitle(message.getTitle());
            result.setMessageID(message.getMessageId());
            result.setNotifyID(message.getNotifyId());
            result.setMessage(message.getDescription());
            result.setTarget(Target.MIUI);
            try {
                result.setExtra(JsonUtils.setJson(message.getExtra()).toString());
            } catch (Exception e) {
                result.setExtra("{}");
            }
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onMessage(context, result);
                }
            });
        }
    }

    /**
     * 公共事件回调，修改别名、注册结果等
     *
     * @param context
     * @param message
     */
    @Override
    public void onCommandResult(final Context context, MiPushCommandMessage message) {
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                if (sIPushInterface != null) {
                    PushHandler.handler().post(new Runnable() {
                        @Override
                        public void run() {
                            sIPushInterface.onRegister(context, mRegId);
                        }
                    });
                }
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                if (sIPushInterface != null) {
                    PushHandler.handler().post(new Runnable() {
                        @Override
                        public void run() {
                            sIPushInterface.onAlias(context, mAlias);
                        }
                    });
                }
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
            }
        }
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage miPushCommandMessage) {
        super.onReceiveRegisterResult(context, miPushCommandMessage);
    }


}
