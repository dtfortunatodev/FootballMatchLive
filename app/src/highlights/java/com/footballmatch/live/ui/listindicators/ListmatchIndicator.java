package com.footballmatch.live.ui.listindicators;

import com.footballmatch.live.data.model.MatchEntity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class ListMatchIndicator extends BaseListMatchIndicator
{
    public ListMatchIndicator(MatchEntity matchEntity)
    {
        super(matchEntity);
    }

    @Override
    public void refreshCell(RecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {
        super.refreshCell(recyclerViewHolder, adapter, position, columnPosition);

        // Set Score
        recyclerViewHolder.tvTimeText.setText(getObjectData().getScore());

    }
}
