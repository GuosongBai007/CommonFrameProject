package com.base.widget.newtablelayout;

import android.view.animation.Interpolator;

/**
 * .封装了部分 {@code ValueAnimator} 的API
 * Created by GuosongBai
 * date:2018/10/15.
 */
class ValueAnimatorCompat {

    interface AnimatorUpdateListener {
        /**
         * <p>动画更新回调</p>
         *
         * @param animator
         */
        void onAnimationUpdate(ValueAnimatorCompat animator);
    }

    interface AnimatorListener {
        /**
         * <p>动画开始</p>
         *
         * @param animator
         */
        void onAnimationStart(ValueAnimatorCompat animator);
        /**
         * <p>动画结束</p>
         *
         * @param animator
         */
        void onAnimationEnd(ValueAnimatorCompat animator);
        /**
         * <p>动画取消</p>
         *
         * @param animator
         */
        void onAnimationCancel(ValueAnimatorCompat animator);
    }

    static class AnimatorListenerAdapter implements AnimatorListener {
        @Override
        public void onAnimationStart(ValueAnimatorCompat animator) {
        }

        @Override
        public void onAnimationEnd(ValueAnimatorCompat animator) {
        }

        @Override
        public void onAnimationCancel(ValueAnimatorCompat animator) {
        }
    }

    interface Creator {
        ValueAnimatorCompat createAnimator();
    }

    static abstract class Impl {
        interface AnimatorUpdateListenerProxy {
            void onAnimationUpdate();
        }

        interface AnimatorListenerProxy {
            void onAnimationStart();
            void onAnimationEnd();
            void onAnimationCancel();
        }

        abstract void start();
        abstract boolean isRunning();
        abstract void setInterpolator(Interpolator interpolator);
        abstract void setListener(AnimatorListenerProxy listener);
        abstract void setUpdateListener(AnimatorUpdateListenerProxy updateListener);
        abstract void setIntValues(int from, int to);
        abstract int getAnimatedIntValue();
        abstract void setFloatValues(float from, float to);
        abstract float getAnimatedFloatValue();
        abstract void setDuration(int duration);
        abstract void cancel();
        abstract float getAnimatedFraction();
        abstract void end();
        abstract long getDuration();
    }

    private final Impl mImpl;

    ValueAnimatorCompat(Impl impl) {
        mImpl = impl;
    }

    public void start() {
        mImpl.start();
    }

    public boolean isRunning() {
        return mImpl.isRunning();
    }

    public void setInterpolator(Interpolator interpolator) {
        mImpl.setInterpolator(interpolator);
    }

    public void setUpdateListener(final AnimatorUpdateListener updateListener) {
        if (updateListener != null) {
            mImpl.setUpdateListener(() -> updateListener.onAnimationUpdate(ValueAnimatorCompat.this));
        } else {
            mImpl.setUpdateListener(null);
        }
    }

    public void setListener(final AnimatorListener listener) {
        if (listener != null) {
            mImpl.setListener(new Impl.AnimatorListenerProxy() {
                @Override
                public void onAnimationStart() {
                    listener.onAnimationStart(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationEnd() {
                    listener.onAnimationEnd(ValueAnimatorCompat.this);
                }

                @Override
                public void onAnimationCancel() {
                    listener.onAnimationCancel(ValueAnimatorCompat.this);
                }
            });
        } else {
            mImpl.setListener(null);
        }
    }

    public void setIntValues(int from, int to) {
        mImpl.setIntValues(from, to);
    }

    public int getAnimatedIntValue() {
        return mImpl.getAnimatedIntValue();
    }

    public void setFloatValues(float from, float to) {
        mImpl.setFloatValues(from, to);
    }

    public float getAnimatedFloatValue() {
        return mImpl.getAnimatedFloatValue();
    }

    public void setDuration(int duration) {
        mImpl.setDuration(duration);
    }

    public void cancel() {
        mImpl.cancel();
    }

    public float getAnimatedFraction() {
        return mImpl.getAnimatedFraction();
    }

    public void end() {
        mImpl.end();
    }

    public long getDuration() {
        return mImpl.getDuration();
    }
}
