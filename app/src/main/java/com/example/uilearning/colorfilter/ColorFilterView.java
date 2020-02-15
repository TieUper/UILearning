package com.example.uilearning.colorfilter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.uilearning.R;

public class ColorFilterView extends View {

    private Paint mPaint;
    private Bitmap mBitmap;

    public ColorFilterView(Context context) {
        this(context,null);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ColorFilterView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.girl);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //去除红色
//        LightingColorFilter lightingColorFilter = new LightingColorFilter(0x00ffff, 0x000000);
//        mPaint.setColorFilter(lightingColorFilter);
//        canvas.drawBitmap(mBitmap,0,0,mPaint);

        //原始图片效果
//        LightingColorFilter lightingColorFilter = new LightingColorFilter(0xffffff, 0x000000);
//        mPaint.setColorFilter(lightingColorFilter);
//        canvas.drawBitmap(mBitmap,0,0,mPaint);

        //绿色更亮
//        LightingColorFilter lighting = new LightingColorFilter(0xffffff,0x003000);
//        mPaint.setColorFilter(lighting);
//        canvas.drawBitmap(mBitmap, 0,0, mPaint);

//        PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.DARKEN);
//        mPaint.setColorFilter(porterDuffColorFilter);
//        canvas.drawBitmap(mBitmap, 100, 0, mPaint);

        ColorMatrix colorMatrix = new ColorMatrix(ColorFilter.colormatrix_hepian);
        //亮度调节
//        colorMatrix.setScale(1,2,1,1);
        //饱和度调节0-无色彩， 1- 默认效果， >1饱和度加强
//        colorMatrix.setSaturation(2);

        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
        mPaint.setColorFilter(colorMatrixColorFilter);
        canvas.drawBitmap(mBitmap, 100, 0, mPaint);

    }

    // 胶片
    public static final float colormatrix_fanse[] = {
            -1.0f, 0.0f, 0.0f, 0.0f, 255.0f,
            0.0f, -1.0f, 0.0f, 0.0f, 255.0f,
            0.0f, 0.0f, -1.0f, 0.0f, 255.0f,
            0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
}
