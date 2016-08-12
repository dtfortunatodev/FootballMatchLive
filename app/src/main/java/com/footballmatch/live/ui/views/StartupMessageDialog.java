package com.footballmatch.live.ui.views;

import android.content.Context;
import android.view.View;
import com.footballmatch.live.data.managers.SharedPreferencesManager;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.settings.StartupMessageEntity;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class StartupMessageDialog extends BasePopupDialog
{
    private static final String PREFS_LAST_MESSAGE_ID_KEY = "PREFS_LAST_MESSAGE_ID_KEY";

    // Data
    private StartupMessageEntity startupMessageEntity;

    public StartupMessageDialog(Context context)
    {
        super(context);
    }

    public StartupMessageDialog(Context context, int themeResId)
    {
        super(context, themeResId);
    }

    protected StartupMessageDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }

    public StartupMessageEntity getStartupMessageEntity()
    {
        return startupMessageEntity;
    }

    public void setStartupMessageEntity(StartupMessageEntity startupMessageEntity)
    {
        this.startupMessageEntity = startupMessageEntity;

        // Setup views
        getTvDescription().setText(startupMessageEntity.getMessage());
        getTvBtnLeft().setVisibility(View.GONE);
        getTvBtnRight().setVisibility(View.VISIBLE);
        getTvBtnRight().setText("OK");
        getTvBtnRight().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dismiss();
            }
        });
    }

    /**
     * Check if should start the dialog and if true then start it automatically
     * @param context
     */
    public static void startDialog(Context context)
    {
        // Check if should show Dialog
        StartupMessageEntity startupMessageEntity = StartupManager.getInstance(context).getAppConfigs().getStartupMessageEntity();

        if (!StartupManager.getInstance(context).getAppConfigs().checkShouldBlockSensibleData() && startupMessageEntity != null && startupMessageEntity.isEnabled() &&
                SharedPreferencesManager.getIntValue(context, PREFS_LAST_MESSAGE_ID_KEY, 0) < startupMessageEntity.getId())
        {
            StartupMessageDialog dialog = new StartupMessageDialog(context);
            dialog.setStartupMessageEntity(startupMessageEntity);
            dialog.show();

            // Update Prefs
            SharedPreferencesManager.putIntValue(context, PREFS_LAST_MESSAGE_ID_KEY, startupMessageEntity.getId());
        }


    }


}
