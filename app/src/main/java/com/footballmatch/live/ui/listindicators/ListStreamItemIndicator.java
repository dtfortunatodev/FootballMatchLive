package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.PlayStreamManager;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class ListStreamItemIndicator extends BaseRecyclerViewIndicator<View, StreamLinkEntity, ListStreamItemIndicator.ListStreamIeamViewHolder>
{

    private String streamName = null;
    public ListStreamItemIndicator(StreamLinkEntity streamLinkEntity)
    {
        super(streamLinkEntity);


    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_stream_item_indicator, null);
    }

    @Override
    public void refreshCell(final ListStreamIeamViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {

        // Setup Text
        if (streamName == null)
        {
            streamName = "";
            switch (getObjectData().getStreamLinkType())
            {
                case WEBPLAYER:
                    streamName = "Webplayer " + (position + 1);
                    break;
                case ACESTREAM:
                    streamName = "Acestream " + (position + 1);
                    break;
                case ARENAVISION:
                    streamName = "AV " + (position + 1);
                    break;
                case SOPCAST:
                    streamName = "SopCast " + (position + 1);
                    break;
            }

            // Set Recommended
            streamName += " " + (getObjectData().isRecommended() ? recyclerViewHolder.getContext().getString(R.string.list_stream_indicator_recommended) : "");
        }
        recyclerViewHolder.tvStreamName.setText(streamName);

        // Setup Icon
        final String streamName = "";
        switch (getObjectData().getStreamLinkType())
        {
            case WEBPLAYER:
                recyclerViewHolder.ivIcon.setImageResource(R.drawable.ic_flash_player_icon);
                break;
            case ACESTREAM:
                recyclerViewHolder.ivIcon.setImageResource(R.drawable.ic_acestream_icon);
                break;
            case ARENAVISION:
                recyclerViewHolder.ivIcon.setImageResource(R.drawable.ic_arenavision);
                break;
            case SOPCAST:
                recyclerViewHolder.ivIcon.setImageResource(R.drawable.ic_sopcast_icon);
                break;
        }

        // Set On Click
        recyclerViewHolder.getItemView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Start Stream
                PlayStreamManager.playStreamManager(recyclerViewHolder.getContext(), getObjectData());
            }
        });

    }

    @Override
    public ListStreamIeamViewHolder getRecycleViewHolder(View view)
    {
        return new ListStreamIeamViewHolder(view);
    }

    protected class ListStreamIeamViewHolder extends BaseRecyclerViewHolder<View>
    {
        public ImageView ivIcon;
        public TextView tvStreamName;

        public ListStreamIeamViewHolder(View itemView)
        {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivListStreamItemIcon);
            tvStreamName = (TextView) itemView.findViewById(R.id.tvListStreamItemName);
        }
    }

}
