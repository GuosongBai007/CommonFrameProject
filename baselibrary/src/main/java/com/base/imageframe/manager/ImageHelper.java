package com.base.imageframe.manager;

import android.content.res.Resources;
import android.graphics.Bitmap;

import com.base.imageframe.utils.ImageOptions;


/**
 * Author:GuosongBai
 * Date:2018/10/31 09:59
 * Description:
 */
public class ImageHelper {
    public static Bitmap roundedIfNeeded(Resources resources, Bitmap bitmap, ImageOptions opts) {
        if (opts != null && opts.isRounded() && bitmap != null) {
            if (opts.getRoundedType() == ImageOptions.RoundedType.Full) {
                bitmap = ImageUtil.getRoundedBitmap(bitmap, opts.getRoundedBorderWidth(),
                        opts.getRoundedBorderColor());
            }
            else if (opts.getRoundedType() == ImageOptions.RoundedType.Corner) {
                bitmap = ImageUtil.getRoundedCornerBitmap(bitmap, opts.getRoundedTopLeft(),
                        opts.getRoundedBorderWidth(),
                        opts.getRoundedBorderColor());
            }
        }
        return bitmap;
    }

    public static Bitmap toGrayscaleIfNeeded(Resources resources, Bitmap bitmap, ImageOptions opts) {
        if (opts != null && opts.isGrayscale() && bitmap != null) {
            bitmap = ImageUtil.toGrayscale(bitmap);
        }
        return bitmap;
    }
}
