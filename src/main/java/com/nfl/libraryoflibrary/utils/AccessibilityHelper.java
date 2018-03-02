package com.nfl.libraryoflibrary.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Rect;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.webkit.WebView;
import android.widget.ScrollView;

/**
 * Created by fuli.niu on 2018/2/27.
 */

public class AccessibilityHelper {

    /**
     * 利用无障碍服务，模拟点击通知栏通知
     *
     * @param event
     */
    public static void sendPendingIntent(AccessibilityEvent event) {
        // A instanceof B 用来判断内存中实际对象A是不是B类型，常用于强制转换前的判断
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            Notification notification = (Notification) event.getParcelableData();
            // 打开对应的聊天界面
            if (null != notification.contentIntent) {
                try {
                    notification.contentIntent.send();
                } catch (PendingIntent.CanceledException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void traverseWindow(AccessibilityNodeInfo rootNode) {
        if (null != rootNode) {
            Rect rect = null ;
            for (int i = rootNode.getChildCount() - 1; i > -1; i--) {
                AccessibilityNodeInfo node = rootNode.getChild(i);
                // 如果 node 为空则跳过该节点
                if (node == null) {
                    continue;
                }
                 node.getBoundsInScreen(rect);
                LogTool.i(i + " className : " + node.getClassName() + " " + (null != rect ? rect.toString() : ""));
//
                if ("android.widget.Button".equals(node.getClassName())) {
                }
            }
        }
    }
}
