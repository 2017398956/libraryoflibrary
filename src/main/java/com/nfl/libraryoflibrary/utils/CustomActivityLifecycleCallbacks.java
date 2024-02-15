package com.nfl.libraryoflibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.core.view.LayoutInflaterCompat;
import androidx.core.view.LayoutInflaterFactory;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu on 2016/8/30.
 */
@TargetApi(15)
public class CustomActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks {

    private String activityName;
    private int viewLevel = 0;// 默认为0 ,表示 DecorView
    private ViewGroup rootView;// DecorView
    private Context context;
    private List<String> filteredActivities;// 不受同一主题设置的 Activity

    public CustomActivityLifecycleCallbacks() {
        this.filteredActivities = new ArrayList<>();
        this.filteredActivities.add("DialogActivity");
        this.filteredActivities.add("NormalActivity");
        this.filteredActivities.add("TransparentActivity");
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//        ApplicationConstant.nowActivity = activity ;
        LogTool.i("ActivityName:" + activity.getClass().getSimpleName() + " created");
        activityName = activity.getComponentName().getClassName();
        boolean canResetActivityTheme = true;
        for (String activityName : filteredActivities) {
            if (null != activityName && activityName.equals(activity.getClass().getSimpleName())) {
                canResetActivityTheme = false;
                break;
            }
        }
        if (canResetActivityTheme) {
            Resources.Theme theme = activity.getTheme();
            theme.applyStyle(R.style.testTheme, true);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
//        ApplicationConstant.nowActivity = activity ;
    }

    @Override
    public void onActivityResumed(Activity activity) {
//        ApplicationConstant.nowActivity = activity ;
//        alertListener(activity);
//        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes() ;
    }


    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {

    }


    /**
     * 拦截已定义的Listener并添加额外的处理方法。
     * 注意：动态生成的UI无法监听到，如Dialog，Popupwindow等；
     * 动态添加的Listener无法监听到，如满足某一条件后再添加OnClickListener；
     * 放在onActivityResumed中会使拦截Listener后添加的新方法会随着OnResume方法的执行次数增加而增加，所以，应放在OnCreate中；
     * 若放在OnCreate中OnCreate方法之后添加的Listener无法监听。
     *
     * @param activity
     */
    private void alertListener(Activity activity) {
        context = activity;
        rootView = (ViewGroup) activity.getWindow().getDecorView();

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LogTool.i("======onTouch======") ;
//                getRootViewOnTouchListener(v) ;
//                return false;
//            }
//        });
        if (null != rootView) {
            logViewsInfo(rootView);
        } else {
            LogTool.i(activityName + "'s DecorView is null.");
        }

    }

    /**
     * 判断是否是ViewGroup
     *
     * @param view
     * @return
     */
    private boolean isViewGroup(View view) {
        if (null != view) {
//            if(view.getClass().isAssignableFrom(ViewGroup.class)){
//                return true ;
//            }
//            LogTool.i(ViewGroup.class + "|------s------|" + view.getClass().toString()) ;
            if (view instanceof ViewGroup) {
                return true;
            }
        }
        return false;
    }

    /**
     * 打印view的信息
     *
     * @param viewGroup
     */
    private void logViewsInfo(ViewGroup viewGroup) {
        int position = 0  ;
        try {
            position = (int) viewGroup.getTag(R.id.id_view_position_tag_key);
        }catch (Exception e){

        }
        viewGroup.setTag(R.id.id_view_level_tag_key, viewLevel);
        viewGroup.setTag(R.id.id_view_position_tag_key, position);
        LogTool.i("viewLevel" + viewLevel + ": " + rootView.getClass());
        viewLevel++;
        int count = viewGroup.getChildCount();
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                View view = viewGroup.getChildAt(i);
                view.setTag(R.id.id_view_level_tag_key, viewLevel);
                view.setTag(R.id.id_view_position_tag_key, i);
                String logHead = viewGroup.getTag(R.id.id_view_position_tag_key) + "|" + i;
                for (int l = 0; l < (Integer) view.getTag(R.id.id_view_level_tag_key); l++) {
                    logHead += "-";
                }
                LogTool.i(logHead + view.getClass().toString());
                if (isViewGroup(view)) {
                    logViewsInfo((ViewGroup) view);
                } else {
                    if (canResetOnClickListener(view)) {
                        setClickListener(view);
                    }
                }
            }
            viewLevel++;
        }
    }

    /**
     * 判断view是否能被拦截OnClickListener方法
     *
     * @param view
     * @return true 可以拦截该view的OnClickListener方法
     */
    private boolean canResetOnClickListener(View view) {
        if (view instanceof Button || view instanceof TextView || view instanceof ImageButton || view instanceof ImageView) {
            String viewName = null;
            try {
                int viewID = view.getId();
                viewName = context.getResources().getResourceName(viewID);
            } catch (Exception e) {

            }
            LogTool.i("应该替换OnClickListener的View：" + view.getClass().getSimpleName() + " | name:" + viewName);
            return true;
        }
        return false;
    }

    private View.OnTouchListener getRootViewOnTouchListener(View view) {
        try {
            Method method = View.class.getDeclaredMethod("getListenerInfo", new Class[0]);
            if (null != method) {
                method.setAccessible(true);
                Object object = method.invoke(view, new Object[]{});// 得到ListenerInfo
                if (null != object) {
                    Class listenerInfoClazz = object.getClass();
                    // 得到ListenerInfo中的mOnFocusChangeListener
                    Field mOnTouchListener = listenerInfoClazz.getDeclaredField("mOnTouchListener");
                    if (null != mOnTouchListener) {
                        mOnTouchListener.setAccessible(true);
                        Object listener = mOnTouchListener.get(object);
                        if (null != listener && listener instanceof View.OnTouchListener) {
                            return (View.OnTouchListener) listener;
                        } else {
                            LogTool.i("替换失败");
                        }
                    } else {
                        LogTool.i("mOnTouchListenerr获取失败");
                    }
                } else {
                    LogTool.i("mOnTouchListener执行失败");
                }
            } else {
                LogTool.i("mOnTouchListener获取失败--");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射获取View的OnClickListener方法
     *
     * @param view
     * @return
     */
    private View.OnClickListener getOnClickListener(View view) {
        boolean hasOnClick = view.hasOnClickListeners();
        if (hasOnClick) {
            try {
                Method method = View.class.getDeclaredMethod("getListenerInfo", new Class[0]);
                if (null != method) {
                    method.setAccessible(true);
                    Object object = method.invoke(view, new Object[]{});// 得到ListenerInfo
                    if (null != object) {
                        Class listenerInfoClazz = object.getClass();
                        // 得到ListenerInfo中的OnClickListener
                        Field mOnClickListener = listenerInfoClazz.getDeclaredField("mOnClickListener");
                        if (null != mOnClickListener) {
                            mOnClickListener.setAccessible(true);
                            Object listener = mOnClickListener.get(object);
                            if (null != listener && listener instanceof View.OnClickListener) {
                                return (View.OnClickListener) listener;
                            }
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void setClickListener(View view) {
        if (null != view) {
            boolean hasOnClick = view.hasOnClickListeners();
            LogTool.i("是否有监听：" + hasOnClick);
            if (hasOnClick) {
                final View.OnClickListener listener = getOnClickListener(view);
                if ((listener instanceof View.OnClickListener)) {
                    view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (null != listener) {
                                LogTool.i("复写了OnClickListener方法");
                                // 为了解决activity视图改变，重新遍历
                                // logViewsInfo(rootView);
                                listener.onClick(v);
                            }
                        }
                    });
                }
            }
        }
    }

    /**
     * 更改字体,只能操作AppCompatActivity
     *
     * @param activity
     */
    private void changeFont(final AppCompatActivity activity) {

        LayoutInflaterCompat.setFactory(LayoutInflater.from(activity), new LayoutInflaterFactory() {

            @Override
            public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
                Typeface typeface = null;
                typeface = Typeface.createFromAsset(activity.getAssets(), "fangzhengpaoxiaoti.TTF");
                LogTool.i("name = " + name);
                int n = attrs.getAttributeCount();
                // 得到button或者textview上的字
                String text = null;
                for (int i = 0; i < n; i++) {
                    LogTool.i(attrs.getAttributeName(i) + " , " + attrs.getAttributeValue(i));
                    if (attrs.getAttributeName(i).equals("text")) {
                        text = attrs.getAttributeValue(i);
                    }
                }
                AppCompatDelegate delegate = activity.getDelegate();
                View view = delegate.createView(parent, name, context, attrs);
                if (view != null && (view instanceof TextView)) {
                    ((TextView) view).setTypeface(typeface);
                }
//                if (name.equals("TextView")) {
//                    Button button = new Button(context, attrs);
//                    button.setText(text);
//                    button.setTypeface(typeface);
//                    return button ;
//                }else {
//                    return null ;
//                }
                return view;
            }
        });
    }

}
