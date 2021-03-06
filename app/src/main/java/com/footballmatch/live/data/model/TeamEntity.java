package com.footballmatch.live.data.model;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class TeamEntity extends BaseEntity
{
    private String teamName;
    private String teamLogoUrl;

    public TeamEntity()
    {

    }

    public String getTeamName()
    {
        return teamName;
    }

    public void setTeamName(String teamName)
    {
        this.teamName = teamName;
    }

    public String getTeamLogoUrl()
    {
        return teamLogoUrl;
    }

    public void setTeamLogoUrl(String teamLogoUrl)
    {
        this.teamLogoUrl = teamLogoUrl;

        // Set Big URL
        if (this.teamLogoUrl != null)
        {
            this.teamLogoUrl = this.teamLogoUrl.replace("/small/", "/big/");
        }

    }
}
