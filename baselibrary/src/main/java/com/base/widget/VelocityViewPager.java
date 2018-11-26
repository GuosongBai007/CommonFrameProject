package com.base.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Author:GuosongBai
 * Date:2018/10/10 14:11
 * Description:当自动切换页面时，速度可变化的ViewPager
 */
public class VelocityViewPager extends ViewPager {
    private Field mScrollerField = null;// 通过反射获取父类的scroller
    private FixedSpeedScroller mScroller = null;// 要替换的mScroller

    /**
     * 控制是否可以滑动切换
     */
    private boolean mIsCanScroll = true;

    public VelocityViewPager(@NonNull Context context) {
        super(context);
        initScroller();
    }

    public VelocityViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initScroller();
    }

    private void initScroller() {
        try {
            mScroller = new FixedSpeedScroller(getContext());
            mScrollerField = ViewPager.class.getField("mScroller");
            mScrollerField.setAccessible(true);
            mScrollerField.set(this, mScroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return mIsCanScroll && super.onTouchEvent(ev);
    }

    public void setDuration(int duration) {
        mScroller.setDuration(duration);
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return mIsCanScroll && super.canScrollVertically(direction);
    }

    public void setCanScroll(boolean canScroll) {
        mIsCanScroll = canScroll;
    }

    public static class FixedSpeedScroller extends Scroller {

        private int mDuration = 5000;
        private static final Interpolator sInterpolator = t -> {
            t -= 1.0f;
            return t * t * t * t * t + 1.0f;
        };

        public FixedSpeedScroller(Context context) {
            super(context, sInterpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator, boolean flywheel) {
            super(context, interpolator, flywheel);
        }

        public void setDuration(int duration) {
            mDuration = duration;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }

}
