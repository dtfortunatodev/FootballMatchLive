package com.footballmatch.live.data.model;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class CompetitionEntity extends BaseEntity
{
    private String competitionName;
    private String competitionLogoUrl;
    private String competitionLink;

    public CompetitionEntity()
    {

    }

    public String getCompetitionName()
    {
        return competitionName;
    }

    public void setCompetitionName(String competitionName)
    {
        this.competitionName = competitionName;
    }

    public String getCompetitionLogoUrl()
    {
        return competitionLogoUrl;
    }

    public void setCompetitionLogoUrl(String competitionLogoUrl)
    {
        this.competitionLogoUrl = competitionLogoUrl;
    }

    public String getCompetitionLink()
    {
        return competitionLink;
    }

    public void setCompetitionLink(String competitionLink)
    {
        this.competitionLink = competitionLink;
    }
}
