package com.nfl.libraryoflibrary.utils.annotation;

import android.app.Activity;
import android.view.View;

import com.nfl.libraryoflibrary.utils.annotation.provider.ActivityProvider;
import com.nfl.libraryoflibrary.utils.annotation.provider.Provider;
import com.nfl.libraryoflibrary.utils.annotation.provider.ViewProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by nfl on 2017/11/29.
 */

// host 表示注解 View 变量所在的类，也就是注解类
// source 表示查找 View 的地方，Activity & View 自身就可以查找，Fragment 需要在自己的 itemView 中查找
// provider 是一个接口，定义了不同对象（比如 Activity、View 等）如何去查找目标 View，项目中分别为 Activity、View 实现了 Provider 接口。
public class ViewFinder {

    private static final ActivityProvider PROVIDER_ACTIVITY = new ActivityProvider();
    private static final ViewProvider PROVIDER_VIEW = new ViewProvider();

    private static final Map<String, Finder> FINDER_MAP = new HashMap<>();

    public static void inject(Activity activity) {
        // for activity
        inject(activity, activity, PROVIDER_ACTIVITY);
    }

    public static void inject(View view) {
        // for view
        inject(view, view);
    }

    public static void inject(Object host, View view) {
        // for fragment
        inject(host, view, PROVIDER_VIEW);
    }

    public static void inject(Object host, Object source, Provider provider) {
        String className = host.getClass().getName();
        try {
            Finder finder = FINDER_MAP.get(className);
            if (finder == null) {
                Class<?> finderClass = Class.forName(className + "$$Finder");
                finder = (Finder) finderClass.newInstance();
                FINDER_MAP.put(className, finder);
            }
            finder.inject(host, source, provider);
        } catch (Exception e) {
            throw new RuntimeException("Unable to inject for " + className, e);
        }
    }
}
