package com.footballmatch.live.ui.viewholders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by David Fortunato on 03/08/2016
 * All rights reserved GoodBarber
 */
public class BaseRecyclerViewHolder<T extends View>  extends RecyclerView.ViewHolder
{


    public BaseRecyclerViewHolder(T itemView)
    {
        super(itemView);
    }

    public T getItemView()
    {
        return (T) itemView;
    }

    public Context getContext()
    {
        return getItemView().getContext();
    }

}
