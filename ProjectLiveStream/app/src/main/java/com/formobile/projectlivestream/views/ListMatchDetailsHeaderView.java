package com.formobile.projectlivestream.views;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.entities.MatchDetailsEntity;
import com.formobile.projectlivestream.interfaces.ViewCreationInterface;
import com.formobile.projectlivestream.utils.ImagesUtil;

import java.io.IOException;
import java.text.SimpleDateFormat;

public class ListMatchDetailsHeaderView extends BaseItemView implements
        ViewCreationInterface {
	
	private MatchDetailsEntity matchDetailsEntity;
	
	public ListMatchDetailsHeaderView(MatchDetailsEntity matchDetailsEntity){
		this.matchDetailsEntity = matchDetailsEntity;
	}

	@Override
	public ViewGroup createView(Activity context, int childPosition) {

        try {
            if(AppConfigsHelper.getStartupConfigs(context, false).isHideMatches() || matchDetailsEntity.getTeamBName() == null || matchDetailsEntity.getTeamBName().isEmpty()){
                return generateViewOnlyTitle(context);
            } else{
                return generateView(generateViewWithDetails(context), childPosition);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return generateViewOnlyTitle(context);
        }
    }
	
	private ViewGroup generateViewWithDetails(Activity activity){
		ViewGroup viewGroup  = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.match_details_header, null);
		
		// Set Team 1 Name
		((TextView) viewGroup.findViewById(R.id.tvMatchDetailsTeam1Name)).setText(matchDetailsEntity.getTeamAName());
		
		// Set Team 2 Name
		((TextView) viewGroup.findViewById(R.id.tvMatchDetailsTeam2Name)).setText(matchDetailsEntity.getTeamBName());
		
		// Set Team 1 Logo
		ImagesUtil.displayTeamImage(activity, (ImageView) viewGroup.findViewById(R.id.ivMatchDetailsTeam1Logo), matchDetailsEntity.getTeamAImageUrl());
		ImagesUtil.displayTeamImage(activity, (ImageView) viewGroup.findViewById(R.id.ivMatchDetailsTeam2Logo), matchDetailsEntity.getTeamBImageUrl());
		
		// Set Result
		if(!matchDetailsEntity.getListMatchStreamingEntity().isLive()){
			SimpleDateFormat resultDate = new SimpleDateFormat("HH:mm");
			((TextView) viewGroup.findViewById(R.id.tvMatchDetailsheaderResult)).setText(resultDate.format(matchDetailsEntity.getListMatchStreamingEntity().getDate()));
		} else{
			((TextView) viewGroup.findViewById(R.id.tvMatchDetailsheaderResult)).setText("Live");
			((TextView) viewGroup.findViewById(R.id.tvMatchDetailsheaderResult)).setTextColor(Color.GREEN);
		}
		
		
		// Set header
		if(matchDetailsEntity.getListMatchStreamingEntity().getDate() != null){
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
			((TextView) viewGroup.findViewById(R.id.tvMatchDetailsDateHeader)).setText(simpleDateFormat.format(matchDetailsEntity.getListMatchStreamingEntity().getDate()));
		} else{
			viewGroup.findViewById(R.id.layoutMatchDetailsDateContainer).setVisibility(View.GONE);
		}
		return viewGroup;
	}

	private ViewGroup generateViewOnlyTitle(Activity activity){
		ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(activity).inflate(R.layout.match_details_header_only_title, null);
		
		// Set Team Names
		((TextView) viewGroup.findViewById(R.id.tvListMatchItemTitle)).setText(matchDetailsEntity.getListMatchStreamingEntity().getTitleMatch());
		
		// Set Date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy HH:mm");
		((TextView)viewGroup.findViewById(R.id.tvListMatchItemDate)).setText(simpleDateFormat.format(matchDetailsEntity.getListMatchStreamingEntity().getDate()));
		((TextView)viewGroup.findViewById(R.id.tvMatchDetailsDateHeader)).setText(simpleDateFormat.format(matchDetailsEntity.getListMatchStreamingEntity().getDate()));
		
		
		// Set result
		if(matchDetailsEntity.getListMatchStreamingEntity().isLive()){
			viewGroup.findViewById(R.id.ivListMatchItemIsLive).setVisibility(View.VISIBLE);
		}
		
		return viewGroup;
	}
	
}
