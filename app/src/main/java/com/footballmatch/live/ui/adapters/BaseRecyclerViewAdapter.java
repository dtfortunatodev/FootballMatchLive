package com.footballmatch.live.ui.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.footballmatch.live.ui.listindicators.BaseRecyclerViewIndicator;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by David Fortunato on 03/08/2016
 * All rights reserved GoodBarber
 */
public abstract class BaseRecyclerViewAdapter<ObjectData> extends RecyclerView.Adapter<BaseRecyclerViewHolder>
{
    //Context
    protected Context mContext;

    // Configs
    private AdapterConfigs             mAdapterConfigs;
    private RecyclerView.LayoutManager mLayoutManager;

    // UI Parameters map
    private Map<Integer, BaseRecyclerViewIndicator> mRecycleViewIndicatorTypes; // Map with <TypeView, BaseRecyclerViewIndicator> to generate the ViewHolder
    private List<BaseRecyclerViewIndicator>         mListBaseRecyclerViewIndicatores; // List of recycle views

    // Listener
    private OnClickRecyclerAdapterViewListener onClickRecyclerAdapterViewListener;

    public BaseRecyclerViewAdapter(Context activity)
    {
        super();
        this.mContext = activity;

        // Init Collections
        this.mAdapterConfigs = new AdapterConfigs();
        this.mListBaseRecyclerViewIndicatores = new ArrayList<>();
        this.mRecycleViewIndicatorTypes = new HashMap<>();
    }

    public BaseRecyclerViewAdapter(Context activity, AdapterConfigs adapterConfigs)
    {
        super();
        this.mContext = activity;

        // Init Collections
        this.mAdapterConfigs = adapterConfigs;
        this.mListBaseRecyclerViewIndicatores = new ArrayList<>();
        this.mRecycleViewIndicatorTypes = new HashMap<>();
    }


    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // List Indicator ViewHolder
        BaseRecyclerViewIndicator baseRecyclerViewIndicator = getRecycleViewIndicatorByType(viewType);

        if (baseRecyclerViewIndicator != null)
        {
            // Create and initialize View
            View view = baseRecyclerViewIndicator.getViewCell(mContext, parent);

            // Get ViewHolder
            BaseRecyclerViewHolder viewHolder = baseRecyclerViewIndicator.getRecycleViewHolder(view);

            return viewHolder;
        }
        else
        {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final BaseRecyclerViewHolder holder, int position)
    {
        final int listIndicatorPosition = getListIndicatorPosition(position);

        // Only refresh List Indicator views
        if (listIndicatorPosition >= 0)
        {
            final BaseRecyclerViewIndicator baseRecyclerViewIndicator = getRecycleViewIndicator(listIndicatorPosition);

            if (baseRecyclerViewIndicator != null)
            {

                // On Click listener
                if (onClickRecyclerAdapterViewListener != null)
                {
                    holder.itemView.setOnClickListener(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            // Callback on listener
                            if (onClickRecyclerAdapterViewListener != null)
                            {
                                onClickRecyclerAdapterViewListener.onItemClick(v, listIndicatorPosition);
                            }
                        }
                    });
                }

                // Refresh Indicator
                baseRecyclerViewIndicator.refreshCell(holder, this, listIndicatorPosition, getColumnPositionOfAdapterPosition(position));

                // Setup Layout Params
                switch (getLayoutManagerType())
                {

                    case STAGGERED_GRID_LAYOUT:
                        if (getAdapterConfigs().getScrollOrientation() == LinearLayout.VERTICAL)
                        {
                            // Update the fullspan in the view
                            StaggeredGridLayoutManager.LayoutParams lpStaggered = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                            if (baseRecyclerViewIndicator != null && baseRecyclerViewIndicator.getViewWidth() == 1)
                            {
                                lpStaggered.setFullSpan(true);
                            }
                            else
                            {
                                lpStaggered.setFullSpan(false);
                            }

                            holder.itemView.setLayoutParams(lpStaggered);
                        }

                        break;
                }
            }


        }

    }

    @Override
    public int getItemCount()
    {
        return getListRecycleViewIndicatores().size();
    }

    @Override
    public int getItemViewType(int position)
    {
        int itemViewType = getRecycleViewIndicator(position).getViewType();
        return itemViewType;
    }

    /***********************************  Methods *********************************************/

    /**
     * Identify the column position an adapter position to a Column Position
     *
     * @param adapterPosition Real position in the adapter list
     *
     * @return Column position (starts in position 0 (zero))
     */
    public int getColumnPositionOfAdapterPosition(int adapterPosition)
    {
        int listIndicatorPosition = getListIndicatorPosition(adapterPosition);

        if (listIndicatorPosition < 0 || getLayoutManagerType() == RecyclerLayoutManagerType.LINEAR_LAYOUT || getColumnsCount() <= 1)
        {
            return 0;
        }
        else
        {
            // Convert list indicator position in Column position
            int columnPosition = listIndicatorPosition % getColumnsCount();
            return columnPosition;
        }
    }

    /**
     * Get or initilize the list indicators
     *
     * @return List of ListIndicators
     */
    protected List<BaseRecyclerViewIndicator> getListRecycleViewIndicatores()
    {
        if (mListBaseRecyclerViewIndicatores == null)
        {
            mListBaseRecyclerViewIndicatores = new ArrayList<>();
        }
        return mListBaseRecyclerViewIndicatores;
    }

    /**
     * Get or initialize the list indicators
     *
     * @return List of ListIndicatores
     */
    private Map<Integer, BaseRecyclerViewIndicator> getMapIndicatorTypes()
    {
        if (mRecycleViewIndicatorTypes == null)
        {
            mRecycleViewIndicatorTypes = new HashMap<>();
        }

        return mRecycleViewIndicatorTypes;
    }

    /**
     * Get BaseRecyclerViewIndicator by type
     *
     * @param typeView Type view to get
     *
     * @return Got BaseRecyclerViewIndicator by type
     */
    protected BaseRecyclerViewIndicator getRecycleViewIndicatorByType(int typeView)
    {
        BaseRecyclerViewIndicator BaseRecyclerViewIndicator = getMapIndicatorTypes().get(typeView);

        return BaseRecyclerViewIndicator;
    }

    /**
     * Get  List Indicator. This should be the method to override in the case the developer wants a different view
     *
     * @param listIndicatorPosition Position of the list indicator
     *
     * @return List indicator
     */
    public BaseRecyclerViewIndicator getRecycleViewIndicator(int listIndicatorPosition)
    {
        BaseRecyclerViewIndicator BaseRecyclerViewIndicator = null;

        if (listIndicatorPosition < getListRecycleViewIndicatores().size())
        {
            BaseRecyclerViewIndicator = getListRecycleViewIndicatores().get(listIndicatorPosition);
        }
        return BaseRecyclerViewIndicator;
    }

    /**
     * Get the real position in the list
     *
     * @param adapterPosition The position retrieved from adapter
     *
     * @return Converted position in the List Indicador ()
     */
    public int getListIndicatorPosition(int adapterPosition)
    {
        return adapterPosition;
    }

    /**
     * Check if the Indicator instance already exists on the Map types (this is used to avoid put repeated indicatores on the Map indicatores)
     *
     * @param BaseRecyclerViewIndicator Indicator to check
     *
     * @return equals or bigger than 0 if exists, or -1 if not exists. If bigger than -1, then that value is the correspondent TypeView of the RecyleIndicator
     */
    private int getRecycleViewIndicatorTypeView(Map<Integer, BaseRecyclerViewIndicator> mapIndicator, BaseRecyclerViewIndicator BaseRecyclerViewIndicator)
    {
        List<BaseRecyclerViewIndicator> listRecycleIndicatorTypes = new ArrayList<>(mapIndicator.values());

        for (BaseRecyclerViewIndicator item : listRecycleIndicatorTypes)
        {
            if (item.getClass().equals(BaseRecyclerViewIndicator.getClass()))
            {
                return item.getViewType();
            }
        }
        return -1;
    }

    /**
     * Add a collection of list indicators
     *
     * @param listIndicators List of indicators to be displayed on the list
     * @param cleanBefore    If should clean the list when add the new collection or should only append the indicators
     */
    protected void addListIndicators(List<BaseRecyclerViewIndicator> listIndicators, boolean cleanBefore)
    {
        List<BaseRecyclerViewIndicator> updatedListIndicators = new ArrayList<>();
        Map<Integer, BaseRecyclerViewIndicator> updatedListIndicatorTypes = new HashMap<>();

        // If should not clean, then should append the new indicatores
        if (!cleanBefore)
        {
            updatedListIndicators.addAll(getListRecycleViewIndicatores());
            updatedListIndicatorTypes.putAll(getMapIndicatorTypes());
        }

        // Generate the new indicators
        if (listIndicators != null)
        {
            for (int i = 0; i < listIndicators.size(); i++)
            {
                int listItemPosition = i + updatedListIndicators.size();
                BaseRecyclerViewIndicator baseRecyclerViewIndicator = listIndicators.get(i);

                // Check if should Override List indicator
                BaseRecyclerViewIndicator overrideIndicator = shouldOverrideListIndicator(listItemPosition, baseRecyclerViewIndicator);
                if (overrideIndicator != null)
                {
                    baseRecyclerViewIndicator = overrideIndicator;
                }

                // Setup the List Indicator Type
                int viewType = getRecycleViewIndicatorTypeView(updatedListIndicatorTypes, baseRecyclerViewIndicator);
                if (viewType == -1) // Means that the new RecycleIndicator is a new type that didn't exists already on the map of indicator type views
                {
                    // Add the new BaseRecyclerViewIndicator into the Map indicator types
                    baseRecyclerViewIndicator.setViewType(updatedListIndicatorTypes.size());
                    updatedListIndicatorTypes.put(baseRecyclerViewIndicator.getViewType(), baseRecyclerViewIndicator);
                }
                else
                {
                    // Already exists the View type for this indicator, so use it
                    baseRecyclerViewIndicator.setViewType(viewType);
                }

                // Add new List indicator
                updatedListIndicators.add(baseRecyclerViewIndicator);
            }
        }

        // Update the current lists
        mRecycleViewIndicatorTypes = updatedListIndicatorTypes;
        mListBaseRecyclerViewIndicatores = updatedListIndicators;

        // Update ListView
        notifyDataSetChanged();
    }

    public AdapterConfigs getAdapterConfigs()
    {
        return mAdapterConfigs;
    }

    /**
     * Select Layout Manager to be used on this Adapter
     *
     * @param generateNewLayout if should generate a new Layout Manager
     *
     * @return LayoutManager implementation
     */
    public RecyclerView.LayoutManager getLayoutManager(boolean generateNewLayout)
    {
        if (mLayoutManager == null || generateNewLayout)
        {
            // Generate a LayoutManager
            switch (getLayoutManagerType())
            {
                case GRID_LAYOUT:
                    mLayoutManager = new GridLayoutManager(mContext, getColumnsCount(), getAdapterConfigs().getScrollOrientation(), false);
                    ((GridLayoutManager) getLayoutManager(false)).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup()
                    {
                        @Override
                        public int getSpanSize(int position)
                        {
                            BaseRecyclerViewIndicator BaseRecyclerViewIndicator = getRecycleViewIndicator(getListIndicatorPosition(position));
                            if (BaseRecyclerViewIndicator != null)
                            {
                                return Math.round(getColumnsCount() * BaseRecyclerViewIndicator.getViewWidth());
                            }
                            else
                            {
                                return 1;
                            }
                        }
                    });
                    break;

                case STAGGERED_GRID_LAYOUT:
                    mLayoutManager = new StaggeredGridLayoutManager(getColumnsCount(), getAdapterConfigs().getScrollOrientation());
                    break;

                case LINEAR_LAYOUT:
                default:
                    mLayoutManager = new LinearLayoutManager(mContext, getAdapterConfigs().getScrollOrientation(), false);
            }
        }

        return mLayoutManager;
    }

    public void setOnClickRecyclerAdapterViewListener(OnClickRecyclerAdapterViewListener onClickRecyclerAdapterViewListener)
    {
        this.onClickRecyclerAdapterViewListener = onClickRecyclerAdapterViewListener;
    }

    /**
     * This method is used in the case that the List Indicator should be intercepted and use other indicator instead
     * (This will be mostly usefull in  Open Product)
     *
     * @param position      Indicator Position
     * @param listIndicator Original List Indicator
     */
    public BaseRecyclerViewIndicator shouldOverrideListIndicator(int position, BaseRecyclerViewIndicator listIndicator)
    {
        return listIndicator;
    }

    /**
     * List of objects to add in the adapter. This is the list of objects that will be converted to ListIndicators
     *
     * @param listData        List of objects data
     * @param cleanListBefore If the list should be clean before update with new data
     */
    public abstract void addListData(List<ObjectData> listData, boolean cleanListBefore);

    /**
     * How many cloumns will be used on this Recycler View
     *
     * @return Number of columns
     */
    public abstract int getColumnsCount();

    /**
     * Get the layout manager type to generate it internally
     *
     * @return Type of LayoutManager to use in the RecyclerView
     */
    public abstract RecyclerLayoutManagerType getLayoutManagerType();


    /**
     * LayoutManager types available
     */
    public enum RecyclerLayoutManagerType
    {
        STAGGERED_GRID_LAYOUT, GRID_LAYOUT, LINEAR_LAYOUT;
    }

    /**
     * On click recycler adapter view listener
     */
    public interface OnClickRecyclerAdapterViewListener
    {
        /**
         * On recycle item click
         *
         * @param view                  Recycled view
         * @param listIndicatorPosition Position of the list indicator
         */
        void onItemClick(View view, int listIndicatorPosition);
    }


    /**
     * Setup Adapter
     */
    public static class AdapterConfigs
    {

        private int scrollOrientation = LinearLayout.VERTICAL;


        public AdapterConfigs()
        {
        }

        public int getScrollOrientation()
        {
            return scrollOrientation;
        }

        public void setScrollOrientation(int scrollOrientation)
        {
            this.scrollOrientation = scrollOrientation;
        }
    }
}
