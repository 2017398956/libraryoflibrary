package com.nfl.libraryoflibrary.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.nfl.libraryoflibrary.utils.runbackgroundpersistently.PersistentActivity;
import com.nfl.libraryoflibrary.utils.runbackgroundpersistently.PersistentService;

/**
 * Created by fuli.niu on 2016/8/12.
 */
public class CustomBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String receiverAction = intent.getAction() + "" ;
        if(receiverAction.equals("android.net.conn.CONNECTIVITY_CHANGE")){

        }else if(receiverAction.equals(CustomBroadcastSender.INTENT_ACTION_APP_START)){
            context.startService(new Intent(context , PersistentService.class)) ;
        }else if(receiverAction.equals(Intent.ACTION_SCREEN_OFF)){
            // 部分机型接收不到
            context.startService(new Intent(context , PersistentService.class)) ;
            context.startActivity(new Intent(context , PersistentActivity.class));
        }else if(receiverAction.equals(Intent.ACTION_USER_PRESENT)){
            context.startService(new Intent(context , PersistentService.class)) ;
            // finish PersistentActivity
        }
    }
}
