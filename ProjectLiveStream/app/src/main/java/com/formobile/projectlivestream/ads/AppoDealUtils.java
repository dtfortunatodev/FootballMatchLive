package com.formobile.projectlivestream.ads;

import android.app.Activity;
import android.util.Log;

import com.appodeal.ads.Appodeal;
import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.configs.AppConfigsHelper;

import java.io.IOException;

/**
 * Created by PTECH on 15-06-2015.
 */
public class AppoDealUtils {
    private static final String TAG = AppoDealUtils.class.getSimpleName();

    private static int APPODEAL_INTERSTITIAL_ADTYPE = Appodeal.INTERSTITIAL;

    public static void initializeAppoDealUtils(Activity activity){
        try {
            Appodeal.initialize(activity, AppConfigsHelper.getStartupConfigs(activity, false).getAppoDealId(), Appodeal.ALL | Appodeal.ANY);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage(), e);
        }
    }

    public static void displayBanner(Activity activity){
        // Add AdView on Container
        Appodeal.setBannerViewId(R.id.appodealBannerView);
        Appodeal.show(activity, Appodeal.BANNER_VIEW);
    }

    public static void displayInterstitial(Activity activity){

        if(Appodeal.show(activity, APPODEAL_INTERSTITIAL_ADTYPE)){
            AdvertiseController.updateLastTimeDisplayed();
        }

        // Set Interstitial AdType
        if(APPODEAL_INTERSTITIAL_ADTYPE == Appodeal.VIDEO){
            APPODEAL_INTERSTITIAL_ADTYPE = Appodeal.INTERSTITIAL;
        } else{
            APPODEAL_INTERSTITIAL_ADTYPE = Appodeal.VIDEO;
        }
    }

}
