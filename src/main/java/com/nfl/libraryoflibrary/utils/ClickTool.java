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
     * @param clickView
     */
    public static void imitationClick(View clickView) {
        int[] location = new int[2] ;
        clickView.getLocationOnScreen(location);
        int x = location[0] ;
        int y = location[1] ;
        long downTime = SystemClock.uptimeMillis();
        MotionEvent downEvent = MotionEvent.obtain(downTime,downTime,MotionEvent.ACTION_DOWN,x,y,0);
        downTime += 200;
        MotionEvent upEvent = MotionEvent.obtain(downTime, downTime,MotionEvent.ACTION_UP, x, y, 0);
        clickView.onTouchEvent(downEvent);
        clickView.onTouchEvent(upEvent);
        downEvent.recycle();
        upEvent.recycle();
    }
}
