package com.example.music_xiehailong;

import android.view.GestureDetector;
import android.view.MotionEvent;

public class SwipeGestureListener extends GestureDetector.SimpleOnGestureListener {

    private static final int SWIPE_THRESHOLD = 50; // 滑动的最小距离
    private static final int SWIPE_VELOCITY_THRESHOLD = 50; // 滑动的最小速度

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float diffX = e2.getX() - e1.getX();
        float diffY = e2.getY() - e1.getY();

        if (Math.abs(diffX) > Math.abs(diffY)) {
            if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (diffX > 0) {
                    onSwipeRight();
                } else {
                    onSwipeLeft();
                }
                return true;
            }
        }
        return false;
    }

    public void onSwipeRight() {
        // 处理向右滑动的事件
    }

    public void onSwipeLeft() {
        // 处理向左滑动的事件
    }
}
