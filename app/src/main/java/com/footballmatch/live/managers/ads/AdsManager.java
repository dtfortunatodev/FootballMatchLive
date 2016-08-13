package com.footballmatch.live.managers.ads;

import android.content.Context;
import android.view.View;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.SharedPreferencesManager;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.settings.AdsConfigs;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.NativeExpressAdView;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by David Fortunato on 08/08/2016
 * All rights reserved GoodBarber
 */
public class AdsManager
{

    // prefs
    private static final String PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED = "PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED";

    // Test Device
    private static final String TEST_DEVICE_ID = "1EC9706F2EBA55D6C3FE89B489471888";

    private AdsConfigs adsConfigs;

    // Instance
    private static AdsManager instance;

    // AdMob Implementation
    private InterstitialAd mInterstitialAd;


    public static AdsManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new AdsManager(context);
        }

        return instance;
    }

    private AdsManager(Context context)
    {
        adsConfigs = StartupManager.getInstance(context).getAppConfigs().getAdsConfigs();
    }


    /**
     * Get native Ad Banner View
     *
     * @param context
     *
     * @return
     */
    public View getNativeAdBannerView(Context context)
    {
        switch (adsConfigs.getAdType())
        {
            case ADMOB:
                NativeExpressAdView nativeExpressAdView = new NativeExpressAdView(context);
                nativeExpressAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, context.getResources().getDimensionPixelSize(R.dimen.ads_native_height)));
                nativeExpressAdView.setAdUnitId(adsConfigs.getAdNativeUnitId());

                // Create an ad request.
                AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID);
                nativeExpressAdView.loadAd(adRequestBuilder.build());
                return nativeExpressAdView;
        }

        return null;
    }



    public void showInsterstitial(final Context context)
    {

        if (adsConfigs.isAdsEnabled() && checkIfShouldDisplayAds(context))
        {
            switch (adsConfigs.getAdType())
            {
                case ADMOB:
                    if (mInterstitialAd == null)
                    {
                        mInterstitialAd = new InterstitialAd(context);
                        mInterstitialAd.setAdUnitId(adsConfigs.getAdInterstitialId());

                        mInterstitialAd.setAdListener(new AdListener()
                        {

                            @Override
                            public void onAdOpened()
                            {
                                super.onAdOpened();

                                // TODO Handle Ad Click
                            }

                            @Override
                            public void onAdFailedToLoad(int i)
                            {
                                super.onAdFailedToLoad(i);
                                // Reset Last Time Displayed
                                SharedPreferencesManager.putLongValue(context,PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED, 0);
                            }

                            @Override
                            public void onAdLoaded()
                            {
                                super.onAdLoaded();
                                if (mInterstitialAd != null)
                                {
                                    mInterstitialAd.show();
                                }

                            }
                        });

                    }

                    // Show Interstitial
                    AdRequest adRequest = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID).build();

                    mInterstitialAd.loadAd(adRequest);
                    break;

            }
            // Update Last Time Displayed
            SharedPreferencesManager.putLongValue(context, PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED, Calendar.getInstance().getTimeInMillis());
        }
    }


    /**
     * Check if should display ads
     *
     * @param context
     *
     * @return
     */
    private boolean checkIfShouldDisplayAds(Context context)
    {
        if (adsConfigs.isAdsEnabled())
        {
            // Check after last time displayed
            long lastTimeDisplayed = SharedPreferencesManager.getLongValue(context, PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED, 0);
            if ((Calendar.getInstance().getTimeInMillis() - lastTimeDisplayed) > adsConfigs.getIntervalBetweenAds())
            {
                // After last time. Check Probability
                Random r = new Random();
                int randomInt = r.nextInt(100);
                if (randomInt < adsConfigs.getProbabilityDisplayAds())
                {
                    return true;
                }
            }
        }
        return false;
    }

    public enum AdsType
    {
        ADMOB;
    }

}