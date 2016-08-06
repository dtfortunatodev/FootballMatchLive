package com.footballmatch.live.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.requests.ResponseDataObject;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public abstract class BaseFragment extends Fragment implements RequestAsyncTask.OnRequestListener, SwipeRefreshLayout.OnRefreshListener
{

    private SwipeRefreshLayout swipeRefreshLayout;
    private ViewGroup fragmentLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        if (fragmentLayout == null)
        {
            fragmentLayout = (ViewGroup) inflater.inflate(R.layout.fragment_base_layout, null);

            // Find view
            swipeRefreshLayout = (SwipeRefreshLayout) fragmentLayout.findViewById(R.id.swipeRefreshLayout);
            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            ViewGroup layoutContent= (ViewGroup) fragmentLayout.findViewById(R.id.frameBaseFragmentContainer);

            // Add Fragment Content
            layoutContent.addView(generateFragmentView(inflater));

        }

        return fragmentLayout;
    }

    @Override
    public void onRequestStart()
    {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void onRequestResponse(ResponseDataObject responseDataObject)
    {
        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * On Refresh triggered from SwipeRefreshLayout
     */
    public void onRefresh()
    {
        loadData();
    }

    public abstract View generateFragmentView(LayoutInflater inflater);


    /**
     * Load data of the fragment
     */
    protected abstract void loadData();

}
