package com.base.imageframe.manager;

import android.net.Uri;

import com.base.imageframe.utils.FrescoHelper;
import com.base.imageframe.utils.ImageOptions;
import com.base.widget.RecyclingImageView;
import com.facebook.common.util.UriUtil;

/**
 * Author:GuosongBai
 * Date:2018/10/31 09:16
 * Description:Fresco 图片加载
 */
public class FrescoManager {

    private static ImageOptions sDefaultOpts = new ImageOptions.Builder().build();
    private static ImageOptions sRoundOpts;

    /**
     * 默认加载图片，对图片样式没有要求 直接加载原图
     */
    public static void displayImageWithDefaultOption(RecyclingImageView draweeView, String uri) {
        if (draweeView == null) {
            return;
        }
        draweeView.setController(FrescoHelper.convertOption(draweeView, Uri.parse(uri), sDefaultOpts).build());
    }

    /**
     * 通过传入 ImageOptions 加载图片
     * 可通过IamgeOptions圆角  圆形  高斯模糊  圆角某一边等
     */
    public static void displayImageView(RecyclingImageView draweeView, String uri, ImageOptions opts) {
        if (draweeView == null) {
            return;
        }
        draweeView.setController(FrescoHelper.convertOption(draweeView, Uri.parse(uri), opts).build());
    }

    /**
     * 加载圆角图片 传入圆角大小
     */
    public static void displayFilletImageView(RecyclingImageView draweeView, String uri, int fillet) {
        if (draweeView == null) {
            return;
        }
        ImageOptions.Builder builder = new ImageOptions.Builder();
        builder.setRoundedRadius(fillet, fillet, fillet, fillet);
        builder.isRounded(true);
        builder.setRoundedType(ImageOptions.RoundedType.Corner);
        draweeView.setController(FrescoHelper.convertOption(draweeView, Uri.parse(uri), builder.build()).build());
    }

    /**
     * 加载圆形图片比如头像
     */
    public static void displayRoundImageView(RecyclingImageView draweeView, String uri) {
        if (draweeView == null) {
            return;
        }
        if (sRoundOpts == null) {
            ImageOptions.Builder builder = new ImageOptions.Builder();
            builder.isRounded(true);
            sRoundOpts = builder.build();
        }
        draweeView.setController(FrescoHelper.convertOption(draweeView, Uri.parse(uri), sRoundOpts).build());
    }


    public static void displayImageWithAutoAnim(RecyclingImageView draweeView, String uri, boolean isPlay) {
        if (draweeView == null) {
            return;
        }
        draweeView.setController(FrescoHelper.convertOption(draweeView, Uri.parse(uri), sDefaultOpts, isPlay).build());
    }

    public static void displayImageWithResId(RecyclingImageView draweeView, int resId,ImageOptions opts) {
        if (draweeView == null) {
            return;
        }
        draweeView.setController(FrescoHelper.convertOption(draweeView, UriUtil.getUriForResourceId(resId), opts).build());
    }

}
