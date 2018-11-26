package com.base.imageframe.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.base.rxgalleryfinal.imageloader.AbsImageLoader;
import com.base.rxgalleryfinal.ui.widget.FixImageView;
import com.base.rxgalleryfinal.ui.widget.SquareRelativeLayout;
import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.common.RotationOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Author:GuosongBai
 * Date:2018/11/2 11:12
 * Description:用于在图片选择器作为图片加载器使用
 */
public class FrescoImageLoader implements AbsImageLoader {

    private DraweeHolder<GenericDraweeHierarchy> draweeHolder;

    public static void setImageSmall(String url,
                                     SimpleDraweeView simpleDraweeView,
                                     int width,
                                     int height,
                                     SquareRelativeLayout relativeLayout, boolean playGif) {

        Uri uri = Uri.parse(url);
        ImageRequest request = ImageRequestBuilder
                .newBuilderWithSource(uri)
                .setRotationOptions(RotationOptions.autoRotate())
                .setResizeOptions(new ResizeOptions(width, height))
                .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setTapToRetryEnabled(true)
                .setImageRequest(request)
                .setAutoPlayAnimations(playGif)
                .setOldController(simpleDraweeView.getController())
                .build();
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(width - 5, height));
        simpleDraweeView.setController(controller);
    }

    private void init(Context ctx, Drawable defaultDrawable) {
        if (draweeHolder == null) {
            Resources resources = ctx.getResources();
            GenericDraweeHierarchy hierarchy = new GenericDraweeHierarchyBuilder(resources)
                    .setPlaceholderImage(defaultDrawable)
                    .setFailureImage(defaultDrawable)
                    .build();
            draweeHolder = DraweeHolder.create(hierarchy, ctx);
        }
    }

    @Override
    public void displayImage(Context context,
                             String path,
                             FixImageView imageView,
                             Drawable defaultDrawable,
                             Bitmap.Config config,
                             boolean resize, boolean isGif,
                             int width,
                             int height,
                             int rotate) {
        init(context, defaultDrawable);

        imageView.setOnImageViewListener(new FixImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                return dr == draweeHolder.getHierarchy().getTopLevelDrawable();
            }

            @Override
            public void onDraw(Canvas canvas) {
                Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                if (drawable == null) {
                    imageView.setImageDrawable(defaultDrawable);
                } else {
                    imageView.setImageDrawable(drawable);
                }
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return draweeHolder.onTouchEvent(event);
            }
        });
        Uri uri = new Uri.Builder()
                .scheme(UriUtil.LOCAL_FILE_SCHEME)
                .path(path)
                .build();

        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri)
                .setAutoRotateEnabled(true);
        if (resize) {
            builder.setResizeOptions(new ResizeOptions(width, height));
        }
        ImageRequest request = builder.build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setOldController(draweeHolder.getController())
                .setImageRequest(request)
                .build();
        draweeHolder.setController(controller);
    }

    public void displayImage(Context context,
                             String path,
                             FixImageView imageView,
                             Drawable defaultDrawable,
                             OnImageLoadEndListener listener) {
        init(context, defaultDrawable);
        imageView.setOnImageViewListener(new FixImageView.OnImageViewListener() {
            @Override
            public void onDetach() {
                draweeHolder.onDetach();
            }

            @Override
            public void onAttach() {
                draweeHolder.onAttach();
            }

            @Override
            public boolean verifyDrawable(Drawable dr) {
                return dr == draweeHolder.getHierarchy().getTopLevelDrawable();
            }

            @Override
            public void onDraw(Canvas canvas) {
                if (listener != null) {
                    listener.onDraw(canvas, draweeHolder);
                } else {
                    Drawable drawable = draweeHolder.getHierarchy().getTopLevelDrawable();
                    if (drawable == null) {
                        imageView.setImageDrawable(defaultDrawable);
                    } else {
                        imageView.setImageDrawable(drawable);
                    }
                }
            }

            @Override
            public boolean onTouchEvent(MotionEvent event) {
                return draweeHolder.onTouchEvent(event);
            }
        });
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(Uri.parse(path));
        ImageRequest request = builder.build();

        PipelineDraweeControllerBuilder controller1 = Fresco.newDraweeControllerBuilder();
        controller1.setAutoPlayAnimations(true);
        controller1.setImageRequest(request);
        controller1.setControllerListener(new BaseControllerListener<ImageInfo>() {
            @Override
            public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                super.onFinalImageSet(id, imageInfo, animatable);
                if (animatable != null) {
                    animatable.start();
                }
            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);

            }
        });
        draweeHolder.setController(controller1.build());
    }


    public interface OnImageLoadEndListener {
        void onDraw(Canvas canvas, DraweeHolder<GenericDraweeHierarchy> draweeHolder);
    }

}
