package com.formobile.projectlivestream;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.formobile.projectlivestream.jsoup.JSoupHelper;
import com.formobile.projectlivestream.ads.AdvertiseController;
import com.formobile.projectlivestream.utils.FullscreenableChromeClient;
import com.formobile.projectlivestream.utils.ProgressController;
import com.google.analytics.tracking.android.EasyTracker;

public class WebPlayerActivity extends BaseActivity {

	public static final String WEB_PLAYER_URL_EXTRA = "WEB_PLAYER_URL_EXTRA";
	
	private String webPlayerUrl;
	private WebView webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.webplayer_livetv);
		
		getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		webPlayerUrl = getIntent().getExtras().getString(WEB_PLAYER_URL_EXTRA);
		webView = (WebView) findViewById(R.id.webViewPlayer);
		
		super.onCreate(savedInstanceState);
		
		loadWebPlayerData();

//        VungleUtils.showVideo();

	}
	
	private void loadWebPlayerData(){

        new AsyncTask<Void, Void, Void>() {
            String webPlayerData;

            @Override
            protected void onPreExecute() {
                ProgressController.startDialog(WebPlayerActivity.this);
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    webPlayerData = JSoupHelper.getHtmlPlayerContainer(getBaseContext(), webPlayerUrl);
                } catch (Exception e) {
                    Log.d("DUMMY", e.getMessage(), e);
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                if(webPlayerData != null && !webPlayerData.isEmpty()){
                    loadWebPlayerFromData(webPlayerData);
                } else{
                    loadWebPlayerFromUrl(webPlayerUrl);
                }
                ProgressController.stopDialog();
                super.onPostExecute(result);
            }

        }.execute();



//        try {
//            if(AppConfigsHelper.getStartupConfigs(getApplicationContext(), false).getProviderType() == StartupConfigs.ProviderType.LIVETV){
//                new AsyncTask<Void, Void, Void>() {
//                    String webPlayerData;
//
//                    @Override
//                    protected void onPreExecute() {
//                        ProgressController.startDialog(WebPlayerActivity.this);
//                        super.onPreExecute();
//                    }
//
//                    @Override
//                    protected Void doInBackground(Void... params) {
//                        try {
//                            webPlayerData = JSoupHelper.getHtmlPlayerContainer(getBaseContext(), webPlayerUrl);
//                        } catch (Exception e) {
//                            Log.d("DUMMY", e.getMessage(), e);
//                        }
//                        return null;
//                    }
//
//                    @Override
//                    protected void onPostExecute(Void result) {
//                        if(webPlayerData != null && !webPlayerData.isEmpty()){
//                            loadWebPlayerFromData(webPlayerData);
//                        } else{
//                            loadWebPlayerFromUrl(webPlayerUrl);
//                        }
//                        ProgressController.stopDialog();
//                        super.onPostExecute(result);
//                    }
//
//                }.execute();
//            }
//            else{
//                loadWebPlayerFromUrl(webPlayerUrl);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            loadWebPlayerFromUrl(webPlayerUrl);
//        }

    }
	
	@Override
	protected void onStart() {
		super.onStart();

		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		
		EasyTracker.getInstance(this).activityStop(this);
	}

	@SuppressLint("NewApi")
	public void loadWebPlayerFromData(String data){

        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(PluginState.ON);
        webView.setInitialScale(100);
        //webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
            	
            	if(!url.startsWith("data:")){
            		return true;
            	} else{
            		return false;
            	}
            }
        });
        
        
        
        
        if(Build.VERSION.SDK_INT >=14) {
            webView.setWebChromeClient(new FullscreenableChromeClient(this));
        } else{
            webView.setWebChromeClient(new WebChromeClient());
        }
        
        if(Build.VERSION.SDK_INT >= 11){
        	webView.getSettings().setBuiltInZoomControls(true);
        	webView.getSettings().setDisplayZoomControls(false);
        }
        
        // Set Settings
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        
        webView.loadData(data, "text/html", "utf-8");
    }
	
	@SuppressLint("NewApi")
	public void loadWebPlayerFromUrl(final String loadUrl){

        webView.getSettings().setUserAgentString("Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.81 Safari/537.36");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(PluginState.ON);


        if(Build.VERSION.SDK_INT >=14) {
            webView.setWebChromeClient(new FullscreenableChromeClient(this));
        } else{
            webView.setWebChromeClient(new WebChromeClient());
        }

        webView.setWebViewClient(new WebViewClient(){
            boolean alreadyFinished = false;

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(!url.equalsIgnoreCase(loadUrl)){
                    if(url.contains("http://creative.xtendmedia.com/")){
                        webView.loadUrl(loadUrl);
                    }
                    return true;
                } else{
                    return false;
                }
            }
            
            @Override
			public void onPageFinished(WebView view, String url) {
                alreadyFinished = true;
				ProgressController.stopDialog();
				super.onPageFinished(view, url);
			}
            
        });
        
        if(Build.VERSION.SDK_INT >= 11){
        	webView.getSettings().setBuiltInZoomControls(true);
        	webView.getSettings().setDisplayZoomControls(false);
        }

        // Set Settings
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.loadUrl(loadUrl);

    }
	

	@Override
	protected void onDestroy() {
		webView.loadData("", "text/html", "utf-8");
		webView.clearCache(true);
        webView = null;

        // Show Ad Interstitial
        AdvertiseController.displayInterstitial(WebPlayerActivity.this);
		
		super.onDestroy();
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if(webView != null){
            if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
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
