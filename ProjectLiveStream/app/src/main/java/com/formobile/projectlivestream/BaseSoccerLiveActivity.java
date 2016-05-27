package com.formobile.projectlivestream;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.formobile.projectlivestream.configs.AppConfigsHelper;
import com.formobile.projectlivestream.interfaces.BaseActivityRefreshInterface;
import com.formobile.projectlivestream.ads.AdvertiseController;
import com.formobile.projectlivestream.utils.AnalyticsHelper;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class BaseSoccerLiveActivity extends BaseActivity{
	public static final String EXTRA_IMAGE_URL = "EXTRA_IMAGE_URL";
	public static final String EXTRA_TITLE = "EXTRA_TITLE";
	public static final String EXTRA_URL = "EXTRA_URL";
	
	public static final String SHARE_PREFERENCES_NAME = "PREFERENCES_SOCCERLIVESTREAMING";
	
	protected ViewGroup viewGroupContainer;
	protected BaseActivityRefreshInterface refreshListener;
	
	protected String extraImageUrl;
	protected String extraTitle;
	protected String extraUrl;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_layout);

        // Display Other Sports
        try {
            if(AppConfigsHelper.getStartupConfigs(getBaseContext(), false).isHasOtherSports()){
                findViewById(R.id.ivHeaderOtherSportsToggle).setVisibility(View.VISIBLE);
            } else{
                findViewById(R.id.ivHeaderOtherSportsToggle).setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		// Find container
		viewGroupContainer = (ViewGroup) findViewById(R.id.layoutBaseLayoutContainer);
		
		// Set Header Icon
		ImageView imageView = (ImageView) findViewById(R.id.ivHeaderIcon);
		imageView.setImageResource(R.drawable.ic_launcher);
		
		// Set extras
		if(getIntent() != null && getIntent().getExtras() != null){
			//extraImageUrl = getIntent().getExtras().getString(EXTRA_IMAGE_URL);
			extraTitle = getIntent().getExtras().getString(EXTRA_TITLE);
			extraUrl = getIntent().getExtras().getString(EXTRA_URL);
			
			if(extraTitle != null){
				setHeader(extraImageUrl, extraTitle);
			}
		}
		
		// Config Listeners
		configHeaderListeners();
	}

	@Override
	protected void onResume() {
		super.onResume();

		// Show Banner
		AdvertiseController.displayBanner(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
//		EasyTracker.getInstance(this).activityStart(this);
	}



	@Override
	protected void onStop() {
		super.onStop();
		
//		EasyTracker.getInstance(this).activityStop(this);
	}

	private void configHeaderListeners(){
		// Share
		findViewById(R.id.ivHeaderShareBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {

              AnalyticsHelper.sendEvent(getBaseContext(), "Share", "Share on: " + ((Object) this).getClass().getSimpleName(), extraUrl);

			  Intent intent = new Intent(Intent.ACTION_SEND);
              intent.setType("text/plain");
              intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName());
//                intent.putExtra(Intent.EXTRA_TEXT, "http://m.formobsports.store.aptoide.com/");
              intent.putExtra(Intent.EXTRA_SUBJECT, "Check out this Live Streaming app for Android :)");
              startActivity(Intent.createChooser(intent, "Share"));
			}
		});
		
		// Refresh
		findViewById(R.id.ivHeaderRefreshBtn).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				refreshPage();
			}
		});
		
		// Other Sports
		findViewById(R.id.ivHeaderOtherSportsToggle).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(getBaseContext(), OtherSportsActivity.class);
				startActivity(intent);
			}
		});
	}
	
	protected void setHeader(String imgUrl, String title){
		// ImageView
		if(imgUrl != null){
			ImageView imageView = (ImageView) findViewById(R.id.ivHeaderIcon);
			ImageLoader.getInstance().displayImage(imgUrl, imageView, new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View view, Bitmap arg2) {
					FadeInBitmapDisplayer.animate(view, 1000);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		
		
		// Set Title
		TextView textView = (TextView) findViewById(R.id.tvHeaderTitle);
		textView.setText(title);
		
	}
	
	protected void clearContainer(){
		if(viewGroupContainer != null){
			viewGroupContainer.removeAllViews();
		}
	}

	protected void addViewItem(ViewGroup viewGroup){
		if(viewGroupContainer != null && viewGroup != null){
			viewGroupContainer.addView(viewGroup);
		}
	}
	
	protected void refreshPage(){
		if(refreshListener != null){
			refreshListener.onRefreshActivity();
		}
	}

}
