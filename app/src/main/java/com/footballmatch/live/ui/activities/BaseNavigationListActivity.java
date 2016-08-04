package com.footballmatch.live.ui.activities;

import android.os.Bundle;
import com.footballmatch.live.R;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.views.BaseRecyclerView;

/**
 * Created by David Fortunato on 03/08/2016
 * All rights reserved GoodBarber
 */
public abstract class BaseNavigationListActivity extends BaseNavigationActivity
{

    // RecyclerView
    private BaseRecyclerView baseRecyclerView;
    private BaseRecyclerViewAdapter baseRecyclerViewAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set Layout
        setContentView(R.layout.activity_base_list_navigation);

        // Setupe Recycler view
        baseRecyclerView = (BaseRecyclerView) findViewById(R.id.listBaseActivityRecyclerView);
        baseRecyclerViewAdapter = generateRecyclerViewAdapter();
        baseRecyclerView.setAdapter(baseRecyclerViewAdapter);
    }

    public BaseRecyclerViewAdapter getBaseRecyclerViewAdapter()
    {
        return baseRecyclerViewAdapter;
    }

    protected abstract BaseRecyclerViewAdapter generateRecyclerViewAdapter();


}
