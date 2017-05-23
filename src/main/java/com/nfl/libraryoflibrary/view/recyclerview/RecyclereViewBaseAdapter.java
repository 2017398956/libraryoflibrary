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

    private CustomRecyclerView<RecyclerView.ViewHolder> customRecyclerView;
    private List<CustomRecyclerView.OnItemClickListener> onItemClickListenerList;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if (null != parent) {
            // 初始化
            customRecyclerView = (CustomRecyclerView<RecyclerView.ViewHolder>) parent;
            onItemClickListenerList = customRecyclerView.getOnItemClickListenerList();
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

}
