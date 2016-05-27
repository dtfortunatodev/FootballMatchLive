package com.footballmatch.live.utils;

import android.util.Log;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved GoodBarber
 */
public class LogUtil
{

    public static final boolean IS_DEBUG = true;

    /**
     * Custom log error
     * @param tag
     * @param message
     * @param e
     */
    public static void e(String tag, String message, Exception e)
    {
        Log.e(tag, message, e);
    }

    /**
     * Custom log debug
     * @param tag
     * @param message
     */
    public static void d(String tag, String message)
    {
        Log.d(tag, message);
    }
}
