package com.base.widget.newtablelayout;

import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

/**
 * 使用Runnable实现动画的通知，主要是在Build.VERSION.SDK_INT<12（HONEYCOMB_MR1）的时候使用
 * 创建请参考{@link AnimationUtils#DEFAULT_ANIMATOR_CREATOR}
 *
 * Created by GuosongBai
 * date:2018/10/15.
 */
class ValueAnimatorCompatImplEclairMr1 extends ValueAnimatorCompat.Impl {

    private static final int HANDLER_DELAY = 10;
    private static final int DEFAULT_DURATION = 200;

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private long mStartTime;
    private boolean mIsRunning;

    private final int[] mIntValues = new int[2];
    private final float[] mFloatValues = new float[2];

    private int mDuration = DEFAULT_DURATION;
    private Interpolator mInterpolator;
    private AnimatorListenerProxy mListener;
    private AnimatorUpdateListenerProxy mUpdateListener;

    private float mAnimatedFraction;

    @Override
    public void start() {
        if (mIsRunning) {//如果已经运行，忽略
            return;
        }

        if (mInterpolator == null) {
            mInterpolator = new AccelerateDecelerateInterpolator();
        }

        mStartTime = SystemClock.uptimeMillis();
        mIsRunning = true;

        if (mListener != null) {
            mListener.onAnimationStart();
        }

        sHandler.postDelayed(mRunnable, HANDLER_DELAY);
    }

    @Override
    public boolean isRunning() {
        return mIsRunning;
    }

    @Override
    public void setInterpolator(Interpolator interpolator) {
        mInterpolator = interpolator;
    }

    @Override
    public void setListener(AnimatorListenerProxy listener) {
        mListener = listener;
    }

    @Override
    public void setUpdateListener(AnimatorUpdateListenerProxy updateListener) {
        mUpdateListener = updateListener;
    }

    @Override
    public void setIntValues(int from, int to) {
        mIntValues[0] = from;
        mIntValues[1] = to;
    }

    @Override
    public int getAnimatedIntValue() {
        return AnimationUtils.lerp(mIntValues[0], mIntValues[1], getAnimatedFraction());
    }

    @Override
    public void setFloatValues(float from, float to) {
        mFloatValues[0] = from;
        mFloatValues[1] = to;
    }

    @Override
    public float getAnimatedFloatValue() {
        return AnimationUtils.lerp(mFloatValues[0], mFloatValues[1], getAnimatedFraction());
    }

    @Override
    public void setDuration(int duration) {
        mDuration = duration;
    }

    @Override
    public void cancel() {
        mIsRunning = false;
        sHandler.removeCallbacks(mRunnable);

        if (mListener != null) {
            mListener.onAnimationCancel();
        }
    }

    @Override
    public float getAnimatedFraction() {
        return mAnimatedFraction;
    }

    @Override
    public void end() {
        if (mIsRunning) {
            mIsRunning = false;
            sHandler.removeCallbacks(mRunnable);

            // 重置fraction
            mAnimatedFraction = 1f;

            if (mUpdateListener != null) {
                mUpdateListener.onAnimationUpdate();
            }

            if (mListener != null) {
                mListener.onAnimationEnd();
            }
        }
    }

    @Override
    public long getDuration() {
        return mDuration;
    }

    private void update() {
        if (mIsRunning) {
            // 更新fraction
            final long elapsed = SystemClock.uptimeMillis() - mStartTime;
            final float linearFraction = elapsed / (float) mDuration;
            mAnimatedFraction = mInterpolator != null
                    ? mInterpolator.getInterpolation(linearFraction)
                    : linearFraction;

            // 通知
            if (mUpdateListener != null) {
                mUpdateListener.onAnimationUpdate();
            }

            // 检查动画是否执行完毕
            if (SystemClock.uptimeMillis() >= (mStartTime + mDuration)) {
                mIsRunning = false;

                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }
        }

        if (mIsRunning) {
            sHandler.postDelayed(mRunnable, HANDLER_DELAY);
        }
    }

    private final Runnable mRunnable = new Runnable() {
        public void run() {
            update();
        }
    };
}
