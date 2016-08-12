package com.footballmatch.live.managers.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.NotificationsManager;
import com.footballmatch.live.ui.activities.LiveMatchesActivity;
import com.footballmatch.live.utils.LogUtil;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.Random;

/**
 * Created by David Fortunato on 10/08/2016
 * All rights reserved GoodBarber
 */
public class AppNotificationService extends FirebaseMessagingService
{
    private static final String TAG = AppNotificationService.class.getSimpleName();

    // Data Extras
    private static final String DATA_TYPE_NOTIF_KEY   = "typeNotif";
    private static final String DATA_LINK_KEY         = "link";
    private static final String DATA_TEAM_HOME_KEY    = "teamHome";
    private static final String DATA_TEAM_AWAY_KEY    = "teamAway";
    private static final String DATA_IS_TOP_MATCH_KEY = "isTopMatch";

    // Extra
    public static final String EXTRA_NOTIF_MATCH_LINK = "link";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        super.onMessageReceived(remoteMessage);

        LogUtil.d(TAG, "Received notification: " + remoteMessage.getNotification().getTitle());

        // Try to display a notification
        displayMatchNotification(remoteMessage);

    }

    private void displayMatchNotification(RemoteMessage remoteMessage)
    {
        String notifType = remoteMessage.getData().get(DATA_TYPE_NOTIF_KEY);
        String matchLink = remoteMessage.getData().get(DATA_LINK_KEY);
        String teamHome = remoteMessage.getData().get(DATA_TEAM_HOME_KEY);
        String teamAway = remoteMessage.getData().get(DATA_TEAM_AWAY_KEY);
        String isTopMatch = remoteMessage.getData().get(DATA_IS_TOP_MATCH_KEY);

        if (notifType == null || notifType.equalsIgnoreCase("matchDetails"))
        {
            if (matchLink != null &&
                    ((isTopMatch != null && isTopMatch.contains("true")) || NotificationsManager.getInstance(getBaseContext()).isTeamRegistered(teamHome) ||
                            NotificationsManager.getInstance(getBaseContext()).isTeamRegistered(teamAway)))
            {
                // build a Notification
                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notif_icon_72_72)
                        .setContentTitle(remoteMessage.getNotification().getTitle()).setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true);

                // Generate the URL Scheme link
                String urlMatch = remoteMessage.getData().get(DATA_LINK_KEY);

                Intent resultIntent = new Intent(getBaseContext(), LiveMatchesActivity.class).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                resultIntent.putExtra(EXTRA_NOTIF_MATCH_LINK, urlMatch);

                // Because clicking the notification opens a new ("special") activity, there's
                // no need to create an artificial back stack.
                int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
                PendingIntent resultPendingIntent = PendingIntent.getActivity(this, uniqueInt, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(resultPendingIntent);

                // Gets an instance of the NotificationManager service
                NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Builds the notification and issues it.
                mNotifyMgr.notify(0, mBuilder.build());

            }
        }
        else
        {
            // build a Notification
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.notif_icon_72_72)
                    .setContentTitle(remoteMessage.getNotification().getTitle()).setContentText(remoteMessage.getNotification().getBody()).setAutoCancel(true);

            // Generate the URL Scheme link
            String urlMatch = remoteMessage.getData().get(DATA_LINK_KEY);

            Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlMatch));
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Because clicking the notification opens a new ("special") activity, there's
            // no need to create an artificial back stack.
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, 0);
            mBuilder.setContentIntent(resultPendingIntent);

            // Sets an ID for the notification
            int mNotificationId = new Random().nextInt(1000);

            // Gets an instance of the NotificationManager service
            NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            // Builds the notification and issues it.
            mNotifyMgr.notify(mNotificationId, mBuilder.build());
        }


    }

}
