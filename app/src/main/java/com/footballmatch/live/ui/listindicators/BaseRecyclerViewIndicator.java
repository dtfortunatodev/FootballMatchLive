package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.footballmatch.live.data.model.BaseEntity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;

/**
 * Created by David Fortunato on 03/08/2016
 * All rights reserved GoodBarber
 */
public abstract class BaseRecyclerViewIndicator<V extends View, ObjectData extends BaseEntity, ViewHolder extends BaseRecyclerViewHolder<V>>
{

    // Data
    private int viewType = 0;
    private float viewWidth = 1;
    private ObjectData objectData;


    public BaseRecyclerViewIndicator(ObjectData objectData)
    {
        this.objectData = objectData;
    }

    /**
     * Get view type identification (this view type should be sequential to use on adapter)
     * @return View type identification
     */
    public int getViewType()
    {
        return viewType;
    }

    /**
     * Set view type identification (this view type should be sequential to use on adapter)
     * @param viewType View type identification
     */
    public void setViewType(int viewType)
    {
        this.viewType = viewType;
    }

    /**
     * Get UI params id (This is used to get differents UI Parameters for the same Indicator type)
     * @return By default is the ViewType
     */
    public String getUIParamsId()
    {
        return String.valueOf(getViewType());
    }

    /**
     * Width in percentage that the view occupy
     * @return Width in percentage (0.0 - 1.0)
     */
    public float getViewWidth()
    {
        return viewWidth;
    }

    /**
     * Width in percentage that the view occupy
     * @param viewWidth Width in percentage (0.0 - 1.0)
     */
    public void setViewWidth(float viewWidth)
    {
        if(viewWidth > 1)
        {
            viewWidth = 1;
        } else if (viewWidth < 0)
        {
            viewWidth = 0;
        }

        this.viewWidth = viewWidth;
    }

    /**
     * Generate the view to display on ListVIew
     * @param context Current context (not application context)
     * @return Generated view
     */
    public abstract V getViewCell(Context context, ViewGroup parent);

    /**
     * Refresh Cell Position
     * @param recyclerViewHolder View holder to refresh data
     * @param position Current position
     * @param columnPosition Column position is used to identify the column when using a Grid Layout
     */
    public abstract void refreshCell(ViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition);

    /**
     * Get object data used to complete UI. This method is important for Open Product in the case
     * the developer wants to override the List Indicator using the same data
     * @return Object data
     */
    public ObjectData getObjectData()
    {
        return objectData;
    }

    /**
     * Generated View Holder
     * @param view Generated view to put in the ViewHolder
     * @return View Holder generated (this is a object that extends the GBRecyclerViewHolder)
     */
    public abstract ViewHolder getRecycleViewHolder(V view);

}
