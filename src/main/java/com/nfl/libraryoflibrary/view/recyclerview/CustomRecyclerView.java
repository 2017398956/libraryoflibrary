package com.nfl.libraryoflibrary.view.recyclerview;/**
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

import com.nfl.libraryoflibrary.listener.CustomOnClickListener;
import com.nfl.libraryoflibrary.utils.ConvertTool;
import com.nfl.libraryoflibrary.utils.TraceKeeper;

/**
 * Created by fuli.niu 2016/8/22
 */
public class CustomRecyclerView extends RecyclerView {

    private Context context;
    private CustomRecyclerViewDivider customRecyclerViewDivider;
    private OnItemClickListener onItemClickListener;

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
     *
     * @param context
     */
    private void init(Context context) {
        this.context = context;
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

        if (null != onItemClickListener) {
            for (int position = 0; position < getChildCount(); position++) {
                getChildAt(position).setOnClickListener(new CustomOnClickListener<Integer>(position) {
                    @Override
                    public void onClick(View view, Integer position) {
                        super.onClick(view, position);
                        onItemClickListener.onClick(view, position);
                    }
                });
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListenerAdded) {
        onItemClickListener = onItemClickListenerAdded;
    }

    public static abstract class OnItemClickListener {
        public void onClick(View view, int position) {
            TraceKeeper.addTrace(view);
        }
    }

}
