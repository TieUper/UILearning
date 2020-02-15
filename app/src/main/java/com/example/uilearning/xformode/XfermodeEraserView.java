package com.example.uilearning.xformode;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.uilearning.R;

public class XfermodeEraserView extends View {

    private Paint mPaint;
    private Bitmap mBitmapTxt;
    private Bitmap mBitmapDst;
    private Bitmap mBitmapPath;
    private Path mPath;
    private float mEventX;
    private float mEventY;

    public XfermodeEraserView(Context context) {
        this(context,null);
    }

    public XfermodeEraserView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public XfermodeEraserView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(80);
        //关闭硬件加速
        setLayerType(LAYER_TYPE_SOFTWARE,null);

        //初始化需要的三个bitmap
        mBitmapTxt = BitmapFactory.decodeResource(getResources(), R.mipmap.result);
        mBitmapDst = BitmapFactory.decodeResource(getResources(), R.mipmap.eraser);
        mBitmapPath = Bitmap.createBitmap(mBitmapDst.getWidth(), mBitmapDst.getHeight(), Bitmap.Config.ARGB_8888);

        //路径(贝赛尔曲线)
        mPath = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmapTxt,0,0,mPaint);

        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), mPaint, Canvas.ALL_SAVE_FLAG);

        Canvas pathCanvas = new Canvas(mBitmapPath);
        pathCanvas.drawPath(mPath,mPaint);

        canvas.drawBitmap(mBitmapPath,0,0,mPaint);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT));

        canvas.drawBitmap(mBitmapDst,0,0,mPaint);

        mPaint.setXfermode(null);

        canvas.restoreToCount(layerId);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mEventX = event.getX();
                mEventY = event.getY();
                mPath.moveTo(mEventX,mEventY);
                break;
            case MotionEvent.ACTION_MOVE:
                float endX = (event.getX() - mEventX) / 2 + mEventX;
                float endY = (event.getY() - mEventY) / 2 + mEventY;
                //画二阶贝赛尔曲线
                mPath.quadTo(mEventX,mEventY,endX,endY);
                mEventX = event.getX();
                mEventY = event.getY();
//                mPath.lineTo(event.getX(),event.getY());
                break;
        }
        invalidate();
        return true;
    }
}
