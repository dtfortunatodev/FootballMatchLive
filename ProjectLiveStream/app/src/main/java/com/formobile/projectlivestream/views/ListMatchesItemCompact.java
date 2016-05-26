package com.formobile.projectlivestream.views;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.entities.CompetitionMatchesCompact;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntity;
import com.formobile.projectlivestream.interfaces.ViewCreationInterface;
import com.formobile.projectlivestream.utils.FavoriteCompetitionsManager;
import com.formobile.projectlivestream.utils.ImagesUtil;

public class ListMatchesItemCompact extends BaseItemView implements
        ViewCreationInterface {
	private CompetitionMatchesCompact competitionMatchesCompact;
	private ViewGroup viewGroup;
	private ViewGroup viewGroupItemsContainer;
	private int childPosition;
	
	public ListMatchesItemCompact(CompetitionMatchesCompact competitionMatchesCompact){
		this.competitionMatchesCompact = competitionMatchesCompact;
		this.childPosition = 0;
		
		
	}
	
	@Override
	public ViewGroup createView(final Activity context, int childPosition) {
		if(viewGroup == null){
			viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.base_list_matches_item_compact, null);
			
			// Set Title
			((TextView) viewGroup.findViewById(R.id.tvBaseListCompactTitle)).setText(competitionMatchesCompact.getListCompetitionEntity().getCompetitionName());
			
			// Set Comp logo
			ImagesUtil.displayLeagueImage(context, (ImageView) viewGroup.findViewById(R.id.ivBaseListCompactTitleIcon), competitionMatchesCompact.getListCompetitionEntity().getCompetitionImageUrl());
			
			// Get Items Container
			viewGroupItemsContainer = (ViewGroup) viewGroup.findViewById(R.id.layoutBaseListCompactContainer);
			
			// Add Matches
			if(competitionMatchesCompact.getListMatchStreamingEntities() != null){
				for(ListMatchStreamingEntity listMatchStreamingEntity : competitionMatchesCompact.getListMatchStreamingEntities()){
					addItem(context, new ListMatchItemView(listMatchStreamingEntity));
				}
			}
			
			// Favorite Icon
			final ImageView imageView = (ImageView) viewGroup.findViewById(R.id.ivBaseListCompactFavoriteIcon);
			if(isFavorite(context)){
				imageView.setImageResource(R.drawable.favorite_blue_star_check);
			} else{
				imageView.setImageResource(R.drawable.favorite_blue_star_uncheck);
			}
			imageView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					FavoriteCompetitionsManager.manageFavoriteComp(competitionMatchesCompact.getListCompetitionEntity().getCompetitionName(), !isFavorite(context));
					if(isFavorite(context)){
						imageView.setImageResource(R.drawable.favorite_blue_star_check);
					} else{
						imageView.setImageResource(R.drawable.favorite_blue_star_uncheck);
					}
				}
			});
			
			
		}
		
		return generateView(viewGroup, childPosition);
	}

	public void addItem(Activity context, ViewCreationInterface viewCreationInterface){
		if(viewCreationInterface != null){
			viewGroupItemsContainer.addView(viewCreationInterface.createView(context, childPosition));
			childPosition ++;
		}
	}
	
	public boolean isFavorite(Context context){
		return FavoriteCompetitionsManager.isCompFavorite(context, competitionMatchesCompact.getListCompetitionEntity().getCompetitionName());
	}

	public ViewGroup getViewGroup() {
		return viewGroup;
	}
	
	
	
}
