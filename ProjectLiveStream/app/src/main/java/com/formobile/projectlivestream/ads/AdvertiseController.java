package com.formobile.projectlivestream.ads;

import android.app.Activity;
import android.content.Context;

import com.formobile.projectlivestream.BaseSoccerLiveActivity;
import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;

import java.io.IOException;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by PTECH on 07-02-2015.
 */
public class AdvertiseController {

    private static final String SHARED_ADS_LAST_TIME_CLICK_V2 = "SHARED_ADS_LAST_TIME_CLICK_V2";

    public static long lastTime = -1;



    public static void onClickAds(Context context){

        try {
            if(context != null){
                context.getSharedPreferences(BaseSoccerLiveActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putLong(SHARED_ADS_LAST_TIME_CLICK_V2, Calendar.getInstance().getTimeInMillis()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initAds(Activity activity){
        try {
            if(activity != null){

                StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(activity, false);

                switch (startupConfigs.getAdProviderTypeV2()){
                    case REVMOB:
                        RevMobUtils.getInstance().startSession(activity);
                        break;

                    case LEADBOLT:
                        LeadBoltUtils.initializeLeadBolt(activity);
                        break;

                    case APPO_DEAL:
                        AppoDealUtils.initializeAppoDealUtils(activity);
                        break;

                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void displayInterstitial(Activity activity){
        try {
            if(activity != null && shouldDisplayAds(activity) && validateLastTimeAndRandom(activity)){

                switch(AppConfigsHelper.getStartupConfigs(activity, false).getAdProviderTypeV2()){


                    case ADMOB:
                        AdMobUtils.showInterstitialEndMatch(activity);
                        break;

                    case LEADBOLT:
                        LeadBoltUtils.loadDisplayAd(activity);
                        break;

                    case REVMOB:
                        RevMobUtils.getInstance().displayInterstitial(activity);
                        break;

                    case APPO_DEAL:
                        AppoDealUtils.displayInterstitial(activity);
                        break;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void displayBanner(Activity activity){
        try {
            if(activity != null && shouldDisplayAds(activity)){

                StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(activity, false);

                switch (startupConfigs.getAdProviderTypeV2()){

                    case ADMOB:
                        AdMobUtils.showBanner(activity);
                        break;

                    case REVMOB:
                        RevMobUtils.getInstance().displayBanner(activity);
                        break;

                    case APPO_DEAL:
                        AppoDealUtils.displayBanner(activity);
                        break;

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean validateLastTimeAndRandom(Context context){

        try {
            StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(context, false);

            // Check probability
            if(startupConfigs.getProbabilityAds() != 100){
                // Probability
                int random = new Random().nextInt(100);

                if(random > startupConfigs.getProbabilityAds()){
                    return false;
                }
            }

            if(lastTime == -1 || (Calendar.getInstance().getTimeInMillis() - lastTime) > AppConfigsHelper.getStartupConfigs(context, false).getPeriodToDisplayAds()){
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();

            return false;
        }

        return false;
    }

    public static boolean shouldDisplayAds(Context context){
        try {
            if(AppConfigsHelper.getStartupConfigs(context, false).getTimeFreeOfAds() > 0){
                long lastTimeClicked = context.getSharedPreferences(BaseSoccerLiveActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).getLong(SHARED_ADS_LAST_TIME_CLICK_V2, 0);

                if(Calendar.getInstance().getTimeInMillis() < lastTimeClicked){
                    return true;
                } else if((Calendar.getInstance().getTimeInMillis() - lastTimeClicked) > AppConfigsHelper.getStartupConfigs(context, false).getTimeFreeOfAds()){
                    return true;
                } else{
                    return false;
                }
            } else{
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;

    }




    public static void updateLastTimeDisplayed(){
        lastTime = Calendar.getInstance().getTimeInMillis();
    }


}
