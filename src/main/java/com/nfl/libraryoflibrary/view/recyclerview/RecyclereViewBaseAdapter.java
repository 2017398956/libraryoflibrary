package com.nfl.libraryoflibrary.view.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.nfl.libraryoflibrary.listener.CustomOnClickListener;

/**
 * Created by fuli.niu on 2017/4/7.
 */

public abstract class RecyclereViewBaseAdapter<T extends RecyclereViewBaseAdapter.BaseViewHolder>
        extends RecyclerView.Adapter<RecyclereViewBaseAdapter.BaseViewHolder> {

    private CustomRecyclerView customRecyclerView;

    @Override
    public T onCreateViewHolder(ViewGroup parent, int viewType) {
        if(null == parent){
            customRecyclerView = (CustomRecyclerView) parent ;
        }
        return null;
    }

    @Override
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
        onBindViewHolder2((T) holder , position);
    }

    public abstract void onBindViewHolder2(T holder, int position) ;

    public class BaseViewHolder extends RecyclerView.ViewHolder {

        private View itemView;

        public BaseViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
        }

        public View getItemView() {
            return itemView;
        }
    }

}
