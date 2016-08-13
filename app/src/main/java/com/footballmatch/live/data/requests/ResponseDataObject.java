package com.footballmatch.live.data.requests;

import java.util.List;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class ResponseDataObject<T>
{
    // Local Response Codes
    public static final int RESPONSE_CODE_OK = 1;
    public static final int RESPONSE_CODE_NOT_MODIFYED = 0;
    public static final int RESPONSE_CODE_FAILED_GETTING_DOCUMENT = -1;

    private List<T> listObjectsResponse;
    private T object;
    private int responseCode;

    public ResponseDataObject()
    {
        responseCode = RESPONSE_CODE_NOT_MODIFYED;
    }

    /**
     * Check if the response is ok
     * @return true if is ok, false otherwise
     */
    public boolean isOk()
    {
        return (getListObjectsResponse() != null && !getListObjectsResponse().isEmpty()) || getResponseCode() > RESPONSE_CODE_NOT_MODIFYED;
    }

    public List<T> getListObjectsResponse()
    {
        return listObjectsResponse;
    }

    public void setListObjectsResponse(List<T> listObjectsResponse)
    {
        this.listObjectsResponse = listObjectsResponse;
    }

    public T getObject()
    {
        return object;
    }

    public void setObject(T object)
    {
        this.object = object;
    }

    public int getResponseCode()
    {
        return responseCode;
    }

    public void setResponseCode(int responseCode)
    {
        this.responseCode = responseCode;
    }
}
