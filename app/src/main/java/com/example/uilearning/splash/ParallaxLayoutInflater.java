package com.example.uilearning.splash;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uilearning.R;

public class ParallaxLayoutInflater extends LayoutInflater {

    private ParallaxFragment mParallaxFragment;

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext) {
        super(original, newContext);
    }

    protected ParallaxLayoutInflater(LayoutInflater original, Context newContext, ParallaxFragment fragment) {
        super(original, newContext);
        mParallaxFragment = fragment;
        setFactory2(new ParallaxFactory(this));
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return new ParallaxLayoutInflater(this,newContext,mParallaxFragment);
    }

    class ParallaxFactory implements Factory2{

        private final String[] sClassPrefix = {
                "android.widget.",
                "android.view."
        };

        int[] attrIds = {
                R.attr.a_in,
                R.attr.a_out,
                R.attr.x_in,
                R.attr.x_out,
                R.attr.y_in,
                R.attr.y_out};

        private LayoutInflater inflater;

        public ParallaxFactory(LayoutInflater inflater) {
            this.inflater = inflater;
        }


        @Nullable
        @Override
        public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            View view = null;
            view = createMyView(name, context, attrs);
            if (view != null) {
                TypedArray array = context.obtainStyledAttributes(attrs, attrIds);
                if (array != null && array.length() > 0) {
                    //获取自定义属性的值
                    ParallaxViewTag tag = new ParallaxViewTag();
                    tag.alphaIn = array.getFloat(0, 0f);
                    tag.alphaOut = array.getFloat(1, 0f);
                    tag.xIn = array.getFloat(2, 0f);
                    tag.xOut = array.getFloat(3, 0f);
                    tag.yIn = array.getFloat(4, 0f);
                    tag.yOut = array.getFloat(5, 0f);
                    view.setTag(R.id.parallax_view_tag, tag);
                }
                mParallaxFragment.getParallaxViews().add(view);
                array.recycle();
            }
            return view;
        }


        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
           return null;
        }

        /**
         * 对于 name  系统控件 TextView
         *            自定义控件 全路径名  com.example.uilearing.MyView
         * @param name
         * @param context
         * @param attrs
         * @return
         */
        private View createMyView(String name, Context context, AttributeSet attrs) {
            if (name.contains(".")) {
                return reflectView(name, null, context, attrs);
            }else {
                for (String prefix : sClassPrefix) {
                    View view = reflectView(name, prefix, context, attrs);
                    //获取系统空间的自定义属性
                    if (view != null) {
                        return view;
                    }
                }
            }
            return null;
        }

        private View reflectView(String name, String prefix, Context context, AttributeSet attributeSet) {
            try {
                return inflater.createView(name, prefix, attributeSet);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }
}
