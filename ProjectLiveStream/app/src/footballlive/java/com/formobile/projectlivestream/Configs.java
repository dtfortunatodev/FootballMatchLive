package com.formobile.projectlivestream;

/**
 * Created by Fortuna on 25-10-2014.
 */
public class Configs {

    // Sport Data
    public static final String URL_SPORT = "/en/allupcomingsports/1/";

    // Admob Keys
    public static final String ADMOB_BANNER_ID = "ca-app-pub-5492791201497132/5451120000";
    public static final String ADMOB_INTERSTITIAL_ID = "ca-app-pub-5492791201497132/8404586407";

    // LeadBolt Keys
    public static final String LEADBOLT_PACKAGE_NAME = "com.bwlfjnsfglfbrxtvjn";
    public static final String APP_FIREWORKS_API_Key = "ocwMtGSAqR8KHcyVyxesH5Lpfl4cqCj8";
    public static final String INTERSTITIAL_ID = "540749344";
    public static final String AUDIO_AD_ID = "500614032";

    public static final AdProviderType AD_PROVIDER_TYPE = AdProviderType.ADMOB;

    public static enum AdProviderType{
        ADMOB, LEADBOLT;
    }

}
