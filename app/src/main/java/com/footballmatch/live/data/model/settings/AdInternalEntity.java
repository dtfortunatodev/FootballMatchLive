package com.footballmatch.live.data.model.settings;

import android.content.Context;
import android.graphics.Color;
import com.footballmatch.live.data.managers.StartupManager;
import com.footballmatch.live.data.model.BaseEntity;
import com.footballmatch.live.utils.Utils;

/**
 * Created by David Fortunato on 12/08/2016
 * All rights reserved GoodBarber
 */
public class AdInternalEntity extends BaseEntity
{

    // Fields
    private boolean isEnabled;
    private String  backgroundImageUrl;
    private String  textDescription;
    private String  urlToOpen;
    private String  packageApp;
    private String  textColor;

    AdInternalEntity()
    {

    }

    /**
     * Check if should display or not the ad
     *
     * @param context
     *
     * @return
     */
    public boolean shouldDisplayAd(Context context)
    {
        return isEnabled && (packageApp == null || packageApp.isEmpty() ||
                !Utils.isPackageInstalled(context, packageApp) && !StartupManager.getInstance(context).getAppConfigs().checkShouldBlockSensibleData());
    }

    public boolean isEnabled()
    {
        return isEnabled;
    }

    public void setEnabled(boolean enabled)
    {
        isEnabled = enabled;
    }

    public String getBackgroundImageUrl()
    {
        return backgroundImageUrl;
    }

    public void setBackgroundImageUrl(String backgroundImageUrl)
    {
        this.backgroundImageUrl = backgroundImageUrl;
    }

    public String getTextDescription()
    {
        return textDescription;
    }

    public void setTextDescription(String textDescription)
    {
        this.textDescription = textDescription;
    }

    public String getUrlToOpen()
    {
        return urlToOpen;
    }

    public void setUrlToOpen(String urlToOpen)
    {
        this.urlToOpen = urlToOpen;
    }

    public String getPackageApp()
    {
        return packageApp;
    }

    public void setPackageApp(String packageApp)
    {
        this.packageApp = packageApp;
    }

    public int getTextColorParsed()
    {
        return Color.parseColor(getTextColor());
    }

    public String getTextColor()
    {
        return textColor;
    }

    public void setTextColor(String textColor)
    {
        this.textColor = textColor;
    }
}
