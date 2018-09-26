package com.footballmatch.live.data.managers;

import android.content.Context;
import android.widget.Toast;
import com.footballmatch.live.R;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.data.model.settings.AppConfigs;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.views.BasePopupDialog;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 06/08/2016
 * All rights reserved GoodBarber
 */
public class PlayStreamManager
{

    // Apps Packade
    private static final String SOPCAST_APP_PACKAGE = "org.sopcast.android";


    /**
     * Play the sream
     *
     * @param streamLinkEntity
     */
    public static void playStreamManager(final Context context, final StreamLinkEntity streamLinkEntity)
    {
        ProgressController.startDialog(context);

        // Get App Configs
        final AppConfigs appConfigs = StartupManager.getInstance(context).getAppConfigs();

        switch (streamLinkEntity.getStreamLinkType())
        {
            case ACESTREAM:
                if (validateAppInstalled(context, R.string.popup_stream_acestream_install_app, appConfigs.getAceStreamAppPackagege(),
                                         appConfigs.getAceStreamAppUrl(), true))
                {
                    // Initialize Acestream
                    Utils.startURL(context, streamLinkEntity.getStreamLinkUrl());
                }
                ProgressController.stopDialog();
                break;

            case SOPCAST:
                if (validateAppInstalled(context, R.string.popup_stream_sopcast_install_app, SOPCAST_APP_PACKAGE, appConfigs.getSopcastStreamAppUrl(), false))
                {
                    // Initialize Acestream
                    Utils.startURL(context, streamLinkEntity.getStreamLinkUrl());
                }
                ProgressController.stopDialog();
                break;

            case WEBPLAYER:
                // Initialize Stream
                Utils.startURL(context, streamLinkEntity.getStreamLinkUrl());
//                if (validateAppInstalled(context, R.string.popup_stream_webplayer_install_app, appConfigs.getWebplayerStreamAppPackage(),
//                                         appConfigs.getWebplayerStreamAppUrl(), true))
//                {
//                    // Initialize Acestream
//                    Utils.startURL(context, streamLinkEntity.getStreamLinkUrl());
//                }
                ProgressController.stopDialog();
                break;

            case ARENAVISION:
                // Get ArenaVision Url
                startArenaVisionLink(context, streamLinkEntity);
                break;

        }

        AnalyticsHelper.getInstance().sendEvent("Stream Opened", "Type:" + streamLinkEntity.getStreamLinkType().name());
    }

    /**
     * Start Running the Arena Vision Link
     * @param context
     * @param streamLinkEntity
     */
    private static void startArenaVisionLink(final Context context, StreamLinkEntity streamLinkEntity)
    {

        final AppConfigs appConfigs = StartupManager.getInstance(context).getAppConfigs();

        // Get ArenaVision Url
        new RequestAsyncTask<>(context, RequestAsyncTask.RequestType.REQUEST_ARENAVISION_LINK, new RequestAsyncTask.OnRequestListener()
        {
            @Override
            public void onRequestStart()
            {

            }

            @Override
            public void onRequestResponse(ResponseDataObject responseDataObject)
            {

                if (responseDataObject.isOk())
                {
                    String streamUrl = (String) responseDataObject.getObject();

                    if (streamUrl.startsWith("acestream://"))
                    {
                        if (validateAppInstalled(context, R.string.popup_stream_acestream_install_app, appConfigs.getAceStreamAppPackagege(),
                                                 appConfigs.getAceStreamAppUrl(), true))
                        {
                            // Initialize Acestream
                            Utils.startURL(context, streamUrl);
                        }
                    }
                    else if (streamUrl.startsWith("sop://"))
                    {
                        if (validateAppInstalled(context, R.string.popup_stream_sopcast_install_app, SOPCAST_APP_PACKAGE,
                                                 appConfigs.getSopcastStreamAppUrl(), false))
                        {
                            // Initialize Acestream
                            Utils.startURL(context, streamUrl);
                        }
                    }
                    else
                    {
                        Toast.makeText(context, R.string.error_getting_arenavision_stream, Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    // Error
                    Toast.makeText(context, R.string.error_getting_arenavision_stream, Toast.LENGTH_SHORT).show();
                }

                ProgressController.stopDialog();
            }
        }).setRequestUrl(streamLinkEntity.getStreamLinkUrl()).execute();
    }


    /**
     * Validate if the AceStream app is installed, if not, should display a popup to ask the user to install the app
     *
     * @param context
     *
     * @return true if is installed, false otherwise
     */
    public static boolean validateAppInstalled(final Context context, int resDescriptionPopup, final String packageName, final String defaultUrlDownload,
                                               final boolean usePlayStore)
    {
        if (!Utils.isPackageInstalled(context, packageName))
        {
            // Init Popup
            BasePopupDialog.startPopup(context, context.getString(resDescriptionPopup), context.getString(R.string.popup_stream_cancel),
                                       context.getString(R.string.popup_stream_download), new BasePopupDialog.OnPopupListener()
                    {
                        @Override
                        public void onLeftBtnClick(BasePopupDialog basePopupDialog)
                        {
                            basePopupDialog.dismiss();
                        }

                        @Override
                        public void onRightBtnClick(BasePopupDialog basePopupDialog)
                        {
                            basePopupDialog.dismiss();

                            // Start Downloading
                            if (usePlayStore)
                            {
                                Utils.downloadPlayStoreApp(context, packageName, defaultUrlDownload);
                            }
                            else
                            {
                                Utils.startURL(context, defaultUrlDownload);
                            }
                        }

                        @Override
                        public void onCancelled()
                        {

                        }
                    });

            return false;
        }
        else
        {
            return true;
        }
    }

}
