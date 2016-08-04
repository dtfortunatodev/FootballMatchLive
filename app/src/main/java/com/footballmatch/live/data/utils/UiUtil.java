package com.footballmatch.live.data.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class UiUtil
{

    /**
     * Convert encoded image to Bitmap
     * @param encodedImage
     * @return
     */
    public static Drawable convertBase64ToBitmap(String encodedImage)
    {
        try
        {
            encodedImage = encodedImage.substring(encodedImage.indexOf(",") + 1);
            byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return new BitmapDrawable(decodedByte);
        }
        catch (Exception e)
        {
            return null;
        }

    }

}
