package com.nfl.libraryoflibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;

/**
 * 由于解决 ScrollView  嵌套 ViewPager 时，ViewPager 要重新计算高度的问题
 */
public class WrapContentViewPager extends ViewPager {


    /************************* 这里用来精确测量 ViewPager 的高度 ************************/
    /************************* 主要用于解决 ScrollView 嵌套 ViewPager 时某个 ************************/
    /************************* Fragment 内容太少，导致向上滚动出屏幕的问题 ************************/

    private int current;// 当前 fragment 的位置
    private int mHeight = 0;// ViewPager 将要被置为的高度
    private int minHeight = -1;
    /**
     * 保存position与对于的View
     */
    private HashMap<Integer, View> mChildrenViews = new HashMap<Integer, View>();

    private boolean exactlyMeasure = false;// 为了兼容以前的版本，这里默认为 false

    /**
     * 重新设置 ViewPager 的高度
     * 当切换 Fragment 的时候要主动调用这个方法
     *
     * @param current 当前 fragment 的位置
     */
    public void resetHeight(int current) {
        if (minHeight > 0 && minHeight > mHeight) {
            mHeight = minHeight;
        }
        this.current = current;
        if (mChildrenViews.size() > current) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
            } else {
                layoutParams.height = mHeight;
            }
            setLayoutParams(layoutParams);
        }
    }

    /**
     * 保存 position 与对于的View
     * Fragment 加载 View 后要调用这个方法来进行绑定
     */
    public void setObjectForPosition(int position, View view) {
        mChildrenViews.put(position, view);
    }

    /**
     * 是否开启精确测量，默认为 false
     *
     * @param exactlyMeasure
     */
    public void setExactlyMeasure(boolean exactlyMeasure) {
        this.exactlyMeasure = exactlyMeasure;
    }

    /**
     * 主要用于解决 ScrollView 嵌套 ViewPager 时，某个 Fragment 的内容过少导致，TabLayout 无法悬浮的问题
     * 或其它需要 ViewPager 有一定高度的情况，只在精确测试时使用
     *
     * @param minHeight
     */
    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    /*********************************************************************************/

    public WrapContentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapContentViewPager(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        if (exactlyMeasure) {
            // 新的处理方式
            if (mChildrenViews.size() > current) {
                View child = mChildrenViews.get(current);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                mHeight = child.getMeasuredHeight();
            }
            if (minHeight > 0 && minHeight > mHeight) {
                mHeight = minHeight;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.EXACTLY);
        } else {
            // 将旧版本的处理方式放在这里
            int height = 0;
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
                int h = child.getMeasuredHeight();
                if (h > height) height = h;
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
