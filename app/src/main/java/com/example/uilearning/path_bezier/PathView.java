package com.example.uilearning.path_bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class PathView extends View {

    private Paint mPaint;
    private Path mPath;

    public PathView(Context context) {
        this(context,null);
    }

    public PathView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //移动
//        mPath.moveTo(100, 70);
        //连线
//        mPath.lineTo(140,800);

//        mPath.rLineTo(250,600);
//        mPath.close();

        //添加子图形addXXX
        //添加弧形
//        mPath.addArc(200, 200, 400, 400, -225, 225);
        //Path.Direction.CW表示顺时针方向绘制，CCW表示逆时针方向
//        mPath.addRect(500, 500, 900, 900, Path.Direction.CW);‘
                //添加一个圆
//        mPath.addCircle(700,700, 200, Path.Direction.CW);
        //添加一个椭圆
//        mPath.addOval(0,0,500,300, Path.Direction.CCW);

        //追加图形 forceMoveTo，true，绘制时移动起点，false，绘制时连接最后一个点与圆弧起点
//        mPath.arcTo(400, 200, 600, 400, -180, 225, false);

//        mPath.moveTo(0, 0);
//        mPath.lineTo(100, 100);
//        mPath.arcTo(400, 200, 600, 400, 0, 270, true);

        //添加一个路径
//        mPath.moveTo(100, 70);
//        mPath.lineTo(140, 180);
//        mPath.lineTo(250, 330);
//        mPath.lineTo(400, 630);
//        mPath.lineTo(100, 830);

        //添加圆角矩形， CW顺时针，CCW逆时针
//        RectF rectF5 = new RectF(200, 800, 700, 1200);
//        mPath.addRoundRect(rectF5, 20, 20, Path.Direction.CCW);

        //画二阶贝塞尔曲线
        mPath.moveTo(300, 500);
//        mPath.quadTo(500, 100, 800, 500);
        //参数表示相对位置，等同于上面一行代码
        mPath.rQuadTo(200, -400, 500, 0);

        //画三阶贝塞尔曲线
        mPath.moveTo(300, 500);
//        mPath.cubicTo(500, 100, 600, 1200, 800, 500);
        //参数表示相对位置，等同于上面一行代码
        mPath.rCubicTo(200, -400, 300, 700, 500, 0);
        canvas.drawPath(mPath,mPaint);

    }
}
