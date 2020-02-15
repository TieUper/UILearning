package com.example.uilearning.animation;

public class VSYNCManager {
    private static VSYNCManager mManager = new VSYNCManager();

    private AnimationFrameCallback mAnimationFrameCallback;

    public void setAnimationFrameCallback(AnimationFrameCallback animationFrameCallback) {
        mAnimationFrameCallback = animationFrameCallback;
    }

    public static VSYNCManager getInstance() {
        return mManager;
    }

    public VSYNCManager() {
        new Thread(mRunnable).start();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(16);
                } catch (Exception e) {
                }
                if (mAnimationFrameCallback != null) {
                    mAnimationFrameCallback.doAnimationFrame(System.currentTimeMillis());
                }
            }
        }
    };


    interface AnimationFrameCallback {
        boolean doAnimationFrame(long currentTime);
    }
}
