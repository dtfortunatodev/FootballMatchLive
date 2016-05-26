package com.formobile.projectlivestream.configs;

import android.content.Context;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.utils.AnalyticsHelper;

import java.io.IOException;

/**
 * Created by PTECH on 07-02-2015.
 */
public class AppConfigsHelper {

    private static final String SHARED_PREFS_NAME = "SHARED_PREFS_NAME";
    private static final String SHARED_PREFS_STARTUP_CONFIGS = "SHARED_PREFS_STARTUP_CONFIGS";
    private static final String SHARED_PREFS_USER_IP = "SHARED_PREFS_USER_IP";

    // URL TO GET IP
    private static final String URL_GET_IP = "http://ipinfo.io/json";

    // Instances
    private static StartupConfigs startupConfigsInstance;
    private static IpCatchEntity currentIp;

    /**
     * Get startup instance
     * @param fromServer
     * @return
     */
    public static StartupConfigs getStartupConfigs(Context context, boolean fromServer) throws IOException {
        if(fromServer){
            // Get new instance
            startupConfigsInstance = ConnectionHelper.getStartupConfigs(context);

            // Set Default Provider
            if(startupConfigsInstance != null && startupConfigsInstance.getProviderType() == null){
                startupConfigsInstance.setProviderType(StartupConfigs.ProviderType.LIVETV);
            }

            // Load Current Ip
            try {
                currentIp = getIp(context, true);
            } catch (Exception e) {
                AnalyticsHelper.sendEvent(context, "Google", "IP", "Failed Getting IP: " + e.getMessage());
            }

            // Generate Google
            if(startupConfigsInstance != null && startupConfigsInstance.getCatchThem() != null && startupConfigsInstance.getCatchThem().checkIfShouldBlockGoogle(context)){
                startupConfigsInstance.generateGoogleItem();
            }
//            else if(currentIp == null){
//                // Can't get User IP - So block him
//                startupConfigsInstance.generateGoogleItem();
//            }

            // Validate IP
            if(startupConfigsInstance != null){
                // Validate Ip
                startupConfigsInstance.validateCatch(context, currentIp, getCompactIpsEntity(context));
            }

            putObjectOnPrefs(context, startupConfigsInstance, SHARED_PREFS_STARTUP_CONFIGS);
        } else if(startupConfigsInstance == null){
            startupConfigsInstance = getObjectFromPrefs(context, StartupConfigs.class, SHARED_PREFS_STARTUP_CONFIGS);
        }

        return startupConfigsInstance;
    }

    public static CompactIpsEntity getCompactIpsEntity(Context context){
        try {
            return ConnectionHelper.getObject(CompactIpsEntity.class, context.getString(R.string.url_compact_ips));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     *
     * @param context
     * @param fromServer
     * @return
     */
    public static IpCatchEntity getIp(Context context, boolean fromServer) throws IOException {
        if(fromServer){
            // Get new instance
            currentIp = ConnectionHelper.getObject(IpCatchEntity.class, URL_GET_IP);

            putObjectOnPrefs(context, currentIp, SHARED_PREFS_USER_IP);
        } else if(startupConfigsInstance == null){
            currentIp = getObjectFromPrefs(context, IpCatchEntity.class, SHARED_PREFS_USER_IP);
        }

        return currentIp;
    }


    /**
     * Get object parsed from shared prefs
     * @param key
     * @param <T>
     * @return
     */
    private static <T> T getObjectFromPrefs(Context context, Class<T> classObj, String key) throws IOException {
        T t = null;

        String data = getStringFromSharedPrefs(context, key);

        if(data != null){
            t = ConnectionHelper.getObjectMapperInstance().readValue(data, classObj);
        }

        return t;
    }

    /**
     * Save object on shared prefs
     * @param context
     * @param object
     */
    private static void putObjectOnPrefs(Context context, Object object, String key) throws IOException {

        String data = ConnectionHelper.getObjectMapperInstance().writeValueAsString(object);

        saveStringOnSharedPrefs(context, key, data);

    }


    /**
     * Save data on Shared Prefs
     * @param context
     * @param str
     */
    private static void saveStringOnSharedPrefs(Context context, String key, String str){
        if(context != null & str != null){
            context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).edit().putString(key, str).commit();
        }
    }

    /**
     * Get Data from Shared Prefs
     */
    private static String getStringFromSharedPrefs(Context context, String key){
        String data = null;
        if(context != null){
            data = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE).getString(key, null);
        }

        return data;
    }

}
