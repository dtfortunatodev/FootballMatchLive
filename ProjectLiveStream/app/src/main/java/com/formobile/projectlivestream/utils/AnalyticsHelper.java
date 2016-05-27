package com.formobile.projectlivestream.utils;

import android.content.Context;
import android.util.Log;
import com.formobile.projectlivestream.CoreLiveStreamingApplication;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by PTECH on 10-02-2015.
 */
public class AnalyticsHelper {
    private static final String TAG = AnalyticsHelper.class.getSimpleName();

    private static Tracker mTracker;

    public static void sendEvent(Context context, String category, String action, String label){
        try {
            if(context != null && getTracker() != null){

                getTracker().send(new HitBuilders
                        .EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
            }
        } catch (Exception e) {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
    }


    /**
     * Get current Tracker
     * @return
     */
    private static Tracker getTracker()
    {
        try
        {
            if(mTracker == null)
            {
                mTracker = CoreLiveStreamingApplication.getInstanceApplication().getDefaultTracker();
            }
        }
        catch (Exception e)
        {
            Log.e(TAG, e.getLocalizedMessage(), e);
        }
        return mTracker;
    }

}
