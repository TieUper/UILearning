package com.example.customerevent;

import com.example.customerevent.litener.OnClickListener;
import com.example.customerevent.litener.OnTouchListener;

public class View {

    private int left;
    private int right;
    private int top;
    private int bottom;

    private OnClickListener mOnClickListener;

    private OnTouchListener mOnTouchListener;

    public View() {
    }

    public View(int left, int right, int top, int bottom) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        mOnClickListener = onClickListener;
    }

    public void setOnTouchListener(OnTouchListener onTouchListener) {
        mOnTouchListener = onTouchListener;
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean result = false;

        if (mOnTouchListener != null && mOnTouchListener.onTouch(this, motionEvent)) {
            result = true;
        }

        if (!result && onTouchEvent(motionEvent)) {
            result = true;
        }

        return result;
    }

    public boolean isContainer(MotionEvent motionEvent) {
        return motionEvent.getX() >= left &&
                motionEvent.getX() <= right &&
                motionEvent.getY() >= top &&
                motionEvent.getY() <= bottom;
    }

    private boolean onTouchEvent(MotionEvent event) {
        if (mOnClickListener != null) {
            mOnClickListener.onClick(this);
            return true;
        }
        return false;
    }
}
