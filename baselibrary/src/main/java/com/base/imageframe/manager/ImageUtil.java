package com.base.imageframe.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.text.TextUtils;

import com.base.imageframe.utils.BitmapGenerator;
import com.base.imageframe.utils.SensorHelper;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Author:GuosongBai
 * Date:2018/10/31 10:02
 * Description:
 */
public class ImageUtil {
    public static Bitmap convert(Bitmap src, Config config) {
        if (src == null) {
            return null;
        }

        Bitmap dst = BitmapGenerator.createBitmap(src.getWidth(), src.getHeight(), config);
        if (dst == null) {
            return null;
        }

        Canvas canvas = new Canvas(dst);
        canvas.drawBitmap(src, 0, 0, null);
        return dst;
    }

    /**
     * 获取灰度图片
     *
     * @param original 原始图片
     * @return 灰度处理后的图片
     */
    public static Bitmap toGrayscale(Bitmap original) {
        int width, height;
        height = original.getHeight();
        width = original.getWidth();
        Bitmap bmpGrayscale = BitmapGenerator.createBitmap(width, height, Config.ARGB_8888);

        if (bmpGrayscale != null) {
            Canvas c = new Canvas(bmpGrayscale);
            Paint paint = new Paint();
            ColorMatrix cm = new ColorMatrix();
            cm.setSaturation(0);
            ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
            paint.setColorFilter(f);
            c.drawBitmap(original, 0, 0, paint);
        }
        return bmpGrayscale;
    }

    /**
     * Drawable转Bitmap
     *
     * @param drawable Drawable对象
     * @return 转换后的Bitmap对象
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;
        try {
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888 : Config.RGB_565);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, width, height);
            drawable.draw(canvas);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 调整inSampleSize
     *
     * @param oldValue
     * @return
     */
    private static int adjustInSampleSize(int oldValue) {
        if (oldValue > 1 && oldValue < 2) {
            return 2;
        } else if ((oldValue > 2 && oldValue < 4) || (oldValue > 4 && oldValue < 6)) {
            return 4;
        } else if ((oldValue > 6 && oldValue < 8) || (oldValue > 8 && oldValue < 12)) {
            return 8;
        } else if ((oldValue > 12 && oldValue < 16) || (oldValue > 16 && oldValue < 24)) {
            return 16;
        } else if ((oldValue > 24 && oldValue < 32) || (oldValue > 32 && oldValue < 48)) {
            return 32;
        } else {
            return oldValue;
        }
    }

    /**
     * 计算BitmapFactory.Options对象中inSampleSize（缩放比例）值
     *
     * @param options   BitmapFactory.Options 对象
     * @param reqWidth  缩放宽度
     * @param reqHeight 缩放高度
     * @return inSampleSize（缩放比例）值
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return adjustInSampleSize(inSampleSize);
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return 旋转的角度
     */
    public static int getPhotoDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片文件
     *
     * @param degree   要旋转的角度
     * @param srcPath  源文件路径
     * @param outPath  输出路径
     * @param isModify 是否修改源文件
     * @return 是否成功
     */
    public static boolean rotateImgByPhotoDegree(int degree, String srcPath, String outPath, int reqWidth, int reqHeight, boolean isModify) {
        try {
            Bitmap src = decodeSampledFile(srcPath, reqWidth, reqHeight, false);
            Matrix matrix = new Matrix();
            matrix.postRotate(degree);
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap bitmap = Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
            File file;
            if (isModify) {
                file = new File(srcPath);
            } else {
                file = new File((outPath));
                if (!file.exists()) {
                    file.createNewFile();
                }
            }
//            StorageUtil.saveImage(bitmap, outPath, CompressFormat.JPEG, 90, true);
            return true;
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将指定路径图片文件按指定大小缩放解码为Bitmap对象
     *
     * @param path        图片路径
     * @param reqWidth    缩放宽度
     * @param reqHeight   缩放高度
     * @param isStretched 是否拉伸
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap decodeSampledFile(String path, int reqWidth, int reqHeight, boolean isStretched) {
        if (TextUtils.isEmpty(path) || reqWidth <= 0 || reqHeight <= 0) {
            return null;
        }

        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, opts);

        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inPreferredConfig = Config.RGB_565;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeFile(path, opts);
            bitmap = decodeSampledBitmap(bitmap, reqWidth, reqHeight, isStretched);
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    /**
     * 将指定图片按指定大小缩放解码为Bitmap对象
     *
     * @param bitmap      图片Bitmap对象
     * @param reqWidth    缩放宽度
     * @param reqHeight   缩放高度
     * @param isStretched 是否拉伸
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap decodeSampledBitmap(Bitmap bitmap, int reqWidth, int reqHeight, boolean isStretched) {
        if (bitmap == null) {
            return null;
        }

        try {
            if (isStretched) {
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, reqWidth, reqHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            } else {
                float scale = getScaleRate(bitmap.getWidth(), bitmap.getHeight(), reqWidth, reqHeight);
                bitmap = ThumbnailUtils.extractThumbnail(bitmap, (int) (scale * bitmap.getWidth()), (int) (scale * bitmap.getHeight()), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            bitmap = null;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            bitmap = null;
        }
        return bitmap;
    }

    public static Bitmap scaleBitmap(Bitmap oldBitmap, int dstWidth, int dstHeight) {
        Bitmap newBitmap = null;
        if (oldBitmap != null) {
            newBitmap = BitmapGenerator.createScaledBitmap(oldBitmap, dstWidth, dstHeight, true);
        }
        return newBitmap;
    }

    public static Bitmap rotate(Bitmap oldBitmap, int degress) {
        Bitmap newBitmap = null;
        if (oldBitmap != null) {
            Matrix m = new Matrix();
            m.postRotate(degress);
            newBitmap = BitmapGenerator.createBitmap(oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), m, true);
        }
        return newBitmap;
    }

    public static Bitmap compress(Bitmap oldBitmap, CompressFormat format, int quality) {
        Bitmap newBitmap = null;
        if (oldBitmap != null) {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            oldBitmap.compress(format, quality, output);
            ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
            newBitmap = BitmapGenerator.decodeStream(input);
        }
        return newBitmap;
    }

    /**
     * 将指定资源图片文件按指定大小缩放解码为Bitmap对象
     *
     * @param res         资源对象
     * @param resId       资源ID
     * @param reqWidth    缩放宽度
     * @param reqHeight   缩放高度
     * @param isStretched 是否拉伸
     * @return 缩放后的Bitmap对象
     */
    public static Bitmap decodeSampledFile(Resources res, int resId, int reqWidth, int reqHeight, boolean isStretched) {
        if (res == null || resId == 0 || reqWidth == 0 || reqHeight == 0) {
            return null;
        }

        final BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, opts);

        opts.inSampleSize = calculateInSampleSize(opts, reqWidth, reqHeight);
        opts.inJustDecodeBounds = false;
        opts.inPurgeable = true;
        opts.inInputShareable = true;
        opts.inPreferredConfig = Config.RGB_565;

        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeResource(res, resId, opts);
            bitmap = decodeSampledBitmap(bitmap, reqWidth, reqHeight, isStretched);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 读取图片属性：旋转角度
     *
     * @param path 图片绝对路径
     * @return degree 旋转角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 设置图片属性：旋转角度
     *
     * @param path   图片绝对路径
     * @param degree 旋转角度
     */
    public static void setPictureDegree(String path, int degree) {
        int orientation;
        switch (degree) {
            case SensorHelper.ORIENTATION_ROTATE_0:
                orientation = ExifInterface.ORIENTATION_NORMAL;
                break;

            case SensorHelper.ORIENTATION_ROTATE_90:
                orientation = ExifInterface.ORIENTATION_ROTATE_90;
                break;

            case SensorHelper.ORIENTATION_ROTATE_180:
                orientation = ExifInterface.ORIENTATION_ROTATE_180;
                break;

            case SensorHelper.ORIENTATION_ROTATE_270:
                orientation = ExifInterface.ORIENTATION_ROTATE_270;
                break;

            default:
                return;
        }
        ;
        try {
            ExifInterface exif = new ExifInterface(path);
            exif.setAttribute(ExifInterface.TAG_ORIENTATION, String.valueOf(orientation));
            exif.saveAttributes();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static float getScaleRate(int srcWidth, int srcHeight, int dstWidth, int dstHeight) {
        if (srcWidth > dstWidth || srcHeight > dstHeight) {
            float scaleWidth = (float) dstWidth / (float) srcWidth;
            float scaleHeight = (float) dstHeight / (float) srcHeight;
            return Math.min(scaleWidth, scaleHeight);
        }
        return 1;
    }

    /**
     * 获取圆形图片
     *
     * @param bitmap 待圆形化bitmap
     * @return 圆形化bitmap
     */
    public static Bitmap getRoundedBitmap(Bitmap bitmap, float borderWidth, int borderColor) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPixel = width <= height ? width / 2 : height / 2;
        return getRoundedCornerBitmap(bitmap, roundPixel, borderWidth, borderColor);
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPixel, float borderWidth, int borderColor) {
        if (bitmap == null) {
            return null;
        }

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = BitmapGenerator.createBitmap(width, height, Config.ARGB_8888);
        if (output == null) {
            return null;
        }
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);
        // paint.setColor(borderColor);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, roundPixel, roundPixel, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);

        if (borderWidth > 0) {
            paint.setColor(borderColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(borderWidth);
            canvas.drawRoundRect(rectF, roundPixel, roundPixel, paint);
        }

        return output;
    }

    private static final int RED_MASK = 0xff0000;
    private static final int RED_MASK_SHIFT = 16;
    private static final int GREEN_MASK = 0x00ff00;
    private static final int GREEN_MASK_SHIFT = 8;
    private static final int BLUE_MASK = 0x0000ff;

    /**
     * Creates a blurred version of the given Bitmap.
     *
     * @param bitmap the input bitmap, presumably a 96x96 pixel contact thumbnail.
     */
    public static Bitmap createBlurredBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        // The bitmap we pass to gaussianBlur() needs to have a width
        // that's a power of 2, so scale up to 128x128.
        final int scaledSize = 128;
        bitmap = Bitmap.createScaledBitmap(bitmap, scaledSize, scaledSize, true /* filter */);
        bitmap = gaussianBlur(bitmap);
        return bitmap;
    }

    /**
     * Apply a gaussian blur filter, and return a new (blurred) bitmap that's
     * the same size as the input bitmap.
     *
     * @param source input bitmap, whose width must be a power of 2
     */
    public static Bitmap gaussianBlur(Bitmap source) {
        int width = source.getWidth();
        int height = source.getHeight();

        // Create a source and destination buffer for the image.
        int numPixels = width * height;
        int[] in = new int[numPixels];
        int[] tmp = new int[numPixels];

        // Get the source pixels as 32-bit ARGB.
        source.getPixels(in, 0, width, 0, 0, width, height);

        // Gaussian is a separable kernel, so it is decomposed into a horizontal
        // and vertical pass.
        // The filter function applies the kernel across each row and transposes
        // the output.
        // Hence we apply it twice to provide efficient horizontal and vertical
        // convolution.
        // The filter discards the alpha channel.
        gaussianBlurFilter(in, tmp, width, height);
        gaussianBlurFilter(tmp, in, width, height);

        // Return a bitmap scaled to the desired size.
        Bitmap filtered = Bitmap.createBitmap(in, width, height, Config.ARGB_8888);
        source.recycle();
        return filtered;
    }

    private static void gaussianBlurFilter(int[] in, int[] out, int width, int height) {
        // This function is currently hardcoded to blur with RADIUS = 4.
        // (If you change RADIUS, you'll have to change the weights[] too.)
        final int RADIUS = 4;
        final int[] weights = {13, 23, 32, 39, 42, 39, 32, 23, 13}; // Adds up
        // to 256
        int inPos = 0;
        int widthMask = width - 1; // width must be a power of two.
        for (int y = 0; y < height; ++y) {
            // Compute the alpha value.
            int alpha = 0xff;
            // Compute output values for the row.
            int outPos = y;
            for (int x = 0; x < width; ++x) {
                int red = 0;
                int green = 0;
                int blue = 0;
                for (int i = -RADIUS; i <= RADIUS; ++i) {
                    int argb = in[inPos + (widthMask & (x + i))];
                    int weight = weights[i + RADIUS];
                    red += weight * ((argb & RED_MASK) >> RED_MASK_SHIFT);
                    green += weight * ((argb & GREEN_MASK) >> GREEN_MASK_SHIFT);
                    blue += weight * (argb & BLUE_MASK);
                }
                // Output the current pixel.
                out[outPos] = (alpha << 24) | ((red >> 8) << RED_MASK_SHIFT) | ((green >> 8) << GREEN_MASK_SHIFT) | (blue >> 8);
                outPos += height;
            }
            inPos += width;
        }
    }

    public static Bitmap createCustomShapeBitmap(Bitmap srcBitmap, Drawable shapeDrawable) {
        Bitmap dstBitmap = null;
        boolean recycle = false;
        if (srcBitmap != null) {
            try {
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

                shapeDrawable.setBounds(0, 0, srcBitmap.getWidth(), srcBitmap.getHeight());
                dstBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);

                Canvas canvas = new Canvas(dstBitmap);
                shapeDrawable.draw(canvas);
                canvas.drawBitmap(srcBitmap, 0, 0, paint);
            } catch (Exception e) {
                e.printStackTrace();
                recycle = true;
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
                recycle = true;
            }

            if (recycle && dstBitmap != null) {
                dstBitmap = null;
            }
        }
        return dstBitmap;
    }

    public static Bitmap createRoundShapeBitmap(Bitmap srcBitmap, int radius, int pinding) {
        Bitmap dstBitmap = null;
        boolean recycle = false;
        if (srcBitmap != null) {
            try {
                Paint paint = new Paint();

                paint.setAntiAlias(true);
                dstBitmap = Bitmap.createBitmap(srcBitmap.getWidth(), srcBitmap.getHeight(), Config.ARGB_8888);
                RectF rect = new RectF(0f, 0f, (float) srcBitmap.getWidth(), (float) srcBitmap.getHeight());
                RectF rect2 = new RectF(0f, 0f, (float) (srcBitmap.getWidth() - pinding), (float) srcBitmap.getHeight());
                Canvas canvas = new Canvas(dstBitmap);
                paint.setColor(0xff224135);
                canvas.drawARGB(0, 0, 0, 0);
                canvas.drawRoundRect(rect2, radius, radius, paint);
                // shapeDrawable.draw(canvas);
                paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

                canvas.drawBitmap
                        (srcBitmap, 0, 0, paint);
            } catch (Exception e) {
                e.printStackTrace();
                recycle = true;
            } catch (OutOfMemoryError err) {
                err.printStackTrace();
                recycle = true;
            }

            if (recycle && dstBitmap != null) {
                dstBitmap = null;
            }
        }
        return dstBitmap;
    }

    /**
     * @param originalColor color, without alpha
     * @param alpha         from 0.0 to 1.0
     * @return
     */
    public static String addAlpha(String originalColor, float alpha) {
        long alphaFixed = Math.round(alpha * 255);
        String alphaHex = Long.toHexString(alphaFixed);
        if (alphaHex.length() == 1) {
            alphaHex = "0" + alphaHex;
        }
        originalColor = originalColor.replace("#", "#" + alphaHex);


        return originalColor;
    }

    public static boolean writeBitmapToFile(String filePath, Bitmap bitmap, CompressFormat format, int quality) {
        if (TextUtils.isEmpty(filePath) || bitmap == null) {
            return false;
        }

        try {
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(format, quality, out);
            out.flush();
            out.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static int getImageFilePixel(String path) {
        if (TextUtils.isEmpty(path))
            return 0;
        int proportion = 0;
        try {
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            int w = opts.outWidth;
            int h = opts.outHeight;
            proportion = w * h;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return proportion;
    }

    public static boolean isGif(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return false;
        if (filePath.endsWith(".gif") || filePath.endsWith(".GIF"))
            return true;
        return false;
    }
}
