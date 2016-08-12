package com.footballmatch.live.data.model.settings;

import com.footballmatch.live.data.model.BaseEntity;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by David Fortunato on 10/08/2016
 * All rights reserved GoodBarber
 */
public class TeamNotificationEntry extends BaseEntity
{

    private Set<String> setTeamNamesRegistered;

    public TeamNotificationEntry()
    {
        setTeamNamesRegistered = new HashSet<>();
    }

    public Set<String> getSetTeamNamesRegistered()
    {
        return setTeamNamesRegistered;
    }

    public void setSetTeamNamesRegistered(Set<String> setTeamNamesRegistered)
    {
        this.setTeamNamesRegistered = setTeamNamesRegistered;
    }
}
