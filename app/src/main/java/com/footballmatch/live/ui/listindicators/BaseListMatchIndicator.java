package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.requests.ImageLoaderHelper;
import com.footballmatch.live.ui.activities.MatchDetailsActivity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by David Fortunato on 04/08/2016
 * All rights reserved GoodBarber
 */
public class BaseListMatchIndicator extends BaseRecyclerViewIndicator<View, MatchEntity, BaseListMatchIndicator.RecyclerViewHolder>
{

    private static final SimpleDateFormat MATCH_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    // Data
    private MatchEntity matchEntity;

    public BaseListMatchIndicator(MatchEntity matchEntity)
    {
        super(matchEntity);
    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_match_item_indicator, null);
    }

    @Override
    public void refreshCell(final RecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {
        Resources resources = recyclerViewHolder.getContext().getResources();

        recyclerViewHolder.tvHomeTeamName.setText(getObjectData().getTeamHome().getTeamName());
        recyclerViewHolder.tvAwayTeamName.setText(getObjectData().getTeamAway().getTeamName());

        if (getObjectData().isLive())
        {
            recyclerViewHolder.tvTimeText.setText(resources.getString(R.string.list_match_is_live));
            recyclerViewHolder.tvTimeText.setTextColor(resources.getColor(R.color.list_match_item_live));
        }
        else
        {
            recyclerViewHolder.tvTimeText.setText(MATCH_TIME_FORMAT.format(new Date(getObjectData().getStartDateMillis())));
            recyclerViewHolder.tvTimeText.setTextColor(resources.getColor(R.color.list_match_item_time));
        }

        // Set Team Logos
        ImageLoaderHelper.loadImage(getObjectData().getTeamHome().getTeamLogoUrl(), recyclerViewHolder.ivTeamHomeLogo, R.drawable.team_generic_logo);
        ImageLoaderHelper.loadImage(getObjectData().getTeamAway().getTeamLogoUrl(), recyclerViewHolder.ivTeamAwayLogo, R.drawable.team_generic_logo);

        // Set on Click
        recyclerViewHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                MatchDetailsActivity.startActivity(recyclerViewHolder.getContext(), getObjectData());
            }
        });

        // Process Block Screen
        procressBlockScreen(recyclerViewHolder);

    }

    private void procressBlockScreen(RecyclerViewHolder recyclerViewHolder)
    {
        if (StartupManager.getInstance(recyclerViewHolder.getContext()).getAppConfigs().checkShouldBlockSensibleData())
        {
            recyclerViewHolder.ivTeamHomeLogo.setVisibility(View.INVISIBLE);
            recyclerViewHolder.ivTeamAwayLogo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public RecyclerViewHolder getRecycleViewHolder(View view)
    {
        return new RecyclerViewHolder(view);
    }

    public class RecyclerViewHolder extends BaseRecyclerViewHolder<View>
    {

        public TextView  tvHomeTeamName;
        public TextView  tvAwayTeamName;
        public TextView  tvTimeText;
        public ImageView ivTeamHomeLogo;
        public ImageView ivTeamAwayLogo;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            tvHomeTeamName = (TextView) itemView.findViewById(R.id.tvListMatchIndicatorTeamHomeName);
            tvAwayTeamName = (TextView) itemView.findViewById(R.id.tvListMatchIndicatorTeamAwayName);
            tvTimeText = (TextView) itemView.findViewById(R.id.tvListMatchIndicatorTimeText);
            ivTeamHomeLogo = (ImageView) itemView.findViewById(R.id.ivListMatchIndicatorTeamHomeLogo);
            ivTeamAwayLogo = (ImageView) itemView.findViewById(R.id.ivListMatchIndicatorTeamAwayLogo);
        }
    }

}
