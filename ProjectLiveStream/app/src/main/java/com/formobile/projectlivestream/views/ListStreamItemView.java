package com.formobile.projectlivestream.views;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.VideoPlayerActivity;
import com.formobile.projectlivestream.WebPlayerActivity;
import com.formobile.projectlivestream.entities.ListStreamEntity;
import com.formobile.projectlivestream.entities.PopupGenericEntity;
import com.formobile.projectlivestream.enums.StreamingTypeEnum;
import com.formobile.projectlivestream.interfaces.PopupGenericInterface;
import com.formobile.projectlivestream.interfaces.ViewCreationInterface;
import com.formobile.projectlivestream.jsoup.JSoupHelper;
import com.formobile.projectlivestream.utils.AnalyticsHelper;
import com.formobile.projectlivestream.utils.PlayerCheck;
import com.formobile.projectlivestream.utils.PopupController;
import com.formobile.projectlivestream.utils.ProgressController;

import java.io.IOException;

public class ListStreamItemView implements ViewCreationInterface {
	private static final String TAG = ListStreamItemView.class.getSimpleName();

	private ListStreamEntity listStreamEntity;
	private ViewGroup viewGroup;
	private String videoPlayerUrl;
	
	public ListStreamItemView(ListStreamEntity listStreamEntity)
	{
		this.listStreamEntity = listStreamEntity;
	}
	@Override
	public ViewGroup createView(final Activity context, int childPosition) {
		ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.match_details_stream_item, null);
	
		// Set Title
		String title = null;
		if(listStreamEntity.getStreamingType() == StreamingTypeEnum.FLASH_STREAM){
			title = "Flash " + (childPosition + 1);
		} else{
			title = "Sopcast " + (childPosition + 1);
		}
        if(listStreamEntity.getExtraDescription() != null){
            title += " (" + listStreamEntity.getExtraDescription() + ")";
        }
		((TextView) viewGroup.findViewById(R.id.tvMatchDetailsStreamItemTitle)).setText(title);
		
		// Set Icon
		switch(listStreamEntity.getStreamingType()){
			case SOPCAST_STREAM:
				((ImageView)viewGroup.findViewById(R.id.ivMatchDetailsStreamItemIcon)).setImageResource(R.drawable.sopcast_icon); 
				break;
			case FLASH_STREAM:
				((ImageView)viewGroup.findViewById(R.id.ivMatchDetailsStreamItemIcon)).setImageResource(R.drawable.flash_player_icon);
				break;
		}
		
		// Set Listener
		viewGroup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (listStreamEntity.getStreamingType()) {
					case FLASH_STREAM:
						openFlashStreamLink(context, listStreamEntity.getLinkUrl());
						break;
					case SOPCAST_STREAM:
						openSopcastStream(context, listStreamEntity.getLinkUrl(), StreamingTypeEnum.FLASH_STREAM);
						break;
				}
			}
		});
		
		return viewGroup;
	}


	public void openFlashStreamLink(final Activity context, final String url){
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected void onPreExecute() {
				ProgressController.startDialog(context);

				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... voids) {

				try {
					// Try to get VideoPlayer Id
					if(videoPlayerUrl == null && listStreamEntity.isTryVideoPlayer()){
                            videoPlayerUrl = JSoupHelper.getStreamHunterVideoPlayerLink(context, url);
                    }
				} catch (IOException e) {
					Log.e(TAG, e.getMessage(), e);
				}

				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {

				if(videoPlayerUrl != null){
					// Open on MXPlayer
//					Toast.makeText(context, "VideoPlayer: " + videoPlayerUrl, Toast.LENGTH_LONG).show();
					openMXPlayerVideo(context, videoPlayerUrl);
				} else{
					// Open on Flash Browser
					openFlashStream(context, url, StreamingTypeEnum.FLASH_STREAM);
				}

				ProgressController.stopDialog();

				super.onPostExecute(aVoid);
			}
		}.execute();
	}


	private void openMXPlayerVideo(final Activity activity, String url){

		if(PlayerCheck.checkMXPlayerInstalled(activity)){
			try {
				// Open Video on MXPlayer
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				intent.setPackage(PlayerCheck.MXPLAYER_APP_PACKAGE);
				activity.startActivity(intent);

				AnalyticsHelper.sendEvent(activity, "OPEN_VIDEO", "Video opened on MX Player", "");
			} catch (Exception e) {
				e.printStackTrace();
//				installMXPlayer(activity);
				openOnVideoPlayer(activity, url);
			}
		} else{

			openOnVideoPlayer(activity, url);
//			installMXPlayer(activity);
		}

	}

	private void openOnVideoPlayer(Activity activity, String url){
		AnalyticsHelper.sendEvent(activity, "OPEN_VIDEO", "Video opened on VideoPlayer", "");
		VideoPlayerActivity.startVideoPlayer(activity, url);
	}

	
	private void openFlashStream(final Activity context, String url, StreamingTypeEnum streamType){

		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
			openFlashFoxStream(context, url);
		} else if(PlayerCheck.checkFlashInstalled(context)){
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				Intent intent = new Intent(context, WebPlayerActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(WebPlayerActivity.WEB_PLAYER_URL_EXTRA, url);
				context.startActivity(intent);
			}
		} else{
			PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_flash_n_found_title, R.string.popup_flash_n_found_description, R.string.popup_flash_n_found_btn_confirm, R.string.popup_flash_n_found_btn_cancel, new PopupGenericInterface() {
				
				@Override
				public void onConfirmationClicked(Dialog dialog) {
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(PlayerCheck.getFlashUrl(context)));
		    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					
					dialog.dismiss();
				}
				
				@Override
				public void onCancelClicked(Dialog dialog) {
					dialog.dismiss();
				}
			});
			PopupController.showGenericPopup(context, popupGenericEntity);
		}
	}

	private void openFlashFoxStream(final Activity activity, String url){
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			intent.setPackage(PlayerCheck.FLASH_KITKAT_BROWSER_PACKAGE);
			activity.startActivity(intent);
			AnalyticsHelper.sendEvent(activity, "OPEN_VIDEO", "Video opened on FLASHFOX", "");
		} catch (Exception e) {
			PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_flash_n_found_title, R.string.popup_flash_n_found_description, R.string.popup_flash_n_found_btn_confirm, R.string.popup_flash_n_found_btn_cancel, new PopupGenericInterface() {

				@Override
				public void onConfirmationClicked(Dialog dialog) {
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(PlayerCheck.FLASH_BROWSER_URL_FOR_KITKAT));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					activity.startActivity(intent);
					AnalyticsHelper.sendEvent(activity, "OPEN_VIDEO", "Install FLASHFOX", "");
					dialog.dismiss();
				}

				@Override
				public void onCancelClicked(Dialog dialog) {
					dialog.dismiss();
				}
			});
			PopupController.showGenericPopup(activity, popupGenericEntity);

		}
	}
	
	private void openSopcastStream(final Activity context, String url, StreamingTypeEnum streamType){
		
		if(PlayerCheck.checkSopcastInstalled(context)){
			Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else{
			PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_sopcast_n_found_title, R.string.popup_sopcast_n_found_description, R.string.popup_sopcast_n_found_btn_confirm, R.string.popup_sopcast_n_found_btn_cancel, new PopupGenericInterface() {
				
				@Override
				public void onConfirmationClicked(Dialog dialog) {
					Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(PlayerCheck.SOPCAST_URL));
		    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.startActivity(intent);
					dialog.dismiss();
				}
				
				@Override
				public void onCancelClicked(Dialog dialog) {
					dialog.dismiss();
				}
			});
			PopupController.showGenericPopup(context, popupGenericEntity);
		}
	}
	
}
