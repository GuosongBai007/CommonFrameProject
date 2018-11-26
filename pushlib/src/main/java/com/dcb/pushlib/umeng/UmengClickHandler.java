package com.dcb.pushlib.umeng;

import android.content.Context;

import com.umeng.message.UHandler;
import com.umeng.message.entity.UMessage;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.PushHandler;
import com.dcb.pushlib.utils.PushUtil;

import java.util.Map;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 */
public class UmengClickHandler implements UHandler {

    private IPushInterface sIPushInterface;

    public void registerInterface(IPushInterface pushInterface) {
        sIPushInterface = pushInterface;
    }

    public IPushInterface getPushInterface() {
        return sIPushInterface;
    }

    public void clearPushInterface() {
        sIPushInterface = null;
    }

    @Override
    public void handleMessage(final Context context, UMessage msg) {
        PushUtil.startLaunchActivity(context);
        if (msg == null)
            return;
        int msgType = -1;
        String userId = "";
        Map<String, String> extraMap = msg.extra;
        if (extraMap != null) {
            if (extraMap.containsKey("msgType")) {
                msgType = Integer.parseInt(extraMap.get("msgType"));
            }
            if (extraMap.containsKey("userid")) {
                userId = extraMap.get("userid");
            }
        }
        if (sIPushInterface != null) {
            final Message result = new Message();
            result.setMessageID(msg.msg_id);
            result.setTitle(msg.title);
            result.setMessage(msg.text);
            result.setMsgType(msgType);
            result.setUserId(userId);
            result.setTarget(Target.UMENG);
            PushHandler.handler().post(new Runnable() {
                @Override
                public void run() {
                    sIPushInterface.onMessageClicked(context, result);
                }
            });
        }
    }
}
