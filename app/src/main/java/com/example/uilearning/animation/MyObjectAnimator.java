package com.example.uilearning.animation;

import android.view.View;

import java.lang.ref.WeakReference;

public class MyObjectAnimator implements VSYNCManager.AnimationFrameCallback {

    private final MyFloatPropertyValuesHolder mValuesHolder;
    private WeakReference<View> target;
    private long mDuration = 0;
    int index = 0;
    private LineInterceptor mInterceptor = new LineInterceptor();

    private MyObjectAnimator(View view, String propertyName, float... values) {
        target = new WeakReference<>(view);
        mValuesHolder = new MyFloatPropertyValuesHolder(propertyName, values);
    }

    public static MyObjectAnimator ofFloat(View view, String propertyName, float... values) {
        return new MyObjectAnimator(view, propertyName, values);
    }

    public void start() {
        mValuesHolder.setUpTargetView();
        VSYNCManager.getInstance().setAnimationFrameCallback(this);
    }

    @Override
    public boolean doAnimationFrame(long currentTime) {
        float total = mDuration / 16;
        float fraction = (index++) / total;
        if (mInterceptor != null) {
            fraction =  mInterceptor.getInterpolation(fraction);
        }
        if (index >= total) {
            index = 0;
        }
        mValuesHolder.setAnimatedValue(target.get(),fraction);
        return false;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }
}
