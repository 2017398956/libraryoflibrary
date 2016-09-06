package com.nfl.libraryoflibrary.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by fuli.niu on 2016/9/1.
 */
public class CustomPullAndDragRefreshListView extends ListView {

    public CustomPullAndDragRefreshListView(Context context) {
        this(context, null);
    }

    public CustomPullAndDragRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

}