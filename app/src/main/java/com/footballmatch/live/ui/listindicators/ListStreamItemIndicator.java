package com.footballmatch.live.ui.listindicators;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.managers.PlayStreamManager;
import com.footballmatch.live.data.model.StreamLinkEntity;
import com.footballmatch.live.data.requests.ImageLoaderHelper;
import com.footballmatch.live.managers.ads.AdsManager;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;
import com.footballmatch.live.ui.views.BasePopupDialog;

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
            if (getObjectData().getTitle() != null && !getObjectData().getTitle().isEmpty())
            {
                streamName = getObjectData().getTitle();
            }
            else
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
        }

        recyclerViewHolder.tvStreamName.setText(streamName);

        // Setup Icon
        if (getObjectData().getLangIcon() != null)
        {
            ImageLoaderHelper.loadImage(getObjectData().getLangIcon(), recyclerViewHolder.ivArrowImageView, R.drawable.ic_play_icon);
        }

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
            public void onClick(final View v)
            {
                startStream(recyclerViewHolder.getContext());
            }
        });

    }

    private void startStream(final Context context)
    {
        AdsManager.getInstance(context).showNonSkippableVideo((Activity) context, new AdsManager.OnInterstitialClosed()
        {
            @Override
            public void onInterstitialClosed(boolean canContinue)
            {
                if (canContinue)
                {
                    // Start Stream
                    PlayStreamManager.playStreamManager(context, getObjectData());
                }
                else
                {
                    BasePopupDialog basePopupDialog = new BasePopupDialog(context);
                    basePopupDialog.setupPoup("You should watch the video until the end to start watching the football match  stream.", "Ok", "Cancel", new BasePopupDialog.OnPopupListener()
                    {
                        @Override
                        public void onLeftBtnClick(BasePopupDialog basePopupDialog)
                        {
                            startStream(context);
                            basePopupDialog.dismiss();
                        }

                        @Override
                        public void onRightBtnClick(BasePopupDialog basePopupDialog)
                        {
                            basePopupDialog.dismiss();
                        }

                        @Override
                        public void onCancelled()
                        {

                        }
                    });
                    basePopupDialog.show();
                }

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
        public ImageView ivArrowImageView;
        public ImageView ivIcon;
        public TextView tvStreamName;

        public ListStreamIeamViewHolder(View itemView)
        {
            super(itemView);
            ivIcon = (ImageView) itemView.findViewById(R.id.ivListStreamItemIcon);
            tvStreamName = (TextView) itemView.findViewById(R.id.tvListStreamItemName);
            ivArrowImageView = itemView.findViewById(R.id.ivListStreamItemPlayIcon);
        }
    }

}
