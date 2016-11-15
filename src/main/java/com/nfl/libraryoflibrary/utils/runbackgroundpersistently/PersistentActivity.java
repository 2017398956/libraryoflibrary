package com.nfl.libraryoflibrary.utils.runbackgroundpersistently;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.nfl.libraryoflibrary.utils.LogTool;

public class PersistentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogTool.i("PersistentActivity") ;
        Window window = getWindow() ;
        window.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams layoutParams = window.getAttributes() ;
        layoutParams.x = 0 ;
        layoutParams.y = 0 ;
        layoutParams.width = 1 ;
        layoutParams.height = 1 ;
        window.setAttributes(layoutParams);
    }
}
