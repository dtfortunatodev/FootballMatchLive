package com.formobile.projectlivestream.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.formobile.projectlivestream.R;
import com.formobile.projectlivestream.configs.OtherSportJSONEntity;
import com.formobile.projectlivestream.entities.OtherSportEntity;
import com.formobile.projectlivestream.interfaces.ViewCreationInterface;
import com.formobile.projectlivestream.utils.AnalyticsHelper;

public class ListOtherSportItemView implements ViewCreationInterface {
	
	private OtherSportJSONEntity otherSportEntity;
	private ViewGroup viewGroup;
	
	public ListOtherSportItemView(OtherSportJSONEntity otherSportEntity)
	{
		this.otherSportEntity = otherSportEntity;;
	}
	@Override
	public ViewGroup createView(final Activity context, int childPosition) {
		ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.other_sport_list_item, null);
		
		// Set Icon
		((ImageView) viewGroup.findViewById(R.id.ivOtherSportItemIcon)).setImageDrawable(getDrawableByName(context, otherSportEntity.getResDrawableId()));
		
		// Set Name
		((TextView) viewGroup.findViewById(R.id.tvOtherSportItemName)).setText(otherSportEntity.getName());
		
		// Button
		if(appInstalledOrNot(context, otherSportEntity.getNamepackage())){
			((TextView) viewGroup.findViewById(R.id.btnOtherSportOpen)).setText(R.string.other_sport_btn_open);
		} else{
			((TextView) viewGroup.findViewById(R.id.btnOtherSportOpen)).setText(R.string.other_sport_btn_install);
		}
		
		// Button Listener
		viewGroup.findViewById(R.id.btnOtherSportOpen).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(appInstalledOrNot(context, otherSportEntity.getNamepackage())){
					// Open App
                    AnalyticsHelper.sendEvent(context, "OtherSports", "Open App", "package: " + otherSportEntity.getNamepackage());
					openApp(context, otherSportEntity.getNamepackage());
				} else{
					// Open Market
                    AnalyticsHelper.sendEvent(context, "OtherSports", "Install App", "package: " + otherSportEntity.getNamepackage());
					openMarket(context, otherSportEntity.getUrlInstall());
					
				}
			}
		});
		
		return viewGroup;
	}


    private Drawable getDrawableByName(Context context, String name){
        Resources resources = context.getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                context.getPackageName());
        return resources.getDrawable(resourceId);
    }

	
	private void openMarket(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

//		try {
////			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + otherSportEntity.getContextName()));
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent);
//		} catch (android.content.ActivityNotFoundException anfe) {
////			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + otherSportEntity.getContextName()));
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://m.formobsports.store.aptoide.com/"));
//		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//			context.startActivity(intent);
//		}
	}
	
	private void openApp(Context context, String packageName){
		try{
			PackageManager pm = context.getPackageManager();
			Intent appStartIntent = pm.getLaunchIntentForPackage(packageName);
			context.startActivity(appStartIntent);
		} catch(Exception e){
			openMarket(context, otherSportEntity.getUrlInstall());
		}
	}
	
	private boolean appInstalledOrNot(Context context, String uri) {
        PackageManager pm = context.getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed ;
    }
	
}
