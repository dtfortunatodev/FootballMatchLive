package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.model.MatchHighlightEntity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class ListMatchHighlightIndicator extends BaseRecyclerViewIndicator<View, MatchHighlightEntity, ListMatchHighlightIndicator.RecyclerViewHolder>
{
    public ListMatchHighlightIndicator(MatchHighlightEntity matchHighlightEntity)
    {
        super(matchHighlightEntity);


    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_stream_item_indicator, null);
    }

    @Override
    public void refreshCell(final RecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {

        // Hide Icon
        recyclerViewHolder.ivIcon.setVisibility(View.INVISIBLE);
        recyclerViewHolder.tvStreamName.setText("Highlight " + position);

        // On Click
        recyclerViewHolder.getItemView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utils.startURL(recyclerViewHolder.getContext(), getObjectData().getGeneratedUrl());
            }
        });

    }

    @Override
    public RecyclerViewHolder getRecycleViewHolder(View view)
    {
        return new RecyclerViewHolder(view);
    }

    protected class RecyclerViewHolder extends BaseRecyclerViewHolder<View>
    {
        public ImageView ivIcon;
        public TextView  tvStreamName;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivListStreamItemIcon);
            tvStreamName = (TextView) itemView.findViewById(R.id.tvListStreamItemName);
        }
    }
}
