package com.footballmatch.live.data.utils;

import org.jsoup.select.Elements;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved GoodBarber
 */
public class DataUtils
{
    /**
     * Check if the elements received is not null and also is not empty
     * @param elements
     * @return
     */
    public static boolean isElementsValid(Elements elements)
    {
        return elements != null && elements.first() != null;
    }

    /**
     * Because we should multiply per 1000 the value that we receive from Server
     * @param currentMillis
     * @return
     */
    public static long generateFinalMillis(long currentMillis)
    {
        if(currentMillis > 0 && currentMillis < Long.parseLong("1000000000000"))
        {
            currentMillis *= 1000;
        }
        return currentMillis;
    }

}
