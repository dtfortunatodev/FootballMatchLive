package com.footballmatch.live.ui.adapters;

import android.content.Context;
import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.ui.listindicators.BaseRecyclerViewIndicator;
import com.footballmatch.live.ui.listindicators.ListMatchIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Fortunato on 04/08/2016
 * All rights reserved GoodBarber
 */
public class LiveMatchesRecyclerAdapter extends BaseRecyclerViewAdapter<MatchEntity>
{
    public LiveMatchesRecyclerAdapter(Context activity)
    {
        super(activity);
    }

    @Override
    public void addListData(List<MatchEntity> listData, boolean cleanListBefore)
    {
        // FIXME Only to test
        List<BaseRecyclerViewIndicator> listTestIndicators = new ArrayList<>();
        for (MatchEntity matchEntity : listData)
        {
            listTestIndicators.add(new ListMatchIndicator(matchEntity));
        }
        addListIndicators(listTestIndicators, cleanListBefore);
    }

    @Override
    public int getColumnsCount()
    {
        return 1;
    }

    @Override
    public RecyclerLayoutManagerType getLayoutManagerType()
    {
        return RecyclerLayoutManagerType.LINEAR_LAYOUT;
    }
}
