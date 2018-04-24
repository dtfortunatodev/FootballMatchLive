package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.BuildConfig;
import com.footballmatch.live.data.managers.AnalyticsHelper;
import com.footballmatch.live.data.model.BaseEntity;
import java.util.List;

/**
 * Created by David Fortunato on 19/07/2016
 * All rights reserved ForViews
 */
public class AppConfigs extends BaseEntity
{

    // Startup Configs
    private int                  blockAfterVersion;
    private boolean              subsNotificationEnabled;
    private boolean              shouldBlockSensibleData;
    private boolean              authenticationEnabled;
    private StartupMessageEntity startupMessageEntity;
    private UpdateRedirectDialog updateRedirectDialog;
    private AdsConfigs           adsConfigs;
    private IpCatchEntity        userCurrentIp;
    private List<IpCatchEntity>  listIpsToValidate;
    private AdInternalEntity     internalAds;
    private String               shareBaseLink;

    // Social
    private String facebookPageUrl;
    private String twitterPageUrl;
    private String redditPageUrl;

    // Apps
    private String aceStreamAppPackagege     = "org.acestream.media";
    private String aceStreamAppUrl           = "http://dl.acestream.org/products/acestream-full/android/latest"; // Default
    private String sopcastStreamAppUrl       = "http://download.sopcast.com/download/SopCast.apk";
    private String webplayerStreamAppPackage = "com.cloudmosa.puffinFree";
    private String webplayerStreamAppUrl     = "https://play.google.com/store/apps/details?id=com.cloudmosa.puffinFree";

    public AppConfigs()
    {
        // Default Config
        adsConfigs = new AdsConfigs();
        updateRedirectDialog = new UpdateRedirectDialog();
        shouldBlockSensibleData = false;
        blockAfterVersion = -1;
        this.shareBaseLink = "https://aep7s.app.goo.gl/";
    }

    public StartupMessageEntity getStartupMessageEntity()
    {
        return startupMessageEntity;
    }

    public void setStartupMessageEntity(StartupMessageEntity startupMessageEntity)
    {
        this.startupMessageEntity = startupMessageEntity;
    }

    public boolean isShouldBlockSensibleData()
    {
        return shouldBlockSensibleData;
    }

    public void setShouldBlockSensibleData(boolean shouldBlockSensibleData)
    {
        this.shouldBlockSensibleData = shouldBlockSensibleData;
    }

    public UpdateRedirectDialog getUpdateRedirectDialog()
    {
        return updateRedirectDialog;
    }

    public void setUpdateRedirectDialog(UpdateRedirectDialog updateRedirectDialog)
    {
        this.updateRedirectDialog = updateRedirectDialog;
    }

    public AdsConfigs getAdsConfigs()
    {
        return adsConfigs;
    }

    public void setAdsConfigs(AdsConfigs adsConfigs)
    {
        this.adsConfigs = adsConfigs;

        if (this.adsConfigs != null)
        {
            this.adsConfigs.setParentAppConfigs(this);
        }
    }

    public List<IpCatchEntity> getListIpsToValidate()
    {
        return listIpsToValidate;
    }

    public void setListIpsToValidate(List<IpCatchEntity> listIpsToValidate)
    {
        this.listIpsToValidate = listIpsToValidate;

        // Update Current Ip (to validate the Block Sensible data)
        setUserCurrentIp(getUserCurrentIp());
    }

    public IpCatchEntity getUserCurrentIp()
    {
        return userCurrentIp;
    }

    public void setUserCurrentIp(IpCatchEntity userCurrentIp)
    {
        this.userCurrentIp = userCurrentIp;

        // Validate the new User Ip
        if (userCurrentIp != null && userCurrentIp.comparateListUserIps(getListIpsToValidate()))
        {
            // Send a report
            AnalyticsHelper.getInstance().sendEvent("UserCatchedIP", userCurrentIp.toString());
            shouldBlockSensibleData = true;

            // Disable Ads
            if (getAdsConfigs() != null)
            {
                getAdsConfigs().setAdsEnabled(false);
            }
        }
    }

    public int getBlockAfterVersion()
    {
        return blockAfterVersion;
    }

    public void setBlockAfterVersion(int blockAfterVersion)
    {
        this.blockAfterVersion = blockAfterVersion;
    }

    public String getAceStreamAppUrl()
    {
        return aceStreamAppUrl;
    }

    public void setAceStreamAppUrl(String aceStreamAppUrl)
    {
        this.aceStreamAppUrl = aceStreamAppUrl;
    }

    public String getSopcastStreamAppUrl()
    {
        return sopcastStreamAppUrl;
    }

    public void setSopcastStreamAppUrl(String sopcastStreamAppUrl)
    {
        this.sopcastStreamAppUrl = sopcastStreamAppUrl;
    }

    public String getWebplayerStreamAppPackage()
    {
        return webplayerStreamAppPackage;
    }

    public void setWebplayerStreamAppPackage(String webplayerStreamAppPackage)
    {
        this.webplayerStreamAppPackage = webplayerStreamAppPackage;
    }

    public String getWebplayerStreamAppUrl()
    {
        return webplayerStreamAppUrl;
    }

    public void setWebplayerStreamAppUrl(String webplayerStreamAppUrl)
    {
        this.webplayerStreamAppUrl = webplayerStreamAppUrl;
    }

    public String getAceStreamAppPackagege()
    {
        return aceStreamAppPackagege;
    }

    public void setAceStreamAppPackagege(String aceStreamAppPackagege)
    {
        this.aceStreamAppPackagege = aceStreamAppPackagege;
    }

    /**
     * Check if should Block sensible data. This also check the maximum version to display the sensible data
     *
     * @returnTrue if should block or false otherwise
     */
    public boolean checkShouldBlockSensibleData()
    {
        return shouldBlockSensibleData || (blockAfterVersion > 0 && BuildConfig.VERSION_CODE >= blockAfterVersion);
    }

    public String getTwitterPageUrl()
    {
        return twitterPageUrl;
    }

    public void setTwitterPageUrl(String twitterPageUrl)
    {
        this.twitterPageUrl = twitterPageUrl;
    }

    public String getFacebookPageUrl()
    {
        return facebookPageUrl;
    }

    public void setFacebookPageUrl(String facebookPageUrl)
    {
        this.facebookPageUrl = facebookPageUrl;
    }

    public String getRedditPageUrl()
    {
        return redditPageUrl;
    }

    public void setRedditPageUrl(String redditPageUrl)
    {
        this.redditPageUrl = redditPageUrl;
    }

    public boolean isSubsNotificationEnabled()
    {
        return subsNotificationEnabled;
    }

    public void setSubsNotificationEnabled(boolean subsNotificationEnabled)
    {
        this.subsNotificationEnabled = subsNotificationEnabled;
    }

    public AdInternalEntity getInternalAds()
    {
        return internalAds;
    }

    public void setInternalAds(AdInternalEntity internalAds)
    {
        this.internalAds = internalAds;
    }

    public String getShareBaseLink()
    {
        return shareBaseLink;
    }

    public void setShareBaseLink(String shareBaseLink)
    {
        this.shareBaseLink = shareBaseLink;
    }

    public boolean isAuthenticationEnabled()
    {
        return authenticationEnabled;
    }

    public void setAuthenticationEnabled(boolean authenticationEnabled)
    {
        this.authenticationEnabled = authenticationEnabled;
    }
}
