package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.BuildConfig;
import com.footballmatch.live.data.model.BaseEntity;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class UpdateRedirectDialog extends BaseEntity
{

    private int     minVersion;
    private int     currentVersion;
    private String  updateLink;
    private boolean autoInstall;

    public UpdateRedirectDialog()
    {
        this.minVersion = 0;
        this.currentVersion = 0;
        this.updateLink = null;
        this.autoInstall = false;
    }

    public int getMinVersion()
    {
        return minVersion;
    }

    public void setMinVersion(int minVersion)
    {
        this.minVersion = minVersion;
    }

    public int getCurrentVersion()
    {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion)
    {
        this.currentVersion = currentVersion;
    }

    public String getUpdateLink()
    {
        return updateLink;
    }

    public void setUpdateLink(String updateLink)
    {
        this.updateLink = updateLink;
    }

    public boolean isBlocked()
    {
        return BuildConfig.VERSION_CODE < minVersion;
    }

    public boolean isUpdateAvailable()
    {
        return BuildConfig.VERSION_CODE < currentVersion;
    }

    public boolean shouldDisplayDialog()
    {
        return (isBlocked() || isUpdateAvailable());
    }

    public boolean isAutoInstall()
    {
        return autoInstall;
    }

    public void setAutoInstall(boolean autoInstall)
    {
        this.autoInstall = autoInstall;
    }
}
