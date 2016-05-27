package com.footballmatch.live.data.managers;

import android.content.Context;
import android.os.AsyncTask;
import com.footballmatch.live.data.requests.LiveMatchRequest;
import com.footballmatch.live.data.requests.ResponseDataObject;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved GoodBarber
 */
public class RequestAsyncTask<T> extends AsyncTask<Void, ResponseDataObject<T>, ResponseDataObject<T>>
{

    // Data
    private RequestType requestType;
    private Context context;
    private OnRequestListener onRequestListener;

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
    }

    @Override
    protected ResponseDataObject<T> doInBackground(Void... params)
    {
        switch (requestType)
        {
            case REQUEST_LIVE_MATCHES:
                return (ResponseDataObject<T>) LiveMatchRequest.getListLiveMatches();
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

    /**
     * Types of requests that exists on the app
     */
    public enum RequestType
    {
        REQUEST_LIVE_MATCHES;
    }


    public interface OnRequestListener
    {
        void onRequestStart();

        void onRequestResponse(ResponseDataObject responseDataObject);
    }

}
