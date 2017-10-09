package com.nfl.libraryoflibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by fuli.niu on 2017/9/28.
 */

public class UnScrollExpandableListView extends ExpandableListView {
    public UnScrollExpandableListView(Context context) {
        super(context);
    }

    public UnScrollExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnScrollExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
