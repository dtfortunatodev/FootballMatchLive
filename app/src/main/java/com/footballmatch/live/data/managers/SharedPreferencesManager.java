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

    public static void putIntValue(Context context, String key, int value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getIntValue(Context context, String key, int defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, defaultValue);
    }

    public static void putLongValue(Context context, String key, long value)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putLong(key, value).commit();
    }

    public static long getLongValue(Context context, String key, long defaultValue)
    {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        return sharedPreferences.getLong(key, defaultValue);
    }

}
