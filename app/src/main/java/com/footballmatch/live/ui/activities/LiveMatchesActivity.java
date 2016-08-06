package com.footballmatch.live.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.footballmatch.live.ui.fragments.LiveMatchesFragment;

public class LiveMatchesActivity extends BaseNavigationActivity
{
    private static final String TAG = LiveMatchesActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide Menu
        setActionBarDrawerToggleType(ActionBarType.HIDDEN);

        // Set Content
        setContentFragment(LiveMatchesFragment.newInstance());
    }

    /**
     * Start Main Activity
     * @param activity
     */
    public static void startActivity(Activity activity)
    {
        Intent intent = new Intent(activity, LiveMatchesActivity.class);
        activity.startActivity(intent);
    }

}
