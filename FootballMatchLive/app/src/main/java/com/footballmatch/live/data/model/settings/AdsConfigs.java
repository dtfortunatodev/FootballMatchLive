package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.data.model.BaseEntity;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class AdsConfigs extends BaseEntity
{
    private boolean adsEnabled;
    private long intervalBetweenAds;

    public AdsConfigs()
    {
        adsEnabled = true;
        intervalBetweenAds = 30000; // 30 seconds
    }

    public boolean isAdsEnabled()
    {
        return adsEnabled;
    }

    public void setAdsEnabled(boolean adsEnabled)
    {
        this.adsEnabled = adsEnabled;
    }

    public long getIntervalBetweenAds()
    {
        return intervalBetweenAds;
    }

    public void setIntervalBetweenAds(long intervalBetweenAds)
    {
        this.intervalBetweenAds = intervalBetweenAds;
    }
}
