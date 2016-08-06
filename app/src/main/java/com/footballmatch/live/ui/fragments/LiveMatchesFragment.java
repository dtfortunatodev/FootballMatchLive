package com.footballmatch.live.ui.fragments;

import android.view.LayoutInflater;
import android.view.View;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.adapters.LiveMatchesRecyclerAdapter;
import com.footballmatch.live.ui.views.BaseRecyclerView;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class LiveMatchesFragment extends BaseFragment
{

    // Views
    private BaseRecyclerView recyclerView;
    private LiveMatchesRecyclerAdapter recyclerAdapter;

    public static LiveMatchesFragment newInstance()
    {
        LiveMatchesFragment liveMatchesFragment = new LiveMatchesFragment();

        return liveMatchesFragment;
    }

    @Override
    public void onRequestResponse(ResponseDataObject responseDataObject)
    {
        if (responseDataObject.isOk())
        {
            recyclerAdapter.addListData(responseDataObject.getListObjectsResponse(), true);
        }
    }

    @Override
    public View generateFragmentView(LayoutInflater inflater)
    {
        recyclerView = new BaseRecyclerView(getContext());
        recyclerAdapter = new LiveMatchesRecyclerAdapter(getContext());
        recyclerView.setAdapter(recyclerAdapter);

        // Start Load Data
        loadData();

        return recyclerView;
    }

    @Override
    protected void loadData()
    {
        new RequestAsyncTask<MatchEntity>(getContext(), RequestAsyncTask.RequestType.REQUEST_LIVE_MATCHES, this).execute();
    }
}
