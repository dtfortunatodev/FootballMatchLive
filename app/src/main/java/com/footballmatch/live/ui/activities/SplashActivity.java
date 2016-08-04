package com.footballmatch.live.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.settings.AppConfigs;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class SplashActivity extends Activity implements StartupManager.OnStartupInitCallback
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        // Set Layout
        setContentView(R.layout.activity_splash_layout);

        // Startup AppConfigs
        StartupManager.getInstance(getApplicationContext()).initStartupManager(this);
    }

    @Override
    public void onStartupInitFinished(boolean isCorrectlyStarted, AppConfigs appConfigs)
    {
        if(StartupManager.getInstance(getApplicationContext()).isAbleToRunApp())
        {
            LiveMatchesActivity.startActivity(this);
            finish();
        } else
        {
            Toast.makeText(SplashActivity.this, "Something failed on initilizing the App. Contact us by email please. The app will shutdown", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
