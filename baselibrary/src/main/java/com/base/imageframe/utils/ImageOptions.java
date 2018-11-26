package com.base.imageframe.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.base.imageframe.manager.ImageHelper;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Author:GuosongBai
 * Date:2018/10/31 09:49
 * Description:RecyclingImageView的属性配置,比如圆角 圆  边框   特色功能：灰度图片，高斯模糊图片
 */
public class ImageOptions {
    private boolean mIsRounded;
    private RoundedType mRoundedType;
    private float mRoundedTopRight;
    private float mRoundedBottomLeft;
    private float mRoundedBottomRight;
    private float mRoundedTopLeft;

    private int mOverLayColor;

    private float mRoundedBorderWidth;
    private int mRoundedBorderColor;
    private boolean mIsBackground;
    private Bitmap mCachedImageOnLoading;
    private Bitmap mCachedImageOnFail;
    private int mImageResOnLoading;
    private int mImageResOnFail;
    private ImageView.ScaleType mLoadingImageScaleType;
    private ImageView.ScaleType mFailImageScaleType;
    private ImageView.ScaleType mImageScaleType = ImageView.ScaleType.CENTER_CROP;
    private int mFadeDuration;
    private Bitmap.Config mBitmapConfig;
    private boolean mIsGrayscale;
    private boolean mIsIgnoreImageView;
    private boolean mIsBlur;
    private boolean mIsResetView;
    private int mBlurRadius;
    private Resize mResize;
    private Callback<Bitmap> mOnLoadEndCallback;
    private OnLoadEnd mOnLoadEnd;

    private boolean mIsProcessAsync;
    private boolean mAutoPlayAnimations;

    private Postprocessor mPostprocessor;

    public ImageOptions(Builder builder) {
        mIsRounded = builder.isRounded;
        mRoundedType = builder.roundedType != null ? builder.roundedType : RoundedType.Full;

        mRoundedTopLeft = builder.mRoundedTopLeft;
        mRoundedTopRight = builder.mRoundedTopRight;
        mRoundedBottomLeft = builder.mRoundedBottomLeft;
        mRoundedBottomRight = builder.mRoundedBottomRight;
        mOverLayColor = builder.mOverlayColor;

        mRoundedBorderWidth = builder.roundedBorderWidth;
        mRoundedBorderColor = builder.roundedBorderColor;
        mIsBackground = builder.isBackground;
        mImageResOnLoading = builder.imageResOnLoading;
        mImageResOnFail = builder.imageResOnFail;
        mLoadingImageScaleType = builder.loadingImageScaleType;
        mFailImageScaleType = builder.failImageScaleType;
        mImageScaleType = builder.imageScaleType;
        mFadeDuration = builder.fadeDuration;
        mBitmapConfig = builder.bitmapConfig;
        mIsGrayscale = builder.isGrayscale;
        mIsIgnoreImageView = builder.isIgnoreImageView;
        mIsBlur = builder.isBlur;
        mBlurRadius = builder.blurRadius;
        mIsResetView = builder.isResetView;
        mOnLoadEndCallback = builder.onLoadEndCallback;
        mOnLoadEnd = builder.onLoadEnd;
        mIsProcessAsync = builder.isProcessAsync;
        mResize = builder.resize;
        mPostprocessor = builder.postprocessor;
        mAutoPlayAnimations = builder.mAutoPlayAnimations;
    }

    public boolean isRounded() {
        return mIsRounded;
    }

    public RoundedType getRoundedType() {
        return mRoundedType;
    }

    public float getRoundedRadius() {
        return mRoundedTopLeft;
    }

    public float getRoundedTopLeft() {
        return mRoundedTopLeft;
    }


    public int getOverlayColor() {
        return mOverLayColor;
    }

    public float getRoundedTopRight() {
        return mRoundedTopRight;
    }

    public float getRoundedBottomLeft() {
        return mRoundedBottomLeft;
    }

    public float getRoundedBottomRight() {
        return mRoundedBottomRight;
    }

    public float getRoundedBorderWidth() {
        return mRoundedBorderWidth;
    }

    public int getRoundedBorderColor() {
        return mRoundedBorderColor;
    }

    public boolean isBackground() {
        return mIsBackground;
    }

    public boolean isGrayscale() {
        return mIsGrayscale;
    }

    public int getImageResOnLoading() {
        return mImageResOnLoading;
    }

    public int getImageResOnFail() {
        return mImageResOnFail;
    }

    public Postprocessor getPostprocessor() {
        return mPostprocessor;
    }

    public ImageView.ScaleType getLoadingImageScaleType() {
        return mLoadingImageScaleType;
    }

    public ImageView.ScaleType getFailImageScaleType() {
        return mFailImageScaleType;
    }

    public int getFadeDuration() {
        return mFadeDuration;
    }

    public Bitmap.Config getBitmapConfig() {
        return mBitmapConfig;
    }

    public Bitmap getImageOnLoading(Resources res) {
        if (mCachedImageOnLoading == null) {
            Bitmap bitmap = mImageResOnLoading != 0 ? BitmapGenerator.decodeResource(res, mImageResOnLoading) : null;
            mCachedImageOnLoading = ImageHelper.roundedIfNeeded(res, bitmap, this);
        }

        return mCachedImageOnLoading;
    }

    public Bitmap getImageOnFail(Resources res) {
        if (mCachedImageOnFail == null) {
            Bitmap bitmap = mImageResOnFail != 0 ? BitmapGenerator.decodeResource(res, mImageResOnFail) : null;
            mCachedImageOnFail = ImageHelper.roundedIfNeeded(res, bitmap, this);
        }

        return mCachedImageOnFail;
    }

    public boolean isIgnoreImageView() {
        return mIsIgnoreImageView;
    }

    public boolean isBlur() {
        return mIsBlur;
    }

    public boolean isResetView() {
        return mIsResetView;
    }

    public int getBlurRadius() {
        return mBlurRadius;
    }

    public Resize getResize() {
        return mResize;
    }

    public Callback<Bitmap> onLoadEndCallback() {
        return mOnLoadEndCallback;
    }

    public void setOnLoadEndCallback(Callback<Bitmap> onLoadEndCallback) {
        this.mOnLoadEndCallback = onLoadEndCallback;
    }

    public OnLoadEnd onLoadEnd() {
        return mOnLoadEnd;
    }

    public boolean isProcessAsync() {
        return mIsProcessAsync;
    }

    public boolean isAutoPlayAnimations() {
        return mAutoPlayAnimations;
    }

    public void setAutoPlayAnimations(boolean autoPlayAnimations) {
        this.mAutoPlayAnimations = autoPlayAnimations;
    }

    public ImageView.ScaleType getImageScaleType() {
        return mImageScaleType;
    }

    public static class Builder {
        private boolean isRounded;
        private RoundedType roundedType;
        private Resize resize;

        private float mRoundedTopLeft;
        private float mRoundedTopRight;
        private float mRoundedBottomLeft;
        private float mRoundedBottomRight;

        private int mOverlayColor = 0x00FFFFFF;

        private float roundedBorderWidth;
        private int roundedBorderColor;
        private boolean isBackground;
        private int imageResOnLoading;
        private int imageResOnFail;
        private ImageView.ScaleType loadingImageScaleType;
        private ImageView.ScaleType failImageScaleType;
        private ImageView.ScaleType imageScaleType = ImageView.ScaleType.CENTER_CROP;
        private int fadeDuration = -1;
        private Bitmap.Config bitmapConfig;
        private boolean isGrayscale;
        private boolean isIgnoreImageView;
        private boolean isBlur;
        private boolean isResetView = true;
        private int blurRadius;
        private Callback<Bitmap> onLoadEndCallback;
        private Postprocessor postprocessor;

        private boolean isProcessAsync;
        private boolean mAutoPlayAnimations;//新增动画自动播放配置 v_0.30.0 change by:GuosngBai
        private OnLoadEnd onLoadEnd;

        public Builder isRounded(boolean isRounded) {
            this.isRounded = isRounded;
            return this;
        }

        public Builder setRoundedType(RoundedType roundedType) {
            this.roundedType = roundedType;
            return this;
        }

        public Builder setRoundedRadius(float roundedRadius) {
            this.mRoundedTopLeft = roundedRadius;
            this.mRoundedTopRight = roundedRadius;
            this.mRoundedBottomLeft = roundedRadius;
            this.mRoundedBottomRight = roundedRadius;
            return this;
        }

        public Builder setRoundedRadius(float roundedTopLeft, float roundedTopRight, float roundedBottomRight, float roundedBottomLeft) {
            this.mRoundedTopLeft = roundedTopLeft;
            this.mRoundedTopRight = roundedTopRight;
            this.mRoundedBottomLeft = roundedBottomLeft;
            this.mRoundedBottomRight = roundedBottomRight;
            return this;
        }

        public Builder setPostprocessor(Postprocessor postprocessor) {
            this.postprocessor = postprocessor;
            return this;
        }

        public Builder setOverlayColor(int overlayColor) {
            this.mOverlayColor = overlayColor;
            return this;
        }

        public Builder setRoundedTopLeft(float roundedTopLeft) {
            mRoundedTopLeft = roundedTopLeft;
            return this;
        }

        public Builder setRoundedTopRight(float roundedTopRight) {
            mRoundedTopRight = roundedTopRight;
            return this;
        }

        public Builder setRoundedBottomLeft(float roundedBottomLeft) {
            mRoundedBottomLeft = roundedBottomLeft;
            return this;
        }

        public Builder setRoundedBottomRight(float roundedBottomRight) {
            mRoundedBottomRight = roundedBottomRight;
            return this;
        }

        public Builder setRoundedBorderWidth(float roundedBorderWidth) {
            this.roundedBorderWidth = roundedBorderWidth;
            return this;
        }

        public Builder setRoundedBorderColor(int roundedBorderColor) {
            this.roundedBorderColor = roundedBorderColor;
            return this;
        }

        public Builder isBackground(boolean isBackground) {
            this.isBackground = isBackground;
            return this;
        }

        public Builder isGrayscale(boolean isGrayscale) {
            this.isGrayscale = isGrayscale;
            return this;
        }

        public Builder showImageOnLoading(int imageRes) {
            this.imageResOnLoading = imageRes;
            return this;
        }

        public Builder showImageOnFail(int imageRes) {
            this.imageResOnFail = imageRes;
            return this;
        }

        public Builder loadingImageScaleType(ImageView.ScaleType loadingImageScaleType) {
            this.loadingImageScaleType = loadingImageScaleType;
            return this;
        }

        public Builder failImageScaleType(ImageView.ScaleType failImageScaleType) {
            this.failImageScaleType = failImageScaleType;
            return this;
        }

        public Builder fadeDuration(int duration) {
            fadeDuration = duration;
            return this;
        }

        public Builder setBitmapConfig(Bitmap.Config config) {
            bitmapConfig = config;
            return this;
        }

        public Builder isIgnoreImageView(boolean isIgnore) {
            this.isIgnoreImageView = isIgnore;
            return this;
        }

        public Builder isBlur(boolean isBlur) {
            this.isBlur = isBlur;
            return this;
        }

        public Builder isResetView(boolean isResetView) {
            this.isResetView = isResetView;
            return this;
        }

        public Builder blurRadius(int blurRadius) {
            this.blurRadius = blurRadius;
            return this;
        }

        public Builder resizeTo(int width, int height) {
            resize = new Resize(width, height);
            return this;
        }

        public Builder onLoadEndCallback(Callback<Bitmap> runnable) {
            this.onLoadEndCallback = runnable;
            return this;
        }

        public Builder onLoadEnd(OnLoadEnd onLoadEnd) {
            this.onLoadEnd = onLoadEnd;
            return this;
        }

        public Builder isProcessAsync(boolean isProcessAsync) {
            this.isProcessAsync = isProcessAsync;
            return this;
        }

        public ImageOptions build() {
            return new ImageOptions(this);
        }

        public boolean isAutoPlayAnimations() {
            return mAutoPlayAnimations;
        }

        public void setAutoPlayAnimations(boolean autoPlayAnimations) {
            this.mAutoPlayAnimations = autoPlayAnimations;
        }

        public ImageView.ScaleType getImageScaleType() {
            return imageScaleType;
        }

        public void setImageScaleType(ImageView.ScaleType imageScaleType) {
            this.imageScaleType = imageScaleType;
        }
    }

    public enum RoundedType {
        Full, Corner
    }

    public static class Resize {
        public int width;
        public int height;

        public Resize(int width, int height) {
            this.width = width;
            this.height = height;
        }
    }

    public interface OnLoadEnd {
        public void onLoadEnd(int result, int width, int height);
    }
}


