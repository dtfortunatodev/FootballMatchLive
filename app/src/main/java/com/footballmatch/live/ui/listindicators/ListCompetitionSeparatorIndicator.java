package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.CompetitionEntity;
import com.footballmatch.live.data.requests.ImageLoaderHelper;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;

/**
 * Created by David Fortunato on 11/08/2016
 * All rights reserved GoodBarber
 */
public class ListCompetitionSeparatorIndicator extends BaseRecyclerViewIndicator<View, CompetitionEntity, ListCompetitionSeparatorIndicator.RecyclerViewHolder>
{


    public ListCompetitionSeparatorIndicator(CompetitionEntity competitionEntity)
    {
        super(competitionEntity);
    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_match_competition_separator, null);
    }

    @Override
    public void refreshCell(RecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {

        // Set competition name
        recyclerViewHolder.tvName.setText(getObjectData().getCompetitionName());


        // Check if should hide or display the competition icon
        if (!StartupManager.getInstance(recyclerViewHolder.getContext()).getAppConfigs().checkShouldBlockSensibleData())
        {
            // Load Competition Icon
            ImageLoaderHelper.loadImage(getObjectData().getCompetitionLogoUrl(), recyclerViewHolder.ivIcon, R.drawable.invisible_drawable);
            recyclerViewHolder.ivIcon.setVisibility(View.VISIBLE);
        }
        else
        {
            recyclerViewHolder.ivIcon.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public RecyclerViewHolder getRecycleViewHolder(View view)
    {
        return new RecyclerViewHolder(view);
    }

    protected class RecyclerViewHolder extends BaseRecyclerViewHolder<View>
    {

        public ImageView ivIcon;
        public TextView tvName;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivListCompetitionCompetitionicon);
            tvName = (TextView) itemView.findViewById(R.id.tvListCompetitionName);
        }
    }
}
