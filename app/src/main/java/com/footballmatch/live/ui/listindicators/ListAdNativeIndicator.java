package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.BaseEntity;
import com.footballmatch.live.managers.ads.AdsManager;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;

/**
 * Created by David Fortunato on 08/08/2016
 * All rights reserved GoodBarber
 */
public class ListAdNativeIndicator extends BaseRecyclerViewIndicator<View, BaseEntity, BaseRecyclerViewHolder<View>>
{
    public ListAdNativeIndicator()
    {
        super(null);
    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {

        return AdsManager.getInstance(context).getNativeAdBannerView(context);
    }

    @Override
    public void refreshCell(BaseRecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {

        // Check if should display ads
        if (StartupManager.getInstance(recyclerViewHolder.getContext()).getAppAdsConfigs().isAdsEnabled())
        {
            recyclerViewHolder.getItemView().setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerViewHolder.getItemView().setVisibility(View.GONE);
        }
    }

    @Override
    public BaseRecyclerViewHolder getRecycleViewHolder(View view)
    {
        return new BaseRecyclerViewHolder(view);
    }
}
