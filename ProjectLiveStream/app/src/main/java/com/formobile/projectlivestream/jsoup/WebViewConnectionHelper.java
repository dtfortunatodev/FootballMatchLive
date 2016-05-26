package com.formobile.projectlivestream.jsoup;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by PTECH on 16-06-2015.
 */
public class WebViewConnectionHelper {

    private static WebView webViewInstance;

    private static WebView getWebViewInstance(Context context){
        if(webViewInstance == null){
            webViewInstance = new WebView(context);
        }

        return webViewInstance;
    }

    public static void loadWebViewHTML(final Context context, String url, CallbackInterface callbackInterface){
        getWebViewInstance(context).getSettings().setJavaScriptEnabled(true);
        getWebViewInstance(context).addJavascriptInterface(new MyJavaScriptInterface(context, callbackInterface), "HtmlViewer");

        getWebViewInstance(context).setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                getWebViewInstance(context).loadUrl("javascript:window.HtmlViewer.showHTML" +
                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
            }
        });

        getWebViewInstance(context).loadUrl(url);
    }


    static class MyJavaScriptInterface {

        private Context ctx;
        private CallbackInterface callbackInterface;

        MyJavaScriptInterface(Context ctx, CallbackInterface callbackInterface) {
            this.ctx = ctx;
            this.callbackInterface = callbackInterface;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            Log.d("DUMMY", "My HTML: " + html);

            if(callbackInterface != null){
                callbackInterface.getHtml(html);
            }
        }

    }

    public interface CallbackInterface{

        void getHtml(String html);

    }

}
