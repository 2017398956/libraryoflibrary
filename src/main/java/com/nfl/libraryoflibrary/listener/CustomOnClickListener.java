package com.nfl.libraryoflibrary.listener;

import android.view.View;

import com.nfl.libraryoflibrary.utils.TraceKeeper;

/**
 * Created by fuli.niu on 2016/8/31.
 */
public class CustomOnClickListener<T> implements View.OnClickListener {

    private T t;

    public CustomOnClickListener() {

    }

    public CustomOnClickListener(T t) {
        this.t = t;
    }

    @Override
    public void onClick(View v) {
        TraceKeeper.addTrace(v);
        this.onClick(v , t);
    }

    public void onClick(View view, T t) {
    }

    public void setT(T t) {
        this.t = t;
    }
}
