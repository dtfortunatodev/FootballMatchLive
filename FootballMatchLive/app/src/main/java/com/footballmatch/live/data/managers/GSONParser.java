package com.footballmatch.live.data.managers;

import com.google.gson.Gson;
import java.util.List;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class GSONParser
{
    private static Gson gsonClient;


    /**
     * Parse a JSON message to object
     * @param jsonMessage Json mesage to parse
     * @param objectClass Object type to convert
     * @param <T> Type of class
     * @return Generate object
     */
    public static <Type> Type parseJSONToObject(String jsonMessage, Class<Type> objectClass)
    {
        return getGsonInstance().fromJson(jsonMessage, objectClass);
    }

    /**
     * Parse object to string
     * @param obj Object to parse
     * @return Json Message
     */
    public static <T> String parseObjectToString(T obj)
    {
        return getGsonInstance().toJson(obj);
    }

    /**
     * Parse a list of objects to a String
     * @param listObjects List of objects to parse
     * @return JSon message generated
     */
    public static <T> String parseListObjectToString(List<T> listObjects)
    {
        StringBuilder jsonMessage = new StringBuilder().append("[");

        if(listObjects != null && !listObjects.isEmpty())
        {
            for(int i = 0; i < listObjects.size(); i++)
            {
                if(i > 0)
                {
                    jsonMessage.append(",");
                }
                jsonMessage.append(parseObjectToString(listObjects.get(i)));
            }
        }

        // Close Json List
        jsonMessage.append("]");

        return jsonMessage.toString();
    }

    /**
     * Get GSON client instance
     * @return GSON instance
     */
    private static Gson getGsonInstance()
    {
        if(gsonClient == null)
        {
            gsonClient = new Gson();
        }

        return gsonClient;
    }

}
