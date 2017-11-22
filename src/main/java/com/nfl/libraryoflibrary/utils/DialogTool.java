package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.OnDismissListener;
import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.beans.OptionPickerViewBaseBeanInterface;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    /**
     * 显示一个"确定"按钮的警告框
     */
    public static void displayAlertDialogOneButton(Context context, String message) {
        hideSoftKeyboard(context);
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle("提示")
                .setMessage(message).setCancelable(true)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();
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
        hideSoftKeyboard2(context);
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
        hideSoftKeyboard2(context);
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
        hideSoftKeyboard2(context);
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
        hideSoftKeyboard2(context);
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
        hideSoftKeyboard2(context);
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
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
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
    public void hideDialog() {
        if (mDialog != null) {
            mDialog.hide();
        }
    }

    public static class TimeSelectedListener {
        public void onSelect(Date date) {
        }
    }

    /**
     * 显示时间控件(年月日)
     * 仿IOS时间滚轮
     *
     * @param view
     */
    public static void showDatePickerDialog(Context context, TextView view) {
        showDatePickerDialog(context, view, "-");
    }

    public static void showDatePickerDialog(Context context, TextView view, TimeSelectedListener timeSelectedListener) {
        showDatePickerDialog(context, view, "-", timeSelectedListener);
    }

    public static void showDatePickerDialog(Context context, final TextView view, final String splitChar) {
        showDatePickerDialog(context , view , splitChar , null) ;
    }

    public static void showDatePickerDialog(Context context, TextView view, final String splitChar, final TimeSelectedListener timeSelectedListener) {
        showDatePickerDialog(context , view , 0 , splitChar , timeSelectedListener);
    }

    public static void showDatePickerDialog(Context context, final TextView view, int typeInt , final String splitChar, final TimeSelectedListener timeSelectedListener){
        hideSoftKeyboard(context);
//        final String splitCharTemp = null == splitChar ? "-" : splitChar;
//        TimePickerView.Type type = TimePickerView.Type.YEAR_MONTH_DAY ;
//        if (typeInt == 1){
//            type = TimePickerView.Type.YEAR_MONTH ;
//        }
//        final TimePickerView timerPickerView = new TimePickerView(context, type);
//        timerPickerView.setCancelable(true);
//        timerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                StringBuilder sb = new StringBuilder();
//                sb.append(calendar.get(Calendar.YEAR));
//                sb.append(splitCharTemp);
//                int month = calendar.get(Calendar.MONTH) + 1;
//                sb.append(month > 9 ? month : ("0" + month));
//                sb.append(splitCharTemp);
//                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//                sb.append(dayOfMonth > 9 ? dayOfMonth : ("0" + dayOfMonth));
//                view.setText(sb.toString());
//                if (null != timeSelectedListener) {
//                    timeSelectedListener.onSelect(date);
//                }
//            }
//        });
//        timerPickerView.show();
    }
    /**
     * 显示时间控件（时分）
     * 仿IOS时间滚轮
     *
     * @param view
     */
    public static void showTimePickerDialog(Context context, TextView view) {
        showTimePickerDialog(context, view, ":");
    }

    public static void showTimePickerDialog(Context context, TextView view, Date date) {
        showTimePickerDialog(context, view, ":", date);
    }

    public static void showTimePickerDialog(Context context, final TextView view, String splitChar) {
        hideSoftKeyboard(context);
//        final String splitCharTemp = null == splitChar ? ":" : splitChar;
//        TimePickerView timerPickerView = new TimePickerView(context, TimePickerView.Type.HOURS_MINS);
//        timerPickerView.setCancelable(true);
//        timerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                StringBuilder sb = new StringBuilder();
//                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//                sb.append(hourOfDay > 9 ? hourOfDay : ("0" + hourOfDay));
//                sb.append(splitCharTemp);
//                int minute = calendar.get(Calendar.MINUTE);
//                sb.append(minute > 9 ? minute : ("0" + minute));
//                view.setText(sb.toString());
//            }
//        });
//        timerPickerView.show();
    }

    public static void showTimePickerDialog(Context context, final TextView view, String splitChar, Date date) {
        hideSoftKeyboard(context);
//        final String splitCharTemp = null == splitChar ? ":" : splitChar;
//        TimePickerView timerPickerView = new TimePickerView(context, TimePickerView.Type.HOURS_MINS);
//        timerPickerView.setTime(date);
//        timerPickerView.setCancelable(true);
//        timerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                StringBuilder sb = new StringBuilder();
//                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//                sb.append(hourOfDay > 9 ? hourOfDay : ("0" + hourOfDay));
//                sb.append(splitCharTemp);
//                int minute = calendar.get(Calendar.MINUTE);
//                sb.append(minute > 9 ? minute : ("0" + minute));
//                view.setText(sb.toString());
//            }
//        });
//        timerPickerView.show();
    }

    public static void showFullTimePickerDialog(Context context, final TextView view) {
        showFullTimePickerDialog(context, view, "-");
    }

    public static void showFullTimePickerDialog(Context context, final TextView view, String dateSplit) {
        hideSoftKeyboard(context);
//        final String dateSplitChar = (null == dateSplit ? "-" : dateSplit);
//        final String timeSplitChar = ":";
//        TimePickerView timerPickerView = new TimePickerView(context, TimePickerView.Type.ALL);
//        timerPickerView.setCancelable(true);
//        timerPickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
//            @Override
//            public void onTimeSelect(Date date) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(date);
//                StringBuilder sb = new StringBuilder();
//
//                sb.append(calendar.get(Calendar.YEAR));
//                sb.append(dateSplitChar);
//                int month = calendar.get(Calendar.MONTH) + 1;
//                sb.append(month > 9 ? month : ("0" + month));
//                sb.append(dateSplitChar);
//                int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
//                sb.append(dayOfMonth > 9 ? dayOfMonth : ("0" + dayOfMonth));
//                sb.append(" ");
//                int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
//                sb.append(hourOfDay > 9 ? hourOfDay : ("0" + hourOfDay));
//                sb.append(timeSplitChar);
//                int minute = calendar.get(Calendar.MINUTE);
//                sb.append(minute > 9 ? minute : ("0" + minute));
//                view.setText(sb.toString());
//            }
//        });
//        timerPickerView.show();
    }

    public static void showOptionsPickerViewDialog(TextView view, ArrayList<? extends OptionPickerViewBaseBeanInterface> options) {
        showOptionsPickerViewDialog(view, options, null);
    }

    public static void showOptionsPickerViewDialog(final TextView view, final ArrayList<? extends OptionPickerViewBaseBeanInterface> options, OnDismissListener onDismissListener) {
//        if (view != null) {
//            hideSoftKeyboard(view.getContext());
//        }
//        if (null == options || options.size() < 1) {
//            return;
//        }
//        OptionsPickerView<OptionPickerViewBaseBeanInterface> optionsPickerView = new OptionsPickerView<>(
//                view.getContext());
//        optionsPickerView.setPicker((ArrayList<OptionPickerViewBaseBeanInterface>) options);
//        optionsPickerView.setCyclic(false);
//        optionsPickerView.setCancelable(true);
//        // 监听确定选择按钮
//        optionsPickerView.setSelectOptions(1);
//        optionsPickerView.setOnoptionsSelectListener(
//                new OptionsPickerView.OnOptionsSelectListener() {
//                    @Override
//                    public void onOptionsSelect(int options1, int option2, int options3) {
//                        view.setText(options.get(options1).getPickerViewText());
//                    }
//                });
//        optionsPickerView.setOnDismissListener(onDismissListener);
//        optionsPickerView.show();
    }

    private static void hideSoftKeyboard(Context context) {
        if (null != context && context instanceof Activity) {
            SoftKeyBoardTool.hideSoftKeyBoard((Activity) context);
        }
    }

    private void hideSoftKeyboard2(Context context) {
        if (null != context && context instanceof Activity) {
            SoftKeyBoardTool.hideSoftKeyBoard((Activity) context);
        }
    }

    public Dialog getDialog() {
        return mDialog;
    }
}
