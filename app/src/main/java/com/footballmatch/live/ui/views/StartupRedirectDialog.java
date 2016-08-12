package com.footballmatch.live.ui.views;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.SharedPreferencesManager;
import com.footballmatch.live.data.model.settings.UpdateRedirectDialog;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 09/08/2016
 * All rights reserved GoodBarber
 */
public class StartupRedirectDialog extends BasePopupDialog
{
    private static final String PREFS_LAST_NEW_VERSION_DISPLAYED_KEY = "PREFS_LAST_NEW_VERSION_DISPLAYED_KEY";

    public StartupRedirectDialog(Context context)
    {
        super(context);
    }

    public StartupRedirectDialog(Context context, int themeResId)
    {
        super(context, themeResId);
    }

    protected StartupRedirectDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
    {
        super(context, cancelable, cancelListener);
    }

    public static void startRedirectDialog(final Activity activity, final UpdateRedirectDialog updateRedirectDialog, @NonNull final OnRedirectDialogListener onRedirectDialogListener)
    {
        if (updateRedirectDialog != null && updateRedirectDialog.shouldDisplayDialog() &&
                SharedPreferencesManager.getIntValue(activity, PREFS_LAST_NEW_VERSION_DISPLAYED_KEY, -1) != updateRedirectDialog.getCurrentVersion())
        {

            OnPopupListener listener = new OnPopupListener()
            {
                @Override
                public void onLeftBtnClick(BasePopupDialog basePopupDialog)
                {
                    if (updateRedirectDialog.isBlocked())
                    {
                        activity.finish();
                    }
                    else
                    {
                        onRedirectDialogListener.proceed();
                    }
                }

                @Override
                public void onRightBtnClick(BasePopupDialog basePopupDialog)
                {
                    Utils.startURL(activity, updateRedirectDialog.getUpdateLink());

                    activity.finish();
                }

                @Override
                public void onCancelled()
                {
                    onRedirectDialogListener.proceed();
                }
            };

            if (updateRedirectDialog.isBlocked())
            {
                startPopup(activity, activity.getString(R.string.dialog_update_app_available_mandatory), activity.getString(R.string.dialog_update_app_btn_left),
                           activity.getString(R.string.dialog_update_app_btn_right), listener);
            }
            else if (updateRedirectDialog.isUpdateAvailable())
            {
                startPopup(activity, activity.getString(R.string.dialog_update_app_available), activity.getString(R.string.dialog_update_app_btn_left),
                           activity.getString(R.string.dialog_update_app_btn_right), listener);

                // update version displayed
                SharedPreferencesManager.putIntValue(activity, PREFS_LAST_NEW_VERSION_DISPLAYED_KEY, updateRedirectDialog.getCurrentVersion());
            }

        }
        else
        {
            onRedirectDialogListener.proceed();
        }
    }


    public interface OnRedirectDialogListener
    {
        void proceed();
    }

}
