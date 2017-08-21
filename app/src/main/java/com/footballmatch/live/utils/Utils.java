package com.footballmatch.live.utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import com.footballmatch.live.data.managers.AnalyticsHelper;
import java.io.File;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class Utils
{

    /**
     * Validate if a package is installed
     * @param context
     * @param packagename
     * @return
     */
    public static boolean isPackageInstalled(Context context, String packagename) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * Start an URL
     * @param context
     * @param url
     */
    public static void startURL(Context context, String url)
    {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void startAutoInstall(final Activity activity, String url)
    {
        //get destination to update file and set Uri
        // aplication with existing package from there. So for me, alternative solution is Download directory in external storage. If there is better
        //solution, please inform us in comment
        String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";
        String fileName = "FootballMatchLive.apk";
        destination += fileName;
        final Uri uri = Uri.parse("file://" + destination);

        //Delete update file if exists
        File file = new File(destination);
        if (file.exists())
            //file.delete() - test this, I think sometimes it doesnt work
            file.delete();


        //set downloadmanager
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setDescription("Please wait, it's downloading the new update.");
        request.setTitle("Updating");

        //set destination
        request.setDestinationUri(uri);

        // get download service and enqueue file
        final DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        final long downloadId = manager.enqueue(request);

        //set BroadcastReceiver to install app when .apk is downloaded
        BroadcastReceiver onComplete = new BroadcastReceiver() {
            public void onReceive(Context ctxt, Intent intent) {
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                install.setDataAndType(uri,
                                       manager.getMimeTypeForDownloadedFile(downloadId));
                activity.startActivity(install);

                activity.unregisterReceiver(this);
                activity.finish();
            }
        };
        //register receiver for when .apk download is compete
        activity.registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    /**
     * Download an app hosted on Play Store
     * @param context
     * @param packageName
     */
    public static void downloadPlayStoreApp(Context context, String packageName, String defaultUrl)
    {
        if (packageName != null && !packageName.isEmpty())
        {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
            }
        } else
        {
            startURL(context, defaultUrl);
        }

    }


    /**
     * Share a link
     * @param activity
     * @param link
     * @param subject
     */
    public static void shareLink(Activity activity, String link, String subject)
    {
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");

        // Add data to the intent, the receiving app will decide
        // what to do with it.
        share.putExtra(Intent.EXTRA_SUBJECT, subject);
        share.putExtra(Intent.EXTRA_TEXT, link);
        share.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(Intent.createChooser(share, "Share a match"));


        // Send to Analytics
        AnalyticsHelper.getInstance().sendEvent("Shared a Match", "Link: " + link);
    }




}
