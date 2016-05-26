package com.formobile.projectlivestream.ads;

import android.app.Activity;
import android.webkit.WebView;

import com.appfireworks.android.track.AppTracker;
import com.bwlfjnsfglfbrxtvjn.AdController;
import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;

import java.util.Calendar;
import java.util.Random;

/**
 * Created by Fortuna on 01-09-2014.
 */
public class LeadBoltUtils {
//    public static final long PERIOD_PLAY_VIDEO = 20000;

    public static long lastTime = -1;

    private static final String AD_BANNER_HTML = "<script type=\"text/javascript\" src=\"http://ad.leadboltads.net/show_app_ad.js?section_id=SECTION_ID\"></script>";

    private static WebView AD_BANNER_INSTANCE = null;

    public static void initializeLeadBolt(final Activity activity){
        try {
            if(lastTime == -1) {
                AdController audio = new AdController(activity, AppConfigsHelper.getStartupConfigs(activity, false).getLeadBoltConfigs().getAudioAdId());
                audio.loadAudioAd();
                lastTime = Calendar.getInstance().getTimeInMillis();
            }else if((Calendar.getInstance().getTimeInMillis() - lastTime) >= AppConfigsHelper.getStartupConfigs(activity, false).getPeriodToDisplayAds()){
                AdController audio = new AdController(activity, AppConfigsHelper.getStartupConfigs(activity, false).getLeadBoltConfigs().getAudioAdId());
                audio.loadAudioAd();
                lastTime = Calendar.getInstance().getTimeInMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadDisplayAd(Activity activity) {
        try {

            // Check probability
            if(AppConfigsHelper.getStartupConfigs(activity, false).getProbabilityAds() != 100){
                // Probability
                int random = new Random().nextInt(101);

                if(random > AppConfigsHelper.getStartupConfigs(activity, false).getProbabilityAds()){
                    return;
                }
            }

            if(lastTime == -1){
                // use this else where in your app to load a Leadbolt Interstitial Ad
                AdController interstitial = new AdController(activity, AppConfigsHelper.getStartupConfigs(activity, false).getLeadBoltConfigs().getInterstitialId());
                interstitial.loadAd();
                lastTime = Calendar.getInstance().getTimeInMillis();
            } else if((Calendar.getInstance().getTimeInMillis() - lastTime) >= AppConfigsHelper.getStartupConfigs(activity, false).getPeriodToDisplayAds()){
                // use this else where in your app to load a Leadbolt Interstitial Ad
                AdController interstitial = new AdController(activity,  AppConfigsHelper.getStartupConfigs(activity, false).getLeadBoltConfigs().getInterstitialId());
                interstitial.loadAd();

                lastTime = Calendar.getInstance().getTimeInMillis();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void loadBannerAd(Activity activity){

//        if(Configs.AD_PROVIDER_TYPE.equals(Configs.AdProviderType.LEADBOLT)){
//
//            if(AD_BANNER_INSTANCE == null){
//                AD_BANNER_INSTANCE = new WebView(activity);
//                String html = AD_BANNER_HTML;
//                html.replaceAll("SECTION_ID", Configs.BANNER_SECTION_ID);
//
//                AD_BANNER_INSTANCE.getSettings().setJavaScriptEnabled(true);
//                AD_BANNER_INSTANCE.loadData(html, "text/html", "utf-8");
//            }
//
//            View viewGroup = activity.findViewById(R.id.adViewContainer);
//            if(viewGroup != null ){
//                ((ViewGroup) viewGroup).removeAllViews();
//
//                ((ViewGroup)viewGroup).addView(AD_BANNER_INSTANCE);
//            }
//        }

    }

    public static void onPause(Activity activity) {

        try {
            if (AppConfigsHelper.getStartupConfigs(activity, false).getAdProviderTypeV2().equals(StartupConfigs.AdProviderType.LEADBOLT) && !activity.isFinishing()) {
                AppTracker.pause(activity.getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onResume(Activity activity) {
        try {
            if(AppConfigsHelper.getStartupConfigs(activity, false).getAdProviderTypeV2().equals(StartupConfigs.AdProviderType.LEADBOLT)){
                AppTracker.resume(activity.getApplicationContext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
