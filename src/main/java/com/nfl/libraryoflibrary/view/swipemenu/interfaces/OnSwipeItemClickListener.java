package com.nfl.libraryoflibrary.view.swipemenu.interfaces;

import com.nfl.libraryoflibrary.view.swipemenu.bean.SwipeMenu;
import com.nfl.libraryoflibrary.view.swipemenu.view.SwipeMenuView;

public interface OnSwipeItemClickListener {

    void onItemClick(SwipeMenuView view, SwipeMenu menu, int index);

}