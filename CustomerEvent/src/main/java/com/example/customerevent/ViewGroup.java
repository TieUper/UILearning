package com.example.customerevent;

import java.util.ArrayList;
import java.util.List;

public class ViewGroup extends View {

    private List<View> childList = new ArrayList<>();
    private View[] childrenViews = new View[0];
    //TouchTarget  模式 内存缓存   move up
    TouchTarget newTouchTarget = null;
    private TouchTarget mFirstTouchTarget;

    public void addView(View view) {
        if (view == null) {
            return;
        }
        childList.add(view);
        childrenViews = childList.toArray(new View[childList.size()]);
    }

    public ViewGroup(int left, int right, int top, int bottom) {
        super(left, right, top, bottom);
    }

    public ViewGroup() {
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        boolean handle = false;
        boolean intercept = onInterceptTouchEvent(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked != MotionEvent.ACTION_CANCEL && !intercept) {
            if (actionMasked == MotionEvent.ACTION_DOWN) {
                View[] children = this.childrenViews;
                for (int i = children.length - 1; i >= 0 ; i--) {
                    View child = children[i];
                    if (!child.isContainer(motionEvent)) {
                        //当前view不在点击区域
                        continue;
                    }
                    if (dispatchTransformedTouchEvent(motionEvent, child)) {
                        //消费事件， 做缓存
                        handle = true;
                        newTouchTarget = addTouchTarget(child);
                        break;
                    }
                }
            }
            if (mFirstTouchTarget == null) {
                handle = dispatchTransformedTouchEvent(motionEvent, null);
            }
        }

        return handle;
    }

    private TouchTarget addTouchTarget(View child) {
        TouchTarget target = TouchTarget.obtain(child);
        target.next = mFirstTouchTarget;
        mFirstTouchTarget = target;
        return target;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return false;
    }

    private boolean dispatchTransformedTouchEvent(MotionEvent motionEvent, View child) {
        if (child == null) {
            return super.dispatchTouchEvent(motionEvent);
        }else {
            return child.dispatchTouchEvent(motionEvent);
        }
    }

    /**
     * 缓存对象 使用链表结构
     * 类似 Message
     */
    private static class TouchTarget {
        private View child;
        private static TouchTarget mRecyclePool;
        private static final Object sRecycleLock = new Object[0];
        private TouchTarget next;
        private static int mPoolCount = 0;

        public static TouchTarget obtain(View view) {
            TouchTarget target;
            synchronized (sRecycleLock) {
                if (mRecyclePool == null) {
                    target = new TouchTarget();
                }else {
                    target = mRecyclePool;
                    mRecyclePool = target.next;
                    mPoolCount --;
                    target.next = null;
                }
            }
            target.child = view;
            return target;
        }

        public void recycle() {

            if (child == null) {
                throw new IllegalStateException("已经被回收过了");
            }
            synchronized (sRecycleLock) {

                if (mPoolCount < 32) {
                    next = mRecyclePool;
                    mRecyclePool = this;
                    mPoolCount += 1;
                }
            }
        }
    }

    private String name;

    @Override
    public String toString() {
        return ""+name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
