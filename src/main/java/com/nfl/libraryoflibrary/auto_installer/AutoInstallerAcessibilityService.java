package com.nfl.libraryoflibrary.auto_installer;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.ScrollView;

import com.nfl.libraryoflibrary.utils.LogTool;

import java.util.HashMap;
import java.util.Map;

public class AutoInstallerAcessibilityService extends AccessibilityService {

    private Map<Integer, Boolean> handleMap = new HashMap<>();

    // 遍历节点，模拟点击安装按钮
    private boolean iterateNodesAndHandle(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo != null) {
            int childCount = nodeInfo.getChildCount();
            if (Button.class.getName().equals(nodeInfo.getClassName())) {
                String nodeCotent = nodeInfo.getText().toString();
                LogTool.i("content is: " + nodeCotent);
                if ("安装".equals(nodeCotent)
//                        || "完成".equals(nodeCotent)
                        || "确定".equals(nodeCotent)
                        || "打开".equals(nodeCotent)) {
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            } else if (ScrollView.class.getName().equals(nodeInfo.getClassName())) {
                // 遇到ScrollView的时候模拟滑动一下
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            }
            for (int i = 0; i < childCount; i++) {
                AccessibilityNodeInfo childNodeInfo = nodeInfo.getChild(i);
                if (iterateNodesAndHandle(childNodeInfo)) {
                    return true;
                }
            }
        }
        return false;
    }

    public AutoInstallerAcessibilityService() {
        super();
        LogTool.i("AutoInstallerAcessibilityService's AutoInstallerAcessibilityService");
    }

    /**
     * 每当有窗口活动时，就会触发 onAccessibilityEvent() 方法，
     * 我们根据传入的 AccessibilityEvent 参数来判断当前事件的类型我们只需要监听
     * TYPE_WINDOW_CONTENT_CHANGED 和 TYPE_WINDOW_STATE_CHANGED 这两种事件就可以了，
     * 因为在整个安装过程中，这两个事件必定有一个会被触发。当然也有两个同时都被触发的可能，
     * 那么为了防止二次处理的情况，这里我们使用了一个Map集合来过滤掉重复事件。
     * 然后通过 iterateNodesAndHandle 方法来进行当前界面节点的判断，如果是按钮节点，
     * 我们就判断按钮上的文字是不是安装、完成、确定、打开这几项，如果是，就进行模拟点击。
     * （完成和打开不能同时在代码中进行判断，如果你安装完软件不想打开软件，就保留完成。反之则保留打开）。
     *
     * @param event
     */
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogTool.i("自动安装助手正在处理...");
        AccessibilityNodeInfo nodeInfo = event.getSource();
        if (nodeInfo != null) {
            int eventType = event.getEventType();


            // TYPE_VIEW_SCROLLED                   4096

            // 打开一个新的 APP type 触发的顺序
            // TYPE_VIEW_HOVER_EXIT                 256     不用过滤广告
            // TYPE_VIEW_CLICKED                    1       不用过滤广告
            // TYPE_WINDOW_CONTENT_CHANGED          2048    不用过滤广告
            // TYPE_VIEW_HOVER_ENTER                128     不用过滤广告
            // TYPE_WINDOW_STATE_CHANGED            32      过滤广告成功

            LogTool.i("AccessibilityEvent's type is " + eventType + " and package name is " + event.getPackageName());
            if (
//                    eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED ||
                    eventType == AccessibilityEvent.TYPE_WINDOWS_CHANGED
            ) {
                if (handleMap.get(event.getWindowId()) == null) {
                    boolean handled = iterateNodesAndHandle(nodeInfo);
                    if (handled) {
                        handleMap.put(event.getWindowId(), true);
                    }
                }
            }

        }
    }

    @Override
    public void onInterrupt() {
        LogTool.i("AutoInstallerAcessibilityService's onInterrupt");
    }
}
