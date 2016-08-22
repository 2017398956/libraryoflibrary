package com.nfl.libraryoflibrary.view;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.nfl.libraryoflibrary.utils.PhoneInfoTool;

/**
 * Created by nfl on 2016/08/22.
 */
public class CustomHorizontalLeftSlidingView extends LinearLayout {

    private ViewDragHelper viewDragHelper;
    private View displayedView;
    private View hiddenView ;
    private int dragDistance;
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

    public CustomHorizontalLeftSlidingView(Context context , View displayedView , View hiddenView){
        this(context, null , -1);
        this.addView(displayedView);
        this.addView(hiddenView);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        displayedView = getChildAt(0);
        hiddenView = getChildAt(1);
        hiddenView.setVisibility(GONE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        dragDistance = hiddenView.getMeasuredWidth();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(viewDragHelper.shouldInterceptTouchEvent(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        viewDragHelper.processTouchEvent(event);
        return true;
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(viewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragHelperCallback extends ViewDragHelper.Callback {

        @Override
        public boolean tryCaptureView(View view, int i) {
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

}
