package com.footballmatch.live.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

        initApp();

    }

    private void initApp()
    {
        // Startup AppConfigs
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            requestWriteExternalStoragePermission();
        }
        else
        {
            StartupManager.getInstance(getApplicationContext()).initStartupManager(this);
        }
    }

    private void requestWriteExternalStoragePermission() {

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("You need to enable permissions to allow our app to work properly.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,  String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // allowed
                    initApp();
                } else {
                    // denied
                    finish();
                }
            break;
        }
    }
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
