package com.example.uilearning.canvas_transform;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class TransformView extends View {

    private Paint mPaint;

    public TransformView(Context context) {
        this(context, null);
    }

    public TransformView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TransformView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /**
         * 1.canvas内部对于状态的保存存放在栈中
         * 2.可以多次调用save保存canvas的状态，并且可以通过getSaveCount方法获取保存的状态个数
         * 3.可以通过restore方法返回最近一次save前的状态，也可以通过restoreToCount返回指定save状态。指定save状态之后的状态全部被清除
         * 4.saveLayer可以创建新的图层，之后的绘制都会在这个图层之上绘制，直到调用restore方法
         * 注意：绘制的坐标系不能超过图层的范围， saveLayerAlpha对图层增加了透明度信息
         */
//        Toast.makeText(getContext(),"first: "+ canvas.getSaveCount(),Toast.LENGTH_SHORT).show();
//
//        int state = canvas.save();
//
//        canvas.drawRect(200,200,700,700,mPaint);
//
//        canvas.translate(50,50);
//
//        mPaint.setColor(Color.GRAY);
//
//        canvas.drawRect(0,0,500,500,mPaint);
//
//        Toast.makeText(getContext(),"second: "+ canvas.getSaveCount(),Toast.LENGTH_SHORT).show();
//
//        canvas.restore();
//
//        mPaint.setColor(Color.BLUE);
//
//        canvas.drawRect(0,0,500,500,mPaint);
//
//        Toast.makeText(getContext(),"third: "+ canvas.getSaveCount(),Toast.LENGTH_SHORT).show();


        //1.平移操作
//        canvas.drawRect(0, 0, 400, 400, mPaint);
//        canvas.translate(50, 50);
//        mPaint.setColor(Color.GRAY);
//        canvas.drawRect(0, 0, 400, 400, mPaint);
//        canvas.drawLine(0, 0, 600, 600, mPaint);

        //缩放操纵

    }
}
