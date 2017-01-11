package com.footballmatch.live.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.settings.AppConfigs;
import com.footballmatch.live.ui.views.BasePopupDialog;
import com.footballmatch.live.ui.views.StartupRedirectDialog;

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
        Intent intent = getIntent();
        setContentView(R.layout.activity_splash_layout);

        // Startup AppConfigs
        StartupManager.getInstance(getApplicationContext()).initStartupManager(this);
    }

    @Override
    public void onStartupInitFinished(boolean isCorrectlyStarted, AppConfigs appConfigs)
    {

        if (isCorrectlyStarted && appConfigs != null)
        {
            // Validate if should Display Dialog
            StartupRedirectDialog.startRedirectDialog(this, StartupManager.getInstance(getBaseContext()).getAppConfigs().getUpdateRedirectDialog(), new StartupRedirectDialog.OnRedirectDialogListener()
            {
                @Override
                public void proceed()
                {
                    if(StartupManager.getInstance(getApplicationContext()).isAbleToRunApp())
                    {
                        LiveMatchesActivity.startActivity(SplashActivity.this);
                        finish();
                    } else
                    {
                        Toast.makeText(SplashActivity.this, "Something failed on initilizing the App. Contact us by email please. The app will shutdown", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            });
        }
        else
        {
            BasePopupDialog.startPopup(this, getResources().getString(R.string.error_no_internet_connection), getResources().getString(R.string.error_no_internet_connection_left_btn),
                                       getResources().getString(R.string.error_no_internet_connection_right_btn), new BasePopupDialog.OnPopupListener()
                    {
                        @Override
                        public void onLeftBtnClick(BasePopupDialog basePopupDialog)
                        {
                            basePopupDialog.dismiss();
                            finish();
                        }

                        @Override
                        public void onRightBtnClick(BasePopupDialog basePopupDialog)
                        {
                            basePopupDialog.dismiss();
                            // Startup AppConfigs
                            StartupManager.getInstance(getApplicationContext()).initStartupManager(SplashActivity.this);
                        }

                        @Override
                        public void onCancelled()
                        {
                            finish();
                        }
                    });
        }



    }

}
