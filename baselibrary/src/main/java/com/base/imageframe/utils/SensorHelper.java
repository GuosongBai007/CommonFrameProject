package com.base.imageframe.utils;

/**
 * Author:GuosongBai
 * Date:2018/10/31 10:06
 * Description:
 */
public class SensorHelper {
    public static final int ORIENTATION_ROTATE_0 = 0;
    public static final int ORIENTATION_ROTATE_90 = 90;
    public static final int ORIENTATION_ROTATE_180 = 180;
    public static final int ORIENTATION_ROTATE_270 = 270;

    public static int getOrientation(float x, float y) {
        if (x >= Math.abs(y))
            return ORIENTATION_ROTATE_0;
        else if (y >= Math.abs(x))
            return ORIENTATION_ROTATE_90;
        else if (Math.abs(x) >= Math.abs(y))
            return ORIENTATION_ROTATE_180;
        else
            return ORIENTATION_ROTATE_270;
    }
}
