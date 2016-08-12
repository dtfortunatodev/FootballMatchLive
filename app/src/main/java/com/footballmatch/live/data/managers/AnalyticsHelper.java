package com.footballmatch.live.data.managers;

import com.footballmatch.live.FootballMatchApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by David Fortunato on 07/08/2016
 * All rights reserved GoodBarber
 */
public class AnalyticsHelper
{

    // Tracker
    private Tracker tracker;


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
        tracker = FootballMatchApplication.getApplication().getDefaultTracker();
    }

    public void sendEvent(String category, String action)
    {
        tracker.send(new HitBuilders.EventBuilder()
                              .setCategory(category)
                              .setAction(action)
                              .build());
    }

}
