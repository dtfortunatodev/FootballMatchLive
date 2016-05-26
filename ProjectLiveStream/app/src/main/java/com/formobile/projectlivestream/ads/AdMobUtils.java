package com.formobile.projectlivestream.ads;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;
import com.formobile.projectlivestream.utils.AnalyticsHelper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class AdMobUtils {
	
	public static final String BANNER_ID_DEFAULT = "ca-app-pub-5492791201497132/7348753208";
	public static final String INTERSTITIAL_ID_DEFAULT = "ca-app-pub-5492791201497132/7627954800";


    private static AdView mAdViewStatic;

//    public static final long INTERVAL_NEXT_INTERSTITIAL = 20000;
	
	public static void showInterstitialEndMatch(final Activity activity){
        try {
            if(AdvertiseController.shouldDisplayAds(activity)){
                StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(activity, false);


                // Create the interstitial.
                final InterstitialAd interstitial = new InterstitialAd(activity);

                String id = startupConfigs.getAdmobConfigs().getInterstitialId();

                if(id == null || id.isEmpty()){
                    id = INTERSTITIAL_ID_DEFAULT;
                }
                interstitial.setAdUnitId(id);

                // Create ad request.
                AdRequest.Builder adBuilderRequest = new AdRequest.Builder()
                        .addKeyword(AppConfigsHelper.getStartupConfigs(activity, false).getAppName());

                // Add List Keywords
                if(AppConfigsHelper.getStartupConfigs(activity, false).getListAdsKeywords() != null && !AppConfigsHelper.getStartupConfigs(activity, false).getListAdsKeywords().isEmpty()){
                    for(String keyword : AppConfigsHelper.getStartupConfigs(activity, false).getListAdsKeywords()){
                        adBuilderRequest.addKeyword(keyword);
                    }
                }

                AdRequest adRequest = adBuilderRequest.build();

                // Begin loading your interstitial.
                interstitial.loadAd(adRequest);
                interstitial.setAdListener(new AdListener() {

                    @Override
                    public void onAdLoaded() {

                        AdvertiseController.updateLastTimeDisplayed();

                        interstitial.show();
                        super.onAdLoaded();

                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        super.onAdFailedToLoad(errorCode);
                    }

                    @Override
                    public void onAdLeftApplication() {

                        try {
                            AnalyticsHelper.sendEvent(activity, "Ads Clicked", "Interstitial", "Startup Message: " + AppConfigsHelper.getStartupConfigs(activity, false).getStartupMessage().isEnabled());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        AdvertiseController.onClickAds(activity);

                        super.onAdLeftApplication();
                    }
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
		
	public static void showBanner(final Activity activity){
        try{
            StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(activity, false);

            View viewGroup = activity.findViewById(R.id.adViewContainer);
            if(viewGroup != null ){
                ((ViewGroup) viewGroup).removeAllViews();

                if(mAdViewStatic == null){
                    mAdViewStatic = new AdView(activity);

//                    String id = Configs.ADMOB_BANNER_ID;
                    String id = startupConfigs.getAdmobConfigs().getBannerId();

                    if(id == null || id.isEmpty()){
                        id = BANNER_ID_DEFAULT;
                    }
                    mAdViewStatic.setAdUnitId(id);
                    mAdViewStatic.setAdSize(AdSize.SMART_BANNER);

                    // Create an ad request. Check logcat output for the hashed device ID to
                    // get test ads on a physical device.
                    // Create ad request.
                    AdRequest.Builder adBuilderRequest = new AdRequest.Builder()
                            .addKeyword(AppConfigsHelper.getStartupConfigs(activity, false).getAppName());

                    // Add List Keywords
                    if(AppConfigsHelper.getStartupConfigs(activity, false).getListAdsKeywords() != null && !AppConfigsHelper.getStartupConfigs(activity, false).getListAdsKeywords().isEmpty()){
                        for(String keyword : AppConfigsHelper.getStartupConfigs(activity, false).getListAdsKeywords()){
                            adBuilderRequest.addKeyword(keyword);
                        }
                    }
                    AdRequest adRequest = adBuilderRequest.build();


                    // Set Listener
                    mAdViewStatic.setAdListener(new AdListener() {
                        @Override
                        public void onAdLeftApplication() {

                            try {
                                AnalyticsHelper.sendEvent(activity, "Ads Clicked", "Banner", "Startup Message: " + AppConfigsHelper.getStartupConfigs(activity, false).getStartupMessage().isEnabled());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            AdvertiseController.onClickAds(activity);

                            super.onAdLeftApplication();
                        }
                    });

                    // Start loading the ad in the background.
                    mAdViewStatic.loadAd(adRequest);
                }

                // Remove From Parent
                if(mAdViewStatic.getParent() != null && mAdViewStatic.getParent() instanceof ViewGroup){
                    ((ViewGroup) mAdViewStatic.getParent()).removeAllViews();
                }


                // Add Banner
                ((ViewGroup)viewGroup).addView(mAdViewStatic);

            }

        } catch(Exception e){
            e.printStackTrace();
        }

	}
}
