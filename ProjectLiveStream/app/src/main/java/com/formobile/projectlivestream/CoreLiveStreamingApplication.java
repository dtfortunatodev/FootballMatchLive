package com.formobile.projectlivestream;

import android.app.Application;
import android.content.Context;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import java.io.File;

public class CoreLiveStreamingApplication extends Application {

	private static final int DISK_CACHE_SIZE = 5; // mb
    private static final int MEMORY_CACHE_SIZE = 5; // mb

    private Tracker mTracker;
    private int iconDefRes = R.drawable.ic_launcher;

    public static CoreLiveStreamingApplication mInstanceApplication;

    @Override
    public void onCreate() {
        super.onCreate();

        // Set Current Instance Application
        mInstanceApplication = this;

        initImageLoader(getApplicationContext());

        // Config Tracker
        Tracker tracker = getDefaultTracker();
        tracker.enableExceptionReporting(true);
        tracker.enableAdvertisingIdCollection(true);
        tracker.enableAutoActivityTracking(true);
    }



	public void initImageLoader(Context context)
    {
        // Init ImageLoader
        File cacheDir = StorageUtils.getCacheDirectory(context);
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .delayBeforeLoading(100)
                .showImageForEmptyUri(iconDefRes)
                .showImageOnFail(iconDefRes)
                .build();
        
        ImageLoaderConfiguration config;
        
        config = new ImageLoaderConfiguration.Builder(context)
        .defaultDisplayImageOptions(displayImageOptions)
        .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE * 1024 * 1024))
        .memoryCacheSize(MEMORY_CACHE_SIZE * 1024 * 1024)
        .discCache(new UnlimitedDiscCache(cacheDir))
        .build();
            
        
        
        ImageLoader.getInstance().init(config);
    }
    
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        
        ImageLoader.getInstance().clearMemoryCache();
    }

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return mTracker;
    }

    public static CoreLiveStreamingApplication getInstanceApplication()
    {
        return mInstanceApplication;
    }
    
}
