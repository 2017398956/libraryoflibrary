package com.nfl.libraryoflibrary.skipads;

import android.accessibilityservice.AccessibilityService;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.TextView;

import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.ToastTool;

import java.util.ArrayList;
import java.util.List;

public class SkipAdsService extends AccessibilityService {

    private String oldPackageName = "";
    private String newPackageName;
    private String viewContent;
    private List<String> filterPackageNames = new ArrayList<>();

    public SkipAdsService() {
        super();
        LogTool.i("SkipAds 服务生成");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogTool.i("SkipAds 服务创建");
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        LogTool.i("SkipAds 服务连接");
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        LogTool.i("广告过滤服务开始工作");
        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            // 如果打开了另一个 App 则开始校验
            if (isNewApp(event.getPackageName().toString()) || true) {
                LogTool.i("开始过滤广告");
                skipAd(event.getSource());
            } else {
                LogTool.i("包名重复");
            }
        } else {
            LogTool.i("不符合校验规则");
        }
    }

    @Override
    public void onInterrupt() {
        LogTool.i("SkipAds 服务中断");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogTool.i("SkipAds 服务销毁");
    }

    /**
     * 是否打开了新的 APP
     *
     * @param newPackageName
     * @return
     */
    private boolean isNewApp(String newPackageName) {
        this.newPackageName = newPackageName;
        if (TextUtils.isEmpty(newPackageName)) {
            return false;
        } else {
            // 过滤掉白名单的 App
            for (String packageName : filterPackageNames) {
                if (newPackageName.equals(packageName)) {
                    return false;
                }
            }
            return !newPackageName.equals(oldPackageName);
        }
    }

    private void skipAd(AccessibilityNodeInfo accessibilityNodeInfo) {
        oldPackageName = newPackageName;
        if (null != accessibilityNodeInfo) {
            for (int i = 0; i < accessibilityNodeInfo.getChildCount(); i++) {
                if (accessibilityNodeInfo.getChild(i).getChildCount() > 0) {
                    skipAd(accessibilityNodeInfo.getChild(i));
                    LogTool.i("group class name is " + accessibilityNodeInfo.getChild(i).getClassName());
                } else {
                    if (TextView.class.getName().equals(accessibilityNodeInfo.getChild(i).getClassName()) ||
                            Button.class.getName().equals(accessibilityNodeInfo.getChild(i).getClassName())
                    ) {
                        try {
                            // getText 有可能为空
                            viewContent = accessibilityNodeInfo.getChild(i).getText().toString();
                        }catch (NullPointerException e){

                        }
                        if (!TextUtils.isEmpty(viewContent)
                                && (viewContent.contains("跳过") || viewContent.equalsIgnoreCase("skip") ||
                                viewContent.contains("进入"))) {
                            accessibilityNodeInfo.getChild(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            Rect outBound = new Rect();
                            accessibilityNodeInfo.getChild(i).getBoundsInScreen(outBound);
                            ;
                            LogTool.i("跳过按钮的坐标信息：" + outBound.toString());
                            ToastTool.showShortToast("跳过了广告");
                        }
                    }
                    LogTool.i("class name is " + accessibilityNodeInfo.getChild(i).getClassName());
                }
            }
        }
    }
}
