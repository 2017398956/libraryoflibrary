package com.nfl.libraryoflibrary.view;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.nfl.libraryoflibrary.R;

/**
 * Created by nfl on 2016/8/23.
 */
public class CustomHorizontalLeftSlidingView2 {

    private View displayedView ;
    private View hiddenView ;
    private LinearLayout ll_displayed ;
    private LinearLayout ll_hidden ;
    private CustomHorizontalLeftSlidingView customHorizontalLeftSlidingView ;
    public CustomHorizontalLeftSlidingView2(Context context , View displayedView , View hiddenView){
        this.displayedView = displayedView ;
        this.hiddenView = hiddenView ;
        customHorizontalLeftSlidingView = (CustomHorizontalLeftSlidingView) LayoutInflater.from(context).inflate(R.layout.view_horizontal_left_sliding , null) ;
        customHorizontalLeftSlidingView.setDragDistance(hiddenView.getWidth());
        ll_displayed = (LinearLayout) customHorizontalLeftSlidingView.findViewById(R.id.ll_displayed) ;
        ll_hidden = (LinearLayout) customHorizontalLeftSlidingView.findViewById(R.id.ll_hidden) ;
        ll_displayed.removeAllViews();
        ll_hidden.removeAllViews();
        ll_displayed.addView(displayedView);
        ll_hidden.addView(hiddenView);
    }

    public CustomHorizontalLeftSlidingView getView(){
        return customHorizontalLeftSlidingView ;
    }
}
