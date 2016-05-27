package com.formobile.projectlivestream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;
import com.formobile.projectlivestream.entities.PopupGenericEntity;
import com.formobile.projectlivestream.interfaces.PopupGenericInterface;
import com.formobile.projectlivestream.utils.AnalyticsHelper;
import com.formobile.projectlivestream.utils.PopupController;
import com.formobile.projectlivestream.utils.ProgressController;
import java.io.IOException;

/**
 * Created by PTECH on 26-01-2015.
 */
public class StartAppActivity extends Activity{
    private static final String TAG = StartAppActivity.class.getSimpleName();

    private static final String PREFS_NAME = "DUMMY_PREFS_ANSWER";
    private static final String PREFS_ALREADY_ANSWERED = "PREFS_ALREADY_ANSWERED";

    private boolean answered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.quote_base_layout);

        loadStartConfigs();

    }


    private void loadStartConfigs(){

        new AsyncTask<Void, Void, StartupConfigs>(){

            int tryCounter = 0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                ProgressController.startDialog(StartAppActivity.this);
            }

            @Override
            protected StartupConfigs doInBackground(Void... voids) {

                try {
                    return AppConfigsHelper.getStartupConfigs(getBaseContext(), true);
                } catch (IOException e) {

                    Log.e(TAG, e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(final StartupConfigs startupConfigs) {

                if(startupConfigs != null){

                    // Check if should send ip
                    try {
                        if(startupConfigs.getCatchThem() != null && startupConfigs.getCatchThem().checkIfShouldCatch(getBaseContext())){
                            AnalyticsHelper.sendEvent(getBaseContext(), "Google", "IP", AppConfigsHelper.getIp(getBaseContext(), false).exportJson());
                        }
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage(), e);
                        AnalyticsHelper.sendEvent(getBaseContext(), "Google", "IP", "Failed sending IP: " + e.getMessage());
                    }

                    if(startupConfigs.getUpdateEntity().checkIfShouldUpdate(getBaseContext())){

                        AnalyticsHelper.sendEvent(getBaseContext(), "Start App", "Update App", "Displayed");

                        // Show Upload App Popup
                        PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_upadte_app_title, R.string.popup_update_app_description, R.string.popup_update_app_btn_confirmation, R.string.popup_update_app_btn_cancel, new PopupGenericInterface() {

                            @Override
                            public void onConfirmationClicked(Dialog dialog) {
                                AnalyticsHelper.sendEvent(getBaseContext(), "Start App", "Update App", "Clicked");

                                dialog.dismiss();
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(startupConfigs.getUpdateEntity().getUrlUpdate()));
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }

                            @Override
                            public void onCancelClicked(Dialog dialog) {
                                AnalyticsHelper.sendEvent(getBaseContext(), "Start App", "Update App", "Cancelled");

                                dialog.dismiss();
                                finish();
                            }
                        });
                        PopupController.showGenericPopup(StartAppActivity.this, popupGenericEntity);

                    } else{
                        CoreMainActivity.startActivity(StartAppActivity.this);

                    }
                } else{
                    // Show Error Popup
                    PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_error_connection_title, R.string.popup_error_connection_description, R.string.popup_error_connection_btn_confirmation, R.string.popup_error_connection_btn_cancel, new PopupGenericInterface() {

                        @Override
                        public void onConfirmationClicked(Dialog dialog) {
                            dialog.dismiss();
                            loadStartConfigs();
                        }

                        @Override
                        public void onCancelClicked(Dialog dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    PopupController.showGenericPopup(StartAppActivity.this, popupGenericEntity);
                }

                ProgressController.stopDialog();

                super.onPostExecute(startupConfigs);
            }
        }.execute();

    }

    @Override
    protected void onStart() {
        super.onStart();

//        EasyTracker.getInstance(this).activityStart(this);
    }



    @Override
    protected void onStop() {
        super.onStop();

//        EasyTracker.getInstance(this).activityStop(this);
    }


    private static enum MessageType{
        CORRECT, WRONG, ALREADY_ANSWERED;
    }

}
