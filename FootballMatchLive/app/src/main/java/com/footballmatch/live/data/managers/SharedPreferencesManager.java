package com.footballmatch.live.data.managers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class SharedPreferencesManager
{

    // Shared Name
    private static final String SHARED_PREFERENCES_NAME = "com.footballmatch.live.SharedPreferences";

    /**
     * Put a new String value into a key index
     * @param context
     * @param key
     * @param value
     */
    public static void putStringMessage(Context context, String key, String value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).commit();
    }

    /**
     * Get a String from preferences
     * @param context
     * @param key
     */
    public static String getStringMessage(Context context, String key, String defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, defaultValue);
    }

}
