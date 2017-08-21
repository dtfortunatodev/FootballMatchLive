package com.footballmatch.live;

import android.app.Application;
import android.os.StrictMode;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by David Fortunato on 09/08/2016
 * All rights reserved GoodBarber
 */
public class FootballMatchApplication extends Application
{

    private FirebaseAnalytics mFirebaseAnalytics;

    private static FootballMatchApplication mApplication;

    @Override
    public void onCreate()
    {
        super.onCreate();

        mApplication = this;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    public static FootballMatchApplication getApplication()
    {
        return mApplication;
    }


    /**
     * @return tracker
     */
    synchronized public FirebaseAnalytics getDefaultTracker() {
        if (mFirebaseAnalytics == null) {
            mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        }
        return mFirebaseAnalytics;
    }
}
