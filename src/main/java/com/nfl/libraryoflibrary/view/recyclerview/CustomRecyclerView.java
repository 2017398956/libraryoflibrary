package com.nfl.libraryoflibrary.view.recyclerview;
/**
 * Created by fuli.niu on 2016/8/22.
 */

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.nfl.libraryoflibrary.utils.ConvertTool;
import com.nfl.libraryoflibrary.utils.LogTool;
import com.nfl.libraryoflibrary.utils.TraceKeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fuli.niu 2016/8/22
 */
public class CustomRecyclerView<V extends RecyclerView.ViewHolder> extends RecyclerView {

    private Context context;
    private boolean canLoadMore = false;// 是否开启加载更多功能
    private ViewParent parentView;
    private RecyclereViewBaseAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CustomRecyclerViewDivider customRecyclerViewDivider;

    private List<OnItemClickListener> onItemClickListenerList;

    private boolean hasStartAnim = false ;
    private LoadMoreState loadMoreState = LoadMoreState.PREPARE;

    public enum LoadMoreState {
        STARTING, // 正在准备加载更多，此时还在拖拽状态
        LOADING,  // 松开手指展示 加载更多 视图，且在加载完前不能修改
        PREPARE      // 加载更多 准备阶段，即还没有被拖拽
    }

    public CustomRecyclerView(Context context) {
        this(context, null);
    }

    public CustomRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(final Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
        setListeners();
    }

    @Override
    protected void onMeasure(int widthSpec, int heightSpec) {
        super.onMeasure(widthSpec, heightSpec);
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

    private void setListeners() {
        addOnScrollListener(onScrollListener);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        this.adapter = (RecyclereViewBaseAdapter) adapter;
    }

    /**
     * @param adapter
     * @param canLoadMore 是否能上拉加载更多
     */
    public void setAdapter(Adapter adapter, boolean canLoadMore) {
        this.setAdapter(adapter);
        this.canLoadMore = canLoadMore;
    }

    /**
     * 清空所有 onItemClickListener
     *
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

    public void setSwipeRefreshLayoutRefreshing(boolean isRefreshing) {
        if (null != swipeRefreshLayout) {
            swipeRefreshLayout.setRefreshing(isRefreshing);
        }
    }

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == SCROLL_STATE_DRAGGING) {
                if (CustomRecyclerView.this.computeVerticalScrollOffset() > 0 && !canScrollVertically(1)) {
                    // 说明滑到了底部
                    if (null != adapter) {
                        setOnDragListener(onDragListener);
                        setOnTouchListener(onTouchListener);
                    }
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
        }
    };

    private OnDragListener onDragListener = new OnDragListener() {

        @Override
        public boolean onDrag(View v, DragEvent event) {
            return false;
        }
    };

    private OnTouchListener onTouchListener = new OnTouchListener() {


        View footerView;
        ViewGroup.LayoutParams layoutParams;
        float downPointY = 0;
        float heigtTemp = 0;// 为正则表示向上拖拽
        int maxHeight = ConvertTool.dp2px(80);

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            LogTool.i("action:" + event.getAction());
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    LogTool.i("downPointY:" + downPointY);
                    downPointY = event.getRawY();
                    break;
                case MotionEvent.ACTION_UP:
                    downPointY = 0;
                    hasStartAnim = false ;
                    // 如果展示了 加载更多
                    if (loadMoreState == LoadMoreState.STARTING) {
                        loadMoreState = LoadMoreState.LOADING;
                        footerView = getChildAt(getChildCount() - 1);
                        if (null != footerView) {
                            layoutParams = footerView.getLayoutParams();
                            if (null != layoutParams && null != adapter ) {
                                if(layoutParams.height < maxHeight){
                                    // 拖拽距离不够不加载更多
                                    finishLoadMore() ;
                                }else {
                                    // 拖拽距离够，加载更多
                                    layoutParams.height = adapter.footerViewMeasureHeight > 0 ? adapter.footerViewMeasureHeight : maxHeight ;
                                    footerView.setLayoutParams(layoutParams);
                                    adapter.refreshFooterViewLoading();
                                    startLoadMore();
                                    // 模拟加载更多成功
                                    postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            finishLoadMore();
                                        }
                                    } , 4000) ;
                                }
                                layoutParams = null;
                            } else {
                                footerView = null;
                            }
                        }
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    // 在没有捕获到 acton_down 事件时，以第一次 move 的位置作为起点
                    // downPoinntY 在每次手指离开屏幕时都会重置为 0
                    if (downPointY == 0) {
                        downPointY = event.getRawY();
                    }
                    heigtTemp = downPointY - event.getRawY();
                    if (isBottom()) {
                        if (loadMoreState == LoadMoreState.PREPARE) {
                            // 添加 footerView
                            adapter.addFooterView();
                            loadMoreState = LoadMoreState.STARTING ;
                        } else if (loadMoreState == LoadMoreState.STARTING) {
                            footerView = getChildAt(getChildCount() - 1);
                            if (null != footerView) {
                                layoutParams = footerView.getLayoutParams();
                                if (null != layoutParams) {
                                    layoutParams.height += heigtTemp;
                                    // 当 footerView 的高度高于设置的最大高度并且还没有刷新状态时更新 footerView 显示
                                    if(layoutParams.height > maxHeight && !hasStartAnim){
                                        adapter.refreshFooterViewStarting();
                                        hasStartAnim = true ;
                                    }
                                    // 当 footerView 的高度超过 3 倍最大高度时，限制 footerView 的高度
                                    if (layoutParams.height > 3 * maxHeight) {
                                        layoutParams.height = 3 * maxHeight;
                                    }
                                    footerView.setLayoutParams(layoutParams);
                                    layoutParams = null;
                                }
                                footerView = null;
                            }
                        }
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * 开始加载更多，网络请求等在这里处理
     */
    public void startLoadMore() {

    }

    /**
     * 加载更多 成功或失败后关闭 footerView
     */
    public void finishLoadMore() {
        loadMoreState = LoadMoreState.PREPARE ;
        if(null != adapter){
            adapter.closeFooterView();
        }
    }

    private boolean isBottom() {
        return CustomRecyclerView.this.computeVerticalScrollOffset() > 0 && !canScrollVertically(1);
    }

    public boolean isCanLoadMore() {
        return canLoadMore;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public LoadMoreState getLoadMoreState() {
        return loadMoreState;
    }

    public void setLoadMoreState(LoadMoreState loadMoreState) {
        this.loadMoreState = loadMoreState;
    }
}
