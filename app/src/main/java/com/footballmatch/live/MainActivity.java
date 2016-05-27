package com.footballmatch.live;

import android.os.Bundle;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.MatchEntity;
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
        new RequestAsyncTask<MatchEntity>(this, RequestAsyncTask.RequestType.REQUEST_LIVE_MATCHES, this).execute();
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
}
