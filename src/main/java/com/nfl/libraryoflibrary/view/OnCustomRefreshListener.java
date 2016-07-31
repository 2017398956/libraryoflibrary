package com.nfl.libraryoflibrary.view;

/**
 * Created by niu on 2016/8/1.
 */
public interface OnCustomRefreshListener {

    /**
     * 下拉刷新
     */
    void onDownPullRefresh();

    /**
     * 上拉加载更多
     */
    void onLoadingMore();
}
