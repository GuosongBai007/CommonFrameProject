package com.base.imageframe.utils;



import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.util.TypedValue;

import java.io.FileDescriptor;
import java.io.InputStream;

/**
 * Author:GuosongBai
 * Date:2018/10/31 09:59
 * Description: 与Bitmap和BitmapFactory中创建Bitmap的方法一一对应
 */
public class BitmapGenerator {

    public static Bitmap createBitmap(Bitmap src) {
        if (src == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(src);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height) {
        if (source == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(source, x, y, width, height);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(Bitmap source, int x, int y, int width, int height, Matrix m, boolean filter) {
        if (source == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(source, x, y, width, height, m, filter);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int width, int height, Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int[] colors, int offset, int stride, int width, int height, Bitmap.Config config) {
        if (colors == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(colors, offset, stride, width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createBitmap(int[] colors, int width, int height, Bitmap.Config config) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(colors, width, height, config);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap createScaledBitmap(Bitmap src, int dstWidth, int dstHeight, boolean filter) {
        if (src == null) {
            return null;
        }

        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createScaledBitmap(src, dstWidth, dstHeight, filter);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length, BitmapFactory.Options opts) {
        if (data == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(data, offset, length, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeByteArray(byte[] data, int offset, int length) {
        if (data == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeByteArray(data, offset, length);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String pathName) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeFile(String pathName, BitmapFactory.Options opts) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(pathName, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFileDescriptor(fd);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeFileDescriptor(FileDescriptor fd, Rect outPadding, BitmapFactory.Options opts) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFileDescriptor(fd, outPadding, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeResource(Resources res, int id, BitmapFactory.Options opts) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(res, id, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeResource(Resources res, int id) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(res, id);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeResourceStream(Resources res, TypedValue value, InputStream is, Rect pad, BitmapFactory.Options opts) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResourceStream(res, value, is, pad, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeStream(InputStream is) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap decodeStream(InputStream is, Rect outPadding, BitmapFactory.Options opts) {
        if (is == null) {
            return null;
        }
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(is, outPadding, opts);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
