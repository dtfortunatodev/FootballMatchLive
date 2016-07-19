package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.data.model.BaseEntity;
import java.util.List;

/**
 * Created by PTECH on 10-02-2015.
 */
public class IpCatchEntity extends BaseEntity
{

    private String ip;
    private String hostname;
    private String city;
    private String region;
    private String country;
    private String loc;
    private String org;

    public IpCatchEntity()
    {

    }

    public String getIp()
    {
        return ip;
    }

    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getHostname()
    {
        return hostname;
    }

    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getRegion()
    {
        return region;
    }

    public void setRegion(String region)
    {
        this.region = region;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry(String country)
    {
        this.country = country;
    }

    public String getLoc()
    {
        return loc;
    }

    public void setLoc(String loc)
    {
        this.loc = loc;
    }

    public String getOrg()
    {
        return org;
    }

    public void setOrg(String org)
    {
        this.org = org;
    }

    public boolean comparateUsersIp(IpCatchEntity ipCatchEntity)
    {
        if (ipCatchEntity != null)
        {

            // Same Ip
            if (ipCatchEntity.getIp() != null && this.getIp() != null && this.getIp().equalsIgnoreCase(ipCatchEntity.getIp()))
            {
                return true;
            }

            // Same Loc
            if (ipCatchEntity.getLoc() != null && this.getLoc() != null && this.getLoc().equalsIgnoreCase(ipCatchEntity.getLoc()))
            {
                return true;
            }

            // Contains Org
            if (ipCatchEntity.getOrg() != null && this.getOrg() != null &&
                    (ipCatchEntity.getOrg().contains(this.getOrg()) || this.getOrg().contains(ipCatchEntity.getOrg())))
            {
                return true;
            }

            // Compare City
            if (ipCatchEntity.getCity() != null && this.getCity() != null && this.getCity().equalsIgnoreCase(ipCatchEntity.getCity()))
            {
                return true;
            }

        }

        return false;
    }

    public boolean comparateListUserIps(List<IpCatchEntity> listUserIps)
    {
        if(listUserIps != null)
        {
            for (IpCatchEntity ipCatchEntity : listUserIps)
            {
                boolean isSimilar = ipCatchEntity.comparateUsersIp(this);
                if(isSimilar)
                {
                    // We found a similar Ip
                    return true;
                }
            }
        }

        return false;
    }

}
