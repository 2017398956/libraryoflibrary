package com.nfl.libraryoflibrary.view.recyclerview;
/**
 * Created by fuli.niu on 2016/8/22.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.nfl.libraryoflibrary.utils.ConvertTool;
import com.nfl.libraryoflibrary.utils.TraceKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu 2016/8/22
 */
public class CustomRecyclerView extends RecyclerView {

    private Context context;
    private CustomRecyclerViewDivider customRecyclerViewDivider;

    private List<OnItemClickListener> onItemClickListenerList;

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    /**
     * 如果没有其他操作，默认添加水平分割线，竖直分布
     * 仿 ListView
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
        onItemClickListenerList = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(context);
        setHasFixedSize(true);
        customRecyclerViewDivider = new CustomRecyclerViewDivider(new ColorDrawable(0xeeeeeeee), OrientationHelper.VERTICAL);
        //单位:px
        customRecyclerViewDivider.setMargin(0, 0, 0, 0);
        customRecyclerViewDivider.setHeight(ConvertTool.dp2px(1));
        addItemDecoration(customRecyclerViewDivider);
        setLayoutManager(manager);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    /**
     * 清空所有 onItemClickListener
     * @param onItemClickListener
     */
    public void addOnItemClickListener(OnItemClickListener onItemClickListener) {
        if (null != onItemClickListener) {
            onItemClickListenerList.add(onItemClickListener);
        }
    }

    public void clearAllOnItemClickListeners() {
        onItemClickListenerList.clear();
    }

    public static abstract class OnItemClickListener {

        public void onClick(View view, int position) {
            TraceKeeper.addTrace(view);
        }
    }

    public List<OnItemClickListener> getOnItemClickListenerList() {
        return onItemClickListenerList;
    }

    public CustomRecyclerViewDivider getCustomRecyclerViewDivider() {
        return customRecyclerViewDivider;
    }
}
