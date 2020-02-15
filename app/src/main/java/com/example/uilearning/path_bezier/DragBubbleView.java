package com.example.uilearning.path_bezier;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.PointFEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.example.uilearning.R;

public class DragBubbleView extends View {

    /**
     * 气泡默认状态--静止
     */
    private final int BUBBLE_STATE_DEFAULT = 0;
    /**
     * 气泡相连
     */
    private final int BUBBLE_STATE_CONNECT = 1;
    /**
     * 气泡分离
     */
    private final int BUBBLE_STATE_APART = 2;
    /**
     * 气泡消失
     */
    private final int BUBBLE_STATE_DISMISS = 3;
    /**
     * 气泡半径
     */
    private float mBubbleRadius;
    /**
     * 气泡颜色
     */
    private int mBubbleColor;
    /**
     * 气泡消息文字
     */
    private String mTextStr;
    /**
     * 气泡消息文字颜色
     */
    private int mTextColor;
    /**
     * 气泡消息文字大小
     */
    private float mTextSize;
    /**
     * 不动气泡的半径
     */
    private float mBubFixedRadius;
    /**
     * 可动气泡的半径
     */
    private float mBubMovableRadius;
    /**
     * 气泡相连状态最大圆心距离
     */
    private float mMaxDist;
    /**
     * 手指触摸偏移量
     */
    private final float MOVE_OFFSET;
    /**
     * 不动气泡的圆心
     */
    private PointF mBubFixedCenter;
    /**
     * 可动气泡的圆心
     */
    private PointF mBubMovableCenter;
    /**
     * 气泡的画笔
     */
    private Paint mBubblePaint;
    /**
     * 贝塞尔曲线path
     */
    private Path mBezierPath;

    private Paint mTextPaint;

    //文本绘制区域
    private Rect mTextRect;

    private Paint mBurstPaint;

    //爆炸绘制区域
    private Rect mBurstRect;

    /**
     * 两气泡圆心距离
     */
    private float mDist;

    /**
     * 气泡状态标志
     */
    private int mBubbleState = BUBBLE_STATE_DEFAULT;
    private ValueAnimator mValueAnimator;

    /**
     * 气泡爆炸的bitmap数组
     */
    private Bitmap[] mBurstBitmapsArray;
    /**
     * 是否在执行气泡爆炸动画
     */
    private boolean mIsBurstAnimStart = false;

    /**
     * 当前气泡爆炸图片index
     */
    private int mCurDrawableIndex;

    /**
     * 气泡爆炸的图片id数组
     */
    private int[] mBurstDrawablesArray = {R.mipmap.burst_1, R.mipmap.burst_2, R.mipmap.burst_3, R.mipmap.burst_4, R.mipmap.burst_5};

    public DragBubbleView(Context context) {
        this(context,null);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DragBubbleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DragBubbleView, defStyleAttr, 0);
        mBubbleRadius = array.getDimension(R.styleable.DragBubbleView_bubble_radius, mBubbleRadius);
        mBubbleColor = array.getColor(R.styleable.DragBubbleView_bubble_color, Color.RED);
        mTextStr = array.getString(R.styleable.DragBubbleView_bubble_text);
        mTextSize = array.getDimension(R.styleable.DragBubbleView_bubble_textSize, mTextSize);
        mTextColor = array.getColor(R.styleable.DragBubbleView_bubble_textColor, Color.WHITE);
        array.recycle();

        //两个圆半径大小一致
        mBubFixedRadius = mBubbleRadius;
        mBubMovableRadius = mBubFixedRadius;
        mMaxDist = 8 * mBubbleRadius;
        MOVE_OFFSET = mMaxDist / 4;

        //抗锯齿
        mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBubblePaint.setColor(mBubbleColor);
        mBezierPath = new Path();

        //文本画笔
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();

        //爆炸画笔
        mBurstPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBurstPaint.setFilterBitmap(true);
        mBurstRect = new Rect();
        mBurstBitmapsArray = new Bitmap[mBurstDrawablesArray.length];
        for (int i = 0; i < mBurstDrawablesArray.length; i++) {
            //将气泡爆炸drawable转为bitmap
            mBurstBitmapsArray[i] = BitmapFactory.decodeResource(getResources(), mBurstDrawablesArray[i]);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //不动气泡圆心
        if (mBubFixedCenter == null) {
            mBubFixedCenter = new PointF(w * 1.0f / 2, h * 1.0f / 2);
        }else {
            mBubFixedCenter.set(w * 1.0f / 2, h * 1.0f / 2);
        }
        //可动气泡圆心
        if (mBubMovableCenter == null) {
            mBubMovableCenter = new PointF(w * 1.0f / 2, h * 1.0f / 2);
        }else {
            mBubMovableCenter.set(w * 1.0f / 2, h * 1.0f / 2);
        }
    }

    /**
     * 1、静止状态，绘制气泡加消息数据
     * 2、连接状态，绘制气泡加消息数据，贝赛尔曲线，本身位置上气泡，大小可变化
     * 3、分类状态，绘制气泡加消息数据
     * 4、消失状态，气泡爆炸
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mBubbleState == BUBBLE_STATE_CONNECT) {
            //绘制不动气泡
            canvas.drawCircle(mBubFixedCenter.x,mBubFixedCenter.y,mBubFixedRadius,mBubblePaint);
            //绘制贝赛尔曲线
            //控制点坐标
            float mAnchorX = (mBubMovableCenter.x + mBubFixedCenter.x) / 2;
            float mAnchorY = (mBubMovableCenter.y + mBubFixedCenter.y) / 2;

            //角度计算
            float sinTheta = (mBubFixedCenter.y - mBubMovableCenter.y) / mDist;
            float cosTheta = (mBubMovableCenter.x - mBubFixedCenter.x) / mDist;

            //B 点坐标
            float mBLocationX = mBubMovableCenter.x - sinTheta * mBubMovableRadius;
            float mBLocationY = mBubMovableCenter.y - cosTheta * mBubMovableRadius;

            //C点坐标
            float mCLocationX = mBubMovableCenter.x + sinTheta * mBubMovableRadius;
            float mCLocationY = mBubMovableCenter.y + cosTheta * mBubMovableRadius;

            //A 点坐标
            float mALocationX = mBubFixedCenter.x - sinTheta * mBubFixedRadius;
            float mALocationY = mBubFixedCenter.y - cosTheta * mBubFixedRadius;

            //D点坐标
            float mDLocationX = mBubFixedCenter.x + sinTheta * mBubFixedRadius;
            float mDLocationY = mBubFixedCenter.y + cosTheta * mBubFixedRadius;

            mBezierPath.reset();
            //A-B
            mBezierPath.moveTo(mALocationX,mALocationY);
            mBezierPath.quadTo(mAnchorX,mAnchorY,mBLocationX,mBLocationY);

            //连接到C 画CD段
            mBezierPath.lineTo(mCLocationX,mCLocationY);
            mBezierPath.quadTo(mAnchorX,mAnchorY,mDLocationX,mDLocationY);
            mBezierPath.close();
            canvas.drawPath(mBezierPath,mBubblePaint);
        }

        if (mBubbleState != BUBBLE_STATE_DISMISS) {
            //绘制气泡加消息数据
            canvas.drawCircle(mBubMovableCenter.x,mBubMovableCenter.y,mBubMovableRadius,mBubblePaint);
            mTextPaint.getTextBounds(mTextStr,0,mTextStr.length(),mTextRect);
            canvas.drawText(mTextStr, mBubMovableCenter.x - mTextRect.width() / 2, mBubMovableCenter.y + mTextRect.height() / 2, mTextPaint);
        }

        if (mBubbleState == BUBBLE_STATE_DISMISS && mCurDrawableIndex < mBurstBitmapsArray.length) {
            mBurstRect.set(
                    (int)(mBubMovableCenter.x - mBubMovableRadius),
                    (int)(mBubMovableCenter.y - mBubMovableRadius),
                    (int)(mBubMovableCenter.x + mBubMovableRadius),
                    (int)(mBubMovableCenter.y + mBubMovableRadius)
            );
            canvas.drawBitmap(mBurstBitmapsArray[mCurDrawableIndex],null,mBurstRect,mBubblePaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mBubbleState != BUBBLE_STATE_DISMISS) {
                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                    if(mDist < mBubbleRadius + MOVE_OFFSET){
                        //加上MOVE_OFFSET方便拖拽
                        mBubbleState = BUBBLE_STATE_CONNECT;
                    }else {
                        mBubbleState = BUBBLE_STATE_DEFAULT;
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (mBubbleState != BUBBLE_STATE_DEFAULT) {
                    mDist = (float) Math.hypot(event.getX() - mBubFixedCenter.x, event.getY() - mBubFixedCenter.y);
                    mBubMovableCenter.x = event.getX();
                    mBubMovableCenter.y = event.getY();
                    if (mBubbleState == BUBBLE_STATE_CONNECT) {
                        if (mDist < mMaxDist - MOVE_OFFSET) {
                            //拖拽距离在指定范围内，调整不动气泡半径
                            mBubFixedRadius = mBubbleRadius - mDist / 8;
                        }else {
                            mBubbleState = BUBBLE_STATE_APART;
                        }
                    }
                    invalidate();
                }
                break;
                case MotionEvent.ACTION_UP:
                    if (mBubbleState == BUBBLE_STATE_CONNECT) {
                        //橡皮筋动画效果
                        startBubbleRestAnim();
                    } else if (mBubbleState == BUBBLE_STATE_APART) {
                        if (mDist < 2 * mBubbleRadius) {
                            startBubbleRestAnim();
                        }else {
                            //爆炸效果
                            startBubbleBurstAnim();
                        }
                    }
                    break;
        }
        return true;
    }

    /**
     * 爆炸播放动画
     */
    private void startBubbleBurstAnim() {
        mBubbleState = BUBBLE_STATE_DISMISS;
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, mBurstBitmapsArray.length);
        valueAnimator.setDuration(500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurDrawableIndex = (int) animation.getAnimatedValue();
                System.out.println("----------------------------" + mCurDrawableIndex);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    private void startBubbleRestAnim() {
        mValueAnimator = ValueAnimator.ofObject(new PointFEvaluator(),
                new PointF(mBubMovableCenter.x, mBubMovableCenter.y),
                new PointF(mBubFixedCenter.x, mBubFixedCenter.y));
        mValueAnimator.setDuration(200);
        mValueAnimator.setInterpolator(new OvershootInterpolator(5f));
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mBubMovableCenter = (PointF) animation.getAnimatedValue();
                invalidate();
            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mBubbleState = BUBBLE_STATE_DEFAULT;
            }
        });
        mValueAnimator.start();
    }
}
