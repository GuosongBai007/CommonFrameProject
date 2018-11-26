package com.dcb.pushlib.oppo;

import android.content.Context;

import com.coloros.mcssdk.PushService;
import com.coloros.mcssdk.mode.AppMessage;
import com.coloros.mcssdk.mode.CommandMessage;
import com.coloros.mcssdk.mode.SptDataMessage;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.model.Message;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.utils.PushHandler;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 * Description: Oppo推送平台接收类 具体可参照OPPO开放平台api
 */
public class OPPOReceiver extends PushService {

    private static IPushInterface sIPushInterface;

    public static void registerInterface(IPushInterface pushInterface){
        sIPushInterface = pushInterface;
    }

    public static void clearPushInterface() {
        sIPushInterface = null;
    }

    /**
     * 命令消息，主要是服务端对客户端调用的反馈，一般应用不需要重写此方法
     *
     * @param context
     * @param commandMessage
     */
    @Override
    public void processMessage(Context context, CommandMessage commandMessage) {
        super.processMessage(context, commandMessage);

    }

    /**
     * 普通应用消息，视情况看是否需要重写
     *
     * @param context
     * @param appMessage
     */
    @Override
    public void processMessage(Context context, AppMessage appMessage) {
        super.processMessage(context, appMessage);
        String content = appMessage.getContent();
        Message message = new Message();
        message.setMessageID("");
        message.setTarget(Target.OPPO);
        message.setExtra(content);
        PushHandler.handler().post(() -> sIPushInterface.onMessage(context, message));
    }


    /**
     * 透传消息处理，应用可以打开页面或者执行命令,如果应用不需要处理透传消息，则不需要重写此方法
     *
     * @param context
     * @param sptDataMessage
     */
    @Override
    public void processMessage(Context context, SptDataMessage sptDataMessage) {
        super.processMessage(context.getApplicationContext(), sptDataMessage);
        String content = sptDataMessage.getContent();
        Message message = new Message();
        message.setMessageID("");
        message.setTarget(Target.OPPO);
        message.setExtra(content);
        PushHandler.handler().post(() -> sIPushInterface.onCustomMessage(context, message));
    }
}
