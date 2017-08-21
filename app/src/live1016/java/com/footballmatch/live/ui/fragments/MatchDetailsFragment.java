package com.footballmatch.live.ui.fragments;

import android.os.Bundle;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.adapters.MatchStreamLinksAdapter;

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

    @Override
    protected BaseRecyclerViewAdapter getRecyclerAdapter()
    {
        if (recyclerViewAdapter == null)
        {
            recyclerViewAdapter = new MatchStreamLinksAdapter(getActivity());
        }

        return recyclerViewAdapter;
    }
}
