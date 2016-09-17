package com.nfl.libraryoflibrary.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
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

/**
 * Created by fuli.niu on 2016/8/30.
 */
@TargetApi(15)
public class CustomActivityLifecycleCallbacks implements Application.ActivityLifecycleCallbacks{

    private String activityName ;
    private int viewLevel = 2 ;// 默认为2
    private ViewGroup rootView ;// DecorView
    private Context context ;
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(final Activity activity) {
        context = activity ;
        activityName = activity.getComponentName().getClassName() ;
        LogTool.i("ActivityName:" + activityName) ;
        rootView = (ViewGroup) activity.getWindow().getDecorView() ;

//        view.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                LogTool.i("======onTouch======") ;
//                getRootViewOnTouchListener(v) ;
//                return false;
//            }
//        });
        if(null != rootView){
            logViewsInfo(rootView);
            viewLevel = 2 ;// logViewsInfo(View view)会改变viewLevel的值，这里恢复默认值
        }else{
            LogTool.i(activityName + "'s DecorView is null.") ;
        }

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

    private boolean isViewGroup(View view){
        if(null != view){
//            if(view.getClass().isAssignableFrom(ViewGroup.class)){
//                return true ;
//            }
//            LogTool.i(ViewGroup.class + "|------s------|" + view.getClass().toString()) ;
            if(view instanceof ViewGroup){
                return true ;
            }
        }
        return false ;
    }

    /**
     * 打印view的信息
     * @param viewGroup
     */
    private void logViewsInfo(ViewGroup viewGroup){
        if(2 == viewLevel){
            viewGroup.setTag(R.id.id_view_level_tag_key , 2);
            viewGroup.setTag(R.id.id_view_parent_tag_key , 0);
            LogTool.i("|-DecorView:" + rootView.getClass()) ;
        }
        int count = viewGroup.getChildCount() ;
        if(count > 0 ){
            for (int i = 0 ; i < count ; i++){
                View view = viewGroup.getChildAt(i) ;
                view.setTag(R.id.id_view_level_tag_key , (Integer) viewGroup.getTag(R.id.id_view_level_tag_key) + 1);
                String logHead = viewGroup.getTag(R.id.id_view_parent_tag_key) + "|";
                for(int l = 0 ; l < (Integer) view.getTag(R.id.id_view_level_tag_key) ; l++){
                    logHead += "-" ;
                }
                LogTool.i(logHead + view.getClass().toString()) ;
                if(isViewGroup(view)){
                    viewGroup.setTag(R.id.id_view_parent_tag_key , i);

//                    LogTool.i("ViewGroup:" + view.getClass().toString()) ;
//                    view.setTag(R.id.id_tag_key , viewLevel );
                    logViewsInfo((ViewGroup) view);
                }else{
                    if(canResetOnClickListener(view)){
                        setClickListener(view);
                    }
                }
            }
            viewLevel++ ;
        }
    }

    private boolean canResetOnClickListener(View view){
        if(view instanceof Button || view instanceof TextView || view instanceof ImageButton || view instanceof ImageView){
            String viewName = null ;
            try{
                int viewID = view.getId() ;
                viewName = context.getResources().getResourceName(viewID) ;
            }catch (Exception e){

            }
            LogTool.i("应该替换OnClickListener的View：" + view.getClass().getSimpleName() + " | name:" + viewName);
            return true ;
        }
        return false ;
    }

    private View.OnTouchListener getRootViewOnTouchListener(View view){
        try {
            Method method = View.class.getDeclaredMethod("getListenerInfo", new Class<?>[]{});
            if (null != method) {
                method.setAccessible(true);
                Object object = method.invoke(view, new Class<?>[]{});// 得到ListenerInfo
                if (null != object) {
                    Class listenerInfoClazz = object.getClass();
                    // 得到ListenerInfo中的mOnFocusChangeListener
                    Field mOnTouchListener = listenerInfoClazz.getDeclaredField("mOnTouchListener");
                    if (null != mOnTouchListener) {
                        mOnTouchListener.setAccessible(true);
                        Object listener = mOnTouchListener.get(object);
                        if (null != listener && listener instanceof View.OnTouchListener) {
                            return (View.OnTouchListener) listener;
                        }else {
                            LogTool.i("替换失败");
                        }
                    }else{
                        LogTool.i("mOnTouchListenerr获取失败");
                    }
                }else{
                    LogTool.i("mOnTouchListener执行失败");
                }
            }else{
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
        return null ;
    }

    /**
     * 通过反射获取View的OnClickListener方法
     * @param view
     * @return
     */
    private View.OnClickListener getOnClickListener(View view) {
        boolean hasOnClick = view.hasOnClickListeners();
        if (hasOnClick) {
            try {
                Method method = View.class.getDeclaredMethod("getListenerInfo", new Class<?>[]{});
                if (null != method) {
                    method.setAccessible(true);
                    Object object = method.invoke(view, null);// 得到ListenerInfo
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
//                                    LogTool.i("复写了OnClickListener方法:" + );
//                                    v.setTag();
                                // 为了解决activity视图改变，重新遍历
                                logViewsInfo(rootView);
                                listener.onClick(v);
                            }
                        }
                    });
                }
            }
        }
    }

}
