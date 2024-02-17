package com.nfl.libraryoflibrary.view;

import android.content.Context;

import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.nfl.libraryoflibrary.utils.LogTool;

/**
 * Created by nfl on 2016/8/23.
 */
public class CustomHorizontalLeftSlidingView extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private View displayedView;// 显示的视图
    private int displayedViewMoveCount = 0 ;
    private View hiddenView;// 被隐藏，等待滑出的视图
    private int dragDistance;// 最大滑动距离
    private boolean viewsFromXML = true ;
    private final double AUTO_OPEN_SPEED_LIMIT = 400.0;// 滑动灵敏度，越小越灵敏，需要滑动的距离越小
    private int draggedX;
    private OnClickCallBack clickCallBack ;

    public CustomHorizontalLeftSlidingView(Context context) {
        this(context, null);
    }

    public CustomHorizontalLeftSlidingView(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomHorizontalLeftSlidingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        viewDragHelper = ViewDragHelper.create(this, new DragHelperCallback());
    }

    /**
     * XMl中所有视图都加载完毕后，执行该方法
     */
    @Override
    protected void onFinishInflate() {
        LogTool.i("onFinishInflate") ;
        super.onFinishInflate();
        if(viewsFromXML){
            displayedView = getChildAt(0);
            hiddenView = getChildAt(1);
            hiddenView.setVisibility(GONE);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        dragDistance = hiddenView.getMeasuredWidth();
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            if(view == displayedView){
                LogTool.i("tryCaptureView:displayedView");
            }else if(view == hiddenView){
                LogTool.i("tryCaptureView:hiddenView" );
            }else{
                LogTool.i("tryCaptureView:other");
            }
            return view == displayedView || view == hiddenView;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            draggedX = left;
            if (changedView == displayedView) {
                hiddenView.offsetLeftAndRight(dx);
            } else {
                displayedView.offsetLeftAndRight(dx);
            }
            if (hiddenView.getVisibility() == View.GONE) {
                hiddenView.setVisibility(View.VISIBLE);
            }
            invalidate();
        }

        @Override
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            if (child == displayedView) {
                final int leftBound = getPaddingLeft();
                final int minLeftBound = -leftBound - dragDistance;
                final int newLeft = Math.min(Math.max(minLeftBound, left), 0);
                return newLeft;
            } else {
                final int minLeftBound = getPaddingLeft() + displayedView.getMeasuredWidth() - dragDistance;
                final int maxLeftBound = getPaddingLeft() + displayedView.getMeasuredWidth() + getPaddingRight();
                final int newLeft = Math.min(Math.max(left, minLeftBound), maxLeftBound);
                return newLeft;
            }
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            return dragDistance;
        }

        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            super.onViewReleased(releasedChild, xvel, yvel);
            boolean settleToOpen = false;
            if (xvel > AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = false;
            } else if (xvel < -AUTO_OPEN_SPEED_LIMIT) {
                settleToOpen = true;
            } else if (draggedX <= -dragDistance / 2) {
                settleToOpen = true;
            } else if (draggedX > -dragDistance / 2) {
                settleToOpen = false;
            }

            final int settleDestX = settleToOpen ? -dragDistance : 0;
            viewDragHelper.smoothSlideViewTo(displayedView, settleDestX, 0);
            ViewCompat.postInvalidateOnAnimation(CustomHorizontalLeftSlidingView.this);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        LogTool.i("onInterceptTouchEvent" + ev.getAction());
        if(viewDragHelper.shouldInterceptTouchEvent(ev)) {
            // 如果 ViewDragHelper 应该拦截触摸事件，则触摸事件不会分发到子 View。
            // 即：处理滑动事件
            // 现在问题：displayedView 滑动时不会触法该事件，hiddenView 滑动会调用该事件
            // 原因：displayedView 滑动时 ev.getAction()为 ACTION_DOWN,
            //       hidden 滑动时 ev.getAction()为 ACTION_MOVE 。
            // 这是由于 hidden 设置 OnClickListener 等触摸类监听方法时，hidden 是 GONE状态
            // 而 displayedView 设置 OnClickListener 等触摸类监听方法时，displayedView 是VISIBLE状态
            LogTool.i("====================") ;
            return true;
        }
        // 由于 super.onInterceptTouchEvent(ev) = false ，所以直接返回false即可
        return true ;
    }


    /**
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN){
            // 解决滑动后 ACTION_UP 没有被触法，displayedViewMoveCount 没清空的问题
            // 所以，重置操作不能放在ACTION_UP触法时进行。ACTION_DOWN一定会触法
            displayedViewMoveCount = 0 ;
        }
        if(event.getAction() == MotionEvent.ACTION_MOVE){
            displayedViewMoveCount++ ;
        }
        LogTool.i("onTouchEvent" + " | Action:" + event.getAction() + " | displayedViewMoveCount:" + displayedViewMoveCount) ;

        if(event.getAction() == MotionEvent.ACTION_UP){
            // 经测试 MotionEvent.ACTION_MOVE 出现的次数不大于2，可看做OnClick事件
            if(displayedViewMoveCount <= 2){
                if(hiddenView.getVisibility() == View.VISIBLE){
                    resetView();
                }
                if(null != clickCallBack){
                    LogTool.i("displayedView has been clicked .") ;
                    clickCallBack.OnClickListener(this);
                }
            }
        }
//        if (event.getAction() == MotionEvent.ACTION_SCROLL){
//            displayedView.setClickable(false);
//            viewDragHelper.processTouchEvent(event);
//            return true ;
//        }else {
//            displayedView.setClickable(true);
//            return false ;
//        }
        /**
         * ViewDragHelper.processTouchEvent(MotionEvent ev)
         *
         * Process a touch event received by the parent view. This method will dispatch callback events
         * as needed before returning. The parent view's onTouchEvent implementation should call this.
         *
         * @param ev The touch event received by the parent view
         */
        viewDragHelper.processTouchEvent(event);
        return true ;
    }

    /**
     * Called by a parent to request that a child update its values for mScrollX
     * and mScrollY if necessary. This will typically be done if the child is
     * animating a scroll using a {@link android.widget.Scroller Scroller}
     * object.
     */
    @Override
    public void computeScroll() {
        LogTool.i("computeScroll") ;
        super.computeScroll();
        /**
         * ViewDragHelper.continueSettling(boolean deferCallbacks)
         *
         * Move the captured settling view by the appropriate amount for the current time.
         * If <code>continueSettling</code> returns true, the caller should call it again
         * on the next frame to continue.
         *
         * @param deferCallbacks true if state callbacks should be deferred via posted message.
         *                       Set this to true if you are calling this method from
         *                       {@link android.view.View#computeScroll()} or similar methods
         *                       invoked as part of layout or drawing.
         * @return true if settle is still in progress
         */
        if(viewDragHelper.continueSettling(true)) {
            /**
             * ViewCompat.postInvalidateOnAnimation(View view)
             *
             * <p>Cause an invalidate to happen on the next animation time step, typically the
             * next display frame.</p>
             *
             * <p>This method can be invoked from outside of the UI thread
             * only when this View is attached to a window.</p>
             *
             * @param view View to invalidate
             */
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setDragDistance(int dragDistance) {
        this.dragDistance = dragDistance;
    }

    /**
     * displayedView的OnClickListener方法
     */
    public  interface OnClickCallBack{
        void OnClickListener(View view);
    }

    /**
     * displayedView的OnClickListener方法 *必须* 通过该方法实现
     * @param callBack 自定义点击事件
     */
    public void setDisplayedViewOnClickListener(OnClickCallBack callBack){
        this.clickCallBack = callBack ;
    }

    /**
     * 恢复初始状态
     */
    public void resetView(){
        viewDragHelper.smoothSlideViewTo(displayedView, 0 , 0);
        ViewCompat.postInvalidateOnAnimation(CustomHorizontalLeftSlidingView.this);
    }
}
