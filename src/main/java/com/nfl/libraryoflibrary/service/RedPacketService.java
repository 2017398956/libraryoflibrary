package com.nfl.libraryoflibrary.service;

import android.accessibilityservice.AccessibilityService;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import com.nfl.libraryoflibrary.utils.AccessibilityHelper;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.ToastTool;

import java.util.List;

/**
 *
 */
public class RedPacketService extends AccessibilityService {

    /**
     * 微信几个页面的包名+地址。用于判断在哪个页面
     * LAUCHER-微信聊天界面
     * LUCKEY_MONEY_RECEIVER-点击红包弹出的界面
     * LUCKEY_MONEY_DETAIL-红包领取后的详情界面
     */

    private String LAUCHER = "com.tencent.mm.ui.LauncherUI";
    private String LUCKEY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    private String LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    /**
     * 用于判断是否点击过红包了
     */
    private boolean isOpenRP;

    private boolean isOpenDetail = false;

    /**
     * 用于判断是否屏幕是亮着的
     */
    private boolean isScreenOn;

    /**
     * 获取PowerManager.WakeLock对象
     */
    private PowerManager.WakeLock wakeLock;

    /**
     * KeyguardManager.KeyguardLock对象
     */
    private KeyguardManager.KeyguardLock keyguardLock;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogTool.i("接收到无障碍服务 onAccessibilityEvent" + event.getEventType());
        int eventType = event.getEventType();
        switch (eventType) {
            //通知栏来信息，判断是否含有微信红包字样，是的话跳转
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                List<CharSequence> texts = event.getText();
                for (CharSequence text : texts) {
                    String content = text.toString();
                    if (!TextUtils.isEmpty(content)) {
                        //判断是否含有[微信红包]字样
                        if (content.contains("[微信红包]")) {
                            wakeUpScreen();
                            // 如果有则打开微信红包页面
                            AccessibilityHelper.sendPendingIntent(event);
                            isOpenRP = false;
                        }
                    }
                }
                break;
            // 界面跳转的监听
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    List<AccessibilityWindowInfo> accessibilityWindowInfos = getWindows();
                    for (AccessibilityWindowInfo accessibilityWindowInfo : accessibilityWindowInfos) {
                        AccessibilityHelper.traverseWindow(accessibilityWindowInfo.getRoot());
                    }
                }
                 AccessibilityHelper.traverseWindow(getRootInActiveWindow());
                String className = event.getClassName().toString();
                // 判断是否是微信聊天界面
                if (LAUCHER.equals(className)) {
                    // 获取当前聊天页面的根布局，开始找红包
                    findRedPacket(getRootInActiveWindow());
                }
                //判断是否是显示‘开’的那个红包界面
                if (LUCKEY_MONEY_RECEIVER.equals(className)) {
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    // 开始抢红包
                    openRedPacket(rootNode);
                }
                // 判断是否是红包领取后的详情界面
                if (isOpenDetail && LUCKEY_MONEY_DETAIL.equals(className)) {
                    isOpenDetail = false;
                    // 返回桌面
                    back2Home();
                    // 如果之前是锁着屏幕的则重新锁回去
                    release();
                }
                break;
        }
    }

    /**
     * 开始打开红包
     */
    private void openRedPacket(AccessibilityNodeInfo rootNode) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            AccessibilityNodeInfo node = rootNode.getChild(i);
            if ("android.widget.Button".equals(node.getClassName())) {
                node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                isOpenDetail = true;
            }
            openRedPacket(node);
        }
    }

    /**
     * 遍历查找红包
     */
    private void findRedPacket(AccessibilityNodeInfo rootNode) {
        if (rootNode != null) {
            // 从最后一行开始找起
            for (int i = rootNode.getChildCount() - 1; i >= 0; i--) {
                AccessibilityNodeInfo node = rootNode.getChild(i);
                // 如果 node 为空则跳过该节点
                if (node == null) {
                    continue;
                }
                CharSequence text = node.getText();
                if (text != null && text.toString().equals("领取红包")) {
                    AccessibilityNodeInfo parent = node.getParent();
                    // while 循环,遍历"领取红包"的各个父布局，直至找到可点击的为止
                    while (parent != null) {
                        if (parent.isClickable()) {
                            // 模拟点击
                            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            // isOpenRP 用于判断该红包是否点击过
                            isOpenRP = true;
                            break;
                        }
                        parent = parent.getParent();
                    }
                }
                // 判断是否已经打开过那个最新的红包了，是的话就跳出for循环，不是的话继续遍历
                if (isOpenRP) {
                    break;
                } else {
                    findRedPacket(node);
                }
            }
        }
    }

    /**
     * 服务连接
     */
    @Override
    protected void onServiceConnected() {
        ToastTool.showShortToast("抢红包服务开启");
        super.onServiceConnected();
    }

    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
        ToastTool.showShortToast("我快被终结了啊");
    }

    /**
     * 服务断开
     */
    @Override
    public boolean onUnbind(Intent intent) {
        ToastTool.showShortToast("抢红包服务已被关闭");
        return super.onUnbind(intent);
    }

    /**
     * 返回桌面
     */
    private void back2Home() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
    }

    /**
     * 判断是否处于亮屏状态
     *
     * @return true-亮屏，false-暗屏
     */
    private boolean isScreenOn() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        isScreenOn = pm.isScreenOn();
        return isScreenOn;
    }

    /**
     * 解锁屏幕
     */
    private void wakeUpScreen() {
        if (isScreenOn()) {
            return;
        }
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //后面的参数|表示同时传入两个值，最后的是调试用的Tag
        wakeLock = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.FULL_WAKE_LOCK, "bright");
        //点亮屏幕
        wakeLock.acquire();
        //得到键盘锁管理器
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        keyguardLock = km.newKeyguardLock("unlock");
        //解锁
        keyguardLock.disableKeyguard();
    }

    /**
     * 释放keyguardLock和wakeLock
     */
    public void release() {
        if (keyguardLock != null) {
            keyguardLock.reenableKeyguard();
            keyguardLock = null;
        }
        if (wakeLock != null) {
            wakeLock.release();
            wakeLock = null;
        }
    }
}
