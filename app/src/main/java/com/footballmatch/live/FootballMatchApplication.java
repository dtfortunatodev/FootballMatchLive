package com.footballmatch.live;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by David Fortunato on 09/08/2016
 * All rights reserved GoodBarber
 */
public class FootballMatchApplication extends Application
{

    private Tracker mTracker;

    private static FootballMatchApplication mApplication;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mApplication = this;
    }

    public static FootballMatchApplication getApplication()
    {
        return mApplication;
    }


    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);

            // Set Automatically Track Screens
            mTracker.enableAutoActivityTracking(true);
        }
        return mTracker;
    }
}
