package com.nfl.libraryoflibrary.listener;

import android.content.DialogInterface;

import com.nfl.libraryoflibrary.utils.TraceKeeper;

/**
 * Created by fuli.niu on 2017/1/12.
 */

public abstract class CustomDialogInterface implements DialogInterface {

    public static abstract class OnClickListener implements DialogInterface.OnClickListener{

        public OnClickListener(){

        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TraceKeeper.addTrace(v);
        }
    }
    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }
}
