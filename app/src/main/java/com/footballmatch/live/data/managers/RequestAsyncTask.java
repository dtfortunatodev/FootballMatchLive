package com.footballmatch.live.data.managers;

import android.content.Context;
import android.os.AsyncTask;
import com.footballmatch.live.data.requests.ArenaVisionGetLinkRequest;
import com.footballmatch.live.data.requests.LiveMatchRequest;
import com.footballmatch.live.data.requests.MatchHighlightRequest;
import com.footballmatch.live.data.requests.ResponseDataObject;
import com.footballmatch.live.data.requests.StreamsMatchListRequest;
import com.footballmatch.live.ui.views.WebViewExtractor;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class RequestAsyncTask<T> extends AsyncTask<Void, ResponseDataObject<T>, ResponseDataObject<T>>
{

    // Data
    private RequestType requestType;
    private Context context;
    private OnRequestListener onRequestListener;
    private String requestUrl;
    private WebViewExtractor webView;

    public RequestAsyncTask(Context context, RequestType requestType, OnRequestListener onRequestListener)
    {
        this.requestType = requestType;
        this.context = context;
        this.onRequestListener = onRequestListener;
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();

        if(onRequestListener != null)
        {
            onRequestListener.onRequestStart();
        }

        switch (requestType)
        {
            case REQUEST_ARENAVISION_LINK:
                webView = new WebViewExtractor(this.context);
                webView.loadUrl(this.requestUrl);
                break;
        }

    }

    @Override
    protected ResponseDataObject<T> doInBackground(Void... params)
    {
        switch (requestType)
        {
            case REQUEST_LIVE_MATCHES:
                return (ResponseDataObject<T>) LiveMatchRequest.getListLiveMatches();
            case REQUEST_LIST_STREAMS:
                return (ResponseDataObject<T>) StreamsMatchListRequest.getListMatchStreams(requestUrl);
            case REQUEST_ARENAVISION_LINK:
                String html = null;
                try
                {
                    Thread.sleep(10000);
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
            case REQUEST_MATCH_HIGHLIGHTS:
                return (ResponseDataObject<T>) MatchHighlightRequest.getMatchHighlightLink(requestUrl);
        }

        return null;
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

    public RequestAsyncTask<T> setRequestUrl(String requestUrl)
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
