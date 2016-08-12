package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.AnalyticsHelper;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.BaseEntity;
import com.footballmatch.live.data.model.settings.AppConfigs;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 09/08/2016
 * All rights reserved GoodBarber
 */
public class ListFollowUsIndicator extends BaseRecyclerViewIndicator<View, BaseEntity, ListFollowUsIndicator.RecyclerViewHolder>
{
    public ListFollowUsIndicator()
    {
        super(null);
    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_follow_us_indicator, null);
    }

    @Override
    public void refreshCell(final RecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {
        // Set Click listener
        final AppConfigs appConfigs = StartupManager.getInstance(recyclerViewHolder.getContext()).getAppConfigs();

        // Setup Facebook Button
        if (appConfigs.getFacebookPageUrl() != null && !appConfigs.getFacebookPageUrl().isEmpty())
        {
            recyclerViewHolder.ivFacebookBtn.setVisibility(View.VISIBLE);
            recyclerViewHolder.ivFacebookBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AnalyticsHelper.getInstance().sendEvent("FollowUs", "FacebookClicked");
                    Utils.startURL(recyclerViewHolder.getContext(), appConfigs.getFacebookPageUrl());
                }
            });
        }
        else
        {
            recyclerViewHolder.ivFacebookBtn.setVisibility(View.GONE);
        }

        // Setup Twitter Button
        if (appConfigs.getTwitterPageUrl() != null && !appConfigs.getTwitterPageUrl().isEmpty())
        {
            recyclerViewHolder.ivTwitterBtn.setVisibility(View.VISIBLE);
            recyclerViewHolder.ivTwitterBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    AnalyticsHelper.getInstance().sendEvent("FollowUs", "TwitterClicked");
                    Utils.startURL(recyclerViewHolder.getContext(), appConfigs.getTwitterPageUrl());
                }
            });
        }
        else
        {
            recyclerViewHolder.ivTwitterBtn.setVisibility(View.GONE);
        }

    }

    @Override
    public ListFollowUsIndicator.RecyclerViewHolder getRecycleViewHolder(View view)
    {
        return new RecyclerViewHolder(view);
    }

    protected class RecyclerViewHolder extends BaseRecyclerViewHolder<View>
    {

        public ImageView ivFacebookBtn;
        public ImageView ivTwitterBtn;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            ivFacebookBtn = (ImageView) itemView.findViewById(R.id.ivListFollowFacebookButton);
            ivTwitterBtn = (ImageView) itemView.findViewById(R.id.ivListFollowTwitterButton);
        }
    }

}
