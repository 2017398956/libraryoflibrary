package com.nfl.libraryoflibrary.view.recyclerview;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by fuli.niu on 2017/4/6.
 */

public class CustomRecyclerViewDivider extends RecyclerView.ItemDecoration {

    private Drawable mDivider;
    private int leftMargin, rightMargin, topMargin, bottomMargin;
    private int width, height;
    private int mOrientation;

    public CustomRecyclerViewDivider(Drawable divider, int orientation) {
        setDivider(divider);
        setOrientation(orientation);
    }

    public void setDivider(Drawable divider) {
        this.mDivider = divider;
        if (mDivider == null) {
            mDivider = new ColorDrawable(0x808080);
        }
        width = mDivider.getIntrinsicWidth();
        height = mDivider.getIntrinsicHeight();
    }

    private void setOrientation(int orientation) {
        if (orientation != LinearLayoutManager.HORIZONTAL && orientation != LinearLayoutManager.VERTICAL) {
            throw new IllegalArgumentException("invalid orientation");
        }
        mOrientation = orientation;
    }

    public void setMargin(int left, int top, int right, int bottom) {
        this.leftMargin = left;
        this.topMargin = top;
        this.rightMargin = right;
        this.bottomMargin = bottom;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawVertical(c, parent);
        }
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        final int top = parent.getPaddingTop() + topMargin;
        final int bottom = parent.getHeight() - parent.getPaddingBottom() - bottomMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getRight() + params.rightMargin + leftMargin;
            final int right = left + width;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int left = parent.getPaddingLeft() + leftMargin;
        final int right = parent.getWidth() - parent.getPaddingRight() - rightMargin;

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin + topMargin;
            final int bottom = top + height;
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (mOrientation == LinearLayoutManager.HORIZONTAL) {
            outRect.set(0, 0, leftMargin + width + rightMargin, 0);
        } else {
            outRect.set(0, 0, 0, topMargin + height + bottomMargin);
        }
    }
}
