package com.footballmatch.live.ui.adapters;

import android.content.Context;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.ui.listindicators.BaseRecyclerViewIndicator;
import com.footballmatch.live.ui.listindicators.ListStreamItemIndicator;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class MatchStreamLinksAdapter extends BaseRecyclerViewAdapter<StreamLinkEntity>
{
    public MatchStreamLinksAdapter(Context activity)
    {
        super(activity);
    }

    @Override
    public void addListData(List<StreamLinkEntity> listData, boolean cleanListBefore)
    {
        List<BaseRecyclerViewIndicator> listIndicators = new ArrayList<>();

        for (StreamLinkEntity streamLinkEntity : listData)
        {
            listIndicators.add(new ListStreamItemIndicator(streamLinkEntity));
        }
        addListIndicators(listIndicators, cleanListBefore);

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
