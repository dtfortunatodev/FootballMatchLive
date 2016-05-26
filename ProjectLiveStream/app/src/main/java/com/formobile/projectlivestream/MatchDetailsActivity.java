package com.formobile.projectlivestream;

import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntity;
import com.formobile.projectlivestream.entities.ListStreamEntity;
import com.formobile.projectlivestream.entities.MatchDetailsEntity;
import com.formobile.projectlivestream.entities.PopupGenericEntity;
import com.formobile.projectlivestream.enums.StreamingTypeEnum;
import com.formobile.projectlivestream.interfaces.BaseActivityRefreshInterface;
import com.formobile.projectlivestream.interfaces.PopupGenericInterface;
import com.formobile.projectlivestream.jsoup.JSoupHelper;
import com.formobile.projectlivestream.ads.AdvertiseController;
import com.formobile.projectlivestream.utils.MatchesUtil;
import com.formobile.projectlivestream.utils.PopupController;
import com.formobile.projectlivestream.utils.ProgressController;
import com.formobile.projectlivestream.utils.SoccerLiveStreamingUtil;
import com.formobile.projectlivestream.views.ListItemCompact;
import com.formobile.projectlivestream.views.ListMatchDetailsHeaderView;
import com.formobile.projectlivestream.views.ListStreamItemView;

import java.io.IOException;
import java.util.List;

public class MatchDetailsActivity extends BaseSoccerLiveActivity {
	public static final String EXTRA_GAME_ENTITY = "EXTRA_GAME_ENTITY";
	
	private ListMatchStreamingEntity listGameEntity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Get Extra Game Entity
		listGameEntity = (ListMatchStreamingEntity) getIntent().getSerializableExtra(EXTRA_GAME_ENTITY);
		
		loadMatchDetails();
		
		// Set Refresh Listener
		this.refreshListener = new BaseActivityRefreshInterface() {
			
			@Override
			public void onRefreshActivity() {
				loadMatchDetails();
			}
		};

//        VungleUtils.showVideo();
	}

	public void loadMatchDetails(){
		new AsyncTask<Void, Void, Void>() {
			MatchDetailsEntity matchDetailsEntity;
			
			@Override
			protected void onPreExecute() {
				clearContainer();
				ProgressController.startDialog(MatchDetailsActivity.this);
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {

					matchDetailsEntity = JSoupHelper.getMatchDetailsEntity(getApplicationContext(), listGameEntity, listGameEntity.getUrlMatch());
					
				} catch (Exception e) {
					Log.e(SoccerLiveStreamingUtil.TAG, e.getMessage(), e);
				}
 				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				
				if(matchDetailsEntity != null){

					matchDetailsEntity.setListMatchStreamingEntity(listGameEntity);
					
					// Add Header
					addViewItem(new ListMatchDetailsHeaderView(matchDetailsEntity).createView(MatchDetailsActivity.this, 0));
					
					// Create Items
                    try {
                        if(!AppConfigsHelper.getStartupConfigs(getBaseContext(), false).isHideMatches()){
                            if(matchDetailsEntity.getListStreamEntities() != null && !matchDetailsEntity.getListStreamEntities().isEmpty()){
                                List<ListStreamEntity> listStreams;
                                int count = 0;

                                // Get Sopcast Streams
                                listStreams = MatchesUtil.getListStreamingByType(StreamingTypeEnum.SOPCAST_STREAM, matchDetailsEntity.getListStreamEntities());
                                if(listStreams != null && !listStreams.isEmpty()){
                                    count ++;
                                    ListItemCompact listItemCompact = new ListItemCompact(getResources().getString(R.string.list_streams_header_sopcast));
                                    addViewItem(listItemCompact.createView(MatchDetailsActivity.this, count));
                                    for(ListStreamEntity listStreamEntity : listStreams){
                                        listItemCompact.addItem(MatchDetailsActivity.this, new ListStreamItemView(listStreamEntity));
                                    }
                                }

                                // Get Sopcast Streams
                                listStreams = MatchesUtil.getListStreamingByType(StreamingTypeEnum.FLASH_STREAM, matchDetailsEntity.getListStreamEntities());
                                if(listStreams != null && !listStreams.isEmpty()){
                                    count ++;
                                    ListItemCompact listItemCompact = new ListItemCompact(getResources().getString(R.string.list_streams_header_flash));
                                    addViewItem(listItemCompact.createView(MatchDetailsActivity.this, count));
                                    for(ListStreamEntity listStreamEntity : listStreams){
                                        listItemCompact.addItem(MatchDetailsActivity.this, new ListStreamItemView(listStreamEntity));
                                    }
                                }
                            } else{
                                Toast.makeText(getBaseContext(), "There's no streams available yet...", Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    // Show Ad Interstitial
                    AdvertiseController.displayInterstitial(MatchDetailsActivity.this);


				} else{
					// Show Error Popup
					PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_error_connection_title, R.string.popup_error_connection_description, R.string.popup_error_connection_btn_confirmation, R.string.popup_error_connection_btn_cancel, new PopupGenericInterface() {
						
						@Override
						public void onConfirmationClicked(Dialog dialog) {
							dialog.dismiss();
							refreshPage();
						}
						
						@Override
						public void onCancelClicked(Dialog dialog) {
							dialog.dismiss();
							finish();
						}
					});
					PopupController.showGenericPopup(MatchDetailsActivity.this, popupGenericEntity);
				}
				
				ProgressController.stopDialog();
				
				super.onPostExecute(result);
			}
		}.execute();
	}
	
	@Override
	protected void onDestroy() {
        // Show Ad Interstitial
        AdvertiseController.displayInterstitial(MatchDetailsActivity.this);
		super.onDestroy();
	}

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
