package com.example.customerevent.litener;


import com.example.customerevent.MotionEvent;
import com.example.customerevent.View;

public interface OnTouchListener {
    boolean onTouch(View v, MotionEvent event);
}
