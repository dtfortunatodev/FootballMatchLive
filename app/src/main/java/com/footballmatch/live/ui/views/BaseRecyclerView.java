package com.footballmatch.live.ui.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.utils.LogUtil;

/**
 * Created by David Fortunato on 03/08/2016
 * All rights reserved GoodBarber
 */
public class BaseRecyclerView extends RecyclerView
{
    public static final String TAG = BaseRecyclerView.class.getSimpleName();

    // Horizontal Effect
    private SwipeHorizontalEffect mSwipeHorizontalEffect = SwipeHorizontalEffect.NONE;

    // GB Adapter
    private BaseRecyclerViewAdapter mGBAdapter;

    public BaseRecyclerView(Context context)
    {
        super(context);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs)
    {
        super(context, attrs);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    public void setAdapter(Adapter adapter)
    {
        if (adapter instanceof BaseRecyclerViewAdapter && adapter != mGBAdapter)
        {
            if (adapter != mGBAdapter)
            {
                setBaseAdapter((BaseRecyclerViewAdapter) adapter);
            }
            else
            {
                super.setAdapter(adapter);
            }

        }
        else
        {
            LogUtil.e(TAG,
                      "ATTENTION: This recycle view was created to work directly with GBBaseRecyclerAdapter. If you are not using the right adapter, this implementation may not work as expected.");

            super.setAdapter(adapter);
        }
    }

    /**
     * Setup GB Adapter implementation
     *
     * @param adapter GB Adapter implementation
     */
    public void setBaseAdapter(BaseRecyclerViewAdapter adapter)
    {
        if (adapter != getAdapter())
        {
            // Only update if is a new adapter
            mGBAdapter = adapter;

            // Setup Layout Manager
            setLayoutManager(adapter.getLayoutManager(true));

            // Set the new adapter
            setAdapter(adapter);
        }
    }

    public SwipeHorizontalEffect getSwipeHorizontalEffect()
    {
        return mSwipeHorizontalEffect;
    }

    public void setSwipeHorizontalEffect(SwipeHorizontalEffect mSwipeHorizontalEffect)
    {
        this.mSwipeHorizontalEffect = mSwipeHorizontalEffect;
    }

    @Override
    public boolean fling(int velocityX, int velocityY)
    {

        if (getSwipeHorizontalEffect() == SwipeHorizontalEffect.PAGER_EFFECT && getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager()).getOrientation() == HORIZONTAL)
        {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();

            //            int viewWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
            int viewWidth = getWidth();

            // views on the screen
            int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
            View lastView = linearLayoutManager.findViewByPosition(lastVisibleItemPosition);
            int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
            View firstView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition);

            // Distance we need to scroll
            int leftMargin = (viewWidth - lastView.getWidth()) / 2;
            int rightMargin = (viewWidth - firstView.getWidth()) / 2 + firstView.getWidth();
            int leftEdge = lastView.getLeft();
            int rightEdge = firstView.getRight();
            int scrollDistanceLeft = leftEdge - leftMargin;
            int scrollDistanceRight = rightMargin - rightEdge;

            if (Math.abs(velocityX) < 1000)
            {
                // It is slow, stay on the current page or go to the next page if more than half

                if (leftEdge > viewWidth / 2)
                {
                    // go to next page
                    smoothScrollBy(-scrollDistanceRight, 0);
                }
                else if (rightEdge < viewWidth / 2)
                {
                    // go to next page
                    smoothScrollBy(scrollDistanceLeft, 0);
                }
                else
                {
                    // stay at current page
                    if (velocityX > 0)
                    {
                        smoothScrollBy(-scrollDistanceRight, 0);
                    }
                    else
                    {
                        smoothScrollBy(scrollDistanceLeft, 0);
                    }
                }
                return true;

            }
            else
            {
                // The fling is fast -> go to next page

                if (velocityX > 0)
                {
                    smoothScrollBy(scrollDistanceLeft, 0);
                }
                else
                {
                    smoothScrollBy(-scrollDistanceRight, 0);
                }
                return true;

            }
        }

        return super.fling(velocityX, velocityY);
    }

    @Override
    public void onScrollStateChanged(int state)
    {
        super.onScrollStateChanged(state);

        if (getSwipeHorizontalEffect() == SwipeHorizontalEffect.PAGER_EFFECT && getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager()).getOrientation() == HORIZONTAL)
        {

            if (state == SCROLL_STATE_IDLE)
            {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                //                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                int screenWidth = getWidth();

                // views on the screen
                int lastVisibleItemPosition = linearLayoutManager.findLastVisibleItemPosition();
                View lastView = linearLayoutManager.findViewByPosition(lastVisibleItemPosition);
                int firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition();
                View firstView = linearLayoutManager.findViewByPosition(firstVisibleItemPosition);

                // distance we need to scroll
                int leftMargin = (screenWidth - lastView.getWidth()) / 2;
                int rightMargin = (screenWidth - firstView.getWidth()) / 2 + firstView.getWidth();
                int leftEdge = lastView.getLeft();
                int rightEdge = firstView.getRight();
                int scrollDistanceLeft = leftEdge - leftMargin;
                int scrollDistanceRight = rightMargin - rightEdge;

                if (leftEdge > screenWidth / 2)
                {
                    smoothScrollBy(-scrollDistanceRight, 0);
                }
                else if (rightEdge < screenWidth / 2)
                {
                    smoothScrollBy(scrollDistanceLeft, 0);
                }
            }

        }
    }

    public enum SwipeHorizontalEffect
    {
        NONE, PAGER_EFFECT;
    }

}
