package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.nfl.libraryoflibrary.R;

import java.util.List;

/**
 * Created by fuli.niu on 2016/8/5.
 */
public class DialogTool {
    private static DialogTool mInstance;
    private String url;
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private Dialog mDialog;

    public DialogTool() {
    }

    public static DialogTool getInstance() {
        if (mInstance == null) {
            mInstance = new DialogTool();
        }
        return mInstance;
    }

    public void displayDialogWithData(Context context, View view,
                                      List<String> dataList) {
        displayDialogWithData(context, view, Gravity.BOTTOM, Color.TRANSPARENT,
                dataList);
    }

    public void displayDialogWithData(Context context, View view, int gravity,
                                      List<String> dataList) {
        displayDialogWithData(context, view, gravity, Color.TRANSPARENT,
                dataList);
    }

    public void displayDialog(Context context, View view) {
        displayDialog(context, view, Gravity.BOTTOM, Color.TRANSPARENT);
    }

    public void displayDialog(Context context, View view, int gravity) {
        displayDialog(context, view, gravity, Color.TRANSPARENT);
    }


    public void displayDialogNone(Context context, View view, int gravity) {
        displayDialogNoneAnim(context, view, gravity, Color.TRANSPARENT);
    }

    public void displayDialogfill(Context context, View view, int gravity) {
        displayDialogFill(context, view, gravity, Color.TRANSPARENT);
    }

    public void downLoadDialog(Context context, View view, int gravity) {
        downLoadDialog(context, view, gravity, Color.TRANSPARENT);
    }

    // 需要传递集合的弹出框 监听系统返回键清楚 集合
    public void displayDialogWithData(Context context, View view, int gravity,
                                      int backGround, final List<String> dataList) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        dismissDialog();
        mDialog = new Dialog(context, R.style.customDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setGravity(gravity);
        mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface arg0) {
                // dataList.clear();
            }
        });
        window.setBackgroundDrawable(new ColorDrawable(backGround));
        window.setWindowAnimations(R.style.customPopMenuAnimation);
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.show();
    }

    public void displayDialog(Context context, View view, int gravity,
                              int backGround) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        dismissDialog();
        mDialog = new Dialog(context, R.style.customDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setGravity(gravity);
        window.setBackgroundDrawable(new ColorDrawable(backGround));
        window.setWindowAnimations(R.style.customPopMenuAnimation);
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.show();
    }

    public void displayDialogNoneAnim(Context context, View view, int gravity,
                                      int backGround) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        dismissDialog();
        mDialog = new Dialog(context, R.style.customDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setGravity(gravity);
        window.setBackgroundDrawable(new ColorDrawable(backGround));
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.show();
    }

    public void displayDialogFill(Context context, View view, int gravity,
                                  int backGround) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        dismissDialog();
        mDialog = new Dialog(context, R.style.customDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
        window.setGravity(gravity);
        window.setBackgroundDrawable(new ColorDrawable(backGround));
        window.setWindowAnimations(R.style.customPopMenuAnimation);
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mParams.height = (int) (display.getHeight() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.show();
    }

    public void downLoadDialog(Context context, View view, int gravity,
                               int backGround) {
        if (context instanceof Activity && ((Activity) context).isFinishing()) {
            return;
        }
        dismissDialog();
        mDialog = new Dialog(context, R.style.customDialog);
        mDialog.setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        Window window = mDialog.getWindow();
//	        diaLogSetonchan();
        window.setGravity(gravity);
        window.setBackgroundDrawable(new ColorDrawable(backGround));
        WindowManager.LayoutParams mParams = mDialog.getWindow()
                .getAttributes();
        WindowManager windowManager =(WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        mParams.width = (int) (display.getWidth() * 1.0);
        mDialog.getWindow().setAttributes(mParams);
        mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();

        }
        mDialog = null;
    }

    public boolean isShowDialog() {
        if (mDialog != null && mDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    // 隐藏弹出框
    public void hideDiaLog() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    // 显示弹出框
    public void showDiaLog() {
        if (mDialog != null) {
            mDialog.show();
        }
    }

    public boolean isNullDiaLog() {
        if (mDialog != null) {
            return true;
        }
        return false;
    }
}
