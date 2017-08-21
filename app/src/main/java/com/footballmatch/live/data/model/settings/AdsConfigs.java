package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.data.model.BaseEntity;
import com.footballmatch.live.managers.ads.AdsManager;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class AdsConfigs extends BaseEntity
{
    private boolean adsEnabled;
    private long intervalBetweenAds;
    private long intervalBetweenAdsReward;
    private String adType;
    private String adNativeUnitId;
    private String adInterstitialId;
    private String adVideoReward;
    private int listIntervalNativeBanner;
    private int probabilityDisplayAds;

    // Parent
    private AppConfigs parentAppConfigs;

    public AdsConfigs()
    {
        adsEnabled = true;
        intervalBetweenAds = 30000; // 30 seconds
        intervalBetweenAdsReward = 86400000;
        listIntervalNativeBanner = 5;
        probabilityDisplayAds = 50;
    }

    public boolean isAdsEnabled()
    {
        if (getParentAppConfigs() != null && getParentAppConfigs().checkShouldBlockSensibleData())
        {
            // Disable ads is should block sensible data
            adsEnabled = false;
        }

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

    public String getAdType()
    {
        return adType;
    }

    public AdsManager.AdsType getAdTypeEnum()
    {
        return AdsManager.AdsType.getTypeByString(getAdType());
    }

    public void setAdType(String adType)
    {
        this.adType = adType;
    }

    public String getAdNativeUnitId()
    {
        return adNativeUnitId;
    }

    public void setAdNativeUnitId(String adNativeUnitId)
    {
        this.adNativeUnitId = adNativeUnitId;
    }

    public String getAdInterstitialId()
    {
        return adInterstitialId;
    }

    public void setAdInterstitialId(String adInterstitialId)
    {
        this.adInterstitialId = adInterstitialId;
    }

    public int getListIntervalNativeBanner()
    {
        return listIntervalNativeBanner;
    }

    public void setListIntervalNativeBanner(int listIntervalNativeBanner)
    {
        this.listIntervalNativeBanner = listIntervalNativeBanner;
    }

    public int getProbabilityDisplayAds()
    {
        return probabilityDisplayAds;
    }

    public void setProbabilityDisplayAds(int probabilityDisplayAds)
    {
        this.probabilityDisplayAds = probabilityDisplayAds;
    }

    public AppConfigs getParentAppConfigs()
    {
        return parentAppConfigs;
    }

    public void setParentAppConfigs(AppConfigs parentAppConfigs)
    {
        this.parentAppConfigs = parentAppConfigs;
    }

    public String getAdVideoReward()
    {
        return adVideoReward;
    }

    public void setAdVideoReward(String adVideoReward)
    {
        this.adVideoReward = adVideoReward;
    }

    public long getIntervalBetweenAdsReward()
    {
        return intervalBetweenAdsReward;
    }

    public void setIntervalBetweenAdsReward(long intervalBetweenAdsReward)
    {
        this.intervalBetweenAdsReward = intervalBetweenAdsReward;
    }
}
