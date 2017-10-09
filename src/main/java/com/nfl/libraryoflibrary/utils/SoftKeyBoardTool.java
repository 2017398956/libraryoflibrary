package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by fuli.niu on 2016/11/22.
 */

public class SoftKeyBoardTool {

    public static void showSoftKeyBoard(Activity activity) {
        if (null == activity) {
            LogTool.i("activity is null , cannot show softkeyboard") ;
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(activity.getWindow().getDecorView(), InputMethodManager.SHOW_FORCED);
    }

    public static boolean showSoftKeyBoard(Activity activity , View view) {
        if (null == activity || view == null) {
            LogTool.i("activity or view  is null , cannot show softkeyboard") ;
            return false ;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.showSoftInput(view , InputMethodManager.SHOW_FORCED);
    }

    /**
     * This may unavailable , please use ' android:windowSoftInputMode="adjustUnspecified|stateHidden" '
     * in this activity's statement of AndroidManifest.xml .
     *
     * @param activity
     */
    public static void hideSoftKeyBoard(Activity activity) {
        if (null == activity) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm.isActive()){
        imm.hideSoftInputFromWindow(activity.getWindow().getDecorView().getWindowToken(), 0);
        }
    }
}
