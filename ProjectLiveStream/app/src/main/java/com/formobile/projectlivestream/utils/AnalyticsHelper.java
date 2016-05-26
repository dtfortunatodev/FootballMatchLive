package com.formobile.projectlivestream.utils;

import android.content.Context;

import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;

/**
 * Created by PTECH on 10-02-2015.
 */
public class AnalyticsHelper {

    public static void sendEvent(Context context, String category, String action, String label){
        try {
            if(context != null){
                EasyTracker.getInstance(context).send(MapBuilder
                        .createEvent(category,     // Event category (required)
                                action,  // Event action (required)
                                label,   // Event label
                                null)            // Event value
                        .build());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
