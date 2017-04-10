package com.nfl.libraryoflibrary.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nfl.libraryoflibrary.listener.CustomOnClickListener;

import java.util.List;

/**
 * Created by fuli.niu on 2017/4/7.
 */

public abstract class RecyclereViewBaseAdapter<T extends RecyclereViewBaseAdapter.BaseViewHolder>
        extends RecyclerView.Adapter<T> {

    private CustomRecyclerView customRecyclerView;
    private List<CustomRecyclerView.OnItemClickListener> onItemClickListenerList;

    @Override
<<<<<<< HEAD
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null == parent) {
            customRecyclerView = (CustomRecyclerView) parent;
=======
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null != parent) {
            // 初始化
            customRecyclerView = (CustomRecyclerView) parent;
            onItemClickListenerList = customRecyclerView.getOnItemClickListenerList();
>>>>>>> 746daaf2f49056f09f0f3f588a5c7a63d42d4991
        }
        return (BaseViewHolder) onCreateViewHolder2(parent, viewType);
    }

    public abstract T onCreateViewHolder2(ViewGroup parent, int viewType);

    @Override
<<<<<<< HEAD
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if (null != holder && null != holder.getItemView()) {
            holder.getItemView().setOnClickListener(null);
            holder.getItemView().setOnClickListener(new CustomOnClickListener<Integer>(position) {
                @Override
                public void onClick(View view, Integer integer) {
                    super.onClick(view, integer);
                    if (null != customRecyclerView.getOnItemClickListener()) {
                        customRecyclerView.getOnItemClickListener().onClick(view, integer);
                    }
                }
            });
        }
        onBindViewHolder2((T) holder, position);
    }

    public abstract void onBindViewHolder2(T holder, int position);

=======
    public void onBindViewHolder(T holder, int position) {
        holder.getCustomOnClickListener().setT(position);
    }

>>>>>>> 746daaf2f49056f09f0f3f588a5c7a63d42d4991
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

}
