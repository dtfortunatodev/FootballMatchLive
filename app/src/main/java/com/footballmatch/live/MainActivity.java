package com.footballmatch.live;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.utils.LogUtil;

public class MainActivity extends BaseNavigationActivity implements RequestAsyncTask.OnRequestListener
{
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // FIXME Remove this request
//        new RequestAsyncTask<MatchEntity>(this, RequestAsyncTask.RequestType.REQUEST_LIVE_MATCHES, this).execute();

        new RequestAsyncTask<StreamLinkEntity>(this, RequestAsyncTask.RequestType.REQUEST_LIST_STREAMS, this)
                .setRequestUrl("http://livefootballvideo.com/streaming/world/club-friendlies/oxford-united-vs-leicester-city").execute();
    }

    @Override
    public void onRequestStart()
    {
        LogUtil.d(TAG, "onRequestStart");
    }

    @Override
    public void onRequestResponse(ResponseDataObject responseDataObject)
    {
        LogUtil.d(TAG, "onRequestResponse");
    }

    /**
     * Start Main Activity
     * @param activity
     */
    public static void startActivity(Activity activity)
    {
        Intent intent = new Intent(activity, MainActivity.class);
        activity.startActivity(intent);
    }

}
