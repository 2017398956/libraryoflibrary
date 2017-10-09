package com.nfl.libraryoflibrary.listener;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.nfl.libraryoflibrary.utils.LogTool;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by fuli.niu on 2017/1/12.
 */

public abstract class CustomDialogInterface implements DialogInterface {

    public static abstract class OnClickListener extends CustomOnClickListener implements DialogInterface.OnClickListener {

        private DialogInterface dialog;
        protected final int SUBMIT = DialogInterface.BUTTON_POSITIVE ;
        protected final int CANCLE = DialogInterface.BUTTON_NEGATIVE ;

        public OnClickListener(){
        }

        /**
         * �ù��췽�����ڵ�� ȷ�� ��ť ��������ʧ������һ��ʹ�������޲ι��췽������
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
                    LogTool.i("�� alertdialog �ĵ��λ�ò�������ť");
                }
            } else {
                LogTool.i("û�е��� setDialog �� �� dialog ���� alertdialog ����ͨ��һ�� View �� setOnclickListener ������ DialogInterface.OnClickListener");
        }
        }

        /**
         * @param dialog
         * @param which
         */
        @Override
        public void onClick(DialogInterface dialog, int which) {
            // TraceKeeper.addTrace(v);

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
