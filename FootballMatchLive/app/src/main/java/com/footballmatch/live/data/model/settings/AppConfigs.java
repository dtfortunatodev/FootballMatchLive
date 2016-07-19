package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.BuildConfig;
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
    private boolean              shouldBlockSensibleData;
    private String               startupMessage;
    private UpdateRedirectDialog updateRedirectDialog;
    private AdsConfigs           adsConfigs;
    private IpCatchEntity        userCurrentIp;
    private List<IpCatchEntity>  listIpsToValidate;


    public AppConfigs()
    {
        // Default Config
        adsConfigs = new AdsConfigs();
        updateRedirectDialog = new UpdateRedirectDialog();
        startupMessage = null;
        shouldBlockSensibleData = false;
        blockAfterVersion = -1;
    }

    public boolean isShouldBlockSensibleData()
    {
        return shouldBlockSensibleData;
    }

    public void setShouldBlockSensibleData(boolean shouldBlockSensibleData)
    {
        this.shouldBlockSensibleData = shouldBlockSensibleData;
    }

    public String getStartupMessage()
    {
        return startupMessage;
    }

    public void setStartupMessage(String startupMessage)
    {
        this.startupMessage = startupMessage;
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
        if(userCurrentIp != null && userCurrentIp.comparateListUserIps(getListIpsToValidate()))
        {
            shouldBlockSensibleData = true;
        }
    }

    /**
     * Check if should Block sensible data. This also check the maximum version to display the sensible data
     * @returnTrue if should block or false otherwise
     */
    public boolean checkShouldBlockSensibleData()
    {
        return shouldBlockSensibleData || (blockAfterVersion > 0 && BuildConfig.VERSION_CODE >= blockAfterVersion);
    }

}
