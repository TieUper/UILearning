package com.example.uilearning.splash;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.uilearning.R;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;

public class ParallaxContainer extends FrameLayout implements ViewPager.OnPageChangeListener {

    private List<ParallaxFragment> mFragmentList;

    ParallaxPagerAdapter adapter;

    private ImageView iv_man;
    public void setIv_man(ImageView iv_man) {
        this.iv_man = iv_man;
    }

    public ParallaxContainer(@NonNull Context context) {
        this(context,null);
    }

    public ParallaxContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        System.out.println("-------------------------" + position);
        //动画
        int containerWidth = getWidth();
        ParallaxFragment outFragment = null;
        try {
            outFragment = mFragmentList.get(position - 1);
        } catch (Exception e) {

        }
        //获取到推出的页面
        ParallaxFragment inFragment = null;

        try {
            inFragment = mFragmentList.get(position);
        }catch (Exception e){}

        if (outFragment != null) {
            //获取Fragment上所有的视图，实现动画效果
            List<View> outViews = outFragment.getParallaxViews();

//            if (outViews != null) {
//                for (View view : outViews) {
//                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag();
//                    if (tag == null) {
//                        continue;
//                    }
//                    ViewHelper.setTranslationX(view,(containerWidth - positionOffsetPixels) * tag.xOut);
//                    ViewHelper.setTranslationY(view,(containerWidth - positionOffsetPixels) * tag.yOut);
//                }
//            }
        }

        if (inFragment != null) {
            List<View> inViews = inFragment.getParallaxViews();
            if (inViews != null) {
                for (View view : inViews) {
                    ParallaxViewTag tag = (ParallaxViewTag) view.getTag(R.id.parallax_view_tag);
                    if (tag == null) {
                        continue;
                    }
                    ViewHelper.setTranslationX(view, (0 - positionOffsetPixels) * tag.xIn);
                    ViewHelper.setTranslationY(view, (0 - positionOffsetPixels) * tag.yIn);
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (position == adapter.getCount() - 1) {
            iv_man.setVisibility(INVISIBLE);
        }else{
            iv_man.setVisibility(VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        AnimationDrawable animation = (AnimationDrawable) iv_man.getBackground();
        switch (state) {
            case ViewPager.SCROLL_STATE_DRAGGING:
                animation.start();
                break;

            case ViewPager.SCROLL_STATE_IDLE:
                animation.stop();
                break;

            default:
                break;
        }
    }

    public void setUp(int... childIds) {
        mFragmentList = new ArrayList<>();
        for (int i = 0; i < childIds.length; i++) {
            ParallaxFragment parallaxFragment = new ParallaxFragment();

            Bundle args = new Bundle();
            args.putInt("layoutId", childIds[i]);
            parallaxFragment.setArguments(args);
            mFragmentList.add(parallaxFragment);
        }
        ViewPager viewPager = new ViewPager(getContext());
        viewPager.setId(R.id.parallax_pager);
        //实例化适配器
        FragmentActivity activity = (FragmentActivity) getContext();
        adapter = new ParallaxPagerAdapter(activity.getSupportFragmentManager(), mFragmentList);
        viewPager.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(this);
        addView(viewPager,0);
    }
}
