package com.nfl.libraryoflibrary.listener;

import android.view.View;

import com.nfl.libraryoflibrary.utils.TraceKeeper;

/**
 * Created by fuli.niu on 2016/8/31.
 */
public class CustomOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(View v) {
        TraceKeeper.addTrace(v);
    }
}
