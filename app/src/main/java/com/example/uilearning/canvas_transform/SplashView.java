package com.example.uilearning.canvas_transform;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.example.uilearning.R;

public class SplashView extends View {

    /**
     * 旋转圆的画笔
     */
    private Paint mPaint;
    //扩散圆的画笔
    private Paint mHolePaint;
    /**
     * 背景色
     */
    private int mBackgroundColor = Color.WHITE;
    private int[] mCircleColors;
    //当前大圆的旋转角度
    private float mCurrentRotateAngle = 0F;
    //旋转大圆的半径
    private float mRotateRadius = 90;
    //6个小球的半径
    private float mCircleRadius = 18;
    //当前大圆的半径
    private float mCurrentRotateRadius = mRotateRadius;
    //表示旋转动画的时长
    private int mRotateDuration = 1200;

    //表示旋转圆的中心坐标
    private float mCenterX;
    private float mCenterY;
    private ValueAnimator mValueAnimator;
    //表示斜对角线长度的一半,扩散圆最大半径
    private float mDistance;
    //扩散圆的半径
    private float mCurrentHoleRadius = 0F;

    public SplashView(Context context) {
        this(context,null);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SplashView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     */
    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCircleColors = context.getResources().getIntArray(R.array.splash_circle_colors);

        mHolePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mHolePaint.setStyle(Paint.Style.STROKE);
        mHolePaint.setColor(Color.RED);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w * 1f / 2;
        mCenterY = h * 1f / 2;
        mDistance = (float) (Math.hypot(w, h) / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mState == null) {
            mState = new RotateState();
        }
        mState.drawState(canvas);
    }

    private SplashState mState;

    private abstract class SplashState{
        abstract void drawState(Canvas canvas);
    }

    //1.旋转
    private class RotateState extends SplashState{

        public RotateState() {
            mValueAnimator = ValueAnimator.ofFloat(0, (float) (Math.PI * 2));
            mValueAnimator.setRepeatCount(2);
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateAngle = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new MergeState();
                }
            });

            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            //绘制背景
            drawBackground(canvas);
            //绘制小球
            drawCircles(canvas);
        }
    }

    //2.扩散聚合
    private class MergeState extends SplashState{

        public MergeState() {
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mRotateRadius);
            mValueAnimator.setDuration(mRotateDuration);
            mValueAnimator.setInterpolator(new OvershootInterpolator(10f));
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentRotateRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    mState = new ExpandState();
                }
            });
            mValueAnimator.reverse();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
            drawCircles(canvas);
        }
    }

    //3.水波纹
    private class ExpandState extends SplashState{

        public ExpandState() {
            mValueAnimator = ValueAnimator.ofFloat(mCircleRadius, mDistance);
            mValueAnimator.setDuration(10000);
            mValueAnimator.setInterpolator(new LinearInterpolator());
            mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mCurrentHoleRadius = (float) animation.getAnimatedValue();
                    invalidate();
                }
            });
            mValueAnimator.start();
        }

        @Override
        void drawState(Canvas canvas) {
            drawBackground(canvas);
        }
    }

    /**
     * 绘制6个小球
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        float rotateAngle = (float) ((2 * Math.PI) / mCircleColors.length);
        for (int i = 0; i < mCircleColors.length; i++) {
            mPaint.setColor(mCircleColors[i]);
            float x = (float) (Math.cos(rotateAngle * i + mCurrentRotateAngle) * mCurrentRotateRadius + mCenterX);
            float y = (float) (Math.sin(rotateAngle * i + mCurrentRotateAngle) * mCurrentRotateRadius + mCenterY);
            canvas.drawCircle(x,y,mCircleRadius,mPaint);
        }
    }

    /**
     * 绘制背景
     * @param canvas
     */
    private void drawBackground(Canvas canvas) {
        if (mCurrentHoleRadius > 0) {
            float strokeWidth = mDistance - mCurrentHoleRadius;
            float radius = strokeWidth / 2 + mCurrentHoleRadius;
            mHolePaint.setStrokeWidth(strokeWidth);
            canvas.drawCircle(mCenterX,mCenterY,radius,mHolePaint);
            System.out.println("---------------------strokeWidth:" + strokeWidth + " radius:" + radius);
        }else {
            canvas.drawColor(mBackgroundColor);
        }
    }
}
