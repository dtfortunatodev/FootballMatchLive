package com.footballmatch.live.data.managers;

import android.os.Bundle;
import com.footballmatch.live.FootballMatchApplication;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by David Fortunato on 07/08/2016
 * All rights reserved GoodBarber
 */
public class AnalyticsHelper
{

    // Tracker
    private FirebaseAnalytics mFirebaseAnalytics;


    private static AnalyticsHelper instance;


    public static AnalyticsHelper getInstance()
    {
        if (instance == null)
        {
            instance = new AnalyticsHelper();
        }

        return instance;
    }

    private AnalyticsHelper()
    {
        mFirebaseAnalytics = FootballMatchApplication.getApplication().getDefaultTracker();
    }

    public void sendEvent(String category, String action)
    {
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, category);
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, action);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

}
