package com.base.utils;

import android.util.Log;

import java.util.HashMap;
import java.util.Locale;

/**
 * Author:GuosongBai
 * Date:2018/10/12 10:12
 * Description: 一个停表类型的Logger,可以打印日志之间的所用时长，用来做一些耗时统计
 * 例如调用
 * TimeLineLogger.begin("test")
 * TimeLineLogger.cut("test",step1)
 * TimeLineLogger.cut("test",step2)
 * TimeLineLogger.end("test")
 * 将得到
 * test:begin
 * test:begin --> step1,cost 100ms
 * test:step1 --> step2,cost 150ms
 * test:end,total cost 250ms
 *
 * 注意此工具类测试使用后需要删除，不要打到正式包中
 */
public class TimeLineLogger {
    private static boolean sIsDebug = true;
    private static final String TAG = "WatchLogger";

    private static class Unit {
        String tag;
        String info;
        long time;

        public Unit(String tag, String info) {
            this.tag = tag;
            this.info = info;
            this.time = System.currentTimeMillis();
        }
    }

    private static HashMap<String, Unit> sStartWatches = new HashMap<>();
    private static HashMap<String, Unit> sLastWatches = new HashMap<>();

    public static void init(boolean isDebug) {
        sIsDebug = isDebug;
    }

    public static void begin(String tag) {
        if (sIsDebug) {
            Unit unit = new Unit(tag, "begin");
            sLastWatches.put(tag, unit);
            sStartWatches.put(tag, unit);
            Log.d(TAG, String.format("%s: %s", tag, unit.info));
        }
    }

    public static void cut(String tag, String info) {
        if (sIsDebug) {
            Unit preUnit = sLastWatches.get(tag);
            if (preUnit != null) {
                Unit unit = new Unit(tag, info);
                sLastWatches.put(tag, unit);
                Log.d(TAG, String.format("%s: %s --> %s cost %d ms", tag, preUnit.info, unit.info, (unit.time - preUnit.time)));
            }
        }
    }

    public static void end(String tag) {
        if (sIsDebug) {
            Unit preUnit = sStartWatches.get(tag);
            if (preUnit != null) {
                Log.d(TAG, String.format(Locale.getDefault(), "%s: end,total cost %d ms", tag, (System.currentTimeMillis() - preUnit.time)));
            }
        }
        sLastWatches.remove(tag);
        sStartWatches.remove(tag);
    }

}
