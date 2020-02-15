package com.example.uilearning.animation;

import android.icu.text.CaseMap;
import android.view.View;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MyFloatPropertyValuesHolder {

    private final String mPropertyName;
    Class mValueType;
    MyKeyFrameSet mMyKeyFrameSet;
    private Method mSetter;

    public MyFloatPropertyValuesHolder(String propertyName, float... values) {
        mPropertyName = propertyName;
        mValueType = float.class;
        mMyKeyFrameSet = MyKeyFrameSet.ofFloat(values);
    }

    public void setUpTargetView() {
        String s = String.valueOf(mPropertyName.charAt(0)).toUpperCase();
        String methodName = "set" + s + mPropertyName.substring(1);
        try {
            mSetter = View.class.getMethod(methodName, mValueType);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置Value的属性
     * @param view  当前view
     * @param fraction  当前的变化百分比
     */
    public void setAnimatedValue(View view, float fraction) {
       float value =  mMyKeyFrameSet.getValue(fraction);
        try {
            mSetter.invoke(view, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
