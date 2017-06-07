package com.nfl.libraryoflibrary.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nfl.libraryoflibrary.R;
import com.nfl.libraryoflibrary.listener.CustomOnClickListener;
import com.nfl.libraryoflibrary.utils.LogTool;

import java.util.List;

/**
 * Created by fuli.niu on 2017/4/7.
 */

public abstract class RecyclereViewBaseAdapter<T extends RecyclereViewBaseAdapter.BaseViewHolder>
        extends RecyclerView.Adapter<T> {

    private CustomRecyclerView<RecyclerView.ViewHolder> customRecyclerView;
    private List<CustomRecyclerView.OnItemClickListener> onItemClickListenerList;
    protected View footerView;
    private ProgressBar progressbar;
    private ImageView iv_arrow;
    private TextView tv_info;
    protected int footerViewMeasureHeight;
    protected int VIEW_TYPE_ITEM = 0;
    protected int VIEW_TYPE_FOOTER = 1;

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {

        if (null == mRotateUpAnim) {
            mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateUpAnim.setFillAfter(true);
        }
        if (null == mRotateDownAnim) {
            mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
            mRotateDownAnim.setFillAfter(true);
        }

        if (null != parent) {
            // 初始化
            customRecyclerView = (CustomRecyclerView<RecyclerView.ViewHolder>) parent;
            onItemClickListenerList = customRecyclerView.getOnItemClickListenerList();
        }
        if (null != customRecyclerView && footerView == null) {
            footerView = LayoutInflater.from(customRecyclerView.getContext()).inflate(R.layout.view_recycler_view_footer, customRecyclerView, false);
            footerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            progressbar = (ProgressBar) footerView.findViewById(R.id.progressbar);
            iv_arrow = (ImageView) footerView.findViewById(R.id.iv_arrow);
            tv_info = (TextView) footerView.findViewById(R.id.tv_info);
            ViewGroup.LayoutParams layoutParams = footerView.getLayoutParams();
            if (null != layoutParams) {
                footerViewMeasureHeight = layoutParams.height;
                LogTool.i("footerViewMeasureHeight:" + footerViewMeasureHeight);
                layoutParams.height = 0;
                footerView.setLayoutParams(layoutParams);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(T holder, int position) {
        holder.getCustomOnClickListener().setT(position);
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private View itemView;
        private CustomOnClickListener customOnClickListener = new CustomOnClickListener<Integer>(0) {
            @Override
            public void onClick(View view, Integer integer) {
                super.onClick(view, integer);
                if (null != onItemClickListenerList && onItemClickListenerList.size() > 0) {
                    for (CustomRecyclerView.OnItemClickListener onItemClickListener : onItemClickListenerList) {
                        onItemClickListener.onClick(view, integer);
                    }
                }
            }
        };

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.itemView.setOnClickListener(customOnClickListener);
        }

        public CustomOnClickListener getCustomOnClickListener() {
            return customOnClickListener;
        }

    }

    /**
     * 自定义 footerView 时不要调用这个方法
     */
    protected void addFooterView() {
        if (null != footerView) {
            progressbar.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            tv_info.setText(R.string.xlistview_footer_hint_normal);
        }
    }

    protected void refreshFooterViewStarting() {
        if (null != footerView) {
            progressbar.setVisibility(View.INVISIBLE);
            iv_arrow.setVisibility(View.VISIBLE);
            iv_arrow.startAnimation(mRotateUpAnim);
            tv_info.setText(R.string.xlistview_footer_hint_ready);
        }
    }

    protected void refreshFooterViewLoading() {
        if (null != footerView) {
            progressbar.setVisibility(View.VISIBLE);
            iv_arrow.clearAnimation();
            iv_arrow.setVisibility(View.INVISIBLE);
            tv_info.setText(R.string.xlistview_header_hint_loading);
        }
    }

    protected void closeFooterView() {
        if (null != footerView) {
            ViewGroup.LayoutParams layoutParams = footerView.getLayoutParams();
            if (null != layoutParams) {
                layoutParams.height = 0;
                footerView.setLayoutParams(layoutParams);
            }
        }
    }

//    public void addFooterView(View view) {
//        if (null != view) {
//            footerView = view;
//        }
//        addFooterView();
//    }

}
