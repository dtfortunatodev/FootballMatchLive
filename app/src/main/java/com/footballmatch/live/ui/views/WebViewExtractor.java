package com.footballmatch.live.ui.views;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by David Fortunato on 24/04/2018
 * All rights reserved GoodBarber
 */

public class WebViewExtractor extends WebView
{

    private MyJavaScriptInterface myJavaScriptInterface;

    public WebViewExtractor(Context context)
    {
        super(context);
        init();
    }

    public WebViewExtractor(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public WebViewExtractor(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        this.myJavaScriptInterface = new MyJavaScriptInterface(getContext());
        getSettings().setJavaScriptEnabled(true);
        addJavascriptInterface(this.myJavaScriptInterface, "HtmlViewer");
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                loadUrl("javascript:window.HtmlViewer.showHTML" +
                                        "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                //startExtractingWithDelay();
            }
        });
    }

    public void startExtractingWithDelay() {
        new AsyncTask<Void, Void, Void>()
        {
            @Override
            protected Void doInBackground(Void... voids)
            {
                // Wait loading
                try
                {
                    Thread.sleep(5000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid)
            {
                loadUrl("javascript:window.HtmlViewer.showHTML" +
                                "('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public String getHtml() {
        return this.myJavaScriptInterface.getLastHtmlExtraction();
    }

    class MyJavaScriptInterface {

        private Context ctx;

        private String lastHtmlExtraction;
        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @android.webkit.JavascriptInterface
        public void showHTML(String html) {
            this.lastHtmlExtraction = html;
        }

        public String getLastHtmlExtraction() {
            return lastHtmlExtraction;
        }
    }
}
