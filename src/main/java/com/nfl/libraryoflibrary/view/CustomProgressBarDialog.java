package com.nfl.libraryoflibrary.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;

/**
 * Created by fuli.niu on 2016/7/12.
 */
public class CustomProgressBarDialog extends Dialog{

    private TextView mLoadingTextView;
    private ProgressBar mProgressBar;
    private View mContentView;
    private static CustomProgressBarDialog customProgressBarDialog ;

    private CustomProgressBarDialog(Context context) {
        super(context, R.style.customProgressBarDialog);
        mContentView = getLayoutInflater().inflate(R.layout.custom_progress_bar_dialog, null);
        setContentView(mContentView);
        mContentView.setBackgroundResource(R.drawable.custom_progress_bar_dialog_bg);
        mLoadingTextView = (TextView) findViewById(android.R.id.text1);
        mProgressBar = (ProgressBar) findViewById(android.R.id.progress);
        setCanceledOnTouchOutside(false);
        setMessage(0);
    }

    private CustomProgressBarDialog(Context context , String info) {
        this(context);
        mLoadingTextView.setText(info);
    }

    public void setMessage(int id) {
        if (0 == id) {
            mLoadingTextView.setText("数据加载中,请稍等...");
        } else if (0 < id) {
            mLoadingTextView.setText(id);
        }
    }

    public void setBackground(int resid) {
        if (resid > 0) {
            mContentView.setBackgroundResource(resid);
        }
    }

    public void setIcon(Drawable d) {
        if (null != d) {
            mProgressBar.setIndeterminateDrawable(d);
        }
    }

    @Override
    public void show() {
        super.show();
    }

    public static void showProgressBarDialog(Context context){
        dimissProgressBarDialog();
        if(null != context){
            customProgressBarDialog =  new CustomProgressBarDialog(context) ;
            customProgressBarDialog.show();
        }
    }

    public static void showProgressBarDialog(Context context , String info){
        dimissProgressBarDialog();
        if(null != context){
            customProgressBarDialog =  new CustomProgressBarDialog(context , info) ;
            customProgressBarDialog.show();
        }
    }

    public static void dimissProgressBarDialog(){
        if(null != customProgressBarDialog){
            customProgressBarDialog.dismiss();
            customProgressBarDialog = null ;
        }
    }

    public static boolean isCustomProgressBarShowing(){
        return null == customProgressBarDialog ? false : customProgressBarDialog.isShowing() ;
    }
}
