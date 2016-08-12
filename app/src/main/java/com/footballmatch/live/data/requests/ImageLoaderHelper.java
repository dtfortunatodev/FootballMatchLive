package com.footballmatch.live.data.requests;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import com.footballmatch.live.R;
import com.footballmatch.live.utils.UiUtil;
import com.squareup.picasso.Picasso;

/**
 * Created by David Fortunato on 05/08/2016
 * All rights reserved GoodBarber
 */
public class ImageLoaderHelper
{

    // Prefix URL
    private static final String PREFIX_URL_IMAGE = "http://livefootballvideo.com/";

    /**
     * Method centralized to load an image
     * @param url
     * @param imageView
     * @param defaultDrawableRes
     */
    public static void loadImage(String url, ImageView imageView, int defaultDrawableRes)
    {
        Drawable defaultDrawable = null;
        if (defaultDrawableRes != 0)
        {
            defaultDrawable = imageView.getResources().getDrawable(defaultDrawableRes);
        }
        else
        {
            defaultDrawable = imageView.getResources().getDrawable(R.drawable.invisible_drawable);
        }
        imageView.setImageDrawable(null);

        if (url == null)
        {
            imageView.setImageDrawable(defaultDrawable);
        }
        else if (url.startsWith("data"))
        {
            imageView.setImageDrawable(UiUtil.convertBase64ToBitmap(url));
        }
        else if (!url.startsWith("http"))
        {
            Picasso.with(imageView.getContext()).load(PREFIX_URL_IMAGE + url).placeholder(R.drawable.invisible_drawable).error(defaultDrawableRes).into(imageView);
        } else
        {
            Picasso.with(imageView.getContext()).load(url).placeholder(R.drawable.invisible_drawable).error(defaultDrawableRes).into(imageView);
        }
    }

}
