package com.footballmatch.live.ui.adapters;

import android.content.Context;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.managers.ads.AdsManager;
import com.footballmatch.live.ui.listindicators.BaseRecyclerViewIndicator;
import com.footballmatch.live.ui.listindicators.ListAdInternalIndicator;
import com.footballmatch.live.ui.listindicators.ListAdNativeIndicator;
import com.footballmatch.live.ui.listindicators.ListCompetitionSeparatorIndicator;
import com.footballmatch.live.ui.listindicators.ListMatchIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Fortunato on 04/08/2016
 * All rights reserved GoodBarber
 */
public class LiveMatchesRecyclerAdapter extends BaseRecyclerViewAdapter<MatchEntity>
{

    public LiveMatchesRecyclerAdapter(Context activity)
    {
        super(activity);
    }

    @Override
    public void addListData(List<MatchEntity> listData, boolean cleanListBefore)
    {
        List<BaseRecyclerViewIndicator> listIndicators = new ArrayList<>();
        int counter = 0;

        // Check if should add Ad Internal
        if (StartupManager.getInstance(mContext).getAppConfigs().getInternalAds() != null &&
                StartupManager.getInstance(mContext).getAppConfigs().getInternalAds().shouldDisplayAd(mContext))
        {
            listIndicators.add(new ListAdInternalIndicator(StartupManager.getInstance(mContext).getAppConfigs().getInternalAds()));
        }

        // Add first ad
        // Check if should add Banner before the Competition separator
        if (StartupManager.getInstance(mContext).getAppAdsConfigs().isAdsEnabled() && StartupManager.getInstance(mContext).getAppAdsConfigs().getAdTypeEnum() !=
                AdsManager.AdsType.APPODEAL)
        {
            listIndicators.add(new ListAdNativeIndicator());
        }

        String currentCompetition = "";
        for (MatchEntity matchEntity : listData)
        {
            if (matchEntity.isAvailable())
            {
                // Check if is a net competition
                if (matchEntity.getCompetitionEntity() != null && matchEntity.getCompetitionEntity().getCompetitionName() != null &&
                        !currentCompetition.equalsIgnoreCase(matchEntity.getCompetitionEntity().getCompetitionName()))
                {
                    // Check if should add Banner before the Competition separator
                    if (counter >= StartupManager.getInstance(mContext).getAppAdsConfigs().getListIntervalNativeBanner() &&
                            StartupManager.getInstance(mContext).getAppAdsConfigs().isAdsEnabled())
                    {
                        listIndicators.add(new ListAdNativeIndicator());
                        counter = 0;
                    }

                    listIndicators.add(new ListCompetitionSeparatorIndicator(matchEntity.getCompetitionEntity()));
                    currentCompetition = matchEntity.getCompetitionEntity().getCompetitionName();
                }

                listIndicators.add(new ListMatchIndicator(matchEntity));


                counter++;
            }
        }
        addListIndicators(listIndicators, cleanListBefore);
    }

    @Override
    public int getColumnsCount()
    {
        return 1;
    }

    @Override
    public RecyclerLayoutManagerType getLayoutManagerType()
    {
        return RecyclerLayoutManagerType.LINEAR_LAYOUT;
    }
}
