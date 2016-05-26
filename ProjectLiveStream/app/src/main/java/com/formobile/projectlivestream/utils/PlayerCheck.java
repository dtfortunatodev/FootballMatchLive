package com.formobile.projectlivestream.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.util.Log;

public class PlayerCheck {
	private static final String TAG = PlayerCheck.class.getSimpleName();

	public static final String FLASH_PLAYER_URL_4 = "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.115.81/install_flash_player_ics.apk";
	public static final String FLASH_PLAYER_URL_LESS_4 = "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.111.73/install_flash_player_pre_ics.apk";
	public static final String MXPLAYER_APP_PACKAGE = "com.mxtech.videoplayer.ad";
	public static final String MXPLAYER_APP_MARKET_INSTALL = "https://play.google.com/store/apps/details?id=" + MXPLAYER_APP_PACKAGE;
	public static final String FLASH_KITKAT_BROWSER_PACKAGE = "mobi.browser.flashfox";
	public static final String FLASH_BROWSER_URL_FOR_KITKAT = "https://play.google.com/store/apps/details?id=" + FLASH_KITKAT_BROWSER_PACKAGE;
	public static final String SOPCAST_URL = "http://download.easetuner.com/download/SopCast-0.9.9.apk";
	

	public static boolean checkMXPlayerInstalled(Context context){
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo ai = pm.getApplicationInfo("com.adobe.flashplayer", 0);
			if (ai != null){
                return true;
            }
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}

		return false;
	}

	public static boolean checkFlashInstalled(Context context){
		boolean flashInstalled = false;
		try {
			PackageManager pm = context.getPackageManager();
			if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
				ApplicationInfo ai = pm.getApplicationInfo("com.adobe.flashplayer", 0);
				if (ai != null)
					flashInstalled = true;
			} else{
				// Find FlashFox
				ApplicationInfo ai = pm.getApplicationInfo(FLASH_KITKAT_BROWSER_PACKAGE, 0);
				if (ai != null)
					flashInstalled = true;
			}

		} catch (NameNotFoundException e) {
		  flashInstalled = false;
		}
		return flashInstalled;
	}
	
	public static String getFlashUrl(Context context){
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		if(currentapiVersion < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH){
			return FLASH_PLAYER_URL_LESS_4;
		} else if(currentapiVersion >= android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH && currentapiVersion < Build.VERSION_CODES.KITKAT){
			return FLASH_PLAYER_URL_4;
		} else{
			// Install FlashFox
			return FLASH_BROWSER_URL_FOR_KITKAT;
		}
	}
	
	//org.sopcast.android
	public static boolean checkSopcastInstalled(Context context){
		boolean flashInstalled = false;
		try {
		  PackageManager pm = context.getPackageManager();
		  ApplicationInfo ai = pm.getApplicationInfo("org.sopcast.android", 0);
		  if (ai != null)
		    flashInstalled = true;
		} catch (NameNotFoundException e) {
		  flashInstalled = false;
		}
		return flashInstalled;
	}
	
}
