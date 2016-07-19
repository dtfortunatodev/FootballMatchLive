package com.footballmatch.live.data.model;

/**
 * Created by David Fortunato on 27/05/2016
 * All rights reserved ForViews
 */
public class MatchEntity extends BaseEntity
{

    // Data
    private TeamEntity teamHome;
    private TeamEntity teamAway;
    private int resultTeamHome;
    private int resultTeamAway;
    private CompetitionEntity competitionEntity;
    private String linkUrl;
    private long startDateMillis;
    private long endDateMillis;

    public MatchEntity()
    {
    }

    public TeamEntity getTeamHome()
    {
        return teamHome;
    }

    public void setTeamHome(TeamEntity teamHome)
    {
        this.teamHome = teamHome;
    }

    public TeamEntity getTeamAway()
    {
        return teamAway;
    }

    public void setTeamAway(TeamEntity teamAway)
    {
        this.teamAway = teamAway;
    }

    public int getResultTeamHome()
    {
        return resultTeamHome;
    }

    public void setResultTeamHome(int resultTeamHome)
    {
        this.resultTeamHome = resultTeamHome;
    }

    public int getResultTeamAway()
    {
        return resultTeamAway;
    }

    public void setResultTeamAway(int resultTeamAway)
    {
        this.resultTeamAway = resultTeamAway;
    }

    public CompetitionEntity getCompetitionEntity()
    {
        return competitionEntity;
    }

    public void setCompetitionEntity(CompetitionEntity competitionEntity)
    {
        this.competitionEntity = competitionEntity;
    }

    public String getLinkUrl()
    {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl)
    {
        this.linkUrl = linkUrl;
    }

    public long getStartDateMillis()
    {
        return startDateMillis;
    }

    public void setStartDateMillis(long startDateMillis)
    {
        this.startDateMillis = startDateMillis;
    }

    public long getEndDateMillis()
    {
        return endDateMillis;
    }

    public void setEndDateMillis(long endDateMillis)
    {
        this.endDateMillis = endDateMillis;
    }
}
