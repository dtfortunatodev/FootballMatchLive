package com.footballmatch.live.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.adapters.LiveMatchesRecyclerAdapter;
import com.footballmatch.live.utils.LogUtil;

public class LiveMatchesActivity extends BaseNavigationListActivity implements RequestAsyncTask.OnRequestListener
{
    private static final String TAG = LiveMatchesActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Start Live Matches request
        new RequestAsyncTask<MatchEntity>(this, RequestAsyncTask.RequestType.REQUEST_LIVE_MATCHES, this).execute();
    }

    @Override
    protected BaseRecyclerViewAdapter generateRecyclerViewAdapter()
    {
        return new LiveMatchesRecyclerAdapter(this);
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

        if (responseDataObject.isOk())
        {
            getBaseRecyclerViewAdapter().addListData(responseDataObject.getListObjectsResponse(), true);
            getBaseRecyclerViewAdapter().addListData(responseDataObject.getListObjectsResponse(), false);
        }
    }

    /**
     * Start Main Activity
     * @param activity
     */
    public static void startActivity(Activity activity)
    {
        Intent intent = new Intent(activity, LiveMatchesActivity.class);
        activity.startActivity(intent);
    }

}
