package com.footballmatch.live.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuItem;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.NotificationsManager;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.settings.AppConfigs;
import com.footballmatch.live.ui.fragments.LiveMatchesFragment;
import com.footballmatch.live.ui.views.PrivacyPolicyDialog;
import com.footballmatch.live.ui.views.StartupMessageDialog;
import com.footballmatch.live.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

public class LiveMatchesActivity extends BaseNavigationActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = LiveMatchesActivity.class.getSimpleName();



    // LiveMatches Fragment
    private LiveMatchesFragment liveMatchesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Hide Menu
        setActionBarDrawerToggleType(ActionBarType.HIDDEN);

        // Set Content
        if (liveMatchesFragment == null)
        {
            liveMatchesFragment = LiveMatchesFragment.newInstance();
            setContentFragment(liveMatchesFragment);

            // Trying to open a match
            liveMatchesFragment.openMatchDetailsFromLink(NotificationsManager.getInstance(getBaseContext()).getMatchDetailsLinkFromIntent(getIntent()));
        }

        // Try to displau startup message
        StartupMessageDialog.startDialog(this);

        PrivacyPolicyDialog.showDialog(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_livematches, menu);

        // Check if should visible the Social Buttons
        AppConfigs appConfigs = StartupManager.getInstance(getBaseContext()).getAppConfigs();
        if (appConfigs.getFacebookPageUrl() != null && !appConfigs.checkShouldBlockSensibleData())
        {
            menu.findItem(R.id.action_follow_facebook).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.action_follow_facebook).setVisible(false);
        }

        if (appConfigs.getTwitterPageUrl() != null && !appConfigs.checkShouldBlockSensibleData())
        {
            menu.findItem(R.id.action_follow_twitter).setVisible(false);
        }
        else
        {
            menu.findItem(R.id.action_follow_twitter).setVisible(false);
        }

        if (appConfigs.getRedditPageUrl() != null && !appConfigs.checkShouldBlockSensibleData())
        {
            menu.findItem(R.id.action_follow_reddit).setVisible(true);
        }
        else
        {
            menu.findItem(R.id.action_follow_reddit).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_follow_facebook:
                Utils.startURL(getBaseContext(), StartupManager.getInstance(getBaseContext()).getAppConfigs().getFacebookPageUrl());
                return true;
            case R.id.action_follow_twitter:
                Utils.startURL(getBaseContext(), StartupManager.getInstance(getBaseContext()).getAppConfigs().getTwitterPageUrl());
                return true;
            case R.id.action_follow_reddit:
                Utils.startURL(getBaseContext(), StartupManager.getInstance(getBaseContext()).getAppConfigs().getRedditPageUrl());
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult)
    {

    }

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        // Trying to open a match
        liveMatchesFragment.openMatchDetailsFromLink(NotificationsManager.getInstance(getBaseContext()).getMatchDetailsLinkFromIntent(intent));
    }


    /**
     * Start Main Activity
     * @param activity
     */
    public static void startActivity(Activity activity)
    {
        Intent intent = new Intent(activity, LiveMatchesActivity.class);
        if (activity.getIntent() != null && activity.getIntent().getData() != null)
        {
            intent.setData(activity.getIntent().getData());
        }
        intent.putExtras(activity.getIntent());
        activity.startActivity(intent);
    }

}
