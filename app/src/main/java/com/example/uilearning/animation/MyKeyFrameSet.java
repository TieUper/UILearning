package com.example.uilearning.animation;

import android.animation.FloatEvaluator;
import android.animation.Keyframe;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class MyKeyFrameSet {

    private final MyKeyFrame mFirstKeyFrame;
    private final List<MyKeyFrame> mMyKeyFrames;
    /**
     * 估值器，用于计算两个关键帧之间的百分比数据
     */
    private final FloatEvaluator mFloatEvaluator;
    private final MyKeyFrame mLastKeyFrame;

    public MyKeyFrameSet(MyKeyFrame... myKeyFrames) {
        mFirstKeyFrame = myKeyFrames[0];
        mLastKeyFrame = myKeyFrames[myKeyFrames.length - 1];
        mMyKeyFrames = Arrays.asList(myKeyFrames);
        mFloatEvaluator = new FloatEvaluator();
    }


    public static MyKeyFrameSet ofFloat(float... values) {
        int length = values.length;
        MyKeyFrame[] keyFrames = new MyKeyFrame[Math.max(length,2)];
        if (length == 1) {
            keyFrames[0] = new MyKeyFrame(0, 1f);
            keyFrames[1] = new MyKeyFrame(1f, values[0]);
        }else {
            keyFrames[0] = new MyKeyFrame(0, values[0]);
            for (int i = 1; i < length; i++) {
                float value = values[i];
                keyFrames[i] = new MyKeyFrame((float) i / (length - 1), value);
            }
        }
        return new MyKeyFrameSet(keyFrames);
    }

    public float getValue(float fraction) {
        MyKeyFrame prevKeyFrame = mFirstKeyFrame;
        for (int i = 1; i < mMyKeyFrames.size(); i++) {
            MyKeyFrame nextKeyFrame = mMyKeyFrames.get(i);
            if (fraction < nextKeyFrame.fraction) {
                return mFloatEvaluator.evaluate(fraction,prevKeyFrame.mValue,nextKeyFrame.mValue);
            }
            prevKeyFrame = nextKeyFrame;
        }
        return 0;
    }
}
