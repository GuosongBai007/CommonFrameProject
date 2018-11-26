package com.dcb.pushlib.oppo;

import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.dcb.pushlib.IPushInterface;
import com.dcb.pushlib.utils.RomUtil;

import java.util.List;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:52
 * Description:Oppo推送注册返回接口
 * 接口文档地址 https://open.oppomobile.com/wiki/doc#id=10196
 */
public class OPPOPushCallback implements PushCallback {

    private IPushInterface mIPushInterface;

    public OPPOPushCallback(IPushInterface IPushInterface) {
        mIPushInterface = IPushInterface;
    }

    //注册的结果,如果注册成功,registerID就是客户端的唯一身份标识
    // 所有回调都需要根据responseCode来判断操作是否成功, 0 代表成功,其他代码失败,失败具体原因可以查阅附录中的错误码列表.
    // onRegister接口返回的registerID是当前客户端的唯一标识,app开发者可以上传保存到应用服务器中,在发送push消息是可以指定registerID发送.
    @Override
    public void onRegister(int i, String s) {
        if (mIPushInterface != null && i == 0) {
            mIPushInterface.onRegister(null, s);
        } else {
            mIPushInterface.onRegisterFailed(RomUtil.ROM_OPPO, "requestCode:" + i);
        }
    }

    //反注册的结果
    @Override
    public void onUnRegister(int i) {
        if (mIPushInterface != null) {
            mIPushInterface.onUnRegister(null);
        }
    }

    //alias操作结果,3个回调分别对应设置alias,取消alias,获取alias列表.如果操作成功,参数alias里面会包含操作成功的alias列表
    @Override
    public void onGetAliases(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onSetAliases(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onUnsetAliases(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    //user account操作结果,3个回调分别对应设置account,取消account,获取account列表.如果操作成功,参数accounts里面会包含操作成功的account列表

    @Override
    public void onSetUserAccounts(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onUnsetUserAccounts(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onGetUserAccounts(int i, List<SubscribeResult> list) {

    }

    //订阅标签(tag)操作结果,3个回调分别对应设置tag,取消tag,获取tag列表.如果操作成功,参数tags里面会包含操作成功的tag列表
    @Override
    public void onSetTags(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onUnsetTags(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onGetTags(int i, List<SubscribeResult> list) {
        if (mIPushInterface != null) {

        }
    }

    //获取当前的push状态返回,根据返回码判断当前的push状态,返回码具体含义可以参考[错误码]
    @Override
    public void onGetPushStatus(int i, int i1) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onSetPushTime(int i, String s) {
        if (mIPushInterface != null) {

        }
    }

    @Override
    public void onGetNotificationStatus(int i, int i1) {
        if (mIPushInterface != null) {

        }
    }
}
