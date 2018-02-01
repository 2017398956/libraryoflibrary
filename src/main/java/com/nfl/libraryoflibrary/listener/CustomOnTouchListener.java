package com.nfl.libraryoflibrary.listener;

import android.view.MotionEvent;
import android.view.View;

import com.nfl.libraryoflibrary.utils.TraceKeeper;

/**
 * Created by fuli.niu on 2018/1/16.
 */

public class CustomOnTouchListener implements View.OnTouchListener {

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        TraceKeeper.addTrace(v);
        return false;
    }
}
