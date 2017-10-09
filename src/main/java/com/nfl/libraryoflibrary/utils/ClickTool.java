package com.nfl.libraryoflibrary.utils;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by fuli.niu on 2016/9/21.
 */

public class ClickTool {

    /**
     * 模拟点击事件
     *
     * @param clickView
     */
    public static void imitationClick(View clickView) {
        imitationClick(clickView, 200);
    }

    /**
     * 模拟点击事件
     * @param clickView 被点击的 View
     * @param millisecond 毫秒，down 事件 和 up 事件的间隔
     */
    public static void imitationClick(View clickView, int millisecond) {
        int[] location = new int[2] ;
        clickView.getLocationOnScreen(location);
        int x = location[0] ;
        int y = location[1] ;
        long downTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.obtain(downTime,downTime,MotionEvent.ACTION_DOWN,x,y,0);
        downTime += millisecond;
        MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,MotionEvent.ACTION_UP, x, y, 0);
        clickView.onTouchEvent(downEvent);
        clickView.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
