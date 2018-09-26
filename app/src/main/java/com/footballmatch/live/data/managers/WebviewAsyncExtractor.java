package com.footballmatch.live.data.managers;

import android.content.Context;
import android.os.AsyncTask;
import com.footballmatch.live.data.requests.ArenaVisionGetLinkRequest;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.ui.views.WebViewExtractor;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class WebviewAsyncExtractor<T> extends AsyncTask<Void, ResponseDataObject<T>, ResponseDataObject<T>>
{

    // Data
    private Context context;
    private OnRequestListener onRequestListener;
    private String requestUrl;
    private WebViewExtractor webView;
    private long loadingTime;

    public WebviewAsyncExtractor(Context context, OnRequestListener onRequestListener, String requestUrl, long loadingTime)
    {
        this.context = context;
        this.onRequestListener = onRequestListener;
        this.requestUrl = requestUrl;
        this.loadingTime = loadingTime;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if(onRequestListener != null)
        {
            onRequestListener.onRequestStart();
        }

        webView = new WebViewExtractor(this.context);
        webView.loadUrl(this.requestUrl);

    }

    @Override
    protected ResponseDataObject<T> doInBackground(Void... params)
    {
        String html = null;
        try
        {
            Thread.sleep(loadingTime);
            html = this.webView.getHtml();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        ResponseDataObject<String> responseDataObject = new ResponseDataObject<>();
        responseDataObject.setObject( ArenaVisionGetLinkRequest.getArenaVisionLink(requestUrl, html));
        if (responseDataObject.getObject() != null && !responseDataObject.getObject().isEmpty())
        {
            responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_OK);
        }
        else
        {
            responseDataObject.setResponseCode(ResponseDataObject.RESPONSE_CODE_FAILED_GETTING_DOCUMENT);
        }

        return (ResponseDataObject<T>) responseDataObject;
    }

    @Override
    protected void onPostExecute(ResponseDataObject<T> responseDataObject)
    {

        if(onRequestListener != null)
        {
            onRequestListener.onRequestResponse(responseDataObject);
        }

        // Clean data
        onRequestListener = null;
        context = null;

        super.onPostExecute(responseDataObject);
    }

    public WebviewAsyncExtractor<T> setRequestUrl(String requestUrl)
    {
        this.requestUrl = requestUrl;
        return this;
    }

    /**
     * Types of requests that exists on the app
     */
    public enum RequestType
    {
        REQUEST_LIVE_MATCHES, REQUEST_LIST_STREAMS, REQUEST_ARENAVISION_LINK, REQUEST_MATCH_HIGHLIGHTS;
    }


    public interface OnRequestListener
    {
        void onRequestStart();

        void onRequestResponse(ResponseDataObject responseDataObject);
    }

}
