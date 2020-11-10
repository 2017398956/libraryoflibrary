package com.nfl.libraryoflibrary.listener;

import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import android.view.View;

import com.nfl.libraryoflibrary.utils.LogTool;

/**
 * Created by fuli.niu on 2017/1/12.
 */

public abstract class CustomDialogInterface implements DialogInterface {

    public static abstract class OnClickListener extends CustomOnClickListener implements DialogInterface.OnClickListener {

        private DialogInterface dialog;
        protected final int SUBMIT = DialogInterface.BUTTON_POSITIVE;
        protected final int CANCLE = DialogInterface.BUTTON_NEGATIVE;

        public OnClickListener() {
        }

        /**
         * 该构造方法用于点击 确定 按钮 ，弹框不消失的需求，一般使用上面无参构造方法即可
         *
         * @param dialog
         */
        public OnClickListener(DialogInterface dialog) {
            this.dialog = dialog;
        }

        @Override
        public final void onClick(View v) {
            super.onClick(v);
            if (null != dialog && dialog instanceof AlertDialog) {
                if (v == ((AlertDialog) dialog).getButton(BUTTON_POSITIVE)) {
                    onClick(dialog, BUTTON_POSITIVE);
                } else if (v == ((AlertDialog) dialog).getButton(BUTTON_NEGATIVE)) {
                    onClick(dialog, BUTTON_NEGATIVE);
                } else if (v == ((AlertDialog) dialog).getButton(BUTTON_NEUTRAL)) {
                    onClick(dialog, BUTTON_NEUTRAL);
                } else {
                    LogTool.i("该 alertdialog 的点击位置不是三大按钮");
                }
            } else {
                LogTool.i("没有调用 setDialog 或 该 dialog 不是 alertdialog 不能通过一般 View 的 setOnclickListener 来代替 DialogInterface.OnClickListener");
            }
        }

        /**
         * @param dialog
         * @param which
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
//             TraceKeeper.addTrace(v);

        }

        public DialogInterface getDialog() {
            return dialog;
        }

        public void setDialog(DialogInterface dialog) {
            this.dialog = dialog;
        }
    }

    @Override
    public void cancel() {

    }

    @Override
    public void dismiss() {

    }


}
