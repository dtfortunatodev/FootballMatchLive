package com.footballmatch.live.managers.ads;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerCallbacks;
import com.appodeal.ads.InterstitialCallbacks;
import com.appodeal.ads.NonSkippableVideoCallbacks;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.SharedPreferencesManager;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.settings.AdsConfigs;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by David Fortunato on 08/08/2016
 * All rights reserved GoodBarber
 */
public class AdsManager
{
    private static final String TAG = AdsManager.class.getSimpleName();

    // prefs
    private static final String PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED = "PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED";
    private static final String PREFS_LAST_TIME_REWARDED_DISPLAYED     = "PREFS_LAST_TIME_REWARDED_DISPLAYED";


    // Test Device
    private static final String TEST_DEVICE_ID = "1EC9706F2EBA55D6C3FE89B489471888";

    private AdsConfigs adsConfigs;

    // Instance
    private static AdsManager instance;

    // AdMob Implementation
    private InterstitialAd mInterstitialAd;

    // Aux
    private boolean mHasAppodealCreated;


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
        mHasAppodealCreated = false;
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
        View adBanner = null;
        switch (adsConfigs.getAdTypeEnum())
        {
            case ADMOB:
                NativeExpressAdView nativeExpressAdView = new NativeExpressAdView(context);
                nativeExpressAdView.setAdSize(new AdSize(AdSize.FULL_WIDTH, context.getResources().getDimensionPixelSize(R.dimen.ads_native_height)));
                nativeExpressAdView.setAdUnitId(adsConfigs.getAdNativeUnitId());

                // Create an ad request.
                AdRequest.Builder adRequestBuilder = new AdRequest.Builder().addTestDevice(TEST_DEVICE_ID);
                nativeExpressAdView.loadAd(adRequestBuilder.build());
                adBanner = nativeExpressAdView;
                break;
            default:
                adBanner = new View(context);
                break;

        }

        return adBanner;
    }

    public void showAdRwardInterstitial(final Context context, final OnAdRewardCallback onAdRewardCallback)
    {
        if (adsConfigs.isAdsEnabled() && adsConfigs.getAdVideoReward() != null && !adsConfigs.getAdVideoReward().isEmpty() && onAdRewardCallback != null && checkShouldDisplayAdReward(context))
        {
            final RewardedVideoAd mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(context);
            mRewardedVideoAd.loadAd(adsConfigs.getAdVideoReward(), new AdRequest.Builder().build());
            mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener()
            {
                private boolean hasVideo = false;
                private RewardItem rewardItem = null;

                @Override
                public void onRewardedVideoAdLoaded()
                {
                    hasVideo = true;
                    mRewardedVideoAd.show();
                }

                @Override
                public void onRewardedVideoAdOpened()
                {
                }

                @Override
                public void onRewardedVideoStarted()
                {
                    hasVideo = true;
                }

                @Override
                public void onRewardedVideoAdClosed()
                {
                    if (hasVideo && rewardItem == null)
                    {
                        onAdRewardCallback.onFailedShouldStop();
                    }
                    else
                    {
                        onAdRewardCallback.onSuccessShouldContinue();
                    }
                }

                @Override
                public void onRewarded(RewardItem rewardItem)
                {
                    onAdRewardCallback.onSuccessShouldContinue();
                    this.rewardItem = rewardItem;

                    // Update Time
                    updateAdRewardedTimestamp(context);
                }

                @Override
                public void onRewardedVideoAdLeftApplication()
                {

                }

                @Override
                public void onRewardedVideoAdFailedToLoad(int i)
                {
                    if (hasVideo && rewardItem == null)
                    {
                        onAdRewardCallback.onFailedShouldStop();
                    }
                    else
                    {
                        onAdRewardCallback.onSuccessShouldContinue();
                    }
                }
            });
        }
        else if (onAdRewardCallback != null)
        {
            onAdRewardCallback.onSuccessShouldContinue();
        }
    }

    public void showNonSkippableVideo(final Activity activity, final OnInterstitialClosed onInterstitialClosed)
    {
        if (adsConfigs.isAdsEnabled())
        {
            switch (adsConfigs.getAdTypeEnum())
            {
                case ADMOB:
                    showAdRwardInterstitial(activity, new OnAdRewardCallback()
                    {
                        @Override
                        public void onSuccessShouldContinue()
                        {
                            onInterstitialClosed.onInterstitialClosed(true);
                        }

                        @Override
                        public void onFailedShouldStop()
                        {
                            onInterstitialClosed.onInterstitialClosed(false);
                        }
                    });
                    break;

                case APPODEAL:
                    Appodeal.show(activity, Appodeal.NON_SKIPPABLE_VIDEO);
                    Appodeal.setNonSkippableVideoCallbacks(new NonSkippableVideoCallbacks()
                    {
                        @Override
                        public void onNonSkippableVideoLoaded()
                        {

                        }

                        @Override
                        public void onNonSkippableVideoFailedToLoad()
                        {
                            onInterstitialClosed.onInterstitialClosed(true);
                        }

                        @Override
                        public void onNonSkippableVideoShown()
                        {
                        }

                        @Override
                        public void onNonSkippableVideoFinished()
                        {
                            onInterstitialClosed.onInterstitialClosed(true);
                            updateAdRewardedTimestamp(activity);
                        }

                        @Override
                        public void onNonSkippableVideoClosed(boolean b)
                        {
                            onInterstitialClosed.onInterstitialClosed(false);
                        }
                    });
                    break;
                default:
                    onInterstitialClosed.onInterstitialClosed(true);
                    break;
            }
        }
        else
        {
            onInterstitialClosed.onInterstitialClosed(true);
        }

    }

    public void showInsterstitial(final Activity context)
    {

        if (adsConfigs.isAdsEnabled() && checkIfShouldDisplayAds(context))
        {
            switch (adsConfigs.getAdTypeEnum())
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
                                SharedPreferencesManager.putLongValue(context, PREFS_LAST_TIME_INTERSTITIAL_DISPLAYED, 0);
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

                case APPODEAL:
                    Appodeal.show(context, Appodeal.INTERSTITIAL);
                    Appodeal.setInterstitialCallbacks(new InterstitialCallbacks()
                    {
                        @Override
                        public void onInterstitialLoaded(boolean b)
                        {

                        }

                        @Override
                        public void onInterstitialFailedToLoad()
                        {
                            Appodeal.show(context, Appodeal.NON_SKIPPABLE_VIDEO);
                        }

                        @Override
                        public void onInterstitialShown()
                        {

                        }

                        @Override
                        public void onInterstitialClicked()
                        {

                        }

                        @Override
                        public void onInterstitialClosed()
                        {

                        }
                    });
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



    public void updateAdRewardedTimestamp(Context context)
    {
        // Update Last Time Displayed
        SharedPreferencesManager.putLongValue(context, PREFS_LAST_TIME_REWARDED_DISPLAYED, Calendar.getInstance().getTimeInMillis());
    }

    public boolean checkShouldDisplayAdReward(Context context)
    {
        return adsConfigs.isAdsEnabled() && adsConfigs.getAdVideoReward() != null && !adsConfigs.getAdVideoReward().isEmpty() &&
                (Calendar.getInstance().getTimeInMillis() - SharedPreferencesManager.getLongValue(context, PREFS_LAST_TIME_REWARDED_DISPLAYED, 0)) >
                        adsConfigs.getIntervalBetweenAdsReward();
    }


    public void onCreate(Activity activity)
    {
        switch (StartupManager.getInstance(activity).getAppAdsConfigs().getAdTypeEnum())
        {
            case APPODEAL:
                if (!mHasAppodealCreated)
                {
                    String appKey = StartupManager.getInstance(activity).getAppAdsConfigs().getAdNativeUnitId();
                    Appodeal.initialize(activity, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER_BOTTOM | Appodeal.NON_SKIPPABLE_VIDEO);
                    //Appodeal.setTesting(true);
                    mHasAppodealCreated = true;
                }
                break;
        }

    }

    public void onResume(Activity activity)
    {
        if (checkIfShouldDisplayAds(activity))
        {
            switch (StartupManager.getInstance(activity).getAppAdsConfigs().getAdTypeEnum())
            {
                case APPODEAL:
                    Appodeal.onResume(activity, Appodeal.BANNER_BOTTOM);
                    Appodeal.setSmartBanners(true);
                    Appodeal.setBannerCallbacks(new BannerCallbacks()
                    {
                        @Override
                        public void onBannerLoaded(int i, boolean b)
                        {
                            Log.d(TAG, "onBannerLoaded");
                        }

                        @Override
                        public void onBannerFailedToLoad()
                        {
                            Log.d(TAG, "onBannerFailedToLoad");
                        }

                        @Override
                        public void onBannerShown()
                        {
                            Log.d(TAG, "onBannerShown");
                        }

                        @Override
                        public void onBannerClicked()
                        {
                            Log.d(TAG, "onBannerClicked");
                        }
                    });
                    Appodeal.show(activity, Appodeal.BANNER_BOTTOM);
                    break;
            }
        }

    }

    public void onStart(Activity activity)
    {
    }

    public void onStop(Activity activity)
    {

    }

    public void onDestory(Activity activity)
    {
    }

    public interface OnAdRewardCallback
    {

        void onSuccessShouldContinue();

        void onFailedShouldStop();

    }

    public enum AdsType
    {
        ADMOB, APPODEAL, UNKNOWN;

        public static AdsType getTypeByString(String type)
        {
            if (type != null)
            {
                if (type.equals(ADMOB.name()))
                {
                    return ADMOB;
                }
                else if (type.equals(APPODEAL.name()))
                {
                    return APPODEAL;
                }
            }
            return UNKNOWN;
        }
    }

    public interface OnInterstitialClosed
    {
        void onInterstitialClosed(boolean canContinue);
    }

}
