package com.footballmatch.live.ui.listindicators;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.footballmatch.live.R;
import com.footballmatch.live.data.model.settings.AdInternalEntity;
import com.footballmatch.live.data.requests.ImageLoaderHelper;
import com.footballmatch.live.ui.adapters.BaseRecyclerViewAdapter;
import com.footballmatch.live.ui.viewholders.BaseRecyclerViewHolder;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class ListAdInternalIndicator extends BaseRecyclerViewIndicator<View, AdInternalEntity, ListAdInternalIndicator.RecyclerViewHolder>
{


    public ListAdInternalIndicator(AdInternalEntity adInternalEntity)
    {
        super(adInternalEntity);
    }

    @Override
    public View getViewCell(Context context, ViewGroup parent)
    {
        return LayoutInflater.from(context).inflate(R.layout.list_ad_internal_indicator, null);
    }

    @Override
    public void refreshCell(final RecyclerViewHolder recyclerViewHolder, BaseRecyclerViewAdapter adapter, int position, int columnPosition)
    {
        // Setup view
        recyclerViewHolder.tvDescription.setText(getObjectData().getTextDescription());
        recyclerViewHolder.tvDescription.setTextColor(getObjectData().getTextColorParsed());

        if (getObjectData().getBackgroundImageUrl() != null && !getObjectData().getBackgroundImageUrl().isEmpty())
        {
            ImageLoaderHelper.loadImage(getObjectData().getBackgroundImageUrl(), recyclerViewHolder.ivBackgroundImage, R.drawable.invisible_drawable);
        }

        // Setup On Click
        recyclerViewHolder.getItemView().setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Utils.startURL(recyclerViewHolder.getContext(), getObjectData().getUrlToOpen());
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
        public TextView tvDescription;
        public ImageView ivBackgroundImage;

        public RecyclerViewHolder(View itemView)
        {
            super(itemView);
            tvDescription = (TextView) itemView.findViewById(R.id.tvListAdInternalDescription);
            ivBackgroundImage = (ImageView) itemView.findViewById(R.id.ivListAdInternalBackgroundImage);
        }
    }

}
