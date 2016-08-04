package com.footballmatch.live.data.managers;

import android.content.Context;
import android.os.AsyncTask;
import com.footballmatch.live.data.model.settings.AppConfigs;
import com.footballmatch.live.data.model.settings.IpCatchEntity;
import com.footballmatch.live.utils.LogUtil;
import java.io.IOException;
import okhttp3.Response;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class StartupManager
{
    private static final String TAG = StartupManager.class.getSimpleName();

    // App Configs
    private static final String URL_GET_USER_IP = "http://ipinfo.io/json";
    private static final String URL_APP_CONFIGS = "https://www.dropbox.com/s/dtkn0sef2atviuh/AppConfigs.json?raw=1";
    private static final String APP_CONFIGS_PREFS_KEY = StartupManager.class.getName() + ".APP_CONFIGS_PREFS_KEY";

    // Singleton Instance
    private static StartupManager instance;

    // Data
    private Context context;
    private AppConfigs appConfigs;


    private StartupManager(Context context)
    {
        this.context = context;

        // Try to get App Configs from Prefs
        String jsonPrefs = SharedPreferencesManager.getStringMessage(context, APP_CONFIGS_PREFS_KEY, null);
        if(jsonPrefs != null)
        {
            appConfigs = GSONParser.parseJSONToObject(jsonPrefs, AppConfigs.class);
        }
    }

    /**
     * Get singleton instance
     *
     * @return
     */
    public static StartupManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new StartupManager(context);
        }
        return instance;
    }


    /**
     * Initialize Startup Configurations
     * @param onStartupInitCallback
     */
    public void initStartupManager(final OnStartupInitCallback onStartupInitCallback)
    {
        new AsyncTask<Void, Void, AppConfigs>()
        {

            @Override
            protected AppConfigs doInBackground(Void... params)
            {
                AppConfigs loadedAppConfig = null;
                IpCatchEntity userIp = null;


                // Load Current User Ip Data
                try
                {
                    Response getIpResponse = HttpRequestManager.getInstance().getData(URL_GET_USER_IP, null, null);
                    if(getIpResponse.isSuccessful())
                    {
                        userIp = GSONParser.parseJSONToObject(getIpResponse.body().string(), IpCatchEntity.class);

                        // Update Current App Configs
                        if(appConfigs != null && userIp != null)
                        {
                            appConfigs.setUserCurrentIp(userIp);
                        }

                    }
                }
                catch (IOException e)
                {
                    LogUtil.e(TAG, e.getMessage(), e);
                }

                // Load AppConfigs
                try
                {
                    Response response = HttpRequestManager.getInstance().getData(URL_APP_CONFIGS, null, null);
                    if(response.isSuccessful())
                    {
                        // Parse Body to AppConfigs
                        loadedAppConfig = GSONParser.parseJSONToObject(response.body().string(), AppConfigs.class);

                        // Update Current App Configs
                        if(loadedAppConfig != null && userIp != null)
                        {
                            loadedAppConfig.setUserCurrentIp(userIp);
                        }

                    }
                }
                catch (IOException e)
                {
                    LogUtil.e(TAG, e.getMessage(), e);
                }
                return loadedAppConfig;
            }

            @Override
            protected void onPostExecute(AppConfigs loadedAppConfig)
            {

                // Update Prefs
                if(loadedAppConfig != null)
                {
                    appConfigs = loadedAppConfig;

                    // Update Prefs
                    SharedPreferencesManager.putStringMessage(context, APP_CONFIGS_PREFS_KEY, appConfigs.toString());

                }

                // Response Callback
                onStartupInitCallback.onStartupInitFinished(onStartupInitCallback != null, loadedAppConfig);

                super.onPostExecute(appConfigs);
            }
        }.execute();

    }


    /**
     * Check if is able to run the app
     * @return True if is able or false otherwise
     */
    public boolean isAbleToRunApp()
    {
        return appConfigs != null;
    }

    /**
     * Interface of Initialization of Startup
     */
    public interface OnStartupInitCallback
    {
        void onStartupInitFinished(boolean isCorrectlyStarted, AppConfigs appConfigs);
    }

}
