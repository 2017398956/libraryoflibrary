package com.nfl.libraryoflibrary.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by fuli.niu on 2016/8/12.
 */
public class CustomBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String receiverAction = intent.getAction() + "" ;
        if(receiverAction.equals("android.net.conn.CONNECTIVITY_CHANGE")){

        }
    }
}
