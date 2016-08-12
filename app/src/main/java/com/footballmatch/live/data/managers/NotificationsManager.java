package com.footballmatch.live.data.managers;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.footballmatch.live.data.model.settings.TeamNotificationEntry;
import com.footballmatch.live.managers.services.AppNotificationService;
import com.footballmatch.live.utils.LogUtil;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by David Fortunato on 10/08/2016
 * All rights reserved GoodBarber
 */
public class NotificationsManager
{
    private static final String TAG = NotificationsManager.class.getSimpleName();

    private static final String PREFS_TEAM_NOTIFICATIONS_ENTRY_KEY = "PREFS_TEAM_NOTIFICATIONS_ENTRY_KEY";

    // Match To Open Link
    private static final String PATH_LINK = "link=";

    // Context
    private Context context;

    // Data
    private TeamNotificationEntry teamNotificationEntry;

    // Singleton Instance
    private static NotificationsManager instance;

    private NotificationsManager(Context context)
    {
        this.context = context;

        String jsonNotifs = SharedPreferencesManager.getStringMessage(context, PREFS_TEAM_NOTIFICATIONS_ENTRY_KEY, null);

        if (jsonNotifs == null)
        {
            teamNotificationEntry = new TeamNotificationEntry();
        }
        else
        {
            teamNotificationEntry = GSONParser.parseJSONToObject(jsonNotifs, TeamNotificationEntry.class);
        }
    }

    public static NotificationsManager getInstance(Context context)
    {
        if (instance == null)
        {
            instance = new NotificationsManager(context);
        }

        return instance;
    }

    public boolean isTeamRegistered(String teamName)
    {
        if (teamName != null)
        {
            return teamNotificationEntry.getSetTeamNamesRegistered().contains(teamName);
        }
        else
        {
            return false;
        }

    }


    public void registerTeam(String teamName)
    {
        if (teamName != null)
        {
            if (!isTeamRegistered(teamName))
            {
                teamNotificationEntry.getSetTeamNamesRegistered().add(teamName);
                try
                {
                    FirebaseMessaging.getInstance().subscribeToTopic(teamName.replaceAll(" ", "_"));
                }
                catch (Exception e)
                {
                    LogUtil.e(TAG, e.getMessage(), e);
                }
                savePrefs();
            }

            // Send Analytics event
            AnalyticsHelper.getInstance().sendEvent("TeamNotifications", "Registered Team: " + teamName);
        }
    }

    public void unregisterTeam(String teamName)
    {
        if (teamName != null)
        {
            if (isTeamRegistered(teamName))
            {
                teamNotificationEntry.getSetTeamNamesRegistered().remove(teamName);
                try
                {
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(teamName.replaceAll(" ", "_"));
                }
                catch (Exception e)
                {
                    LogUtil.e(TAG, e.getMessage(), e);
                }
                savePrefs();
            }

            // Send Analytics event
            AnalyticsHelper.getInstance().sendEvent("TeamNotifications", "Unregistered Team: " + teamName);
        }
    }

    /**
     * Save preferences
     */
    private void savePrefs()
    {
        SharedPreferencesManager.putStringMessage(context, PREFS_TEAM_NOTIFICATIONS_ENTRY_KEY, teamNotificationEntry.toString());
    }


    /**
     * Get Match link to open
     * @return
     */
    public String getMatchDetailsLinkFromIntent(Intent intent)
    {
        if (intent != null)
        {
            String action = intent.getAction();
            Uri data = intent.getData();

            if (data != null || intent.getStringExtra(AppNotificationService.EXTRA_NOTIF_MATCH_LINK) != null)
            {

                String uriString = null;
                if (data != null && data.toString().contains(PATH_LINK))
                {
                    uriString = data.toString();
                    uriString = uriString.substring(uriString.indexOf("link=") + PATH_LINK.length());
                }

                if (uriString == null || uriString.isEmpty())
                {
                    uriString = intent.getStringExtra(AppNotificationService.EXTRA_NOTIF_MATCH_LINK);
                }

                // Find Link
                if (uriString != null && !uriString.isEmpty())
                {
                    return uriString;
                }
            }

            // Clean Data Intent
            intent.setData(null);
            if (intent.getExtras() != null)
            {
                intent.getExtras().clear();
            }
        }

        return null;
    }

}
