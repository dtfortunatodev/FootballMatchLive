package com.formobile.projectlivestream.views;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.formobile.projectlivestream.BaseSoccerLiveActivity;
import com.formobile.projectlivestream.MatchDetailsActivity;
import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntity;
import com.formobile.projectlivestream.interfaces.ViewCreationInterface;

import java.text.SimpleDateFormat;

public class ListMatchItemView implements ViewCreationInterface {

	private ListMatchStreamingEntity lisMatchStreamingEntity;
	
	public ListMatchItemView(ListMatchStreamingEntity listMatchStreamingEntity){
		this.lisMatchStreamingEntity = listMatchStreamingEntity;
	}
	
	@Override
	public ViewGroup createView(final Activity context, int childPosition) {
		ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.list_match_item_only_title, null);
		
		if(childPosition == 0){
			// Remove top separator
			viewGroup.findViewById(R.id.frameListMatchTopSeparator).setVisibility(View.GONE);
		}
		
		// Set Team Names
		((TextView) viewGroup.findViewById(R.id.tvListMatchItemTitle)).setText(lisMatchStreamingEntity.getTitleMatch());
		
		// Set Date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
		((TextView)viewGroup.findViewById(R.id.tvListMatchItemDate)).setText(simpleDateFormat.format(lisMatchStreamingEntity.getDate())); 
		
		// Listener Click
		viewGroup.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MatchDetailsActivity.class);
				intent.putExtra(BaseSoccerLiveActivity.EXTRA_IMAGE_URL, lisMatchStreamingEntity.getListCompetitionEntity().getCompetitionImageUrl());
				intent.putExtra(BaseSoccerLiveActivity.EXTRA_TITLE, lisMatchStreamingEntity.getListCompetitionEntity().getCompetitionName());
				intent.putExtra(BaseSoccerLiveActivity.EXTRA_URL, lisMatchStreamingEntity.getUrlMatch());
				intent.putExtra(MatchDetailsActivity.EXTRA_GAME_ENTITY, lisMatchStreamingEntity);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		
		// Set result
		if(lisMatchStreamingEntity.isLive()){
			viewGroup.findViewById(R.id.ivListMatchItemIsLive).setVisibility(View.VISIBLE);
		}

		return viewGroup;
	}

}
