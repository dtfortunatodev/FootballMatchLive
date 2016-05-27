package com.footballmatch.live.data.managers;

import com.footballmatch.live.utils.LogUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by David Fortunato on 27/05/2016
 *
 * This is a singleton Implementation
 *
 */
public class HttpRequestManager
{
    private static final String TAG = HttpRequestManager.class.getSimpleName();

    // Cache Configs
    private static final String CACHE_FILE_NAME = HttpRequestManager.class.getName() + ".Cache";
    private static final long CACHE_FILE_SIZE = 10000; // 10mb

    // Singleton Instance
    private static HttpRequestManager instance;


    // Data
    private OkHttpClient client;
    private Cache cache;

    private HttpRequestManager()
    {
        cache = new Cache(new File(CACHE_FILE_NAME), CACHE_FILE_SIZE);
        client = new OkHttpClient
                .Builder()
                .cache(cache)
                .build();
    }

    /**
     * Get request
     * @param url Url of the request
     * @param params Params to put on request
     * @param headers Headers used on request
     * @return Response
     */
    public Response getData(String url, Map<String, String> params, Map<String, String> headers)
    {
        // Request Builder
        Request.Builder requestBuilder = new Request.Builder();

        // Set URL
        requestBuilder.url(url);

        // Add Params
        url = generateUrlWithParams(url, params);

        // Add Headers
        if(headers != null && !headers.isEmpty())
        {
            for (String header : new ArrayList<>(headers.keySet()))
            {
                requestBuilder.addHeader(header, headers.get(header));
            }
        }


        Response response = null;
        try
        {
            response = client.newCall(requestBuilder.build()).execute();
        }
        catch (IOException e)
        {
            LogUtil.e(TAG, e.getMessage(), e);
        }

        return response;
    }


    /**
     * Complete the Url with (if exists) params
     * @param url Base Url
     * @param params Params to append to the Url
     * @return Completed URL
     */
    private String generateUrlWithParams(String url, Map<String, String> params){
        // Add Params
        if(params != null && !params.isEmpty())
        {
            boolean isFirstParam = true;
            for (String param : new ArrayList<>(params.keySet()))
            {
                if(isFirstParam)
                {
                    url += "?" + param + "=" + params.get(param);
                } else
                {
                    url += "&" + param + "=" + params.get(param);
                }
            }
        }

        return url;
    }

    /**
     * Get or generate a Singleton instance
     * @return HttpRequestManger instance. Never return a Null object
     */
    public static HttpRequestManager getInstance()
    {
        if(instance == null)
        {
            instance = new HttpRequestManager();
        }

        return instance;
    }



}
