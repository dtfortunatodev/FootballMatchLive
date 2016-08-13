package com.footballmatch.live.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.footballmatch.live.data.managers.RequestAsyncTask;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.data.model.MatchHighlightEntity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.adapters.MatchHighlightsAdapter;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class MatchDetailsFragment extends BaseMatchDetailsFragment
{
    private BaseRecyclerViewAdapter recyclerViewAdapter;

    /**
     * New Fragment Instance
     *
     * @param matchEntity
     *
     * @return
     */
    public static BaseMatchDetailsFragment newInstance(MatchEntity matchEntity)
    {
        BaseMatchDetailsFragment matchDetailsFragment = new MatchDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_MATCH_ENTITY_OBJ, matchEntity);
        matchDetailsFragment.setArguments(bundle);

        return matchDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Remove Date
        viewTvDate.setText("");

        // Set Score
        viewTvTime.setText(matchEntity.getScore());

        return view;
    }

    @Override
    protected void loadData()
    {
        // Request Match Highlights
        new RequestAsyncTask<MatchHighlightEntity>(getContext(), RequestAsyncTask.RequestType.REQUEST_MATCH_HIGHLIGHTS, this).setRequestUrl(matchEntity.getLinkUrl())
                .execute();
    }

    @Override
    protected BaseRecyclerViewAdapter getRecyclerAdapter()
    {
        if (recyclerViewAdapter == null)
        {
            recyclerViewAdapter = new MatchHighlightsAdapter(getContext());
        }
        return recyclerViewAdapter;
    }
}
