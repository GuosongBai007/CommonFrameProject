package com.base.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.base.imageframe.utils.FrescoHelper;
import com.base.imageframe.utils.ImageOptions;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.generic.GenericDraweeHierarchyInflater;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Author:GuosongBai
 * Date:2018/10/31 09:50
 * Description:
 */
public class RecyclingImageView extends SimpleDraweeView {

    private Drawable mDrawable;
    private GenericDraweeHierarchyBuilder mHierarchyBuilder;
    private ImageOptions mImageOptions;

    public RecyclingImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
    }

    public RecyclingImageView(Context context) {
        super(context);
    }

    public RecyclingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclingImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecyclingImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void inflateHierarchy(Context context, AttributeSet attrs) {
        mHierarchyBuilder = GenericDraweeHierarchyInflater.inflateBuilder(context, attrs);
        if (getDrawable() != null) {
            mHierarchyBuilder.setPlaceholderImage(getDrawable(), FrescoHelper.mapScaleType(getScaleType()));
        }
        mHierarchyBuilder.setActualImageScaleType(FrescoHelper.mapScaleType(getScaleType()));
        setAspectRatio(mHierarchyBuilder.getDesiredAspectRatio());
        GenericDraweeHierarchy hierarchy = mHierarchyBuilder.build();
        setHierarchy(hierarchy);
    }

    @Override
    protected void onDetachedFromWindow() {
        //setImageDrawable(null);
        setBackgroundDrawable(null);
        super.onDetachedFromWindow();
    }

    @Override
    public Drawable getDrawable() {
        if (mDrawable != null) {
            return mDrawable;
        }
        return super.getDrawable();
    }

    @Override
    public void setImageDrawable(final Drawable drawable) {
        mDrawable = drawable;
        mImageOptions = null;
        if (drawable == null) {
            super.setImageDrawable(drawable);
        }
        try {
            mHierarchyBuilder.setPlaceholderImage(drawable, getHierarchy().getActualImageScaleType());
            getHierarchy().setPlaceholderImage(drawable, getHierarchy().getActualImageScaleType());
            mHierarchyBuilder.setRoundingParams(null);
            getHierarchy().setRoundingParams(null);

            setHierarchy(getHierarchy());
            setController(null);
        } catch (NullPointerException e) {
            //init过程中，Drawee相关的变量还未初始化会造成空指针
            //super.setImageDrawable(drawable);
        }
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        setImageDrawable(new BitmapDrawable(getResources(), bm));
    }

    @Override
    public boolean isOpaque() {
        return true;
    }

    @Override
    public void setImageResource(int resId) {
        setImageDrawable(getResources().getDrawable(resId));
    }

    @Override
    public void setScaleType(final ScaleType scaleType) {
        super.setScaleType(scaleType);
        try {
            getHierarchy().setActualImageScaleType(FrescoHelper.mapScaleType(scaleType));
            getHierarchy().setPlaceholderImage(mHierarchyBuilder.getPlaceholderImage(), FrescoHelper.mapScaleType(scaleType));
            setHierarchy(getHierarchy());
        } catch (NullPointerException e) {
            //init过程中，Drawee相关的变量还未初始化会造成空指针
        }
    }

    public ImageOptions getImageOptions() {
        return mImageOptions;
    }

    public void setImageOptions(ImageOptions imageOptions) {
        mImageOptions = imageOptions;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        try {
            super.onDraw(canvas);
        } catch (Exception e) {
        }

    }
}

