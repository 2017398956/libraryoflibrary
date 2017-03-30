package com.nfl.libraryoflibrary.utils.databinding;

import android.view.View;

import com.nfl.libraryoflibrary.utils.TraceKeeper;

/**
 * Created by fuli.niu on 2017/3/6.
 */

public class CommonHanding {

    public void onClick(View view){
        TraceKeeper.addTrace(view);
    }
}
