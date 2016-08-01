package com.nfl.libraryoflibrary.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.constant.ApplicationContext;

/**
 * Created by fuli.niu on 2016/7/12.
 */
public class ToastTool {

    public static void showShortToast(String info){
        Toast.makeText(ApplicationContext.applicationContext , info , Toast.LENGTH_SHORT).show();
    }

    public static void showShortToast(int resId){
        Toast.makeText(ApplicationContext.applicationContext , ApplicationContext.applicationContext.getResources().getText(resId) , Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String info){
        Toast.makeText(ApplicationContext.applicationContext , info , Toast.LENGTH_LONG).show();
    }

    public static void showCustomShortToast(String info){

        Toast toast = Toast.makeText(ApplicationContext.applicationContext , info , Toast.LENGTH_SHORT) ;
        ViewGroup v = (ViewGroup) toast.getView() ;
        v.setBackgroundResource(R.drawable.toast_bg);
        ((TextView) v.getChildAt(0)).setTextColor(Color.WHITE);
        toast.show();
    }
}
