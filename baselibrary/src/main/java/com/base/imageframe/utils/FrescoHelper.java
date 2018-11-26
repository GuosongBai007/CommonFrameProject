package com.base.imageframe.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.base.utils.AppUtils;
import com.base.widget.RecyclingImageView;
import com.facebook.binaryresource.BinaryResource;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.cache.common.WriterCallback;
import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeControllerBuilder;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.RoundingParams;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.postprocessors.IterativeBoxBlurPostProcessor;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Author:GuosongBai
 * Date:2018/10/31 09:49
 * Description:
 */
public class FrescoHelper {

    /**
     * 把部分ImageOption的属性转为Fresco支持的属性。对应Fresco的Hierarchy
     */
    public static void setHierarchyFromOption(RecyclingImageView draweeView, ImageOptions options) {
        if (options == null) {
            options = new ImageOptions.Builder().build();
        }
        ImageOptions lastOptions = draweeView.getImageOptions();
        GenericDraweeHierarchy hierarchy = draweeView.getHierarchy();
        //圆角
        if (lastOptions == null || lastOptions.isRounded() != options.isRounded()
                || lastOptions.getRoundedType().equals(options.getRoundedType())
                || lastOptions.getRoundedTopLeft() != options.getRoundedTopLeft()
                || lastOptions.getRoundedBorderColor() != options.getRoundedBorderColor()
                || lastOptions.getRoundedBorderWidth() != options.getRoundedBorderWidth()) {
            if (options.isRounded()) {
                if (options.getRoundedType() == ImageOptions.RoundedType.Full) {
                    hierarchy.setRoundingParams(RoundingParams.asCircle()
                            .setBorderWidth(options.getRoundedBorderWidth())
                            .setBorderColor(options.getRoundedBorderColor()));
                } else if (options.getRoundedType() == ImageOptions.RoundedType.Corner) {
                    RoundingParams roundingParams = new RoundingParams();
                    roundingParams.setCornersRadii(options.getRoundedTopLeft(), options.getRoundedTopRight(), options.getRoundedBottomRight(), options.getRoundedBottomLeft());
                    roundingParams
                            .setBorderWidth(options.getRoundedBorderWidth())
                            .setBorderColor(options.getRoundedBorderColor());
                    hierarchy.setRoundingParams(roundingParams);
                } else {
                    hierarchy.setRoundingParams(null);
                }
            } else {
                hierarchy.setRoundingParams(null);
            }
        }
        //灰度
        if (lastOptions == null || lastOptions.isGrayscale() != options.isGrayscale()) {
            if (options.isGrayscale()) {
                hierarchy.setActualImageColorFilter(getGrayImageColorFilter());
            } else {
                hierarchy.setActualImageColorFilter(null);
            }
        }
        //渐变时间
        if (lastOptions == null || lastOptions.getFadeDuration() != options.getFadeDuration()) {
            if (options.getFadeDuration() != -1) {
                hierarchy.setFadeDuration(options.getFadeDuration());
            } else {
                hierarchy.setFadeDuration(75);
            }
        }

        //加载中的占位图
        if (lastOptions == null || lastOptions.getImageResOnLoading() != options.getImageResOnLoading()) {
            if (options.getImageResOnLoading() != 0) {
                ScalingUtils.ScaleType scaleType = mapScaleType(options.getLoadingImageScaleType());
                hierarchy.setPlaceholderImage(AppUtils.getDrawable(options.getImageResOnLoading()), scaleType);
            } else {
                hierarchy.setPlaceholderImage(null);
            }
        }
        //加载失败的占位图
        if (lastOptions == null || lastOptions.getImageResOnFail() != options.getImageResOnFail()) {
            if (options.getImageResOnFail() != 0) {
                ScalingUtils.ScaleType scaleType = mapScaleType(options.getFailImageScaleType());
                hierarchy.setFailureImage(AppUtils.getDrawable(options.getImageResOnFail()), scaleType);
            } else {
                hierarchy.setFailureImage(null);
            }
        }

        ScalingUtils.ScaleType scaleType = mapScaleType(options.getImageScaleType());
        hierarchy.setActualImageScaleType(scaleType);

        draweeView.setImageOptions(options);
    }

    /**
     * 把部分ImageOption的属性转为Fresco支持的属性。对应Fresco的ImageRequest
     */
    public static ImageRequestBuilder getRequestBuilderFromOption(Uri uri, ImageOptions options) {
        if (options == null) {
            options = new ImageOptions.Builder().build();
        }
        ImageRequestBuilder requestBuilder = ImageRequestBuilder.newBuilderWithSource(uri);
        Postprocessor postprocessor = options.getPostprocessor();
        if (postprocessor != null) {
            requestBuilder.setPostprocessor(postprocessor);
        }
        //模糊
        if (options.isBlur()) {
            requestBuilder.setPostprocessor(new IterativeBoxBlurPostProcessor(options.getBlurRadius()));
            //requestBuilder.setPostprocessor(getBlurPostprocessor(uri, options.getBlurRadius()));
        }
        if (options.getResize() != null) {
            requestBuilder.setResizeOptions(new ResizeOptions(options.getResize().width, options.getResize().height));
        }

        return requestBuilder;
    }

    /**
     * 把部分ImageOption的属性转为Fresco支持的属性。对应Fresco的controller
     */
    public static PipelineDraweeControllerBuilder getControllerBuilderFromOption(RecyclingImageView draweeView, ImageOptions options) {
        if (options == null) {
            options = new ImageOptions.Builder().build();
        }
        PipelineDraweeControllerBuilder controllerBuilder;
        controllerBuilder = Fresco.newDraweeControllerBuilder().setAutoPlayAnimations(options.isAutoPlayAnimations());
        //回调
        if (options.onLoadEnd() != null) {
            final ImageOptions.OnLoadEnd onLoadEnd = options.onLoadEnd();
            controllerBuilder.setControllerListener(new BaseControllerListener<ImageInfo>() {

                @Override
                public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
                    onLoadEnd.onLoadEnd(Callback.RESULT_SUCCESS, imageInfo.getWidth(), imageInfo.getHeight());
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    onLoadEnd.onLoadEnd(Callback.RESULT_FAILED, 0, 0);
                }

            });
        }
        return controllerBuilder;
    }

    /**
     * 转换ImageOption的属性，并返回一个DraweeControllerBuilder
     */
    public static PipelineDraweeControllerBuilder convertOption(RecyclingImageView draweeView, Uri uri, ImageOptions options) {
        setHierarchyFromOption(draweeView, options);
        PipelineDraweeControllerBuilder controllerBuilder = getControllerBuilderFromOption(draweeView, options);
        controllerBuilder.setImageRequest(getRequestBuilderFromOption(uri, options).build());
        return controllerBuilder;
    }

    /**
     * 转换ImageOption的属性，并返回一个DraweeControllerBuilder
     *
     * @param isAutoPlayAnimations 是否自动播放webp动画
     */
    public static PipelineDraweeControllerBuilder convertOption(RecyclingImageView draweeView, Uri uri, ImageOptions options, boolean isAutoPlayAnimations) {
        setHierarchyFromOption(draweeView, options);
        PipelineDraweeControllerBuilder controllerBuilder = getControllerBuilderFromOption(draweeView, options);
        controllerBuilder.setImageRequest(getRequestBuilderFromOption(uri, options).build());
        if (isAutoPlayAnimations) {
            controllerBuilder.setAutoPlayAnimations(true);
        } else {
            controllerBuilder.setAutoPlayAnimations(false);
        }
        return controllerBuilder;
    }


    /**
     * 转换ImageOption的属性，并返回一个DraweeControllerBuilder。该方法不会从网络中下载图像，只从缓存里读取图形
     */
    public static PipelineDraweeControllerBuilder convertOptionAndOnlyCache(RecyclingImageView draweeView, Uri uri, ImageOptions options) {
        setHierarchyFromOption(draweeView, options);
        ImageRequestBuilder requestBuilder = getRequestBuilderFromOption(uri, options);
        requestBuilder.setLowestPermittedRequestLevel(ImageRequest.RequestLevel.DISK_CACHE);
        PipelineDraweeControllerBuilder controllerBuilder = getControllerBuilderFromOption(draweeView, options);
        controllerBuilder.setImageRequest(requestBuilder.build());
        return controllerBuilder;
    }

    /**
     * 转换optons的属性，传入多个uri，顺序优先
     *
     * @param draweeView
     * @param options
     * @param uris
     * @return
     */
    public static PipelineDraweeControllerBuilder convertOptionMultiUri(RecyclingImageView draweeView, ImageOptions options, Uri... uris) {
        int size = uris.length;
        setHierarchyFromOption(draweeView, options);
        ImageRequest[] imageRequests = new ImageRequest[size];
        for (int i = 0; i < uris.length; i++) {
            ImageRequestBuilder imageRequestBuilder = ImageRequestBuilder.newBuilderWithSource(uris[i]).setPostprocessor(options.getPostprocessor());
            if (options.isBlur()) {
                imageRequestBuilder.setPostprocessor(new IterativeBoxBlurPostProcessor(options.getBlurRadius()));
                //requestBuilder.setPostprocessor(getBlurPostprocessor(uri, options.getBlurRadius()));
            }
            if (options.getResize() != null) {
                imageRequestBuilder.setResizeOptions(new ResizeOptions(options.getResize().width, options.getResize().height));
            }
            imageRequests[i] = imageRequestBuilder.build();
        }
        PipelineDraweeControllerBuilder builder = getControllerBuilderFromOption(draweeView, options);
        builder.setFirstAvailableImageRequests(imageRequests);

        return builder;
    }

    /**
     * 获取灰度图的滤镜
     */
    public static ColorFilter getGrayImageColorFilter() {
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        return new ColorMatrixColorFilter(cm);
    }

    /**
     * 获取高斯模糊的后处理器
     *
     * @param blurRadius 模糊半径
     */
    public static Postprocessor getBlurPostprocessor(final Uri uri, final int blurRadius) {
        return new BasePostprocessor() {

            @Override
            public CacheKey getPostprocessorCacheKey() {
                return new SimpleCacheKey(getName() + uri.toString());
            }

            @Override
            public String getName() {
                return "BlurPostprocessor";
            }

            @Override
            public void process(Bitmap bitmap) {
                FastBlur.doBlur(bitmap, blurRadius, true);
            }

            @Override
            public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
                super.process(destBitmap, sourceBitmap);
            }

            @Override
            public CloseableReference<Bitmap> process(Bitmap sourceBitmap, PlatformBitmapFactory bitmapFactory) {
                return super.process(sourceBitmap, bitmapFactory);
            }
        };
    }

    /**
     * 从磁盘缓存中读取图片
     *
     * @param uri
     * @return
     */
    public static Bitmap getBitmapFormDiskCache(Uri uri) {
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        if (resource != null) {
            try {
                InputStream inputStream = resource.openStream();
                Bitmap bitmap = BitmapGenerator.decodeStream(inputStream);
                inputStream.close();
                return bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取磁盘缓存中的图片路径
     *
     * @param uri
     * @return
     */
    public static String getDiskCacheFilePath(Uri uri) {
        String path = "";
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
        BinaryResource resource = ImagePipelineFactory.getInstance().getMainFileCache().getResource(cacheKey);
        if (resource instanceof FileBinaryResource) {
            path = ((FileBinaryResource) resource).getFile().getAbsolutePath();
        }
        if (TextUtils.isEmpty(path)) {
            resource = ImagePipelineFactory.getInstance().getSmallImageFileCache().getResource(cacheKey);
            if (resource instanceof FileBinaryResource) {
                path = ((FileBinaryResource) resource).getFile().getAbsolutePath();
            }
        }
        return path;
    }

    /**
     * 将图片加入到Fresco缓存中去
     *
     * @param uri
     * @param bitmap
     */
    public static void addBitmapToDiskCache(Uri uri, final Bitmap bitmap) {
        ImageRequest imageRequest = ImageRequest.fromUri(uri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance().getEncodedCacheKey(imageRequest, null);
        try {
            ImagePipelineFactory.getInstance().getMainFileCache().insert(cacheKey, new WriterCallback() {
                @Override
                public void write(OutputStream os) throws IOException {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断有没有缓存
     *
     * @param uri
     * @return
     */
    public static boolean hasCached(Uri uri) {
        return Fresco.getImagePipeline().isInBitmapMemoryCache(uri)
                || Fresco.getImagePipeline().isInDiskCacheSync(uri);
    }

    /**
     * 映射ImageView的ScaleType到Fresco的ScaleType
     *
     * @param scaleType
     * @return
     */
    public static ScalingUtils.ScaleType mapScaleType(ImageView.ScaleType scaleType) {
        if (scaleType == null) {
            return GenericDraweeHierarchyBuilder.DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
        }
        switch (scaleType) {
            case FIT_XY:
                return ScalingUtils.ScaleType.FIT_XY;
            case FIT_START:
                return ScalingUtils.ScaleType.FIT_START;
            case FIT_CENTER:
                return ScalingUtils.ScaleType.FIT_CENTER;
            case FIT_END:
                return ScalingUtils.ScaleType.FIT_END;
            case CENTER:
                return ScalingUtils.ScaleType.CENTER;
            case CENTER_CROP:
                return ScalingUtils.ScaleType.CENTER_CROP;
            case CENTER_INSIDE:
                return ScalingUtils.ScaleType.CENTER_INSIDE;
            case MATRIX:
                //  DraweeView的缩放和ImageView的缩放类型基本上是相同的，fitXY，centerCrop，
                // 唯一与ImageView有区别的就是，他不支持matrix属性，但是追加了一个focusCrop属性来替代matrix属性，
                // 这里在设置属性的时候，xml使用fresco:actualScaleType来设置DraweeView的缩放属性或者是使用GenericDraweeHierarchy属性去设置。
                // 实际上focusCrop 是会通过算计让人像居中显示 实测效果和centerCrop类似，
                // 要想实现ImageViwe的MATRIX的平铺效果 就直接重写一下这段代码  通过计算以宽的一方为准  直接平铺效果
                return new ScalingUtils.AbstractScaleType() {

                    @Override
                    public void getTransformImpl(Matrix outTransform, Rect parentRect, int childWidth, int childHeight, float focusX, float focusY, float scaleX, float scaleY) {
                        // 取宽度和高度需要缩放的倍数中最小的一个
                        final float sX = (float) parentRect.width() / (float) childWidth;
                        final float sY = (float) parentRect.height() / (float) childHeight;
                        float scale = Math.max(scaleX, scaleY);

                        // 计算为了均分空白区域，需要偏移的x、y方向的距离
                        float dx = parentRect.left + (parentRect.width() - childWidth * scale) * 0.0f;
                        float dy = parentRect.top + (parentRect.height() - childHeight * scale) * 0.0f;

                        // 最后我们应用它
                        outTransform.setScale(scale, scale);
                        outTransform.postTranslate((int) (dx + 0.0f), (int) (dy + 0.0f));
                    }
                };
//                return ScalingUtils.ScaleType.FOCUS_CROP;
            default:
                return GenericDraweeHierarchyBuilder.DEFAULT_ACTUAL_IMAGE_SCALE_TYPE;
        }
    }

    public static void clearBitmapCache() {
        Fresco.getImagePipeline().clearMemoryCaches();
    }

    /**
     * 根据缓存的Uri 清除本地缓存
     */
    public static void clearBitMapCacheByUri(Uri uri) {
        Fresco.getImagePipeline().evictFromCache(uri);
    }

    public static void prefetchImageToDisk(String urlStr, ImageOptions options) {
        ImageRequest request = getRequestBuilderFromOption(Uri.parse(urlStr), options).build();
//        DataSource<Void> dataSource = Fresco.getImagePipeline().prefetchToDiskCache(request, null);
        Fresco.getImagePipeline().prefetchToDiskCache(request, null);
    }

    /**
     * 通过把bitmap转换成圆形
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

        if (bitmap == null) {
            return null;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = Color.RED;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = bitmap.getWidth() / 2;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;

    }
}

