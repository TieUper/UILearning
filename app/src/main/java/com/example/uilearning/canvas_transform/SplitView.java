package com.example.uilearning.canvas_transform;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.example.uilearning.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 小球爆炸动画
 */
public class SplitView extends View {

    /**
     * 所以小球的集合
     */
    private List<Ball> mBallList = new ArrayList<>();
    /**
     * 粒子直径
     */
    private float d = 3;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;

    public SplitView(Context context) {
        this(context, null);
    }

    public SplitView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SplitView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 小球初始化
     */
    private void init() {
        mPaint = new Paint();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.pic);
        for (int i = 0; i < bitmap.getWidth(); i++) {
            for (int j = 0; j < bitmap.getHeight(); j++) {
                Ball ball = new Ball();
                ball.color = bitmap.getPixel(i, j);
                ball.x = i * d + d / 2;
                ball.y = j * d + d / 2;
                ball.r = d / 2;

                //速度(-20,20)
                ball.vX = (float) (Math.pow(-1, Math.ceil(Math.random() * 1000)) * 20 * Math.random());
                ball.vY = rangInt(-15, 35);
                //加速度
                ball.aX = 0;
                ball.aY = 0.98f;

                mBallList.add(ball);
            }
        }

        mValueAnimator = ValueAnimator.ofFloat(0, 1);
        mValueAnimator.setRepeatCount(-1);
        mValueAnimator.setDuration(2000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                updateBall();
                invalidate();
            }
        });
    }

    /**
     * 更新粒子位置
     */
    private void updateBall() {
        for (Ball ball :
                mBallList) {
            ball.x += ball.vX;
            ball.y += ball.vY;

            ball.vX += ball.aX;
            ball.vY += ball.aY;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(500,500);
        for (Ball ball : mBallList) {
            mPaint.setColor(ball.color);
            canvas.drawCircle(ball.x,ball.y,ball.r,mPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        System.out.println("-------------------------我被调用了");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //执行动画
            mValueAnimator.start();
        }
        return super.onTouchEvent(event);
    }

    private int rangInt(int i, int j) {
        int max = Math.max(i, j);
        int min = Math.min(i, j) - 1;
        //在0到(max - min)范围内变化，取大于x的最小整数 再随机
        return (int) (min + Math.ceil(Math.random() * (max - min)));
    }
}
