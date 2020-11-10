package com.nfl.libraryoflibrary.service;

import android.accessibilityservice.AccessibilityService;

import androidx.annotation.NonNull;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityWindowInfo;

import java.util.List;

public class TestAccessibilityServiceService extends AccessibilityService {
    public TestAccessibilityServiceService() {
        super();
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    protected boolean onGesture(int gestureId) {
        return super.onGesture(gestureId);
    }

    @Override
    protected boolean onKeyEvent(KeyEvent event) {
        return super.onKeyEvent(event);
    }

    @Override
    public List<AccessibilityWindowInfo> getWindows() {
        return super.getWindows();
    }

    @Override
    public AccessibilityNodeInfo getRootInActiveWindow() {
        return super.getRootInActiveWindow();
    }

    @Override
    public AccessibilityNodeInfo findFocus(int focus) {
        return super.findFocus(focus);
    }

    @Override
    public Object getSystemService(@NonNull String name) {
        return super.getSystemService(name);
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {

    }
}
