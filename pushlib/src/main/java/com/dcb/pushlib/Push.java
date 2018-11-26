package com.dcb.pushlib;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;

import com.coloros.mcssdk.callback.PushCallback;
import com.coloros.mcssdk.mode.SubscribeResult;
import com.dcb.pushlib.oppo.OPPOPushCallback;
import com.dcb.pushlib.oppo.OPPOReceiver;
import com.huawei.android.hms.agent.EmuiReceiver;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.meizu.cloud.pushsdk.PushManager;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.dcb.pushlib.flyme.FlymeReceiver;
import com.dcb.pushlib.jpush.JpushReceiver;
import com.dcb.pushlib.miui.MiuiReceiver;
import com.dcb.pushlib.model.Target;
import com.dcb.pushlib.model.TokenModel;
import com.dcb.pushlib.umeng.UmengClickHandler;
import com.dcb.pushlib.umeng.UmengReceiver;
import com.dcb.pushlib.utils.RomUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.JPushInterface;

/**
 * Author:GuosongBai
 * Date:2018/11/16 16:08
 * 推送集成类  内部集成了小米  华为  魅族  友盟  OPPO推送（只支持4.0以上的系统）
 */
public class Push {

    public static String sPushToken;

    /**
     * 初始化配置
     *
     * @param context
     * @param debug
     */
    public static void register(Application context, boolean debug) {
        register(context, debug, null);
    }

    /**
     * 注册
     *
     * @param context
     * @param debug
     * @param pushInterface
     */
    public static void register(final Application context, boolean debug, final IPushInterface pushInterface) {
        if (context == null)
            return;

        try {
            if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
                if (pushInterface != null) {
                    FlymeReceiver.registerInterface(pushInterface);
                }
                PushManager.register(context, Const.getFlyme_app_id(), Const.getFlyme_app_key());
            } else if (RomUtil.rom() == Target.EMUI) {//华为
                if (pushInterface != null) {
                    EmuiReceiver.registerInterface(pushInterface);
                }
                HMSAgent.init(context);
                HMSAgent.Push.getToken(rst -> {
                });
            } else if (RomUtil.rom() == Target.MIUI) {//小米
                if (pushInterface != null) {
                    MiuiReceiver.registerInterface(pushInterface);
                }
                if (shouldInit(context)) {
                    MiPushClient.registerPush(context, Const.getMiui_app_id(), Const.getMiui_app_key());
                }
            } else if (RomUtil.rom() == Target.OPPO) {//OPPO
                if (pushInterface != null) {
                    OPPOReceiver.registerInterface(pushInterface);
                }
                if (com.coloros.mcssdk.PushManager.isSupportPush(context)) {
                    // oppo sdk内部封装的平台是否支持oppo推送
                    com.coloros.mcssdk.PushManager.getInstance().register(
                            context,
                            Const.getOppo_app_id(),
                            Const.getOppo_app_secret(), new OPPOPushCallback(pushInterface));
                }
            } else if (RomUtil.rom() == Target.JPUSH) {//极光
                if (pushInterface != null) {
                    JpushReceiver.registerInterface(pushInterface);
                }
                if (shouldInit(context)) {
                    JPushInterface.setDebugMode(debug);
                    JPushInterface.init(context);
                }
            } else if (RomUtil.rom() == Target.UMENG) {//友盟
                if (pushInterface != null) {
                    UmengReceiver.registerInterface(pushInterface);
                }
                UMConfigure.setLogEnabled(debug);
                UMConfigure.init(context, UMConfigure.DEVICE_TYPE_PHONE, Const.getUmeng_app_secret());
                UmengReceiver umengReceiver = new UmengReceiver();
                UmengClickHandler umengClickHandler = new UmengClickHandler();
                if (pushInterface != null) {
                    umengClickHandler.registerInterface(pushInterface);
                }
                PushAgent.getInstance(context).setResourcePackageName(Const.getUmeng_package_name());
                PushAgent.getInstance(context).setMessageHandler(umengReceiver);
                PushAgent.getInstance(context).setNotificationClickHandler(umengClickHandler);
                PushAgent.getInstance(context).register(new IUmengRegisterCallback() {
                    @Override
                    public void onSuccess(String s) {
                        if (pushInterface != null) {
                            Log.i("GuosongBai onSuccess", s);
                            pushInterface.onRegister(context, s);
                        }
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        pushInterface.onRegisterFailed("umeng", "error：" + s + "===" + s1);
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerJpush(Application context, boolean debug) {
        try {
            JPushInterface.setDebugMode(debug);
            JPushInterface.init(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于小米推送的注册
     *
     * @param context
     * @return
     */
    private static boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        if (am != null) {
            List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
            String mainProcessName = context.getPackageName();
            int myPid = Process.myPid();
            if (null != processInfos && null != mainProcessName) {
                for (ActivityManager.RunningAppProcessInfo info : processInfos) {
                    if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 注销推送
     *
     * @param context
     */
    public static void unregister(final Context context) {
        if (context == null)
            return;
        if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
            PushManager.unRegister(context, Const.getFlyme_app_id(), Const.getFlyme_app_key());
        } else if (RomUtil.rom() == Target.EMUI) {//华为
            if (TextUtils.isEmpty(sPushToken)) {
                HMSAgent.Push.deleteToken(sPushToken, rst -> {
                    if (null != EmuiReceiver.getPushInterface()) {
                        EmuiReceiver.getPushInterface().onUnRegister(context);
                    }
                });
            }
        } else if (RomUtil.rom() == Target.MIUI) {//小米
            MiPushClient.unregisterPush(context);
        } else if (RomUtil.rom() == Target.OPPO) {//OPPO
            com.coloros.mcssdk.PushManager.getInstance().unRegister();
        } else if (RomUtil.rom() == Target.JPUSH) {//极光
            JPushInterface.stopPush(context);
        } else if (RomUtil.rom() == Target.OPPO) {
            com.coloros.mcssdk.PushManager.getInstance().unRegister();
        } else if (RomUtil.rom() == Target.UMENG) {//友盟
            PushAgent.getInstance(context).disable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    if (null != UmengReceiver.getPushInterface()) {
                        UmengReceiver.getPushInterface().onUnRegister(context);
                    }
                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }


    /**
     * 设置推送回调
     *
     * @param pushInterface
     */
    public static void setPushInterface(Context context, IPushInterface pushInterface) {
        if (pushInterface == null)
            return;
        if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
            FlymeReceiver.registerInterface(pushInterface);
        } else if (RomUtil.rom() == Target.EMUI) {//华为
            EmuiReceiver.registerInterface(pushInterface);
        } else if (RomUtil.rom() == Target.MIUI) {//小米
            MiuiReceiver.registerInterface(pushInterface);
        } else if (RomUtil.rom() == Target.OPPO) {//OPPO
            OPPOReceiver.registerInterface(pushInterface);
        } else if (RomUtil.rom() == Target.JPUSH) {//极光
            JpushReceiver.registerInterface(pushInterface);
        } else if (RomUtil.rom() == Target.UMENG) {//友盟
            UmengReceiver.registerInterface(pushInterface);
        }
    }


    /**
     * 设置别名，
     * 华为不支持alias的写法，所以只能用tag，tag只能放map，所以alias作为value,key为name
     *
     * @param context
     * @param alias
     */
    public static void setAlias(final Context context, String alias) {
        if (TextUtils.isEmpty(alias))
            return;

        if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
            PushManager.subScribeAlias(context, Const.getFlyme_app_id(), Const.getFlyme_app_key(), getToken(context).getToken(), alias);
        } else if (RomUtil.rom() == Target.EMUI) {//华为
            Map<String, String> tag = new HashMap<>();
            tag.put("name", alias);
        } else if (RomUtil.rom() == Target.MIUI) {//小米
            MiPushClient.setAlias(context, alias, null);
        } else if (RomUtil.rom() == Target.OPPO) {//OPPO
            List<String> data = new ArrayList<>();
            data.add(alias);
            com.coloros.mcssdk.PushManager.getInstance().setAliases(data);
        } else if (RomUtil.rom() == Target.JPUSH) {//极光
            JPushInterface.setAlias(context, 0, alias);
        } else if (RomUtil.rom() == Target.UMENG) {//友盟
            PushAgent.getInstance(context).setAlias(alias, "", (isSuccess, s) -> {
                if (isSuccess && null != UmengReceiver.getPushInterface()) {
                    UmengReceiver.getPushInterface().onAlias(context, s);
                }
            });
        }
    }

    /**
     * 获取唯一的token,如果获取数据为空，请延迟几秒后在取，或在onRegister中接收token
     *
     * @param context
     * @return
     */
    public static TokenModel getToken(final Context context) {
        if (context == null)
            return null;
        final TokenModel tokenModel = new TokenModel();
        if (!TextUtils.isEmpty(sPushToken)) {
            tokenModel.setToken(sPushToken);
            return tokenModel;
        } else {
            if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
                tokenModel.setToken(PushManager.getPushId(context));
            } else if (RomUtil.rom() == Target.EMUI) {//华为
                HMSAgent.Push.getToken(null);
            } else if (RomUtil.rom() == Target.MIUI) {//小米
                sPushToken = MiPushClient.getRegId(context);
            } else if (RomUtil.rom() == Target.OPPO) {
                sPushToken = com.coloros.mcssdk.PushManager.getInstance().getRegisterID();
            } else if (RomUtil.rom() == Target.JPUSH) {//极光
                sPushToken = JPushInterface.getRegistrationID(context);
            } else if (RomUtil.rom() == Target.UMENG) {//友盟
                sPushToken = PushAgent.getInstance(context).getRegistrationId();
            }
        }
        if (!TextUtils.isEmpty(sPushToken)) {
            tokenModel.setToken(sPushToken);
            return tokenModel;
        }
        return null;
    }


    /**
     * 停止推送
     */
    public static void pause(final Context context) {
        if (context == null)
            return;
        if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
            PushManager.unRegister(context, Const.getFlyme_app_id(), Const.getFlyme_app_key());
        } else if (RomUtil.rom() == Target.EMUI) {//华为
            HMSAgent.Push.enableReceiveNotifyMsg(false, null);
            HMSAgent.Push.enableReceiveNormalMsg(false, null);
            if (EmuiReceiver.getPushInterface() != null) {
                EmuiReceiver.getPushInterface().onPaused(context);
            }
        } else if (RomUtil.rom() == Target.MIUI) {//小米
            MiPushClient.pausePush(context, null);
            if (MiuiReceiver.getPushInterface() != null) {
                MiuiReceiver.getPushInterface().onPaused(context);
            }
        } else if (RomUtil.rom() == Target.OPPO) {//OPPO
            com.coloros.mcssdk.PushManager.getInstance().pausePush();
        } else if (RomUtil.rom() == Target.JPUSH) {//极光
            if (!JPushInterface.isPushStopped(context)) {
                JPushInterface.stopPush(context);
                if (JpushReceiver.getPushInterface() != null) {
                    JpushReceiver.getPushInterface().onPaused(context);
                }
            }
        } else if (RomUtil.rom() == Target.UMENG) {//友盟
            PushAgent.getInstance(context).disable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    if (null != UmengReceiver.getPushInterface()) {
                        UmengReceiver.getPushInterface().onPaused(context);
                    }
                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }


    /**
     * 开启推送
     *
     * @param context
     */
    public static void resume(final Context context) {
        if (context == null)
            return;
        if (MzSystemUtils.isBrandMeizu(context) || RomUtil.rom() == Target.FLYME) {//魅族
            PushManager.register(context, Const.getMiui_app_id(), Const.getFlyme_app_key());
            if (FlymeReceiver.getPushInterface() != null) {
                FlymeReceiver.getPushInterface().onResume(context);
            }
        } else if (RomUtil.rom() == Target.EMUI) {//华为
            HMSAgent.Push.enableReceiveNotifyMsg(true, null);
            HMSAgent.Push.enableReceiveNormalMsg(true, null);
            if (EmuiReceiver.getPushInterface() != null) {
                EmuiReceiver.getPushInterface().onResume(context);
            }
        } else if (RomUtil.rom() == Target.MIUI) {//小米
            MiPushClient.resumePush(context, null);
            if (MiuiReceiver.getPushInterface() != null) {
                MiuiReceiver.getPushInterface().onResume(context);
            }
        } else if (RomUtil.rom() == Target.OPPO) {//OPPO
            com.coloros.mcssdk.PushManager.getInstance().resumePush();
        } else if (RomUtil.rom() == Target.JPUSH) {//极光
            JPushInterface.resumePush(context);
            if (JpushReceiver.getPushInterface() != null) {
                JpushReceiver.getPushInterface().onResume(context);
            }
        } else if (RomUtil.rom() == Target.UMENG) {//友盟
            PushAgent.getInstance(context).enable(new IUmengCallback() {
                @Override
                public void onSuccess() {
                    if (null != UmengReceiver.getPushInterface()) {
                        UmengReceiver.getPushInterface().onResume(context);
                    }
                }

                @Override
                public void onFailure(String s, String s1) {

                }
            });
        }
    }

}
