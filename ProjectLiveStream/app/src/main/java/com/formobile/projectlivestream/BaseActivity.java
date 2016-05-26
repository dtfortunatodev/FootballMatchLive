package com.formobile.projectlivestream;

import android.app.Activity;

import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;
import com.formobile.projectlivestream.ads.LeadBoltUtils;

/**
 * Created by Fortuna on 16-10-2014.
 */
public class BaseActivity extends Activity {

    @Override
    protected void onPause() {

        try {
            if(AppConfigsHelper.getStartupConfigs(getBaseContext(), false).getAdProviderTypeV2().equals(StartupConfigs.AdProviderType.LEADBOLT)){
                LeadBoltUtils.onPause(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.onPause();
    }

    @Override
    protected void onResume() {
        try {
            if(AppConfigsHelper.getStartupConfigs(getBaseContext(), false).getAdProviderTypeV2().equals(StartupConfigs.AdProviderType.LEADBOLT)){
                LeadBoltUtils.onResume(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        super.onResume();
    }
}
