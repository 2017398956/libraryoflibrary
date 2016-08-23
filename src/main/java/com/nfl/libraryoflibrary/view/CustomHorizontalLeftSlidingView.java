package com.nfl.libraryoflibrary.view;

import android.app.Activity;
import android.content.Context;

import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.ToastTool;

/**
 * Created by nfl on 2016/8/23.
 */
public class CustomHorizontalLeftSlidingView extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private View displayedView;// 显示的视图
    private View hiddenView;// 被隐藏，等待滑出的视图
    private int dragDistance;// 最大滑动距离
    private boolean viewsFromXML = true ;
    private final double AUTO_OPEN_SPEED_LIMIT = 800.0;
    private int draggedX;

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
        LogTool.i("onMeasure") ;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        dragDistance = hiddenView.getMeasuredWidth();
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
            if(view == displayedView){
                LogTool.i("tryCaptureView:" + displayedView);
            }else if(view == hiddenView){
                LogTool.i("tryCaptureView:" + hiddenView);
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

    /**
     * Implement this method to intercept all touch screen motion events.  This
     * allows you to watch events as they are dispatched to your children, and
     * take ownership of the current gesture at any point.
     *
     * <p>Using this function takes some care, as it has a fairly complicated
     * interaction with {@link View#onTouchEvent(MotionEvent)
     * View.onTouchEvent(MotionEvent)}, and using it requires implementing
     * that method as well as this one in the correct way.  Events will be
     * received in the following order:
     *
     * <ol>
     * <li> You will receive the down event here.
     * <li> The down event will be handled either by a child of this view
     * group, or given to your own onTouchEvent() method to handle; this means
     * you should implement onTouchEvent() to return true, so you will
     * continue to see the rest of the gesture (instead of looking for
     * a parent view to handle it).  Also, by returning true from
     * onTouchEvent(), you will not receive any following
     * events in onInterceptTouchEvent() and all touch processing must
     * happen in onTouchEvent() like normal.
     * <li> For as long as you return false from this function, each following
     * event (up to and including the final up) will be delivered first here
     * and then to the target's onTouchEvent().
     * <li> If you return true from here, you will not receive any
     * following events: the target view will receive the same event but
     * with the action {@link MotionEvent#ACTION_CANCEL}, and all further
     * events will be delivered to your onTouchEvent() method and no longer
     * appear here.
     * </ol>
     *
     * @param ev The motion event being dispatched down the hierarchy.
     * @return Return true to steal motion events from the children and have
     * them dispatched to this ViewGroup through onTouchEvent().
     * The current target will receive an ACTION_CANCEL event, and no further
     * messages will be delivered here.
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 经测试 MotionEvent.ACTION_MOVE 出现的次数不大于2，可看做OnClick事件
        LogTool.i("onInterceptTouchEvent" + ev.getAction()) ;
        /**
         * ViewDragHelper.shouldInterceptTouchEvent(MotionEvent ev)
         *
         * Check if this event as provided to the parent view's onInterceptTouchEvent should
         * cause the parent to intercept the touch event stream.
         *
         * @param ev MotionEvent provided to onInterceptTouchEvent
         * @return true if the parent view should return true from onInterceptTouchEvent
         */
        if(viewDragHelper.shouldInterceptTouchEvent(ev)) {
            LogTool.i("====================") ;
            return true;
        }
        LogTool.i("-----------------------"+ super.onInterceptTouchEvent(ev)) ;
//        return super.onInterceptTouchEvent(ev);
        return false ;
    }


    /**
     * @param event The motion event.
     * @return True if the event was handled, false otherwise.
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        LogTool.i("onTouchEvent" + " | Action:" + event.getAction()) ;
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
}
