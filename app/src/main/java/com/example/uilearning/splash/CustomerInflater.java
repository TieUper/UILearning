package com.example.uilearning.splash;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.uilearning.R;

import java.lang.reflect.Constructor;

public class CustomerInflater extends LayoutInflater {


    private final ParallaxFragment mFragment;

    protected CustomerInflater(LayoutInflater original, Context newContext, ParallaxFragment fragment) {
        super(original, newContext);
        mFragment = fragment;
        setFactory2(new ParallaxFactory());
    }

    @Override
    public LayoutInflater cloneInContext(Context newContext) {
        return null;
    }

    class ParallaxFactory implements Factory2{

        private String[] sClassPrefix = {"android.widget.","android.view."};

        int[] attrIds = {
                R.attr.a_in,
                R.attr.a_out,
                R.attr.x_in,
                R.attr.x_out,
                R.attr.y_in,
                R.attr.y_out};

        @Nullable
        @Override
        public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            View view = null;
            view = createMyView(name,attrs);
            if (view != null) {
                TypedArray array = context.obtainStyledAttributes(attrs, attrIds);
                if (array != null && array.length() > 0) {
                    ParallaxViewTag tag = new ParallaxViewTag();
                    tag.alphaIn = array.getFloat(0, 0);
                    tag.alphaOut = array.getFloat(1, 0);
                    tag.xIn = array.getFloat(2, 0);
                    tag.xOut = array.getFloat(3, 0);
                    tag.yIn = array.getFloat(4, 0);
                    tag.yOut = array.getFloat(5, 0);
                    view.setTag(tag);
                }
                mFragment.getParallaxViews().add(view);
                array.recycle();
            }
            return view;
        }

        private View createMyView(String name, AttributeSet attrs) {
            if (name.contains(".")) {
                return reflectView(name, null, attrs);
            }else {
                for (String prefix:
                     sClassPrefix) {
                    View view = reflectView(name, prefix, attrs);
                    if (view != null) {
                        return view;
                    }
                }
                return null;
            }
        }

        private View reflectView(String name, String prefix, AttributeSet attrs) {
            try {
                return createView(name, prefix, attrs);
            } catch (ClassNotFoundException e) {
                return null;
            }
//            try {
//                Class<?> aClass = Class.forName(name);
//                Constructor<?> constructor = aClass.getConstructor(Context.class, AttributeSet.class);
//                constructor.newInstance(context, attrs);
//            } catch (ClassNotFoundException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }

        }

        @Nullable
        @Override
        public View onCreateView(@NonNull String name, @NonNull Context context, @NonNull AttributeSet attrs) {
            return null;
        }
    }
}
