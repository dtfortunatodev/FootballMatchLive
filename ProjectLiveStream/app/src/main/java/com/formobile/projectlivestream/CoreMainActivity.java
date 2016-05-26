package com.formobile.projectlivestream;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.Toast;

import com.formobile.projectlivestream.ads.AdvertiseController;
import com.formobile.projectlivestream.ads.LeadBoltUtils;
import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.configs.StartupConfigs;
import com.formobile.projectlivestream.entities.CompetitionMatchesCompact;
import com.formobile.projectlivestream.entities.ListMatchStreamingEntity;
import com.formobile.projectlivestream.entities.PopupGenericEntity;
import com.formobile.projectlivestream.interfaces.BaseActivityRefreshInterface;
import com.formobile.projectlivestream.interfaces.PopupGenericInterface;
import com.formobile.projectlivestream.jsoup.JSoupHelper;
import com.formobile.projectlivestream.utils.AnalyticsHelper;
import com.formobile.projectlivestream.utils.FavoriteCompetitionsManager;
import com.formobile.projectlivestream.utils.MatchesUtil;
import com.formobile.projectlivestream.utils.PopupController;
import com.formobile.projectlivestream.utils.ProgressController;
import com.formobile.projectlivestream.utils.SoccerLiveStreamingUtil;
import com.formobile.projectlivestream.views.ListMatchesItemCompact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CoreMainActivity extends BaseSoccerLiveActivity{

	public static final String SHARE_PREFERENCES_EXITCOUNT = "SHARE_PREFERENCES_EXITCOUNT";
    public static final String SHARED_PREFS_STARTUP_MESSAGE_ID = "SHARED_PREFS_STARTUP_MESSAGE_ID";
	public static final int SHARE_PREFERENCES_EXITCOUNT_DEF_VAL = 0;

	private List<ListMatchesItemCompact> listCompetitionsCompact;
	private boolean isOnlyFavoriteComps;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		loadListCompetitions();
		
		// Set Refresh Listener
		this.refreshListener = new BaseActivityRefreshInterface() {
			
			@Override
			public void onRefreshActivity() {
				loadListCompetitions();
			}
		};
		
		// Favorite Toggle
		isOnlyFavoriteComps = false;
		findViewById(R.id.ivHeaderFavoriteToggle).setVisibility(View.VISIBLE);
		findViewById(R.id.ivHeaderFavoriteToggle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				toggleFavoriteCompetitions();
			}
		});

        // Advertise Init
        AdvertiseController.initAds(this);

        // Display Startup Message
        try {
            displayStartupMessage(AppConfigsHelper.getStartupConfigs(getBaseContext(), false).getStartupMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

	public void loadListCompetitions(){
		new AsyncTask<Void, Void, Void>() {

			List<ListMatchStreamingEntity> listMatchStreamingEntities;
			List<CompetitionMatchesCompact> lisCompetitionMatchesCompacts;
			
			@Override
			protected void onPreExecute() {
				clearContainer();
				ProgressController.startDialog(CoreMainActivity.this);
				
				// Favs
				if(listCompetitionsCompact != null){
					listCompetitionsCompact.clear();
				}
				listCompetitionsCompact = new ArrayList<ListMatchesItemCompact>();
				isOnlyFavoriteComps = false;
				
				super.onPreExecute();
			}

			@Override
			protected Void doInBackground(Void... params) {

				try {
					// Load fav comps
					FavoriteCompetitionsManager.loadFavoriteCompetitions(getBaseContext());
					
					listMatchStreamingEntities = JSoupHelper.getListMatchStreamingEntities(getApplicationContext(), AppConfigsHelper.getStartupConfigs(getBaseContext(), false).getUrlSport());
					
					if(listMatchStreamingEntities != null){
						lisCompetitionMatchesCompacts = MatchesUtil.convertMatchesToCompetitionCompact(listMatchStreamingEntities);
					}
					
				} catch (Exception e) {
					Log.e(SoccerLiveStreamingUtil.TAG, e.getMessage(), e);
					AnalyticsHelper.sendEvent(getBaseContext(), "ERROR", "LIVEMATCHES", e.getMessage());
				}
 				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {

				// Create Items
				if(lisCompetitionMatchesCompacts != null){
					int count = 0;
					for(CompetitionMatchesCompact competitionMatchesCompact : lisCompetitionMatchesCompacts){
						ListMatchesItemCompact listMatchesItemCompact = new ListMatchesItemCompact(competitionMatchesCompact);
						addViewItem(listMatchesItemCompact.createView(CoreMainActivity.this, count));
						listCompetitionsCompact.add(listMatchesItemCompact);
						count ++;
					}
					
					// Show Ad Interstitial
					AdvertiseController.displayInterstitial(CoreMainActivity.this);

                    ProgressController.stopDialog();
				} else if(listMatchStreamingEntities != null && listMatchStreamingEntities.isEmpty()){

                    // Check if should try Fallback
                    if(checkFallbackLoadData()){
						AnalyticsHelper.sendEvent(getBaseContext(), "ERROR", "FALLBACK", "Try fallback");
                        loadListCompetitions();
                    } else{
                        Toast.makeText(getBaseContext(), "No matches found. Try again later...", Toast.LENGTH_LONG).show();
                        ProgressController.stopDialog();
                    }

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
					PopupController.showGenericPopup(CoreMainActivity.this, popupGenericEntity);

                    ProgressController.stopDialog();
				}
				

				
				super.onPostExecute(result);
			}
		}.execute();
	}

    public boolean checkFallbackLoadData(){
        try {
            StartupConfigs startupConfigs = AppConfigsHelper.getStartupConfigs(getApplicationContext(), false);

            if(startupConfigs != null && startupConfigs.getUrlFallbackData() != null && startupConfigs.getUrlFallbackData().length() > 0){
                startupConfigs.setUrlSport(startupConfigs.getUrlFallbackData());
                startupConfigs.setUrlFallbackData(null);

//                switch (startupConfigs.getProviderType()){
//                    case LIVETV:
//                        startupConfigs.setProviderType(StartupConfigs.ProviderType.STREAMHUNTER);
//                        break;
//                    case STREAMHUNTER:
//                        startupConfigs.setProviderType(StartupConfigs.ProviderType.LIVETV);
//                        break;
//                }

                return true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

	public void toggleFavoriteCompetitions(){
		isOnlyFavoriteComps = !isOnlyFavoriteComps;
		ImageView imageView = (ImageView) findViewById(R.id.ivHeaderFavoriteToggle);
		
		
		if(isOnlyFavoriteComps){
			imageView.setImageResource(R.drawable.favorite_white_star_check);
			boolean foundSome = false;
			for(ListMatchesItemCompact listMatchesItemCompact : listCompetitionsCompact){
				if(!listMatchesItemCompact.isFavorite(getBaseContext())){
					listMatchesItemCompact.getViewGroup().setVisibility(View.GONE);
				} else{
					foundSome = true;
				}
			}
			
			if(!foundSome){
				Toast.makeText(getBaseContext(), "You don\'t have any favorite competition", Toast.LENGTH_LONG).show();
			}
		} else{
			imageView.setImageResource(R.drawable.favorite_white_star_uncheck);
			for(ListMatchesItemCompact listMatchesItemCompact : listCompetitionsCompact){
				listMatchesItemCompact.getViewGroup().setVisibility(View.VISIBLE);
			}
		}
		
	}
	

	@Override
	public void onBackPressed() {
		int exitCount = getSharedPreferences(BaseSoccerLiveActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(SHARE_PREFERENCES_EXITCOUNT, SHARE_PREFERENCES_EXITCOUNT_DEF_VAL);
		exitCount++;
		getSharedPreferences(BaseSoccerLiveActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(SHARE_PREFERENCES_EXITCOUNT, exitCount).commit();
		
		if(exitCount == 5 || exitCount == 10 || exitCount == 15 || exitCount == 20 || exitCount == 30){
			PopupGenericEntity popupGenericEntity = new PopupGenericEntity(R.string.popup_rate_title, R.string.popup_rate_description, R.string.popup_rate_btn_confirm, R.string.popup_rate_btn_cancel, new PopupGenericInterface() {
				
				@Override
				public void onConfirmationClicked(Dialog dialog) {
					final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
					try {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
					} catch (android.content.ActivityNotFoundException anfe) {
					    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
					}
					
					getSharedPreferences(BaseSoccerLiveActivity.SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(SHARE_PREFERENCES_EXITCOUNT, 100).commit();

                    AnalyticsHelper.sendEvent(getBaseContext(), "Rate", "Clicked", "");
					
				}
				
				@Override
				public void onCancelClicked(Dialog dialog) {
					dialog.dismiss();
					finish();
				}
			});
			PopupController.showGenericPopup(this, popupGenericEntity);
            AnalyticsHelper.sendEvent(getBaseContext(), "Rate", "Displayed", "");
		}
		else{
			super.onBackPressed();
		}
	}

	@Override
	protected void onDestroy() {
		FavoriteCompetitionsManager.saveFavoriteCompetitions(getBaseContext());
		super.onDestroy();
	}

    @Override
    protected void onResume() {
        super.onResume();

        LeadBoltUtils.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        LeadBoltUtils.onPause(this);
    }


    /**
     * Display a startup message
     * @param startupMessage
     */
    private void displayStartupMessage(StartupConfigs.StartupMessage startupMessage){


        if(startupMessage != null && startupMessage.isEnabled()){
            // Check last startup displayed
            int lastId = getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).getInt(SHARED_PREFS_STARTUP_MESSAGE_ID, -1);

            if(lastId != startupMessage.getId()){

                // Update Shared Prefs
                getSharedPreferences(SHARE_PREFERENCES_NAME, Context.MODE_PRIVATE).edit().putInt(SHARED_PREFS_STARTUP_MESSAGE_ID, startupMessage.getId()).commit();

                // Show Error Popup
                PopupGenericEntity popupGenericEntity = new PopupGenericEntity(startupMessage.getTitle(), startupMessage.getMessage(), "Ok", null, new PopupGenericInterface() {

                    @Override
                    public void onConfirmationClicked(Dialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancelClicked(Dialog dialog) {
                        dialog.dismiss();
                    }
                });
                PopupController.showGenericPopup(CoreMainActivity.this, popupGenericEntity);
            }
        }
    }

    public static void startActivity(Activity activity){
        Intent intent = new Intent(activity, CoreMainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
        activity.finish();
    }
}
